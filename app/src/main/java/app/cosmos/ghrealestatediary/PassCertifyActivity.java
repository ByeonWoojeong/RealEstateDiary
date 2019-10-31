package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
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
import android.webkit.WebView;
import android.widget.CheckBox;
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

public class PassCertifyActivity extends AppCompatActivity {

    Context context;
    ImageView back;
    FrameLayout certify_con, ok_con, next_con;
    TextView certify, ok, guide_txt_1, guide_txt_2, next;
    EditText phone, number;
    ProgressBar progressBar;
    AQuery aQuery = null;
    OneBtnDialog oneBtnDialog;
    String step = "";
    boolean isClick;
    int secCount;
    Handler handler = new Handler();

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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_certify);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(PassCertifyActivity.this, true);
            }
        }

        final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        aQuery = new AQuery(this);
        back = findViewById(R.id.back);
        certify_con = findViewById(R.id.certify_con);
        ok_con = findViewById(R.id.ok_con);
        next_con = findViewById(R.id.next_con);
        certify = findViewById(R.id.certify);
        ok = findViewById(R.id.ok);
        guide_txt_1 = findViewById(R.id.guide_txt_1);
        guide_txt_2 = findViewById(R.id.guide_txt_2);
        next = findViewById(R.id.next);
        phone = findViewById(R.id.phone);
        number = findViewById(R.id.number);
        progressBar = findViewById(R.id.progress);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#7199ff"), android.graphics.PorterDuff.Mode.SRC_IN);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

        //인증 번호 전송
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
                    String url = UrlManager.getBaseUrl() + "login/certsms";
                    Map<String, Object> params = new HashMap<String, Object>();
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

                                                Toast.makeText(PassCertifyActivity.this, "인증번호를 전송 하였습니다.", Toast.LENGTH_SHORT).show();
                                                step = "send";
                                            } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                                handler.removeCallbacks(count);
                                                isClick = false;
                                                step = "fail";
                                                oneBtnDialog = new OneBtnDialog(PassCertifyActivity.this, "인증 번호 전송 오류\n다시 시도해주세요.", "확인");
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

//                    progressBar.setVisibility(View.VISIBLE);
//                    params.put("phone", phone.getText().toString().trim());
//                    aQuery.progress(progressBar).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            try {
//                                progressBar.setVisibility(View.GONE);
//                                Log.i("인증", "" + jsonObject);
//                                if (jsonObject.getBoolean("return")) {    //인증번호 전송됨
//                                    Toast.makeText(PassCertifyActivity.this, "인증번호 전송 완료", Toast.LENGTH_SHORT).show();
//                                    step = "send";
//
//                                } else if (!jsonObject.getBoolean("return")) {    //서버 에러
//                                    handler.removeCallbacks(count);
//                                    isClick = false;
//                                    step = "fail";
//                                    oneBtnDialog = new OneBtnDialog(PassCertifyActivity.this, "인증 번호 전송 오류\n다시 시도해주세요.", "확인");
//                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                    oneBtnDialog.setCancelable(false);
//                                    oneBtnDialog.show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
                } else {
                    Toast.makeText(PassCertifyActivity.this, secCount + "초 후에 다시 인증해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //인증 번호 확인
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
                String url = UrlManager.getBaseUrl() + "member/verifyphone";
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
                                            step = "ok";
                                            next_con.setBackgroundColor(Color.parseColor("#7199ff"));
                                        } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                            guide_txt_2.setText("* 인증 번호가 일치하지 않습니다.");
                                            guide_txt_2.setTextColor(Color.parseColor("#fa2b2b"));
                                            step = "fail";
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
//                            Log.i("인증", " 확인 " + jsonObject);
//                            if (jsonObject.getBoolean("return")) {    //인증번호 전송됨
//                                guide_txt_2.setText("* 인증 번호가 일치합니다.");
//                                guide_txt_2.setTextColor(Color.parseColor("#7199ff"));
//                                step = "ok";
//                                next_con.setBackgroundColor(Color.parseColor("#7199ff"));
//                            } else if (!jsonObject.getBoolean("return")) {
//                                guide_txt_2.setText("* 인증 번호가 일치하지 않습니다.");
//                                guide_txt_2.setTextColor(Color.parseColor("#fa2b2b"));
//                                step = "fail";
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
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

                if ("".equals(phone.getText().toString())) {
                    //휴대폰 인증을 해주세요.
                    oneBtnDialog = new OneBtnDialog(PassCertifyActivity.this, "휴대폰 번호 인증을\n해주세요.", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                } else if ("".equals(number.getText().toString())) {
                    //휴대폰 인증을 해주세요.
                    oneBtnDialog = new OneBtnDialog(PassCertifyActivity.this, "인증 확인을 해주세요.", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                } else if ("".equals(step)) {
                    oneBtnDialog = new OneBtnDialog(PassCertifyActivity.this, "휴대폰 번호 인증을\n해주세요.", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                } else if ("send".equals(step)) {
                    oneBtnDialog = new OneBtnDialog(PassCertifyActivity.this, "인증 번호를\n확인해주세요.", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                } else if ("fail".equals(step)) {
                    oneBtnDialog = new OneBtnDialog(PassCertifyActivity.this, "휴대폰 번호 인증 확인을\n해주세요.", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                } else if ("ok".equals(step)) {
                    next_con.setBackgroundColor(Color.parseColor("#7199ff"));
                    Intent intent = new Intent(PassCertifyActivity.this, PassChangeActivity.class);
                    setResult(999);
                    finish();
                    startActivityForResult(intent, 1);
                }

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
