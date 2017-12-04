package care.skuniv.ac.kr.careparent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;

public class SignUp extends AppCompatActivity {
    private EditText id, pw, std_name, grade, parent_hp;
    private Button signUp , cancel;
    private String getId, getPw, getStd_name, getGrade, getParent_hp;
    private InfoSign infoSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        id = (EditText)findViewById(R.id.edit_id);
        pw = (EditText)findViewById(R.id.edit_password);
        std_name = (EditText)findViewById(R.id.edit_stdname);
        grade = (EditText)findViewById(R.id.edit_grade);
        parent_hp = (EditText)findViewById(R.id.edit_hp);
        signUp = (Button)findViewById(R.id.btn_sign);
        cancel = (Button)findViewById(R.id.btn_cancel);
        Log.d("test", "<<<<<<<<<<<<test>>>>>>>>>>>>>>>>>");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoSignUp = new InfoSign();
                infoSignUp.setId(id.getText().toString());
                infoSignUp.setPw(pw.getText().toString());
                infoSignUp.setStd_name(std_name.getText().toString());
                infoSignUp.setGrade(grade.getText().toString());
                infoSignUp.setParent_hp(parent_hp.getText().toString());
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Log.d("token",refreshedToken);

                SendSignUpInfo sendSignUpInfo = new SendSignUpInfo(infoSignUp);
                sendSignUpInfo.execute();


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Log.d("token",refreshedToken);
//                String msg ="hihihihi";
//                PushMessage sendMessage = new PushMessage("http://117.17.142.132:8080/CareServer/pushMessage",msg);
//                sendMessage.execute();
//                Log.d("message" ,""+ msg);
            }
        });
    }
}
