package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

import app.cosmos.ghrealestatediary.CustomTabLayout.TabLayoutWithArrow;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class MemberActivity extends AppCompatActivity {

    ImageView back;
    AQuery aQuery = null;
    TextView invite, tab_text1, tab_text2;
    View view1, view2;
    ViewPager viewPager;
    TabLayoutWithArrow tabLayout;
    PagerAdapterGroup adapter;
    FragmentManager fragmentManager;
    FrameLayout invite_con;

    String tab_selected, groupNumber;

    TwoBtnRecDialog twoBtnRecDialog;
    OneBtnDialog oneBtnDialog;
    ProgressBar progressBar;
    InputMethodManager imm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(MemberActivity.this, true);
            }
        }

        Intent intent = getIntent();
        groupNumber = intent.getStringExtra("group");

        aQuery = new AQuery(this);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#7199ff"), android.graphics.PorterDuff.Mode.SRC_IN);
        back = findViewById(R.id.back);
        invite = findViewById(R.id.invite);
        invite_con = findViewById(R.id.invite_con);

        tabLayout = findViewById(R.id.tablayout);
        view1 = getLayoutInflater().inflate(R.layout.custom_tab_text, null);
        tab_text1 = (TextView) view1.findViewById(R.id.tab_text);
        tab_text1.setText("그룹원");
        tab_text1.setTextColor(Color.parseColor("#ffffff"));
        tab_selected = "0";
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));

        view2 = getLayoutInflater().inflate(R.layout.custom_tab_text, null);
        tab_text2 = (TextView) view2.findViewById(R.id.tab_text);
        tab_text2.setText("공유매물");
        tab_text2.setTextColor(Color.parseColor("#80ffffff"));
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));

        tabLayout.setTabGravity(TabLayoutWithArrow.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.viewpager_group);
        fragmentManager = getSupportFragmentManager();
        adapter = new PagerAdapterGroup(fragmentManager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayoutWithArrow.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setOnTabSelectedListener(new TabLayoutWithArrow.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayoutWithArrow.Tab tab) {
                tab_selected = tab.getPosition() + "";
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    tab_text1.setTextColor(Color.parseColor("#ffffff"));
                    tab_text2.setTextColor(Color.parseColor("#80ffffff"));
                } else if (tab.getPosition() == 1) {
                    tab_text1.setTextColor(Color.parseColor("#80ffffff"));
                    tab_text2.setTextColor(Color.parseColor("#ffffff"));
                }
            }

            @Override
            public void onTabUnselected(TabLayoutWithArrow.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayoutWithArrow.Tab tab) {
                tab_selected = tab.getPosition() + "";
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        invite_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invite.callOnClick();
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoBtnRecDialog = new TwoBtnRecDialog(MemberActivity.this);
                twoBtnRecDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                twoBtnRecDialog.setCancelable(false);
                twoBtnRecDialog.show();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    public class TwoBtnRecDialog extends Dialog {
        TwoBtnRecDialog twoBtnDialog = this;
        Context context;

        public TwoBtnRecDialog(final Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_group_make_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title = (TextView) findViewById(R.id.title);
            final EditText name = (EditText) findViewById(R.id.content);
            final TextView statusTxt = (TextView) findViewById(R.id.status);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            title.setText("그룹 초대");
            name.setHint("핸드폰 번호 11자리");
            name.setInputType(InputType.TYPE_CLASS_NUMBER);
            statusTxt.setText("*일치하는 회원이 없습니다.");
            statusTxt.setVisibility(View.GONE);
            btn1.setText("취소");
            btn2.setText("확인");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                    twoBtnDialog.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                    SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    String url = UrlManager.getBaseUrl() + "group/invite";
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
                                .add("group", groupNumber)
                                .add("phone", name.getText().toString())
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
                                                //초대 성공
                                                twoBtnDialog.dismiss();
//                                                mSwipeRefresh.setVisibility(View.VISIBLE);
                                                Toast.makeText(MemberActivity.this, "\'" + name.getText().toString() + "\'님을 초대하였습니다.", Toast.LENGTH_SHORT).show();
                                            } else if (!jsonObject.getBoolean("return")) {//return이 false 면?

                                                statusTxt.setVisibility(View.VISIBLE);
                                                statusTxt.setText("*일치하는 회원이 없습니다.");

                                                if ("invite".equals(jsonObject.getString("type"))) {
                                                    statusTxt.setText("*이미 초대한 회원입니다.");

                                                } else if ("member".equals(jsonObject.getString("type"))) {
                                                    statusTxt.setText("*이미 그룹의 회원입니다.");
                                                } else if ("pay".equals(jsonObject.getString("type"))) {
                                                    //기간 만료
                                                    twoBtnDialog.dismiss();
                                                    Intent intent = new Intent(MemberActivity.this, BillingListActivity.class);
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
//                     Map<String, Object> params = new HashMap<String, Object>();
//                     params.put("group", groupNumber);
//                     params.put("phone", name.getText().toString());
//                     Log.i("MemberActivity", " params " + params);
//                     aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                         @Override
//                         public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                             try {
//                                 Log.i("MemberActivity", " JSONObject " + jsonObject.toString());
//                                 if (jsonObject.getBoolean("return")) {
//                                     //초대 성공
//                                     twoBtnDialog.dismiss();
////                                    mSwipeRefresh.setVisibility(View.VISIBLE);
//                                     Toast.makeText(MemberActivity.this, name.getText().toString() + "\'님을 초대하였습니다.", Toast.LENGTH_SHORT).show();
//
//                                 } else if (!jsonObject.getBoolean("return")) {
//                                     statusTxt.setVisibility(View.VISIBLE);
//                                     statusTxt.setText("*일치하는 회원이 없습니다.");
//
//                                     if ("invite".equals(jsonObject.getString("type"))) {
//                                         statusTxt.setText("*이미 초대한 회원입니다.");
//
//                                     } else if ("member".equals(jsonObject.getString("type"))) {
//                                         statusTxt.setText("*이미 그룹의 회원입니다.");
//                                     } else if ("pay".equals(jsonObject.getString("type"))) {
//                                         //기간 만료
//                                         twoBtnDialog.dismiss();
//                                         Intent intent = new Intent(MemberActivity.this, BillingListActivity.class);
//                                         startActivity(intent);
//                                         finish();
//                                     }
//                                 }
//
//                             } catch (JSONException e) {
//                                 e.printStackTrace();
//                             }
//                         }
//                     }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
                }
            });
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
