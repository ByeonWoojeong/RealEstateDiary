package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class PushActivity extends AppCompatActivity {

    Context context;
    private static final int REQUEST_CODE = 999;
    AQuery aQuery = null;
    //    boolean vibCheck = true;
//    String pushRing = "";
    ImageView back;
    FrameLayout menu1_con, menu2_con;
    View menu2_line;
    SwitchCompat menu1_btn, menu2_btn;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        setContentView(R.layout.activity_push);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(PushActivity.this, true);
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
        menu1_con = (FrameLayout) findViewById(R.id.menu1_con);
        menu2_con = (FrameLayout) findViewById(R.id.menu2_con);
        menu2_line = (View) findViewById(R.id.menu2_line);
        menu1_btn = (SwitchCompat) findViewById(R.id.menu1_btn);
        menu2_btn = (SwitchCompat) findViewById(R.id.menu2_btn);
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        String url = UrlManager.getBaseUrl() + "member/getpush";
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
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                    if ("1".equals(jsonObject.getJSONObject("data").getString("total"))) {
                                        menu1_btn.setChecked(true);
                                    } else {
                                        menu1_btn.setChecked(false);
                                    }

                                    if ("1".equals(jsonObject.getJSONObject("data").getString("deal"))) {
                                        menu2_btn.setChecked(true);
                                    } else {
                                        menu2_btn.setChecked(false);
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
//                    Log.i("PUSH" , "GET " + jsonObject);
//
//                    if (jsonObject.getBoolean("return")) {
//
//                        if("1".equals(jsonObject.getJSONObject("data").getString("total"))){
//                            menu1_btn.setChecked(true);
//                        }else{
//                            menu1_btn.setChecked(false);
//                        }
//
//                        if("1".equals(jsonObject.getJSONObject("data").getString("deal"))){
//                            menu2_btn.setChecked(true);
//                        }else{
//                            menu2_btn.setChecked(false);
//                        }
//
//                    } else if (!jsonObject.getBoolean("return")) {
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.header("User-Agent", "gh_mobile{" + getToken + "} android"));

//        SharedPreferences prefPushRing = PushActivity.this.getSharedPreferences("prefPushRing", Activity.MODE_PRIVATE);
//        pushRing = prefPushRing.getString("prefPushRing", "");
//        if (!"".equals(pushRing+"")) {
//            RingtoneManager ringtoneManager = new RingtoneManager(this);
//            Ringtone alarmRingtone = ringtoneManager.getRingtone(this, Uri.parse(pushRing));
//            if ("알 수 없는 벨소리".equals(alarmRingtone.getTitle(this)+"")||"null".equals(alarmRingtone.getTitle(this)+"")){
//                menu2_text.setText("무음");
//            } else {
//                menu2_text.setText(alarmRingtone.getTitle(this));
//            }
//        } else {
//            menu2_text.setText("기본 벨소리");
//        }
//        SharedPreferences prefVibCheck = PushActivity.this.getSharedPreferences("prefVibCheck", Activity.MODE_PRIVATE);
//        vibCheck = prefVibCheck.getBoolean("prefVibCheck", true);
//        if (vibCheck) {
//            menu3_btn.setChecked(true);
//        } else {
//            menu3_btn.setChecked(false);
//        }
        menu1_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu1_btn.performClick();
            }
        });
        menu1_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                String url = UrlManager.getBaseUrl() + "member/setpush";
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

                    if (isChecked) {

                        RequestBody body1 = new FormBody.Builder()
                                .add("total", "1")
                                .build();
                        Request request = new Request.Builder().url(url).header("GHsoft-Agent", "" + getToken).header("User-Agent", "android").post(body1).build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override public void onFailure(Call call, IOException e) { //통신 실패
                            }

                            @Override public void onResponse(Call call, Response response) throws IOException {
                                final String res = response.body().string();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = new JSONObject(res);
                                            if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                            }else if(!jsonObject.getBoolean("return")){//return이 false 면?

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                } else {

                        RequestBody body0 = new FormBody.Builder()
                                .add("total", "0")
                                .build();
                        Request request = new Request.Builder().url(url).header("GHsoft-Agent", "" + getToken).header("User-Agent", "android").post(body0).build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override public void onFailure(Call call, IOException e) { //통신 실패
                            }

                            @Override public void onResponse(Call call, Response response) throws IOException {
                                final String res = response.body().string();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = new JSONObject(res);
                                            if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                            }else if(!jsonObject.getBoolean("return")){//return이 false 면?

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                }

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                }

//                Map<String, Object> params = new HashMap<String, Object>();
//                if (isChecked) {
//                    params.put("total", 1);
//                } else {
//                    params.put("total", 0);
//                }
//                Log.i("PUSH", "SET " + params);
//                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                    @Override
//                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                        try {
//                            Log.i("PUSH", "SET " + jsonObject);
//
//                            if (jsonObject.getBoolean("return")) {
//
//                            } else if (!jsonObject.getBoolean("return")) {
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.header("User-Agent", "gh_mobile{" + getToken + "} android"));
            }
        });
        menu2_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu2_btn.performClick();
            }
        });
        menu2_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                String url = UrlManager.getBaseUrl() + "member/setpush";
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

                    if (isChecked) {

                        RequestBody body1 = new FormBody.Builder()
                                .add("deal", "1")
                                .build();
                        Request request = new Request.Builder().url(url).header("GHsoft-Agent", "" + getToken).header("User-Agent", "android").post(body1).build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override public void onFailure(Call call, IOException e) { //통신 실패
                            }

                            @Override public void onResponse(Call call, Response response) throws IOException {
                                final String res = response.body().string();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = new JSONObject(res);
                                            if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                            }else if(!jsonObject.getBoolean("return")){//return이 false 면?

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                    } else {

                        RequestBody body0 = new FormBody.Builder()
                                .add("deal", "0")
                                .build();
                        Request request = new Request.Builder().url(url).header("GHsoft-Agent", "" + getToken).header("User-Agent", "android").post(body0).build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override public void onFailure(Call call, IOException e) { //통신 실패
                            }

                            @Override public void onResponse(Call call, Response response) throws IOException {
                                final String res = response.body().string();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = new JSONObject(res);
                                            if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                            }else if(!jsonObject.getBoolean("return")){//return이 false 면?

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                    }

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                }

//                Map<String, Object> params = new HashMap<String, Object>();
//                if (isChecked) {
//                    params.put("deal", 1);
//                } else {
//                    params.put("deal", 0);
//                }
//                Log.i("PUSH", "SET " + params);
//                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                    @Override
//                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                        try {
//                            Log.i("PUSH", "SET " + jsonObject);
//
//                            if (jsonObject.getBoolean("return")) {
//
//                            } else if (!jsonObject.getBoolean("return")) {
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.header("User-Agent", "gh_mobile{" + getToken + "} android"));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE) {
//            Uri pickedUri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
//            SharedPreferences prefPushRing = this.getSharedPreferences("prefPushRing", Activity.MODE_PRIVATE);
//            SharedPreferences.Editor editor = prefPushRing.edit();
//            editor.clear();
//            editor.putString("prefPushRing", pickedUri+"");
//            editor.commit();
//            RingtoneManager ringtoneManager = new RingtoneManager(this);
//            Ringtone alarmRingtone = ringtoneManager.getRingtone(this, pickedUri);
//            if ("알 수 없는 벨소리".equals(alarmRingtone.getTitle(this)+"")) {
//                menu2_text.setText("무음");
//            } else {
//                menu2_text.setText(alarmRingtone.getTitle(this));
//            }
        }
    }
}
