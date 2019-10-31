package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.ArrayList;
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

public class JoinKakaoActivity extends AppCompatActivity {

    Context context;
    AQuery aQuery = null;
    InputMethodManager ipm;
    boolean isKeyBoardVisible;
    FrameLayout next_con;
    LinearLayout join_kakao, address_next_con, email_con, name_con, realestate_name_con, realestate_tel_con, rec_phone_con;
    TextView next, realestate_tel_area;
    EditText name, address, realestate_name, realestate_tel, rec_phone;
    ImageView back, address_next;
    String joinSuccess, address1;
    ArrayList<String> address2 = new ArrayList<String>();
    OneBtnDialog oneBtnDialog;
    ProgressBar progressBar;
    boolean isName, isAddress, isRName, isRTel;

    boolean checkEmail(String email) {
        Pattern pattern = Pattern.compile("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }

    boolean checkNumber(String number) {
        boolean checkAreaNumber = Pattern.matches("(\\d{3,4})(\\d{4})", number);
        return checkAreaNumber;
    }

    boolean checkPhoneNumber(String number) {
        boolean checkPhoneNumber = Pattern.matches("(01[016789])(\\d{3,4})(\\d{4})", number);
        return checkPhoneNumber;
    }

    boolean keyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    @Override
    public void finish() {
        super.finish();
        if (joinSuccess != null) {
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        } else {
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isKeyBoardVisible) {
            ipm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_kakao);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(JoinKakaoActivity.this, true);
            }
        }
        aQuery = new AQuery(this);
        Intent intent = getIntent();
        String getName = intent.getStringExtra("name");
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#7199ff"), android.graphics.PorterDuff.Mode.SRC_IN);
        join_kakao = (LinearLayout) findViewById(R.id.join_kakao);
        ipm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        back = (ImageView) findViewById(R.id.back);
        address_next_con = (LinearLayout) findViewById(R.id.address_next_con);
        next_con = (FrameLayout) findViewById(R.id.next_con);
        name_con = findViewById(R.id.name_con);
        realestate_name_con = findViewById(R.id.realestate_name_con);
        realestate_tel_con = findViewById(R.id.realestate_tel_con);
        rec_phone_con = findViewById(R.id.rec_phone_con);
        next = (TextView) findViewById(R.id.next);
        name = (EditText) findViewById(R.id.name);
        name.setText(getName);
        name.setRawInputType(EditorInfo.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        name.addTextChangedListener(textWatcherName);
        address = (EditText) findViewById(R.id.address);
        address.addTextChangedListener(textWatcherAddress);
        address_next = (ImageView) findViewById(R.id.address_next);
        realestate_name = (EditText) findViewById(R.id.realestate_name);
        realestate_name.setRawInputType(EditorInfo.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        realestate_name.addTextChangedListener(textWatcherRName);
        realestate_tel_area = (TextView) findViewById(R.id.realestate_tel_area);
        realestate_tel = (EditText) findViewById(R.id.realestate_tel);
        rec_phone = (EditText) findViewById(R.id.rec_phone);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rec_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher("KR"));   //EditText 에 전화번호 입력시 자동서식화
        } else {
            rec_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        }
        SharedPreferences shard_phone = getSharedPreferences("phone", Activity.MODE_PRIVATE);
        final String getPhone = shard_phone.getString("phone", "");
        SharedPreferences addr1CheckList = getSharedPreferences("addr1CheckList", Activity.MODE_PRIVATE);
        SharedPreferences addr2CheckList = getSharedPreferences("addr2CheckList", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = addr1CheckList.edit();
        editor.clear();
        editor.commit();
        SharedPreferences.Editor editor2 = addr2CheckList.edit();
        editor2.clear();
        editor2.commit();

        name_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.requestFocus();
            }
        });

        realestate_name_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realestate_name.requestFocus();
            }
        });

        realestate_tel_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realestate_tel.requestFocus();
            }
        });

        rec_phone_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rec_phone.requestFocus();
            }
        });

        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        address_next.callOnClick();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        address_next.callOnClick();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        address_next.callOnClick();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        address_next.callOnClick();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        address_next.callOnClick();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        address_next_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_next.callOnClick();
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_next.callOnClick();
            }
        });
        address_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinKakaoActivity.this, Addr1SelectActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        realestate_tel.addTextChangedListener(new TextWatcher() {
            @Override   //입력되는 텍스트에 변화가 있을 때
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (checkNumber(realestate_tel.getText().toString())) {
                    if (7 == realestate_tel.getText().toString().length()) {
                        realestate_tel.setText(realestate_tel.getText().toString().substring(0, 3) + "-" + realestate_tel.getText().toString().substring(3));
                        rec_phone.requestFocus();
                    }
                    return;
                }

                if (s.length() > 0) {
                    isRTel = true;
                } else {
                    isRTel = false;
                }
            }

            @Override   //입력이 끝났을 때
            public void afterTextChanged(Editable arg0) {

                if (isName && isAddress && isRName && isRTel) {
                    next.setTextColor(getResources().getColor(R.color.baseColor));
                } else {
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
            }

            @Override   //입력하기 전에
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        rec_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        next.callOnClick();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        next.callOnClick();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        next.callOnClick();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        next.callOnClick();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        next.callOnClick();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                if (isKeyBoardVisible) {
                    ipm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                if ("".equals(name.getText().toString()) || "" == name.getText().toString()) {
                    oneBtnDialog = new OneBtnDialog(JoinKakaoActivity.this, "이름을 입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if ("".equals(address.getText().toString()) || "" == address.getText().toString()) {
                    oneBtnDialog = new OneBtnDialog(JoinKakaoActivity.this, "지역을 선택해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if ("".equals(realestate_name.getText().toString()) || "" == realestate_name.getText().toString()) {
                    oneBtnDialog = new OneBtnDialog(JoinKakaoActivity.this, "부동산 이름을\n입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if ("".equals(realestate_tel.getText().toString()) || "" == realestate_tel.getText().toString()) {
                    oneBtnDialog = new OneBtnDialog(JoinKakaoActivity.this, "부동산 연락처를\n입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if (!checkNumber(realestate_tel.getText().toString().replaceAll("-", "").replaceAll("\\)", "").trim())) {
                    oneBtnDialog = new OneBtnDialog(JoinKakaoActivity.this, "부동산 연락처를\n사실적으로 입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if (!"".equals(rec_phone.getText().toString().trim())) {
                    if (!checkPhoneNumber(rec_phone.getText().toString().replaceAll("-", "").replaceAll("\\)", "").trim())) {
                        oneBtnDialog = new OneBtnDialog(JoinKakaoActivity.this, "추천인 전화번호\n형식을 맞춰주세요 !", "확인");
                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        oneBtnDialog.setCancelable(false);
                        oneBtnDialog.show();
                        return;
                    }
                }
                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                String url = UrlManager.getBaseUrl() + "join/profile";
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
                    FormBody.Builder formBuilder = new FormBody.Builder()
                            .add("name", name.getText().toString().trim())
                            .add("addr1", address1);
                    for (int i = 0; i < address2.size(); i++) {
                        formBuilder.add("addr2[" + i + "]", address2.get(i));
                    }
                    formBuilder.add("realestate_name", realestate_name.getText().toString().trim())
                            .add("realestate_tel", realestate_tel_area.getText().toString().trim() + realestate_tel.getText().toString().trim())
                            .add("rec_phone", rec_phone.getText().toString().trim());

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

                                            SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = prefLoginChecked.edit();
                                            editor.clear();
                                            editor.putBoolean("loginChecked", true);
                                            editor.putString("loginPhoneNumber", getPhone);
                                            editor.putString("type", "kakao");
                                            editor.commit();
                                            joinSuccess = "1";
                                            Intent intent = new Intent(JoinKakaoActivity.this, MainActivity.class);
                                            setResult(999);
                                            finish();
                                            startActivityForResult(intent, 1);
                                        } else if (!jsonObject.getBoolean("return")) {//return이 false 면?

                                            oneBtnDialog = new OneBtnDialog(JoinKakaoActivity.this, "이미 사용중인\n전화번호 입니다 !", "확인");
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

//                Map<String, Object> params = new HashMap<String, Object>();
//                params.put("name", name.getText().toString().trim());
//                params.put("addr1", address1);
//                for (int i = 0; i < address2.size(); i++){
//                    params.put("addr2["+i+"]", address2.get(i));
//                }
//                params.put("realestate_name", realestate_name.getText().toString().trim());
//                params.put("realestate_tel", realestate_tel_area.getText().toString().trim() + realestate_tel.getText().toString().trim());
//                params.put("rec_phone", rec_phone.getText().toString().trim());
//                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                    @Override
//                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                        try {
//                            Log.i("KAKAO", "" + jsonObject.toString());
//                            if (jsonObject.getBoolean("return")) {
//                                SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = prefLoginChecked.edit();
//                                editor.clear();
//                                editor.putBoolean("loginChecked", true);
//                                editor.putString("loginPhoneNumber", getPhone);
//                                editor.putString("type", "kakao");
//                                editor.commit();
//                                joinSuccess = "1";
//                                Intent intent = new Intent(JoinKakaoActivity.this, MainActivity.class);
//                                setResult(999);
//                                finish();
//                                startActivityForResult(intent, 1);
//                            } else if (!jsonObject.getBoolean("return")) {
//
//                                if("logout".equals(jsonObject.getString("type"))){
//                                    Toast.makeText(JoinKakaoActivity.this, "다른 기기에서 로그인 하였습니다.", Toast.LENGTH_SHORT).show();
//                                    SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = prefLoginChecked.edit();
//                                    editor.clear();
//                                    editor.commit();
//                                    Intent intent = new Intent(JoinKakaoActivity.this, LoginActivity.class);
//                                    finish();
//                                    startActivity(intent);
//                                }
//
//                                oneBtnDialog = new OneBtnDialog(JoinKakaoActivity.this, "이미 사용중인\n전화번호 입니다 !", "확인");
//                                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                oneBtnDialog.setCancelable(false);
//                                oneBtnDialog.show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_CANCELED:
                break;
            case 777:
                SharedPreferences addr1CheckList = getSharedPreferences("addr1CheckList", Activity.MODE_PRIVATE);
                SharedPreferences addr2CheckList = getSharedPreferences("addr2CheckList", Activity.MODE_PRIVATE);
                Map<String, ?> keys = addr2CheckList.getAll();
                if (0 != keys.size()) {
                    String getAddress2 = "";
                    int i = 0;
                    for (Map.Entry<String, ?> entry : keys.entrySet()) {
                        address2.add(i, entry.getValue().toString());
                        if (i != 0) {
                            getAddress2 += " ";
                        }
                        getAddress2 += entry.getValue().toString();
                        i++;
                    }
                    address1 = addr1CheckList.getString("addr1", "");
                    address.setText(address1 + " " + getAddress2);
                    realestate_name.requestFocus();
                    ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
                    if ("서울특별시".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("02)");
                    } else if ("부산광역시".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("051)");
                    } else if ("인천광역시".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("032)");
                    } else if ("광주광역시".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("062)");
                    } else if ("대구광역시".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("053)");
                    } else if ("대전광역시".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("042)");
                    } else if ("울산광역시".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("052)");
                    } else if ("세종특별자치시".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("044)");
                    } else if ("경기도".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("031)");
                    } else if ("강원도".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("033)");
                    } else if ("충청북도".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("043)");
                    } else if ("충청남도".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("041)");
                    } else if ("전라북도".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("063)");
                    } else if ("전라남도".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("061)");
                    } else if ("경상북도".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("054)");
                    } else if ("경상남도".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("055)");
                    } else if ("제주도".equals(addr1CheckList.getString("addr1", ""))) {
                        realestate_tel_area.setText("064)");
                    }
                    break;
                }
        }
    }

    TextWatcher textWatcherName = new TextWatcher() {
        @Override   //텍스트 입력 전
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override   //텍스트 변경 중
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                isName = true;
            } else {
                isName = false;
            }
        }

        @Override   //텍스트 입력 후
        public void afterTextChanged(Editable s) {

            if (isName && isAddress && isRName && isRTel) {
                next.setTextColor(getResources().getColor(R.color.baseColor));
            } else {
                next.setTextColor(getResources().getColor(R.color.grayColor));
            }
        }
    };

    TextWatcher textWatcherAddress = new TextWatcher() {
        @Override   //텍스트 입력 전
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override   //텍스트 변경 중
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length() > 0) {
                isAddress = true;
            } else {
                isAddress = false;
            }
        }

        @Override   //텍스트 입력 후
        public void afterTextChanged(Editable s) {
            if (isName && isAddress && isRName && isRTel) {
                next.setTextColor(getResources().getColor(R.color.baseColor));
            } else {
                next.setTextColor(getResources().getColor(R.color.grayColor));
            }
        }
    };

    TextWatcher textWatcherRName = new TextWatcher() {
        @Override   //텍스트 입력 전
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override   //텍스트 변경 중
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length() > 0) {
                isRName = true;
            } else {
                isRName = false;
            }
        }

        @Override   //텍스트 입력 후
        public void afterTextChanged(Editable s) {

            if (isName && isAddress && isRName && isRTel) {
                next.setTextColor(getResources().getColor(R.color.baseColor));
            } else {
                next.setTextColor(getResources().getColor(R.color.grayColor));
            }
        }
    };

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
