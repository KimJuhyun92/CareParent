package care.skuniv.ac.kr.careparent;

import android.os.AsyncTask;

import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * Created by 김주현 on 2017-11-11.
 */


public class ServerLogin extends AsyncTask<Void, Void, String> {
    String answer;
    String url;
    int type;
    String parent_id;
    String parent_pw;


    ServerLogin(String url, String parent_id, int type) {
        this.url = url;
        this.parent_id = parent_id;
        this.type = type;
    }

    ServerLogin(String url, String parent_id, String parent_pw, int type) {
        this.url = url;
        this.parent_id = parent_id;
        this.parent_pw = parent_pw;
        this.type = type;
    }

    @Override
    protected String doInBackground(Void... params) {

        //request 를 보내줄 클라이언트 생성   (okhttp 라이브러리 사용)
        OkHttpClient client = new OkHttpClient();
        Response response;
        RequestBody requestBody = null;

        switch (type){
            case 1:
                requestBody = new FormBody.Builder().add("parent_id",parent_id).build();
                break;
            case 2:
                requestBody = new FormBody.Builder().add("parent_id",parent_id).add("parent_pw",parent_pw).build();
                break;
        }
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try {
            response = client.newCall(request).execute();
            /////////////////////////////////// newcall 하고 응답받기를 기다리는중
            answer = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("answer", ""+answer);
        return answer;
    }

    @Override
    protected void onPostExecute(String s) {
        //super.onPostExecute(s);
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
    }

    public RequestBody selectStudentList(RequestBody requestBody) {
        return requestBody = new FormBody.Builder().build();
    }
}
