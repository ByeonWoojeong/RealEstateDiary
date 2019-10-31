package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class JoinCertifyActivity extends AppCompatActivity {

    Context context;
    WebView webView;
    ImageView back;
    FrameLayout certify_con, ok_con, next_con;
    LinearLayout total_con, service_agree_con, info_agree_con;
    TextView certify, ok, guide_txt_1, guide_txt_2, txt_total_agree, txt_service_agree, txt_info_agree, next;
    EditText phone, number;
    CheckBox checkbox_total, checkbox_service, checkbox_info;
    ProgressBar progressBar;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;
    String joinSuccess;
    String sns = "";
    boolean isClick;
    int secCount;
    Handler handler = new Handler();

    AgreeDialog agreeDialog;

    Runnable count = new Runnable() {
        public void run() {
            secCount--;
            handler.postDelayed(this, 1000);
            if (secCount < 0) {
                handler.removeCallbacks(count);
                isClick = false;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_certify);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(JoinCertifyActivity.this, true);
            }
        }

        final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        webView = findViewById(R.id.popup);
        back = findViewById(R.id.back);
        certify_con = findViewById(R.id.certify_con);
        ok_con = findViewById(R.id.ok_con);
        next_con = findViewById(R.id.next_con);
        certify = findViewById(R.id.certify);
        ok = findViewById(R.id.ok);
        guide_txt_1 = findViewById(R.id.guide_txt_1);
        guide_txt_2 = findViewById(R.id.guide_txt_2);
        total_con = findViewById(R.id.total_con);
        service_agree_con = findViewById(R.id.service_agree_con);
        info_agree_con = findViewById(R.id.info_agree_con);
        txt_total_agree = findViewById(R.id.txt_total_agree);
        txt_service_agree = findViewById(R.id.txt_service_agree);
        txt_info_agree = findViewById(R.id.txt_info_agree);
        next = findViewById(R.id.next);
        phone = findViewById(R.id.phone);
        number = findViewById(R.id.number);
        checkbox_total = findViewById(R.id.checkbox_total);
        checkbox_service = findViewById(R.id.checkbox_service);
        checkbox_info = findViewById(R.id.checkbox_info);
        progressBar = findViewById(R.id.progress);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        aQuery = new AQuery(this);


        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    certify_con.setBackgroundColor(Color.parseColor("#7199ff"));
                    certify.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    certify_con.setBackgroundResource(R.drawable.certify_background);
                    certify.setTextColor(Color.parseColor("#111111"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    ok_con.setBackgroundColor(Color.parseColor("#7199ff"));
                    ok.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    ok_con.setBackgroundResource(R.drawable.certify_background);
                    ok.setTextColor(Color.parseColor("#111111"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        certify_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                certify.callOnClick();
            }
        });
        certify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imm.hideSoftInputFromWindow(phone.getWindowToken(), 0);

                if (!isClick) {
                    isClick = true;
                    secCount = 30;
                    count.run();
                    SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    String url = UrlManager.getBaseUrl() + "join/certsms";
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
                                .add("phone", phone.getText().toString().trim())
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
                                            progressBar.setVisibility(View.GONE);
                                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                                Toast.makeText(JoinCertifyActivity.this, "인증 번호를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                                            } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                                handler.removeCallbacks(count);
                                                isClick = false;

                                                if ("db".equals(jsonObject.getString("type"))) {
                                                    oneBtnDialog = new OneBtnDialog(JoinCertifyActivity.this, "Error to Server\n관리자에게 문의해주세요.", "확인");
                                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    oneBtnDialog.setCancelable(false);
                                                    oneBtnDialog.show();
                                                } else if ("sms".equals(jsonObject.getString("type"))) {
                                                    //sms 에러 (문자 갯수 부족, 번호 부정확)
                                                    oneBtnDialog = new OneBtnDialog(JoinCertifyActivity.this, "인증 번호를 전송할 수 없습니다.\n관리자에게 문의해주세요.", "확인");
                                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    oneBtnDialog.setCancelable(false);
                                                    oneBtnDialog.show();
                                                } else if ("dup".equals(jsonObject.getString("type"))) {
                                                    oneBtnDialog = new OneBtnDialog(JoinCertifyActivity.this, "이미 등록된 \n휴대폰 번호입니다.", "확인");
                                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    oneBtnDialog.setCancelable(false);
                                                    oneBtnDialog.show();
                                                } else if ("data".equals(jsonObject.getString("type"))) {
                                                    oneBtnDialog = new OneBtnDialog(JoinCertifyActivity.this, "휴대폰 번호를 입력해주세요.", "확인");
                                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    oneBtnDialog.setCancelable(false);
                                                    oneBtnDialog.show();
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
//                    progressBar.setVisibility(View.VISIBLE);
//                    params.put("phone", phone.getText().toString().trim());
//                    aQuery.progress(progressBar).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            try {
//                                progressBar.setVisibility(View.GONE);
//                                Log.i("인증", "" + jsonObject);
//                                if (jsonObject.getBoolean("return")) {    //인증번호 전송됨
//
//                                    Toast.makeText(JoinCertifyActivity.this, "인증번호 전송 완료", Toast.LENGTH_SHORT).show();
//
//                                } else if (!jsonObject.getBoolean("return")) {    //서버 에러
//                                    handler.removeCallbacks(count);
//                                    isClick = false;
//
//                                    Log.i("인증", "false");
//
//                                    if ("db".equals(jsonObject.getString("type"))) {
//                                        oneBtnDialog = new OneBtnDialog(JoinCertifyActivity.this, "Error to Server\n관리자에게 문의해주세요.", "확인");
//                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                        oneBtnDialog.setCancelable(false);
//                                        oneBtnDialog.show();
//                                    } else if ("sms".equals(jsonObject.getString("type"))) {
//                                        //sms 에러 (문자 갯수 부족, 번호 부정확)
//                                        oneBtnDialog = new OneBtnDialog(JoinCertifyActivity.this, "인증 번호를 전송할 수 없습니다.\n관리자에게 문의해주세요.", "확인");
//                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                        oneBtnDialog.setCancelable(false);
//                                        oneBtnDialog.show();
//                                    } else if ("dup".equals(jsonObject.getString("type"))) {
//                                        oneBtnDialog = new OneBtnDialog(JoinCertifyActivity.this, "이미 등록된 \n휴대폰 번호입니다.", "확인");
//                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                        oneBtnDialog.setCancelable(false);
//                                        oneBtnDialog.show();
//                                    } else if ("data".equals(jsonObject.getString("type"))) {
//                                        oneBtnDialog = new OneBtnDialog(JoinCertifyActivity.this, "휴대폰 번호를 입력해주세요.", "확인");
//                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                        oneBtnDialog.setCancelable(false);
//                                        oneBtnDialog.show();
//                                    }
//
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
                } else {
                    Toast.makeText(JoinCertifyActivity.this, secCount + "초 후에 다시 인증해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ok_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.callOnClick();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imm.hideSoftInputFromWindow(number.getWindowToken(), 0);

                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                String url = UrlManager.getBaseUrl() + "join/verifysms";
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
                            .add("phone", phone.getText().toString().trim())
                            .add("cert", number.getText().toString().trim())
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
                                        progressBar.setVisibility(View.GONE);
                                        if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                            guide_txt_2.setText("* 인증 번호가 일치합니다.");
                                            guide_txt_2.setTextColor(Color.parseColor("#7199ff"));

                                            if ("ok".equals(jsonObject.getString("type"))) {
                                                //휴대폰 번호 변경 완료 메인페이지
                                                SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = prefLoginChecked.edit();
                                                editor.clear();
//                                    editor.putBoolean("loginChecked", true);
//                                    editor.putString("type", "kakao");
                                                editor.commit();
                                                joinSuccess = "1";
                                                Intent intent = new Intent(JoinCertifyActivity.this, MainActivity.class);
                                                setResult(999);
                                                finish();
                                                startActivityForResult(intent, 1);
                                            }

                                            if ("detail".equals(jsonObject.getString("type"))) {
                                                if (jsonObject.getBoolean("sns")) {
                                                    sns = "sns";
                                                } else {
                                                    sns = "normal";
                                                }
                                            }
                                        } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                            guide_txt_2.setText("* 인증 번호가 일치하지 않습니다.");
                                            guide_txt_2.setTextColor(Color.parseColor("#fa2b2b"));
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

//                Map<String, Object> params = new HashMap<String, Object>();
//                progressBar.setVisibility(View.VISIBLE);
//                params.put("phone", phone.getText().toString().trim());
//                params.put("cert", number.getText().toString().trim());
//                aQuery.progress(progressBar).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                    @Override
//                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                        try {
//                            progressBar.setVisibility(View.GONE);
//                            if (jsonObject.getBoolean("return")) {
//                                guide_txt_2.setText("* 인증 번호가 일치합니다.");
//                                guide_txt_2.setTextColor(Color.parseColor("#7199ff"));
//
//                                if ("ok".equals(jsonObject.getString("type"))) {
//                                    //휴대폰 번호 변경 완료 메인페이지
//                                    SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = prefLoginChecked.edit();
//                                    editor.clear();
////                                    editor.putBoolean("loginChecked", true);
////                                    editor.putString("type", "kakao");
//                                    editor.commit();
//                                    joinSuccess = "1";
//                                    Intent intent = new Intent(JoinCertifyActivity.this, MainActivity.class);
//                                    setResult(999);
//                                    finish();
//                                    startActivityForResult(intent, 1);
//                                }
//
//                                if ("detail".equals(jsonObject.getString("type"))) {
//                                    if (jsonObject.getBoolean("sns")) {
//                                        sns = "sns";
//                                    } else {
//                                        sns = "normal";
//                                    }
//                                }
//                            } else if (!jsonObject.getBoolean("return")) {
//                                guide_txt_2.setText("* 인증 번호가 일치하지 않습니다.");
//                                guide_txt_2.setTextColor(Color.parseColor("#fa2b2b"));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
            }
        });

        //전체 동의
        total_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_total_agree.callOnClick();
            }
        });
        txt_total_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //서비스 이용약관
        service_agree_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_service_agree.callOnClick();
            }
        });
        txt_service_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                webView.loadUrl(UrlManager.getBaseUrl() + "/main/policy/1");
                agreeDialog = new AgreeDialog(JoinCertifyActivity.this, "1");
                agreeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                agreeDialog.setCancelable(false);
                agreeDialog.show();
            }
        });

        //개인정보 취급방침
        info_agree_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_info_agree.callOnClick();
            }
        });
        txt_info_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                agreeDialog = new AgreeDialog(JoinCertifyActivity.this, "2");
                agreeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                agreeDialog.setCancelable(false);
                agreeDialog.show();
            }
        });

        checkbox_total.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.i("CHECK", "" + isChecked);
                if (isChecked) {
                    checkbox_service.setChecked(true);
                    checkbox_info.setChecked(true);
                } else {
                    if (checkbox_service.isChecked() && checkbox_info.isChecked()) {
                        checkbox_service.setChecked(false);
                        checkbox_info.setChecked(false);
                    }

                }
            }
        });

        checkbox_service.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("CHECK", "" + isChecked);
                if (checkbox_service.isChecked() && checkbox_info.isChecked()) {
                    checkbox_total.setChecked(true);
                } else {
                    checkbox_total.setChecked(false);
                }
            }
        });
        checkbox_info.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("CHECK", "" + isChecked);
                if (checkbox_service.isChecked() && checkbox_info.isChecked()) {
                    checkbox_total.setChecked(true);
                } else {
                    checkbox_total.setChecked(false);
                }
            }
        });

        next_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.callOnClick();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("".equals(sns)) {
                    //휴대폰 인증을 해주세요.
                    oneBtnDialog = new OneBtnDialog(JoinCertifyActivity.this, "휴대폰 인증을 해주세요.", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                } else if ("sns".equals(sns) || "normal".equals(sns)) {
                    if (checkbox_total.isChecked()) {
                        next_con.setBackgroundColor(Color.parseColor("#7199ff"));
                        if ("sns".equals(sns)) {
                            //카카오 가입
                            Intent intent = new Intent(JoinCertifyActivity.this, JoinKakaoActivity.class);
                            setResult(999);
                            finish();
                            startActivityForResult(intent, 1);
                        } else if ("normal".equals(sns)) {
                            //일반 가입
                            Intent intent = new Intent(JoinCertifyActivity.this, JoinNomalActivity.class);
                            setResult(999);
                            finish();
                            startActivityForResult(intent, 1);
                        }

                    } else {
                        oneBtnDialog = new OneBtnDialog(JoinCertifyActivity.this, "이용 약관을 동의해주세요.", "확인");
                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        oneBtnDialog.setCancelable(false);
                        oneBtnDialog.show();
                    }
                }

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }


    public class AgreeDialog extends Dialog {
        AgreeDialog agreeDialog = this;
        Context context;

        public AgreeDialog(final Context context, final String type) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_agree_dialog);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.80);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, height);
            this.context = context;
            final TextView title1 = (TextView) findViewById(R.id.title1);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            title1.setText("");
            btn1.setText("확인");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agreeDialog.dismiss();
                }
            });
            SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
            String getToken = get_token.getString("Token", "");
            String url = UrlManager.getBaseUrl() + "main/policy/" + type;
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
                                        title1.setText(jsonObject.getJSONObject("data").getString("content"));
                                    }else if(!jsonObject.getBoolean("return")){//return이 false 면?
                                        Toast.makeText(context, "정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
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

//            Map<String, Object> params = new HashMap<>();
//            aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                @Override
//                public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                    Log.i("POLICY", " " + jsonObject);
//                    try {
//                        if (jsonObject.getBoolean("return")) {
//
//                            title1.setText(jsonObject.getJSONObject("data").getString("content"));
//                        } else {
//
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));

        }
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
