package care.skuniv.ac.kr.careparent;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import care.skuniv.ac.kr.careparent.R;

/**
 * 그리드뷰 어댑터
 */

public class CalendarAdapter extends BaseAdapter {
    private final List<InfoAttendance> list;
    private final LayoutInflater inflater;
    /**
     * 생성자
     *
     * @param context
     * @param list
     */

    private Calendar mCalendar;

    public CalendarAdapter(Context context, List<InfoAttendance> list) {
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class ViewHolder {
        TextView tvItemGridView;
        ImageView state;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public InfoAttendance getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.date, parent, false);
            holder = new ViewHolder();
            holder.tvItemGridView = (TextView) convertView.findViewById(R.id.tv_item_gridview);
            holder.state = (ImageView) convertView.findViewById(R.id.state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InfoAttendance infoAttendance = getItem(position);
        holder.tvItemGridView.setText("" + infoAttendance.getDate());

        switch (infoAttendance.getAtt_state()) {
            case 0 :
                holder.state.setVisibility(View.INVISIBLE);
                break;
            case 1 :
                holder.state.setImageResource(R.drawable.green);
                break;
            case 2 :
                holder.state.setImageResource(R.drawable.yellow);
                break;
            case 3 :
                holder.state.setImageResource(R.drawable.red);
                break;
            default: break;
        }
        //해당 날짜 텍스트 컬러,배경 변경
        mCalendar = Calendar.getInstance();

        //오늘 day 가져옴
        Integer today = mCalendar.get(Calendar.DAY_OF_MONTH);
        String sToday = String.valueOf(today);
        if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
            holder.tvItemGridView.setTextColor(convertView.getResources().getColor(R.color.colorPrimary));
        }
        return convertView;
    }
}
