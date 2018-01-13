package care.skuniv.ac.kr.careparent;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Fragment2 extends Fragment  implements TextView.OnClickListener {

    //연,월 텍스트뷰
    private TextView tvDate;
    //월 이동 버튼 텍스트뷰
    private TextView leftBtn;
    private TextView rightBtn;
    //그리드뷰를 위한 캘린더어댑터
    private CalendarAdapter calendarAdapter;
    //일 저장 리스트
    private ArrayList<InfoAttendance> dayList;
    private GridView gridView;
    private Calendar mCalendar;
    //학생번호를 받아오기 위한 변수
    private String std_no;
    SharedPreferences info;

    public Fragment2()
    {
        // required
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment2, null);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        gridView = (GridView) view.findViewById(R.id.gridview);
        leftBtn = (TextView) view.findViewById(R.id.leftBtn);
        rightBtn = (TextView) view.findViewById(R.id.rightBtn);

        //텍스트뷰 클릭 버튼
        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);

        // 오늘에 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        //연,월,일을 따로 저장
        final SimpleDateFormat YearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat MonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        //final SimpleDateFormat DayFormat = new SimpleDateFormat("dd", Locale.KOREA);
        //현재 날짜 텍스트뷰에 뿌려줌
        tvDate.setText(YearFormat.format(date) + "년" + MonthFormat.format(date) + "월");
        //gridview 요일 표시
        //일~월 셋팅
        setDayList();
        //캘린더 달력 가져오기
        mCalendar = Calendar.getInstance();
        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCalendar.set(Integer.parseInt(YearFormat.format(date)), Integer.parseInt(MonthFormat.format(date)) - 1, 1);
        //이번달 시작 공백 수
        int dayNum = mCalendar.get(Calendar.DAY_OF_WEEK);
        //1일을 해다 요일에 매칭 시키기 위해 공백부분 추가
        for (int i = 1; i < dayNum; i++) {
            dayList.add(new InfoAttendance("", 0));
        }
        setCalendarDate(mCalendar.get(Calendar.MONTH) + 1, mCalendar.get(Calendar.YEAR));
        calendarAdapter = new CalendarAdapter(getActivity(), dayList);
        gridView.setAdapter(calendarAdapter);
        return view;
    }

    private void setDayList(){
        //gridview 맨윗줄에 요일 표시
        dayList = new ArrayList<InfoAttendance>();
        dayList.add(new InfoAttendance("일", 0));
        dayList.add(new InfoAttendance("월", 0));
        dayList.add(new InfoAttendance("화", 0));
        dayList.add(new InfoAttendance("수", 0));
        dayList.add(new InfoAttendance("목", 0));
        dayList.add(new InfoAttendance("금", 0));
        dayList.add(new InfoAttendance("토", 0));
    }

    private void setCalendarDate(int month, int year) {
        info = getActivity().getSharedPreferences("info",Activity.MODE_PRIVATE);
        std_no = info.getString("std_no","none");
        Log.d("check std_no",std_no);
        mCalendar.set(Calendar.MONTH, month - 1);
        // get Student Attendance data
        ServerConn serverConn = new ServerConn("http://" + URLPath.url + ":8080/CareServer/getAttList",std_no, 3);
        String result = "";
        try {
            result = serverConn.execute().get();
            Log.d("connect", result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        InfoAttendance[] infoAttendances = gson.fromJson(result, InfoAttendance[].class);

        //달력과 학생 출석상태를 매핑시켜주는 부분
        int searchIndex = 0;
        for (int i = 0; i < mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            boolean state = false;
            for (int j = searchIndex; j < infoAttendances.length; j++) {
                String date = infoAttendances[j].getDate();
                String tokenDate[] = date.split("-");
                if(Integer.parseInt(tokenDate[0]) == year) {
                    if (Integer.parseInt(tokenDate[1]) == month) {
                        if (Integer.parseInt(tokenDate[2]) == i + 1) {
                            dayList.add(new InfoAttendance("" + (i + 1), infoAttendances[j].getAtt_state()));
                            searchIndex++;
                            state = true;
                            break;
                        }
                    }
                }
            }
            if (!state)
                dayList.add(new InfoAttendance("" + (i + 1), 0));
        }
    }

    /////////////이전 달 셋팅 ////////////////////
    private Calendar getLastMonth(Calendar calendar)
    {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        tvDate.setText(mCalendar.get(Calendar.YEAR) + "년 "
                + (mCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }

    ////////////다음 달 셋팅/////////////////////
    private Calendar getNextMonth(Calendar calendar)
    {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        tvDate.setText(mCalendar.get(Calendar.YEAR) + "년 "
                + (mCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.leftBtn:
                setDayList();
                mCalendar = getLastMonth(mCalendar);
                int dayNum = mCalendar.get(Calendar.DAY_OF_WEEK);
                //1일 - 요일 매칭 시키기 위해 공백 add
                for (int i = 1; i < dayNum; i++) {
                    dayList.add(new InfoAttendance("", 0));
                }
                setCalendarDate(mCalendar.get(Calendar.MONTH) + 1,mCalendar.get(Calendar.YEAR));
                calendarAdapter = new CalendarAdapter(getActivity(), dayList);
                gridView.setAdapter(calendarAdapter);
                break;

            case R.id.rightBtn:
                setDayList();
                mCalendar = getNextMonth(mCalendar);
                dayNum = mCalendar.get(Calendar.DAY_OF_WEEK);
                //1일 - 요일 매칭 시키기 위해 공백 add
                for (int i = 1; i < dayNum; i++) {
                    dayList.add(new InfoAttendance("", 0));
                }
                setCalendarDate(mCalendar.get(Calendar.MONTH) + 1,mCalendar.get(Calendar.YEAR));
                calendarAdapter = new CalendarAdapter(getActivity(), dayList);
                gridView.setAdapter(calendarAdapter);
                break;
        }
    }
}