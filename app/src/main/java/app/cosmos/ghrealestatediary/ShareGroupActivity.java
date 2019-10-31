package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import app.cosmos.ghrealestatediary.DTO.Group;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class ShareGroupActivity extends AppCompatActivity {

    Context context;
    ImageView back;
    FrameLayout under_text_con;
    TextView under_text;
    ListView my_group_listview;
    ArrayList<Group> groupData;
    GroupListAdapter groupListAdapter;
    Parcelable state;
    OneBtnDialog oneBtnDialog;
    ProgressBar progressBar;
    ArrayList<String> idx;
    int myType;

    AQuery aQuery = null;

    void refreshGroupList() {
        state = null;
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        String url = UrlManager.getBaseUrl() + "group/list";
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
                                JSONObject jsonObject = new JSONObject(res);
                                groupData.clear();
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                    JSONArray jsonArray = new JSONArray(jsonObject.getString("mylist"));
                                    if (jsonArray.length() == 0) {
                                        my_group_listview.setVisibility(View.GONE);
                                    } else {
                                        my_group_listview.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                            groupData.add(new Group(getJsonObject.getString("group"), getJsonObject.getString("name"), getJsonObject.getString("phone"), getJsonObject.getInt("type")));
                                        }
                                        my_group_listview.setAdapter(groupListAdapter);
                                        groupListAdapter.notifyDataSetChanged();
                                    }
                                } else if (!jsonObject.getBoolean("return")) {//return이 false 면?

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
//                try {
//                    Log.i("ShareGroupActivity", " groupList 2 " + jsonObject);
//                    groupData.clear();
//                    if (jsonObject.getBoolean("return")) {
//                        JSONArray jsonArray = new JSONArray(jsonObject.getString("mylist"));
//                        Log.i("ShareGroupActivity", " group list jsonArray " + jsonArray);
//                        if(jsonArray.length()==0){
//                            my_group_listview.setVisibility(View.GONE);
//                        }else{
//                            my_group_listview.setVisibility(View.VISIBLE);
//                            for (int i = 0; i<jsonArray.length(); i++) {
//                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
//                                groupData.add(new Group(getJsonObject.getString("group"), getJsonObject.getString("name"), getJsonObject.getString("phone"), getJsonObject.getInt("type")));
//                            }
//                            my_group_listview.setAdapter(groupListAdapter);
//                            groupListAdapter.notifyDataSetChanged();
//                        }
//
//                    } else if (!jsonObject.getBoolean("return")) {
//
//                        my_group_listview.setVisibility(View.GONE);
//                        my_group_listview.setAdapter(groupListAdapter);
//                        groupListAdapter.notifyDataSetChanged();
//
//                        if("pay".equals(jsonObject.getString("type"))){
//                            //기간 만료
//                        }
//                    }
//                    if (state != null) {
//                        my_group_listview.onRestoreInstanceState(state);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_group);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(ShareGroupActivity.this, true);
            }
        }

        idx = (ArrayList<String>) getIntent().getStringArrayListExtra("listIdx");
        Log.i("ShareGroupActivity", " " + idx);
        SharedPreferences shareGroupList = getSharedPreferences("shareGroupList", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = shareGroupList.edit();
        editor.clear();
        editor.commit();

        aQuery = new AQuery(this);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#7199ff"), android.graphics.PorterDuff.Mode.SRC_IN);
        back = findViewById(R.id.back);
        under_text_con = findViewById(R.id.under_text_con);
        under_text = findViewById(R.id.under_text);
        my_group_listview = findViewById(R.id.my_group_listview);
        groupData = new ArrayList<Group>();
        groupListAdapter = new GroupListAdapter(ShareGroupActivity.this, R.layout.list_group, groupData, my_group_listview, myType, "share");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        under_text_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                under_text.callOnClick();
            }
        });
        under_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences shareGroupList = getSharedPreferences("shareGroupList", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = shareGroupList.edit();
                Map<String, ?> keys = shareGroupList.getAll();
                ArrayList<String> group = new ArrayList<String>();
                Log.i("shareGroupList", "keys " + keys.size());
                if (0 != keys.size()) {
                    String getShareGroup = "";
                    int i = 0;
                    for (Map.Entry<String, ?> entry : keys.entrySet()) {
                        group.add(i, entry.getValue().toString());
                        if (i != 0) {
                            getShareGroup += " ";
                        }
                        getShareGroup += entry.getValue().toString();
                        i++;
                    }
                }

                if (0 == groupListAdapter.getCountCheck()) {
                    oneBtnDialog = new OneBtnDialog(ShareGroupActivity.this, "그룹을 하나라도\n 선택해 주세요!", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                } else {

                    SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    String url = UrlManager.getBaseUrl() + "group/add";
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
                        FormBody.Builder formBuilder = new FormBody.Builder();
                        for (int i = 0; i < group.size(); i++) {
                            formBuilder.add("group[" + i + "]", group.get(i));
                        }
                        for (int i = 0; i < idx.size(); i++) {
                            formBuilder.add("idx[" + i + "]", idx.get(i));
                        }
                        RequestBody body = formBuilder.build();
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
                                            JSONObject jsonObject = new JSONObject(res);
                                            progressBar.setVisibility(View.GONE);
                                            if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                                Toast.makeText(ShareGroupActivity.this, "매물 공유에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                                setResult(123);
                                                finish();
                                            } else if (!jsonObject.getBoolean("return")) {//return이 false 면?

                                                oneBtnDialog = new OneBtnDialog(ShareGroupActivity.this, "이미 공유된\n 매물 입니다!", "확인");
                                                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                oneBtnDialog.setCancelable(false);
                                                oneBtnDialog.show();

                                                if ("pay".equals(jsonObject.getString("type"))) {
                                                    //기간 만료
                                                    Intent intent = new Intent(ShareGroupActivity.this, BillingListActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
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

//                    Map<String, Object> params = new HashMap<String, Object>();
//                    for (int i = 0; i < group.size(); i++){
//                        params.put("group["+i+"]", group.get(i));
//                    }
//                    for (int i = 0; i < idx.size(); i++){
//                        params.put("idx["+i+"]", idx.get(i));
//                    }
////                    params.put("group", group);
////                    params.put("idx", idx);
//                    Log.i("ShareGroupActivity", "params :: " + params);
//                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            try {
//                                Log.i("ShareGroupActivity", " jsonObject :: " + jsonObject);
//                                if (jsonObject.getBoolean("return")) {
//                                    Toast.makeText(ShareGroupActivity.this, "매물 공유에 성공하였습니다.", Toast.LENGTH_SHORT).show();
//                                    setResult(123);
//                                    finish();
//                                } else if (!jsonObject.getBoolean("return")) {
//
//                                    oneBtnDialog = new OneBtnDialog(ShareGroupActivity.this, "이미 공유된\n 매물 입니다!", "확인");
//                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                    oneBtnDialog.setCancelable(false);
//                                    oneBtnDialog.show();
//
//                                    if("pay".equals(jsonObject.getString("type"))) {
//                                        //기간 만료
//                                        Intent intent = new Intent(ShareGroupActivity.this, BillingListActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
                }
            }
        });

        refreshGroupList();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    public void onPause() {
        super.onPause();
        state = my_group_listview.onSaveInstanceState();
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
