package care.skuniv.ac.kr.careparent;

/**
 * Created by 김주현 on 2017-12-01.
 */

public class InfoAttendance {
    private String date;
    private int att_state;
    public InfoAttendance(String date, int att_state) {
        this.date = date;
        this.att_state = att_state;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAtt_state() {
        return att_state;
    }

    public void setAtt_state(int att_state) {
        this.att_state = att_state;
    }
}
