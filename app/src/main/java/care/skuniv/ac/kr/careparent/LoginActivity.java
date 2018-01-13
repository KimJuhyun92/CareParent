package care.skuniv.ac.kr.careparent;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private String input_id = " ";
    private String input_password = " ";
    private EditText id_edit, password_edit;
    private Button login_button;
    private Button signUp_button;
    private String parent_id;
    private String parent_pw;
    private String std_no;
    private String state;
    private String saved_id;
    private String saved_stdNo;

    SharedPreferences info;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        info = getSharedPreferences("info", Activity.MODE_PRIVATE);
        editor = info.edit();

        id_edit = (EditText)findViewById(R.id.editText_id);
        password_edit = (EditText)findViewById(R.id.editText_password);
        login_button = (Button)findViewById(R.id.button_login);
        signUp_button = (Button)findViewById(R.id.button_signUp);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_id = id_edit.getText().toString();
                input_password = password_edit.getText().toString();

                if(input_id != null && input_password != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Log.d("check", input_id+", " + input_password + "!!!");
                    ServerLogin serverLogin = new ServerLogin("http://" + URLPath.url + ":8080/CareServer/login", input_id, input_password, 2);
                    try {
                        String result = serverLogin.execute().get();
                        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                        InfoLogin[] infoLogins = gson.fromJson(result, InfoLogin[].class);
                        for(InfoLogin infoLogin : infoLogins) {
                            parent_id = infoLogin.getParent_id();
                            parent_pw = infoLogin.getParent_pw();
                            std_no = infoLogin.getStd_no();
                            state = infoLogin.getState();
                        }
                        Log.d("check parent_id", parent_id);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if(state.equals("0")){
                        Toast.makeText(LoginActivity.this, "아이디 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if(state.equals("1")) {
                        Log.d("test11111111",info.getString("std_no","none"));
                        //SharedPreference에 값 저장
                        editor.putString("parent_id", parent_id);
                        editor.putString("std_no",std_no);
                        editor.commit(); //완료한다.
                        startActivity(intent);
                        finish();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "아이디 혹은 비밀번호를 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
}
