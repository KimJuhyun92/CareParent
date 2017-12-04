package care.skuniv.ac.kr.careparent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by 김주현 on 2017-11-24.
 */

public class NoticeAdapter extends BaseAdapter implements View.OnClickListener{

    private LayoutInflater layoutInflater;

    // Activity에서 가져온 객체정보를 저장할 변수
    private InfoNotice mNotice;
    private Context mContext;

    // 리스트 아이템 데이터를 저장할 배열
    private ArrayList<InfoNotice> mNoticeData;

    // ListView 내부 View들을 가르킬 변수들
    private TextView index, date, title,content;
    private Button checkNotice;
    String noticeContent;

    public NoticeAdapter (Context context){
        super();
        mNoticeData = new ArrayList<InfoNotice>();
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mNoticeData.size();
    }

    @Override
    public InfoNotice getItem(int position) {
        return mNoticeData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        mContext = parent.getContext();
//        View v = View.inflate(mContext, R.layout.notice, null);
        if (v == null) {
            // inflater를 이용하여 사용할 레이아웃을 가져옵니다.
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.notice, parent, false);
        }

        // 레이아웃이 메모리에 올라왔기 때문에 이를 이용하여 포함된 뷰들을 참조할 수 있습니다.
        index = (TextView) v.findViewById(R.id.index);
        date = (TextView) v.findViewById(R.id.date);
        title = (TextView) v.findViewById(R.id.title);
        checkNotice = (Button) v.findViewById(R.id.checkNotice);

        // 받아온 position 값을 이용하여 배열에서 아이템을 가져온다.
        mNotice = getItem(position);

        // Tag를 이용하여 데이터와 뷰를 묶습니다.
        index.setTag(mNotice);
        date.setTag(mNotice);
        title.setTag(mNotice);
        checkNotice.setTag(mNotice);

        // 데이터의 실존 여부를 판별합니다.
        if (mNotice != null) {
            checkNotice.setOnClickListener(this);
            index.setText(String.valueOf(mNotice.getNotice_id()));
            date.setText(String.valueOf(mNotice.getNotice_date()));
            title.setText(String.valueOf(mNotice.getNotice_title()));
        }
        else{
            index.setText("");
            date.setText("");
            title.setText("");
        }
        return v;
    }
    // 데이터를 추가하는 것을 위해서 만들어 준다.
    public void add(InfoNotice infoNotice) {
        mNoticeData.add(infoNotice);
    }

    @Override
    public void onClick(View v) {
        final InfoNotice infoNotice = (InfoNotice) v.getTag();
        switch (v.getId()) {
            case R.id.checkNotice:
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("공지사항 내용");
                content = new TextView(mContext);
                content.setText(String.valueOf(infoNotice.getNotice_content()));
                dialog.setView(content);

                dialog.setNegativeButton("확인",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                dialog.show();
        }
    }
}
