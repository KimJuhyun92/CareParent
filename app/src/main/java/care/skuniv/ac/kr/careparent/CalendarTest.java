package care.skuniv.ac.kr.careparent;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class CalendarTest extends Activity {
    /**
     * 연/월 텍스트뷰
     */
    private TextView tvDate;
    /**
     * 그리드뷰 어댑터
     */
    private CalendarAdapter calendarAdapter;
    /**
     * 일 저장 할 리스트
     */
//    private ArrayList<String> dayList;
    private ArrayList<InfoAttendance> dayList;
    /**
     * 그리드뷰
     */
    private GridView gridView;
    /**
     * 캘린더 변수
     */
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        tvDate = (TextView) findViewById(R.id.tv_date);
        gridView = (GridView) findViewById(R.id.gridview);

        // 오늘에 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        //현재 날짜 텍스트뷰에 뿌려줌
        tvDate.setText(curYearFormat.format(date) + "/" + curMonthFormat.format(date));

        //gridview 요일 표시
        dayList = new ArrayList<InfoAttendance>();
        dayList.add(new InfoAttendance("일", 0));
        dayList.add(new InfoAttendance("월", 0));
        dayList.add(new InfoAttendance("화", 0));
        dayList.add(new InfoAttendance("수", 0));
        dayList.add(new InfoAttendance("목", 0));
        dayList.add(new InfoAttendance("금", 0));
        dayList.add(new InfoAttendance("토", 0));

        mCalendar = Calendar.getInstance();

        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCalendar.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
        int dayNum = mCalendar.get(Calendar.DAY_OF_WEEK);

        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            dayList.add(new InfoAttendance("", 0));
        }

        setCalendarDate(mCalendar.get(Calendar.MONTH) + 1);
        calendarAdapter = new CalendarAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(calendarAdapter);
    }

    /**
     * 해당 월에 표시할 일 수 구함
     *
     * @param month
     */

    private void setCalendarDate(int month) {
        mCalendar.set(Calendar.MONTH, month - 1);
        // get data
        ServerConn serverConn = new ServerConn("http://" + URLPath.home + ":8080/CareServer/getAttList", "10",3);
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

        int searchIndex = 0;

        for (int i = 0; i < mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            boolean state = false;
            for (int j = searchIndex; j < infoAttendances.length; j++) {
                String date = infoAttendances[j].getDate();
                String tokenDate[] = date.split("-");
                if (Integer.parseInt(tokenDate[2]) == i+1) {
                    dayList.add(new InfoAttendance("" + (i + 1), infoAttendances[j].getAtt_state()));
                    searchIndex ++;
                    state = true;
                    break;
                }
            }
            if (!state)
                dayList.add(new InfoAttendance("" + (i + 1), 0));
        }
    }
}


