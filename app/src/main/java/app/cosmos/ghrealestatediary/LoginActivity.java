package app.cosmos.ghrealestatediary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.textclassifier.TextClassification;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import java.net.MalformedURLException;
import java.net.URL;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;


import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
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
import okhttp3.ResponseBody;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;
import static com.kakao.util.helper.Utility.getPackageInfo;

@SuppressLint("NewApi")
public class LoginActivity extends AppCompatActivity {

    Context context;
    SessionCallback mKakaocallback;
    AQuery aQuery = null;   //HTTP 통신 Library
    LinearLayout activity_login;
    ScrollView scrollView;
    EditText login_phone, login_password;
    TextView login_btn, join_go_btn, pass_get_btn, kakao_login;
    InputMethodManager ipm;
    FrameLayout login_btn_con, kakao_login_con;
    OneBtnDialog oneBtnDialog;

    LinearLayout layoutLogin;

    boolean keyboardShown(View rootView) {
        final int softKeyboardHeight = 100;

        //getWindowVisibleDisplayFrame(Rect)로 보이는 뷰의 사이즈를 구할 수 있다.
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);

        //크기, 밀도 및 글꼴 크기 조정과 같은 디스플레이에 대한 일반 정보를 설명하는 구조체.
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();

        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {   //키 해시 구하기
            PackageInfo info = getPackageManager().getPackageInfo("app.cosmos.ghrealestatediary", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(LoginActivity.this, true);
            }
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(LoginActivity.this, "인터넷이 연결되어 있지 않아 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
            System.exit(0); //프로그램 종료.
        }




//        SSLConnect ssl = new SSLConnect();
//        ssl.postHttps(UrlManager.getBaseUrl(), 1000, 1000);



        SslConnect sslConnect = new SslConnect(LoginActivity.this);
        sslConnect.postHttps(UrlManager.getBaseUrl(), 5000, 5000);
        context = getApplicationContext();
        aQuery = new AQuery(this);
        ipm = (InputMethodManager) LoginActivity.this.getSystemService(INPUT_METHOD_SERVICE);   //앱과 현재 입력 메소드 간의 상호작용을 중재.
        activity_login = (LinearLayout) findViewById(R.id.activity_login);
        login_phone = (EditText) findViewById(R.id.login_phone);
        login_password = (EditText) findViewById(R.id.login_password);
        login_btn_con = (FrameLayout) findViewById(R.id.login_btn_con);
        login_btn = (TextView) findViewById(R.id.login_btn);
        kakao_login = (TextView) findViewById(R.id.kakao_login);
        kakao_login_con = (FrameLayout) findViewById(R.id.kakao_login_con);
        join_go_btn = (TextView) findViewById(R.id.join_go_btn);
        pass_get_btn = (TextView) findViewById(R.id.pass_get_btn);
        layoutLogin = (LinearLayout) findViewById(R.id.layout_login);

        layoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_login.requestFocus();  //포커스를 준다.
                ipm.hideSoftInputFromWindow(layoutLogin.getWindowToken(), 0);   //키보드 숨기기
            }
        });

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        //getViewTreeObserver().addOnGlobalLayoutListener : 특정뷰가 완전히 로딩되었는지 체크.
        login_phone.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {  //뷰가 불러지고 나서 처리
                if (keyboardShown(login_phone.getRootView())) {
                    scrollView.smoothScrollTo(0, scrollView.getBottom());   //smoothScorollTo : 특정 위치만큼만 스크롤
                } else {
                    activity_login.requestFocus();
                    scrollView.smoothScrollTo(0, 0);
                }
            }
        });

        {
            SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
            final String getToken = get_token.getString("Token", "");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                login_phone.setInputType(android.text.InputType.TYPE_CLASS_PHONE);  //EditText에 번호만 입력되도록
                login_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher()); //폰 형식으로 변화(하이픈)ㄴ
            } else {
                login_phone.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
                login_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
                }
            login_phone.setEnabled(true);

            //setOnEditorActionListener : EditText 키보드 엔터키 기능 변경
            login_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    switch (actionId) {
                        case EditorInfo.IME_ACTION_NEXT:    //다음
                            login_btn.callOnClick(); //자동 클릭
                            break;
                        case EditorInfo.IME_ACTION_GO:  //이동
                            login_btn.callOnClick();
                            break;
                        case EditorInfo.IME_ACTION_SEND:    //보내기
                            login_btn.callOnClick();
                            break;
                        case EditorInfo.IME_ACTION_SEARCH:  //검색
                            login_btn.callOnClick();
                            break;
                        case EditorInfo.IME_ACTION_DONE:    //완료
                            login_btn.callOnClick();
                            break;
                        default:
                            return false;
                    }
                    return true;
                }
            });
            join_go_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    login_password.setText("");
                    Intent intent = new Intent(LoginActivity.this, JoinCertifyActivity.class);
                    startActivityForResult(intent, 1);
                }
            });
            pass_get_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    login_password.setText("");
                    Intent intent = new Intent(LoginActivity.this, PassgetActivity.class);
                    startActivityForResult(intent,1);
                }
            });
            login_btn_con.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login_btn.callOnClick();
                }
            });
            login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("".equals(login_phone.getText().toString()) || "" == login_phone.getText().toString()) {    //문자열 비교
                        oneBtnDialog = new OneBtnDialog(LoginActivity.this, "핸드폰 번호(ID)를\n입력해 주세요 !", "확인");
                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        oneBtnDialog.setCancelable(false);
                        oneBtnDialog.show();
                        return;
                    }
                    if ("".equals(login_password.getText().toString()) || "" == login_password.getText().toString()) {
                        oneBtnDialog = new OneBtnDialog(LoginActivity.this, "비밀번호를\n입력해 주세요 !", "확인");
                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        oneBtnDialog.setCancelable(false);
                        oneBtnDialog.show();
                        return;
                    }
