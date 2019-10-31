package app.cosmos.ghrealestatediary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import app.cosmos.ghrealestatediary.DTO.Member;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*******************************************그룹 관리 (그룹원)******************************************************/

public class FirstFragmentGroup extends Fragment {

    private final String TAG = "FirstFragmentGroup";

    ListView memberListView;
    ProgressBar progress;
    SwipeRefreshLayout mSwipeRefreshLayout;

    AQuery aQuery = null;
    Context context;
    View view;
    String token, groupNumber;
    ArrayList<Member> data;
    MemberListAdapter memberListAdapter;
    Parcelable state;

    int type;

    OneBtnDialog oneBtnDialog;


    void refreshMemberList() {
        data.clear();
        SharedPreferences get_token = getActivity().getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = UrlManager.getBaseUrl() + "group/memberlist";

        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            SSLContext sslContext = SslUtils.getSslContextForCertificateFile(context, "BPClass2RootCA-sha2.cer");
            OkHttpClient client = new OkHttpClient.Builder().sslSocketFactory(sslContext.getSocketFactory(), trustManager).build();
            RequestBody body = new FormBody.Builder()
                    .add("group", groupNumber)
                    .build();
            Request request = new Request.Builder().url(url).header("GHsoft-Agent", "" + getToken).header("User-Agent", "android").post(body).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) { //통신 실패
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String res = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                data.clear();
                                JSONObject jsonObject = new JSONObject(res);
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                    JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                        Log.i("MEMBERACTIVITY", " " + getJsonObject);
                                        data.add(new Member(getJsonObject.getString("name"), getJsonObject.getString("phone"), getJsonObject.getString("idx"), getJsonObject.getInt("type"), groupNumber));
                                    }
                                    memberListView.setAdapter(memberListAdapter);
                                    memberListAdapter.notifyDataSetChanged();

                                } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                    memberListView.setAdapter(memberListAdapter);
                                    memberListAdapter.notifyDataSetChanged();

                                    if ("pay".equals(jsonObject.getString("type"))) {
                                        //기간 만료
                                        Intent intent = new Intent(context, BillingListActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                }
                                if (state != null) {
                                    memberListView.onRestoreInstanceState(state);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }



//        Map<String, Object> params = new HashMap<String, Object>();
//        progress.setVisibility(View.VISIBLE);
//        params.put("group", groupNumber);
//        aQuery.progress(progress).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                try {
//                    Log.i(TAG, "" + jsonObject);
//                    progress.setVisibility(View.GONE);
//                    data.clear();
//                    if (jsonObject.getBoolean("return")) {
//                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
//                        for (int i = 0; i<jsonArray.length(); i++) {
//                            JSONObject getJsonObject = jsonArray.getJSONObject(i);
//                            data.add(new Member(getJsonObject.getString("name"), getJsonObject.getString("phone"), getJsonObject.getString("idx"), getJsonObject.getInt("type"), groupNumber));
//                        }
//                        memberListView.setAdapter(memberListAdapter);
//                        memberListAdapter.notifyDataSetChanged();
//                    } else if (!jsonObject.getBoolean("return")) {
//                        memberListView.setAdapter(memberListAdapter);
//                        memberListAdapter.notifyDataSetChanged();
//
//                        if ("pay".equals(jsonObject.getString("type"))) {
//                            //기간 만료
//                            Intent intent = new Intent(context, BillingListActivity.class);
//                            startActivity(intent);
//                            getActivity().finish();
//                        }
//                    }
//                    if (state != null) {
//                        memberListView.onRestoreInstanceState(state);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
    }

    @Override
    public void onPause() {
        super.onPause();
        state = memberListView.onSaveInstanceState();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first_group, container, false);
        SharedPreferences prefToken = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        token = prefToken.getString("Token", "");
        aQuery = new AQuery(context);

        progress = view.findViewById(R.id.progress);
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        Intent intent = getActivity().getIntent();
        groupNumber = intent.getStringExtra("group");



        memberListView = view.findViewById(R.id.memberListView);
        data = new ArrayList<Member>();
        memberListAdapter = new MemberListAdapter(context, R.layout.list_member, data, memberListView, groupNumber, type);

        refreshMemberList();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public class OneBtnDialog extends Dialog {
        OneBtnDialog oneBtnDialog = this;
        Context context;

        public OneBtnDialog(final Context context, final String text, final String btnText) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_one_btn_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title1 = (TextView) findViewById(R.id.title1);
            TextView title2 = (TextView) findViewById(R.id.title2);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            title2.setVisibility(View.GONE);
            title1.setText(text);
            btn1.setText(btnText);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oneBtnDialog.dismiss();
                }
            });
        }
    }
}
