package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class PassChangeActivity extends AppCompatActivity {

    Context context;
    AQuery aQuery = null;
    InputMethodManager ipm;
    FrameLayout next_con;
    TextView next;
    ImageView back;
    LinearLayout password_con, new_password_con, new_password_re_con;
    EditText password, new_password, new_password_re;
    OneBtnDialog oneBtnDialog;
    ProgressBar progressBar;

    boolean isPW, isNewRe, isNew, isKeyBoardVisible;

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
        if (isKeyBoardVisible) {
            ipm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
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
        setContentView(R.layout.activity_pass_change);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(PassChangeActivity.this, true);
            }
        }
        aQuery = new AQuery(this);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#7199ff"), android.graphics.PorterDuff.Mode.SRC_IN);
        ipm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Intent intent = getIntent();
        String idx = intent.getStringExtra("idx");
        next_con = (FrameLayout) findViewById(R.id.next_con);
        next = (TextView) findViewById(R.id.next);
        back = (ImageView) findViewById(R.id.back);
        password_con = (LinearLayout) findViewById(R.id.password_con);
        new_password_con = (LinearLayout) findViewById(R.id.new_password_con);
        new_password_re_con = (LinearLayout) findViewById(R.id.new_password_re_con);
        password = (EditText) findViewById(R.id.password);
        password.addTextChangedListener(textWatcherPW);
        password.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyboardShown(password.getRootView())) {
                    isKeyBoardVisible = true;
                } else {
                    isKeyBoardVisible = false;
                }
            }
        });
        new_password = (EditText) findViewById(R.id.new_password);
        new_password.addTextChangedListener(textWatcherNew);
        new_password.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyboardShown(new_password.getRootView())) {
                    isKeyBoardVisible = true;
                } else {
                    isKeyBoardVisible = false;
                }
            }
        });

        new_password_re = (EditText) findViewById(R.id.new_password_re);
        new_password_re.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyboardShown(new_password_re.getRootView())) {
                    isKeyBoardVisible = true;
                } else {
                    isKeyBoardVisible = false;
                }
            }
        });
        new_password_re.addTextChangedListener(textWatcherNewRe);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        new_password.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        new_password.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        new_password.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        new_password.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        new_password.requestFocus();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        new_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        new_password_re.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        new_password_re.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        new_password_re.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        new_password_re.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        new_password_re.requestFocus();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        password_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        new_password_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_password.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        new_password_re_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_password_re.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
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
                if ("".equals(password.getText().toString()) || "" == password.getText().toString()) {
                    oneBtnDialog = new OneBtnDialog(PassChangeActivity.this, "비밀번호를 입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if (4 > password.getText().toString().length()) {
                    oneBtnDialog = new OneBtnDialog(PassChangeActivity.this, "비밀번호는 최소 4자\n이상으로 작성해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if ("".equals(new_password.getText().toString()) || "" == new_password.getText().toString()) {
                    oneBtnDialog = new OneBtnDialog(PassChangeActivity.this, "새 비밀번호를\n입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if (4 > new_password.getText().toString().length()) {
                    oneBtnDialog = new OneBtnDialog(PassChangeActivity.this, "비밀번호는 최소 4자\n이상으로 작성해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if ("".equals(new_password_re.getText().toString()) || "" == new_password_re.getText().toString()) {
                    oneBtnDialog = new OneBtnDialog(PassChangeActivity.this, "새 비밀번호 확인을\n입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if (4 > new_password_re.getText().toString().length()) {
                    oneBtnDialog = new OneBtnDialog(PassChangeActivity.this, "비밀번호는 최소 4자\n이상으로 작성해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if (!new_password.getText().toString().equals(new_password_re.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(PassChangeActivity.this, "비밀번호 확인이 다릅니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                String url = UrlManager.getBaseUrl() + "member/passchange";
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
                            .add("pass", new_password.getText().toString().trim())
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
                                            Toast.makeText(PassChangeActivity.this, "비밀번호 변경을 완료 하였습니다.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                            oneBtnDialog = new OneBtnDialog(PassChangeActivity.this, "비밀번호를 정확히\n입력해 주세요 !", "확인");
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
//                params.put("pass", new_password.getText().toString().trim());
//                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                    @Override
//                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                        try {
//                            if (jsonObject.getBoolean("return")) {
//                                Toast.makeText(PassChangeActivity.this, "변경 완료 하였습니다.", Toast.LENGTH_SHORT).show();
//                                finish();
//                            } else if (!jsonObject.getBoolean("return")) {
//
//                                oneBtnDialog = new OneBtnDialog(PassChangeActivity.this, "비밀번호를 정확히\n입력해 주세요 !", "확인");
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

    TextWatcher textWatcherPW = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length() > 0) {
                isPW = true;
            } else {
                isPW = false;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (isPW && isNewRe && isNew) {
                next.setTextColor(getResources().getColor(R.color.baseColor));
            } else {
                next.setTextColor(getResources().getColor(R.color.grayColor));
            }
        }
    };

    TextWatcher textWatcherNewRe = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                isNewRe = true;
            } else {
                isNewRe = false;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (isPW && isNewRe && isNew) {
                next.setTextColor(getResources().getColor(R.color.baseColor));
            } else {
                next.setTextColor(getResources().getColor(R.color.grayColor));
            }
        }
    };

    TextWatcher textWatcherNew = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length() > 0) {
                isNew = true;
            } else {
                isNew = false;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (isPW && isNewRe && isNew) {
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
