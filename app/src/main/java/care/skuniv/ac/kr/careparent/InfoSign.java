package care.skuniv.ac.kr.careparent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by 김주현 on 2017-11-20.
 */

public class InfoSign {
    private String id, pw, std_name, grade, parent_hp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getStd_name() {
        return std_name;
    }

    public void setStd_name(String std_name) {
        this.std_name = std_name;
        try {
            this.std_name = URLEncoder.encode(this.std_name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getParent_hp() {
        return parent_hp;
    }

    public void setParent_hp(String parent_hp) {
        this.parent_hp = parent_hp;
    }
}
