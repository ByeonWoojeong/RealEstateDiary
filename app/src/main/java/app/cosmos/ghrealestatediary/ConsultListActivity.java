package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

import app.cosmos.ghrealestatediary.DTO.Consult;
import app.cosmos.ghrealestatediary.DTO.Main;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class ConsultListActivity extends AppCompatActivity {

    public static Context context;
    AQuery aQuery = null;
    ImageView back;
    FrameLayout next_con;
    TextView next;
    Parcelable state;
    ArrayList<Consult> data;
    ConsultListAdapter consultListAdapter;
    ListView listView;
    SwipeRefreshLayout mSwipeRefresh;
    ProgressBar progressBar;

    void refreshList() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = UrlManager.getBaseUrl() + "memo/list";
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
            progressBar.setVisibility(View.VISIBLE);
            RequestBody body = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder().url(url).header("GHsoft-Agent", "" + getToken).header("User-Agent", "android").post(body).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) { //통신 실패
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String res = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                data.clear();
                                JSONObject jsonObject = new JSONObject(res);
                                progressBar.setVisibility(View.GONE);
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                    JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                                    if (jsonArray.length() == 0) {
                                        listView.setVisibility(View.GONE);
                                    } else {
                                        listView.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                            data.add(new Consult(getJsonObject.getString("idx"), getJsonObject.getString("name")));
                                        }
                                        listView.setAdapter(consultListAdapter);
                                        consultListAdapter.notifyDataSetChanged();
                                    }
                                } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                    listView.setVisibility(View.GONE);
                                    listView.setAdapter(consultListAdapter);
                                    consultListAdapter.notifyDataSetChanged();
                                }
                                if (state != null) {
                                    listView.onRestoreInstanceState(state);
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
//        progressBar.setVisibility(View.VISIBLE);
//        aQuery.progress(progressBar).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                try {
//                    Log.i("MEMO", " " + jsonObject);
//                    progressBar.setVisibility(View.GONE);
//                    data.clear();
//                    if (jsonObject.getBoolean("return")) {
//                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
//                        if(jsonArray.length()==0){
//                            listView.setVisibility(View.GONE);
//                        }else{
//                            listView.setVisibility(View.VISIBLE);
//                            for (int i = 0; i<jsonArray.length(); i++) {
//                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
//                                data.add(new Consult(getJsonObject.getString("idx"), getJsonObject.getString("name")));
//                            }
//                            listView.setAdapter(consultListAdapter);
//                            consultListAdapter.notifyDataSetChanged();
//                        }
//
//
//                    } else if (!jsonObject.getBoolean("return")) {
//                        listView.setVisibility(View.GONE);
//                        listView.setAdapter(consultListAdapter);
//                        consultListAdapter.notifyDataSetChanged();
//                    }
//                    if (state != null) {
//                        listView.onRestoreInstanceState(state);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    @Override
    public void onPause() {
        super.onPause();
        state = listView.onSaveInstanceState();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_list);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(ConsultListActivity.this, true);
            }
        }
        aQuery = new AQuery(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next_con = (FrameLayout) findViewById(R.id.next_con);
        next = (TextView) findViewById(R.id.next);
        next_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.callOnClick();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultListActivity.this, ConsultWriteActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        listView = (ListView) findViewById(R.id.memberListView);
        data = new ArrayList<Consult>();
        consultListAdapter = new ConsultListAdapter(ConsultListActivity.this, R.layout.list_consult, data, listView);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#e3e3e3"), android.graphics.PorterDuff.Mode.SRC_IN);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefresh.setColorSchemeResources(R.color.baseColor, R.color.baseColor);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                state = null;
                refreshList();
                mSwipeRefresh.setRefreshing(false);
            }
        });
//        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
//        final String getToken = get_token.getString("Token", "");
//        String url = UrlManager.getBaseUrl() + "/member/mypage";
//        Map<String, Object> params = new HashMap<String, Object>();
//        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                try {
//                    if (jsonObject.getBoolean("return")) {
//                        if ("만료".equals(jsonObject.getJSONObject("data").getString("date"))) {
//                            setResult(999);
//                            finish();
//                        }
//                    } else if (!jsonObject.getBoolean("return")) {
//                        if("logout".equals(jsonObject.getString("type"))){
//                            Toast.makeText(ConsultListActivity.this, "다른 기기에서 로그인 하였습니다.", Toast.LENGTH_SHORT).show();
//                            SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = prefLoginChecked.edit();
//                            editor.clear();
//                            editor.commit();
//                            Intent intent = new Intent(ConsultListActivity.this, LoginActivity.class);
//                            finish();
//                            startActivity(intent);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
    }

}