//
//                    try {
//                        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//                        trustManagerFactory.init((KeyStore) null);
//                        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
//                        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
//                            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
//                        }
//                        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
//                        SSLContext sslContext = SslUtils.getSslContextForCertificateFile(context, "BPClass2RootCA-sha2.cer");
//                        OkHttpClient client = new OkHttpClient.Builder().sslSocketFactory(sslContext.getSocketFactory(), trustManager).build();
//                        RequestBody body = new FormBody.Builder().add("phone", login_phone.getText().toString()).add("pass", login_password.getText().toString()).build();
//                        Request request = new Request.Builder().url(UrlManager.getBaseUrl() + "login/pass").header("GHsoft-Agent", "" + getToken).header("User-Agent", "android").post(body).build();
//                        client.newCall(request).enqueue(new Callback() {
//                            @Override public void onFailure(Call call, IOException e) {
//                            }
//
//                            @Override public void onResponse(Call call, Response response) throws IOException {
//                                final String res = response.body().string();
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        try {
//                                            JSONObject jsonObject = new JSONObject(res);
//                                            Log.i("OKHTTP", " jsonObject " + jsonObject);
//                                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
//                                                SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
//                                                SharedPreferences.Editor editor = prefLoginChecked.edit();
//                                                editor.clear();
//                                                editor.putBoolean("loginChecked", true);
//                                                editor.putString("loginPhoneNumber", login_phone.getText().toString());
//                                                editor.commit();
//
//                                                runOnUiThread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        finish();
//                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                                        startActivity(intent);
//                                                        ipm.hideSoftInputFromWindow(login_password.getWindowToken(), 0);
//                                                    }
//                                                });
//
//                                            } else if (!jsonObject.getBoolean("return")) {
//                                                runOnUiThread(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        oneBtnDialog = new OneBtnDialog(LoginActivity.this, "비밀번호를 정확하게\n입력해 주세요 !", "확인");
//                                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                                        oneBtnDialog.setCancelable(false);
//                                                        oneBtnDialog.show();
//                                                    }
//                                                });
//                                            }
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                });
//                            }
//                        });
//                    } catch (NoSuchAlgorithmException e) {
//                        e.printStackTrace();
//                    } catch (KeyStoreException e) {
//                        e.printStackTrace();
//                    }


                    String url = UrlManager.getBaseUrl() + "login/pass";
                    final Map<String, Object> params = new HashMap<String, Object>();
                    params.put("phone", login_phone.getText().toString());  //작성한 폰번호를 서버 phone으로 보냄
                    params.put("pass", login_password.getText().toString());

                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            Log.i("HTTP", " " + jsonObject );
                            try {
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                    SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefLoginChecked.edit();
                                    editor.clear();
                                    editor.putBoolean("loginChecked", true);
                                    editor.putString("loginPhoneNumber", login_phone.getText().toString());
                                    editor.commit();

                                    finish();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    ipm.hideSoftInputFromWindow(login_password.getWindowToken(), 0);
                                } else if (!jsonObject.getBoolean("return")) {
//                                    if ("kakao".equals(jsonObject.getString("type"))) {
//                                        oneBtnDialog = new OneBtnDialog(LoginActivity.this, "카카오로 이미 회원가입이\n되어 있습니다.\n카카오로 로그인 해주세요 !", "확인");
//                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                        oneBtnDialog.setCancelable(false);
//                                        oneBtnDialog.show();
//                                    } else if ("detail".equals(jsonObject.getString("type"))) {
//                                        login_password.setText("");
//                                        Intent intent = new Intent(LoginActivity.this, JoinKakaoActivity.class);
//                                        intent.putExtra("name", jsonObject.getString("name"));
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                                        startActivityForResult(intent, 1);
//                                    } else if ("delete".equals(jsonObject.getString("type"))) {
//                                        oneBtnDialog = new OneBtnDialog(LoginActivity.this, "탈퇴 후 30일간\n재가입 불가합니다 !", "확인");
//                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                        oneBtnDialog.setCancelable(false);
//                                        oneBtnDialog.show();
//                                    } else if("logout".equals(jsonObject.getString("type"))){
//                                        Toast.makeText(LoginActivity.this, "다른 기기에서 로그인 하였습니다.", Toast.LENGTH_SHORT).show();
//                                        SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
//                                        SharedPreferences.Editor editor = prefLoginChecked.edit();
//                                        editor.clear();
//                                        editor.commit();
//                                        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
//                                        finish();
//                                        startActivity(intent);
//                                    } else {
                                        oneBtnDialog = new OneBtnDialog(LoginActivity.this, "비밀번호를 정확하게\n입력해 주세요 !", "확인");
                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        oneBtnDialog.setCancelable(false);
                                        oneBtnDialog.show();
//                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
                }
            });
        }

        kakao_login_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kakao_login.callOnClick();
            }
        });
        kakao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagement.requestLogout(new LogoutResponseCallback() { // 로그아웃
                    @Override
                    public void onCompleteLogout() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mKakaocallback = new SessionCallback();
                                        com.kakao.auth.Session.getCurrentSession().removeCallback(mKakaocallback);
                                        com.kakao.auth.Session.getCurrentSession().addCallback(mKakaocallback);
                                        LoginButton loginButton = (LoginButton) findViewById(R.id.kakaotalk_original_btn);
                                        loginButton.callOnClick();
                                    }
                                });
                            }
                        }).start();
                    }
                });
            }
        });

    }



    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            KakaorequestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Session.getCurrentSession().removeCallback(mKakaocallback);
            mKakaocallback = new SessionCallback();
            Session.getCurrentSession().addCallback(mKakaocallback);
        }
    }

    protected void KakaorequestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                int ErrorCode = errorResult.getErrorCode();
                int ClientErrorCode = -777;
                if (ErrorCode == ClientErrorCode) {
                    Toast.makeText(getApplicationContext(), "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    LogManager.print("오류로 카카오 로그인 실패");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                LogManager.print("오류로 카카오 로그인 실패");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                final String userName = userProfile.getNickname();
                final String userId = String.valueOf(userProfile.getId());
                final String profileUrl = userProfile.getProfileImagePath();

                SharedPreferences kakaotalkData = LoginActivity.this.getSharedPreferences("kakaotalkData", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = kakaotalkData.edit();
                editor.clear();
                editor.putString("kakao", userId + "");
                editor.putString("name", userName + "");
                editor.putString("url", profileUrl + "");
                editor.commit();

                SharedPreferences phone = getSharedPreferences("phone", Activity.MODE_PRIVATE);
                final String getPhone = phone.getString("phone", "");
                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");

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
                            .add("token", Session.getCurrentSession().getAccessToken()).build();
                    Request request = new Request.Builder().url(UrlManager.getBaseUrl() + "login/kakao").header("GHsoft-Agent", "" + getToken).header("User-Agent", "android").post(body).build();
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
                                            SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
                                            SharedPreferences.Editor editor2 = prefLoginChecked.edit();
                                            editor2.clear();
                                            editor2.putBoolean("loginChecked", true);
                                            editor2.putString("loginPhoneNumber", getPhone);
                                            editor2.putString("type", "kakao");
                                            editor2.commit();

                                            finish();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                            startActivity(intent);
                                        }else if(!jsonObject.getBoolean("return")){//return이 false 면?
                                            //추가 정보 입력
                                            finish();
                                            Intent intent = new Intent(LoginActivity.this, JoinKakaoActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                            startActivityForResult(intent, 1);
                                        }
                                        Session.getCurrentSession().removeCallback(mKakaocallback);
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







//                SharedPreferences phone = getSharedPreferences("phone", Activity.MODE_PRIVATE);
//                final String getPhone = phone.getString("phone", "");
//                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
//                final String getToken = get_token.getString("Token", "");
//                Log.i("LOGIN", " getToken " + getToken);
//                String url = UrlManager.getBaseUrl() + "login/kakao";
//                Map<String, Object> params = new HashMap<String, Object>();
//                params.put("token", Session.getCurrentSession().getAccessToken());
//                Log.i("LOGIN", " params " + params);
//                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                    @Override
//                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                        Log.i("LOGIN", " KAKAO "  + jsonObject);
//
//                        try {
//                            if (jsonObject.getBoolean("return")) {
//                                //로그인 성공
//                                SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
//                                SharedPreferences.Editor editor2 = prefLoginChecked.edit();
//                                editor2.clear();
//                                editor2.putBoolean("loginChecked", true);
//                                editor2.putString("loginPhoneNumber", getPhone);
//                                editor2.putString("type", "kakao");
//                                editor2.commit();
//
//                                finish();
//                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                                startActivity(intent);
//                            } else if (!jsonObject.getBoolean("return")) {

//                                if ("phone".equals(jsonObject.getString("type"))) {
//                                    //번호 인증
//                                    finish();
//                                    Intent intent = new Intent(LoginActivity.this, JoinCertifyActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                                    startActivityForResult(intent, 1);
//                                } else if ("detail".equals(jsonObject.getString("type"))) {
//                                    //추가 정보 입력
//                                    finish();
//                                    Intent intent = new Intent(LoginActivity.this, JoinKakaoActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                                    startActivityForResult(intent, 1);
//                                }
//                            }
//                            Session.getCurrentSession().removeCallback(mKakaocallback);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
            }

            @Override
            public void onNotSignedUp() {
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(mKakaocallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Session.getCurrentSession().removeCallback(mKakaocallback);
            mKakaocallback = new SessionCallback();
            Session.getCurrentSession().addCallback(mKakaocallback);
            return;
        }
        switch (resultCode) {
            case RESULT_CANCELED:
                break;
            case 999:
                setResult(999);
                finish();
                break;
            case 9999:

                finish();
                break;
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

    public class SSLConnect {
        // always verify the host - dont check for certificate
        final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        /**
         * Trust every server - don't check for any certificate
         */
        private void trustAllHosts() {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[] {};
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
            }};

            // Install the all-trusting trust manager
            try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public HttpsURLConnection postHttps(String url, int connTimeout, int readTimeout) {
            trustAllHosts();

            HttpsURLConnection https = null;
            try {
                https = (HttpsURLConnection) new URL(url).openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                https.setConnectTimeout(connTimeout);
                https.setReadTimeout(readTimeout);
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return https;
        }
    }

    public class SSLClasspathTrustStoreLoader {
        public void setTrustStore(String trustStore, String password) throws Exception {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream keystoreStream = SSLClasspathTrustStoreLoader.class.getResourceAsStream(trustStore);
            keystore.load(keystoreStream, password.toCharArray());
            trustManagerFactory.init(keystore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustManagers, null);
            SSLContext.setDefault(sc);
        }
    }

}
