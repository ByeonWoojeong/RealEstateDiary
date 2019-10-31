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
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class PassgetActivity extends AppCompatActivity {

    Context context;
    AQuery aQuery = null;
    ImageView back;
    TextView guide_txt_2;
    EditText phone, number;
    FrameLayout certify_con, ok_con;
    LinearLayout pass_get_layout;
    TextView certify, ok, password;
    ProgressBar progressBar;
    OneBtnDialog oneBtnDialog;
    int secCount;
    Handler handler = new Handler();
    boolean isClick;
    InputMethodManager imm;

    Runnable count = new Runnable() {
        public void run() {
            secCount --;
            handler.postDelayed(this, 1000);
            if (secCount < 0) {
                handler.removeCallbacks(count);
                isClick = false;
            }
        }
    };


    boolean checkEmail(String email){
        Pattern pattern = Pattern.compile("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
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
        setContentView(R.layout.activity_passget);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(PassgetActivity.this, true);
            }
        }

        aQuery = new AQuery(this);
        back = (ImageView) findViewById(R.id.back);
        phone = (EditText) findViewById(R.id.phone);
        number = (EditText) findViewById(R.id.number);
        guide_txt_2 = (TextView) findViewById(R.id.guide_txt_2);
        certify_con = (FrameLayout) findViewById(R.id.certify_con);
        certify = (TextView) findViewById(R.id.certify);
        ok_con = (FrameLayout) findViewById(R.id.ok_con);
        ok = (TextView) findViewById(R.id.ok);
        pass_get_layout = (LinearLayout) findViewById(R.id.pass_get_layout);
        password = (TextView) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        pass_get_layout.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                phone.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
                phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        } else {
            phone.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
            phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        }

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 13){
                    certify_con.setBackgroundColor(Color.parseColor("#7199ff"));
                    certify.setTextColor(Color.parseColor("#ffffff"));
                }else{
                    certify_con.setBackgroundResource(R.drawable.certify_background);
                    certify.setTextColor(Color.parseColor("#111111"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 4){
                    ok_con.setBackgroundColor(Color.parseColor("#7199ff"));
                    ok.setTextColor(Color.parseColor("#ffffff"));
                }else{
                    ok_con.setBackgroundResource(R.drawable.certify_background);
                    ok.setTextColor(Color.parseColor("#111111"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
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

                if (!isClick) {

                //인증 번호 발송 버튼 이벤트
                if ("".equals(phone.getText().toString()) || "" == phone.getText().toString()) {
                    oneBtnDialog = new OneBtnDialog(PassgetActivity.this, "핸드폰 번호를\n입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

                isClick = true;
                secCount = 30;
                count.run();

                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                String url = UrlManager.getBaseUrl() + "login/certsms";
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
                            @Override public void onFailure(Call call, IOException e) { //통신 실패
                            }

                            @Override public void onResponse(Call call, Response response) throws IOException {
                                final String res = response.body().string();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = new JSONObject(res);
                                            progressBar.setVisibility(View.GONE);
                                            if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                                Toast.makeText(getApplicationContext(), "인증 번호를 전송하였습니다.", Toast.LENGTH_SHORT).show();
                                                secCount = 30;
                                                count.run();
                                                certify.setEnabled(false);
                                            }else if(!jsonObject.getBoolean("return")){//return이 false 면?

                                                handler.removeCallbacks(count);
                                                isClick = false;
                                                if ("no".equals(jsonObject.getString("type"))) {
                                                    oneBtnDialog = new OneBtnDialog(PassgetActivity.this, "없는 번호입니다.", "확인");
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

//                Map<String, Object> params = new HashMap<String, Object>();
//                progressBar.setVisibility(View.VISIBLE);
//                params.put("phone", phone.getText().toString().trim());
//                aQuery.progress(progressBar).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                    @Override
//                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                        try {
//                            progressBar.setVisibility(View.GONE);
//                            Log.i("인증", "  " +jsonObject);
//                            if (jsonObject.getBoolean("return")) {    //인증번호 전송됨
//                                 Toast.makeText(getApplicationContext(), "인증 번호 전송 완료", Toast.LENGTH_SHORT).show();
//
//                                secCount = 30;
//                                count.run();
//                                certify.setEnabled(false);
//
//                            } else if (!jsonObject.getBoolean("return")) {    //서버 에러
//
//                                handler.removeCallbacks(count);
//                                isClick = false;
//
//                                Log.i("인증", "false");
//
//                                if ("no".equals(jsonObject.getString("type"))) {
//                                    oneBtnDialog = new OneBtnDialog(PassgetActivity.this, "없는 번호", "확인");
//                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                    oneBtnDialog.setCancelable(false);
//                                    oneBtnDialog.show();
//                                }
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
                } else {
                    Toast.makeText(PassgetActivity.this,  secCount + "초 후에 다시 인증해주세요.", Toast.LENGTH_SHORT).show();
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
                String url = UrlManager.getBaseUrl() + "login/verifysms";
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
                        @Override public void onFailure(Call call, IOException e) { //통신 실패
                        }

                        @Override public void onResponse(Call call, Response response) throws IOException {
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
                                                //휴대폰 번호 변경 완료
                                                pass_get_layout.setVisibility(View.VISIBLE);
                                                password.setText(jsonObject.getString("pass"));
                                            }
                                            if ("pass".equals(jsonObject.getString("type"))) {
                                                pass_get_layout.setVisibility(View.VISIBLE);
                                                password.setText(jsonObject.getString("pass"));
                                            }
                                        }else if(!jsonObject.getBoolean("return")){//return이 false 면?
                                            guide_txt_2.setText("* 인증 번호가 일치하지 않습니다.");
                                            guide_txt_2.setTextColor(Color.parseColor("#fa2b2b"));
                                            Toast.makeText(getApplicationContext(), "인증 번호 틀림", Toast.LENGTH_SHORT).show();
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
//                            Log.i("인증", " 확인 " +jsonObject);
//                            if (jsonObject.getBoolean("return")) {
//
//                                guide_txt_2.setText("* 인증 번호가 일치합니다.");
//                                guide_txt_2.setTextColor(Color.parseColor("#7199ff"));
//                                if ("ok".equals(jsonObject.getString("type"))) {
//                                    //휴대폰 번호 변경 완료
//                                    pass_get_layout.setVisibility(View.VISIBLE);
//                                    password.setText(jsonObject.getString("pass"));
//                                }
//
//                                if ("pass".equals(jsonObject.getString("type"))) {
//                                    pass_get_layout.setVisibility(View.VISIBLE);
//                                    password.setText(jsonObject.getString("pass"));
//                                }
//
//                            } else if (!jsonObject.getBoolean("return")) {
//
//                                guide_txt_2.setText("* 인증 번호가 일치하지 않습니다.");
//                                guide_txt_2.setTextColor(Color.parseColor("#fa2b2b"));
//                                Toast.makeText(getApplicationContext(), "인증 번호 틀림", Toast.LENGTH_SHORT).show();
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
