package care.skuniv.ac.kr.careparent;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 김주현 on 2017-11-16.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService
{
    String TAG = "TEST";
    String strUrl;
    String result;

    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<TOKEN>>>>>>>>>>>>>>>>>>>>>>>" );

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    private void sendRegistrationToServer(String token) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("uid", "aa")
                .add("token", token)
                .build();

        //request
        Request request = new Request.Builder()
                .url("http://124.5.71.132:8080/CareServer/getPushMsg")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

