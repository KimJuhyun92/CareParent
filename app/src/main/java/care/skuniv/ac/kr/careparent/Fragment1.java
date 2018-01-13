package care.skuniv.ac.kr.careparent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

public class Fragment1 extends Fragment {
    public Fragment1()
    {
        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, null);
        NoticeAdapter adapter = new NoticeAdapter(getActivity());
        ListView listview = (ListView) view.findViewById(R.id.noticeListView);
        listview.setAdapter(adapter);

        //헤더 연결
        View header = inflater.inflate(R.layout.notice_header, null, false);
        listview.addHeaderView(header);

        //db add
        ServerConn serverConn = new ServerConn("http://"+URLPath.url+":8080/CareServer/getNoticeList", 1);
        try {
            String result = serverConn.execute().get();
            Log.d("connect",result);
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            InfoNotice[] infoNotices = gson.fromJson(result, InfoNotice[].class);
            for (InfoNotice infoNotice : infoNotices) {
                try {
                    String title = infoNotice.getNotice_title();
                    title = URLDecoder.decode(title, "UTF-8");
                    infoNotice.setNotice_title(title);
                    String content = infoNotice.getNotice_content();
                    content = URLDecoder.decode(content, "UTF-8");
                    infoNotice.setNotice_content(content);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                adapter.add(infoNotice);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        return view;
    }
}