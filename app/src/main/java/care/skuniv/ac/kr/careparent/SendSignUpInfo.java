package care.skuniv.ac.kr.careparent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SendSignUpInfo extends AsyncTask<Void,Void,String>{
    String answer;
    String token = FirebaseInstanceId.getInstance().getToken();
    InfoSign infoSignArrayList;

    SendSignUpInfo(InfoSign infoSignUpList){
       this.infoSignArrayList = infoSignUpList;
    }
    @Override
    protected String doInBackground(Void... params) {

        //request 를 보내줄 클라이언트 생성   (okhttp 라이브러리 사용)
        OkHttpClient client = new OkHttpClient();
        Response response;
        RequestBody requestBody = null;

        Log.d("<<<<<<token test>>>>>",""+token);
        //회원 정보를 보내는 부분

        Log.d("<<<<<<Ok>>>>>",">>>>>>>>>>>>>>>>>>>");
        requestBody = new FormBody.Builder().add("signUpInfo",toJsonForInfoSign(infoSignArrayList)).add("token",token).build();

        Request request = new Request.Builder()
                .url("http://117.17.142.132:8080/CareServer/getSignUpInfo")
                .post(requestBody)
                .build();
        try {
            response = client.newCall(request).execute(
            );
            /////////////////////////////////// newcall 하고 응답받기를 기다리는중
            answer = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return answer;
    }

    @Override
    protected void onPostExecute(String s) {
        //super.onPostExecute(s);

        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
    }
    public String toJsonForInfoSign(InfoSign signUpList) { // StrudentList를 받아오기위한 함수
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(signUpList);
    }

}
