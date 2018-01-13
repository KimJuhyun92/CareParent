package care.skuniv.ac.kr.careparent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;

import javax.net.ssl.HandshakeCompletedEvent;

public class FirstActivity extends AppCompatActivity {

    private String parent_id="";
    private String saved_id="";
    private String saved_pw="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //아이디가 저장되어 있다면 받아옴
        SharedPreferences info = getSharedPreferences("info", Activity.MODE_PRIVATE);
        saved_id = info.getString("parent_id","none");
        //자동 로그인 부분
        ServerLogin serverLogin = new ServerLogin("http://" + URLPath.url + ":8080/CareServer/login", saved_id, saved_pw,2);
        try {
            String result = serverLogin.execute().get();
            Log.d("connect",result);
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            InfoLogin[] infoLogins = gson.fromJson(result, InfoLogin[].class);
            for(InfoLogin infoLogin : infoLogins) {
                parent_id = infoLogin.getParent_id();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(saved_id.equals(parent_id)){
            Intent intent = new Intent(FirstActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
