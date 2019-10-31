package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import app.cosmos.ghrealestatediary.DTO.Notice;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class NoticeActivity extends AppCompatActivity {

    Context context;
    AQuery aQuery = null;
    InputMethodManager ipm;
    String token, idx;
    ExpandableListView listView;
    ArrayList<Notice> data = null;
    ArrayList<ArrayList<Notice>> data2;
    NoticeListAdapter adapter = null;
    JSONObject jsonObjectList;
    OneBtnDialog oneBtnDialog;
    ProgressBar progressBar;

    @Override
    protected void onResume() {
        super.onResume();
        String url = UrlManager.getBaseUrl() + "main/notice";
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
            Request request = new Request.Builder().url(url).header("GHsoft-Agent", "" + token).header("User-Agent", "android").post(body).build();
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
                            data = new ArrayList<Notice>();
                            data2 = new ArrayList<ArrayList<Notice>>();
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                progressBar.setVisibility(View.GONE);
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                    final JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        jsonObjectList = jsonArray.getJSONObject(i);
                                        data.add(new Notice(jsonObjectList.getString("idx"), jsonObjectList.getString("title"), jsonObjectList.getString("content"), jsonObjectList.getString("date")));
                                        data2.add(new ArrayList<Notice>());
                                        data2.get(i).add(new Notice(jsonObjectList.getString("idx"), jsonObjectList.getString("title"), jsonObjectList.getString("content"), jsonObjectList.getString("date")));
                                    }
                                    adapter = new NoticeListAdapter(NoticeActivity.this, listView, R.layout.list_faq1, R.layout.list_faq2, data, data2);
                                    listView.setAdapter(adapter);
                                    if (idx != null) {
                                        adapter.clickItem(idx);
                                    }
                                } else if (!jsonObject.getBoolean("return")) {//return이 false 면?

                                    data.clear();
                                    data2.clear();
                                    adapter = new NoticeListAdapter(NoticeActivity.this, listView, R.layout.list_faq1, R.layout.list_faq2, data, data2);
                                    listView.setAdapter(adapter);
                                    oneBtnDialog = new OneBtnDialog(NoticeActivity.this, "공지사항이 없습니다 !", "확인");
                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    oneBtnDialog.setCancelable(false);
                                    oneBtnDialog.show();
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
//        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                data = new ArrayList<Notice>();
//                data2 = new ArrayList<ArrayList<Notice>>();
//                try {
//                    if (jsonObject.getBoolean("return")) {
//                        final JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            jsonObjectList = jsonArray.getJSONObject(i);
//                            data.add(new Notice(jsonObjectList.getString("idx"), jsonObjectList.getString("title"), jsonObjectList.getString("content"), jsonObjectList.getString("date")));
//                            data2.add(new ArrayList<Notice>());
//                            data2.get(i).add(new Notice(jsonObjectList.getString("idx"), jsonObjectList.getString("title"), jsonObjectList.getString("content"), jsonObjectList.getString("date")));
//                        }
//                        adapter = new NoticeListAdapter(NoticeActivity.this, listView, R.layout.list_faq1, R.layout.list_faq2, data, data2);
//                        listView.setAdapter(adapter);
//                        if (idx != null) {
//                            adapter.clickItem(idx);
//                        }
//                    } else {
//                        data.clear();
//                        data2.clear();
//                        adapter = new NoticeListAdapter(NoticeActivity.this, listView, R.layout.list_faq1, R.layout.list_faq2, data, data2);
//                        listView.setAdapter(adapter);
//                        oneBtnDialog = new OneBtnDialog(NoticeActivity.this, "검색 결과가 없습니다 !", "확인");
//                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        oneBtnDialog.setCancelable(false);
//                        oneBtnDialog.show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.header("GHsoft-Agent", "" + token).header("User-Agent", "android"));
//        .header("GHsoft-Agent", "" + token));
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
        setContentView(R.layout.activity_notice);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(NoticeActivity.this, true);
            }
        }
        aQuery = new AQuery(this);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#7199ff"), android.graphics.PorterDuff.Mode.SRC_IN);
        Intent intent = getIntent();
        idx = intent.getStringExtra("idx");
        ipm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        SharedPreferences prefToken = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        token = prefToken.getString("Token", "");
        listView = (ExpandableListView) findViewById(R.id.memberListView);
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
