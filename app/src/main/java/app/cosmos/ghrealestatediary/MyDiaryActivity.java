package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

public class MyDiaryActivity extends AppCompatActivity {

    Context context;
    AQuery aQuery = null;
    ImageView back, yunjang;
    TextView email, day, tab_text1, tab_text2, tab_text3;
    View view1, view2, view3;
    ViewPager viewPager;
    TabLayoutWithArrow tabLayout;
    PagerAdapterMyDiary adapter;
    FragmentManager fragmentManager;
    ProgressBar progressBar;
    static String tab_selected2, getIdx, getEmail, getDate, getName, getPhone, getAddr1, getAddr2, getRealestate_name, getRealestate_tel, group_idx, group, type, why_bill;
    OneBtnDialog oneBtnDialog;

    public void infoRefresh() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        String url = UrlManager.getBaseUrl() + "member/mypage";
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
                                    getDate = jsonObject.getJSONObject("data").getString("date");
                                    getPhone = jsonObject.getJSONObject("data").getString("phone");
                                    type = jsonObject.getJSONObject("data").getString("type");
                                    if ("null".equals(getDate)) {
                                        why_bill = "1";
                                        email.setText(getPhone);
                                        day.setText("0");
                                    } else {
                                        why_bill = "2";
                                        email.setText(getPhone);
                                        day.setText(getDate);
                                        if ("sns".equals(type)) {
                                            FirstFragmentMyDiary firstFragmentMyDiary = (FirstFragmentMyDiary) fragmentManager.findFragmentByTag("FirstFragmentMyDiary");
                                            firstFragmentMyDiary.menu1_2_con.setVisibility(View.GONE);
                                        }
                                        SecondFragmentMyDiary secondFragmentMyDiary = (SecondFragmentMyDiary) fragmentManager.findFragmentByTag("SecondFragmentMyDiary");
                                        if ("0".equals(group_idx)) {
                                            secondFragmentMyDiary.menu2_1_con.setVisibility(View.VISIBLE);
                                            secondFragmentMyDiary.menu2_2_con.setVisibility(View.GONE);
                                            secondFragmentMyDiary.menu2_3_con.setVisibility(View.VISIBLE);
                                            secondFragmentMyDiary.menu2_4_con.setVisibility(View.GONE);
                                            secondFragmentMyDiary.menu2_5_con.setVisibility(View.GONE);
                                        } else {
                                            if ("0".equals(group)) {
                                                secondFragmentMyDiary.menu2_1_con.setVisibility(View.GONE);
                                                secondFragmentMyDiary.menu2_2_con.setVisibility(View.GONE);
                                                secondFragmentMyDiary.menu2_3_con.setVisibility(View.GONE);
                                                secondFragmentMyDiary.menu2_4_con.setVisibility(View.VISIBLE);
                                                secondFragmentMyDiary.menu2_5_con.setVisibility(View.VISIBLE);
                                            } else {
                                                secondFragmentMyDiary.menu2_1_con.setVisibility(View.GONE);
                                                secondFragmentMyDiary.menu2_2_con.setVisibility(View.VISIBLE);
                                                secondFragmentMyDiary.menu2_3_con.setVisibility(View.GONE);
                                                secondFragmentMyDiary.menu2_4_con.setVisibility(View.VISIBLE);
                                                secondFragmentMyDiary.menu2_5_con.setVisibility(View.VISIBLE);
                                            }
                                        }
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
//        progressBar.setVisibility(View.VISIBLE);
//        aQuery.progress(progressBar).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                try {
//                    progressBar.setVisibility(View.GONE);
//                    Log.i("MyDiaryActivity", "  " +jsonObject.toString());
//                    if (jsonObject.getBoolean("return")) {
//                        getDate = jsonObject.getJSONObject("data").getString("date");
//                        getPhone = jsonObject.getJSONObject("data").getString("phone");
//                        type = jsonObject.getJSONObject("data").getString("type");
//                        if ("null".equals(getDate)) {
//                            why_bill = "1";
//                            email.setText(getPhone);
//                            day.setText("0");
//                        } else {
//                            why_bill = "2";
//                            email.setText(getPhone);
//                            day.setText(getDate);
//                            if ("sns".equals(type)) {
//                                FirstFragmentMyDiary firstFragmentMyDiary = (FirstFragmentMyDiary) fragmentManager.findFragmentByTag("FirstFragmentMyDiary");
//                                firstFragmentMyDiary.menu1_2_con.setVisibility(View.GONE);
//                            }
//                            SecondFragmentMyDiary secondFragmentMyDiary = (SecondFragmentMyDiary) fragmentManager.findFragmentByTag("SecondFragmentMyDiary");
//                            if ("0".equals(group_idx)) {
//                                secondFragmentMyDiary.menu2_1_con.setVisibility(View.VISIBLE);
//                                secondFragmentMyDiary.menu2_2_con.setVisibility(View.GONE);
//                                secondFragmentMyDiary.menu2_3_con.setVisibility(View.VISIBLE);
//                                secondFragmentMyDiary.menu2_4_con.setVisibility(View.GONE);
//                                secondFragmentMyDiary.menu2_5_con.setVisibility(View.GONE);
//                            } else {
//                                if ("0".equals(group)) {
//                                    secondFragmentMyDiary.menu2_1_con.setVisibility(View.GONE);
//                                    secondFragmentMyDiary.menu2_2_con.setVisibility(View.GONE);
//                                    secondFragmentMyDiary.menu2_3_con.setVisibility(View.GONE);
//                                    secondFragmentMyDiary.menu2_4_con.setVisibility(View.VISIBLE);
//                                    secondFragmentMyDiary.menu2_5_con.setVisibility(View.VISIBLE);
//                                } else {
//                                    secondFragmentMyDiary.menu2_1_con.setVisibility(View.GONE);
//                                    secondFragmentMyDiary.menu2_2_con.setVisibility(View.VISIBLE);
//                                    secondFragmentMyDiary.menu2_3_con.setVisibility(View.GONE);
//                                    secondFragmentMyDiary.menu2_4_con.setVisibility(View.VISIBLE);
//                                    secondFragmentMyDiary.menu2_5_con.setVisibility(View.VISIBLE);
//                                }
//                            }
//                        }
//                    } else if (!jsonObject.getBoolean("return")) {
//                        if("logout".equals(jsonObject.getString("type"))){
//                            Toast.makeText(MyDiaryActivity.this, "다른 기기에서 로그인 하였습니다.", Toast.LENGTH_SHORT).show();
//                            SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = prefLoginChecked.edit();
//                            editor.clear();
//                            editor.commit();
//                            Intent intent = new Intent(MyDiaryActivity.this, LoginActivity.class);
//                            finish();
//                            startActivity(intent);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        infoRefresh();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        getIdx = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_diary);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(MyDiaryActivity.this, true);
            }
        }
        aQuery = new AQuery(this);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#7199ff"), android.graphics.PorterDuff.Mode.SRC_IN);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        email = (TextView) findViewById(R.id.email);
        day = (TextView) findViewById(R.id.day);
        yunjang = (ImageView) findViewById(R.id.pay);
        tabLayout = (TabLayoutWithArrow) findViewById(R.id.tabsMain);
        view1 = getLayoutInflater().inflate(R.layout.custom_tab_text, null);
        tab_text1 = (TextView) view1.findViewById(R.id.tab_text);
        tab_text1.setText("기본설정");
        tab_text1.setTextColor(Color.parseColor("#ffffff"));
        tab_selected2 = "0";
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));

//        view2 = getLayoutInflater().inflate(R.layout.custom_tab_text, null);
//        tab_text2 = (TextView) view2.findViewById(R.id.tab_text);
//        tab_text2.setText("그룹관리");
//        tab_text2.setTextColor(Color.parseColor("#80ffffff"));
//        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));

        view3 = getLayoutInflater().inflate(R.layout.custom_tab_text, null);
        tab_text3 = (TextView) view3.findViewById(R.id.tab_text);
        tab_text3.setText("기타");
        tab_text3.setTextColor(Color.parseColor("#80ffffff"));
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));

        tabLayout.setTabGravity(TabLayoutWithArrow.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.viewpager_my_diary);
        fragmentManager = getSupportFragmentManager();
        adapter = new PagerAdapterMyDiary(fragmentManager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayoutWithArrow.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setOnTabSelectedListener(new TabLayoutWithArrow.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayoutWithArrow.Tab tab) {
                tab_selected2 = tab.getPosition() + "";
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    tab_text1.setTextColor(Color.parseColor("#ffffff"));
//                    tab_text2.setTextColor(Color.parseColor("#80ffffff"));
                    tab_text3.setTextColor(Color.parseColor("#80ffffff"));
                } else if (tab.getPosition() == 1) {
                    tab_text1.setTextColor(Color.parseColor("#80ffffff"));
//                    tab_text2.setTextColor(Color.parseColor("#ffffff"));
                    tab_text3.setTextColor(Color.parseColor("#ffffff"));
                }
//                else if (tab.getPosition() == 2) {
//                    tab_text1.setTextColor(Color.parseColor("#80ffffff"));
//                    tab_text2.setTextColor(Color.parseColor("#80ffffff"));
//                    tab_text3.setTextColor(Color.parseColor("#ffffff"));
//                }
            }

            @Override
            public void onTabUnselected(TabLayoutWithArrow.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayoutWithArrow.Tab tab) {
                tab_selected2 = tab.getPosition() + "";
            }
        });
        yunjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (getIdx != null) {
                Intent intent = new Intent(MyDiaryActivity.this, BillingListActivity.class);
                intent.putExtra("why_bill", why_bill);
                startActivityForResult(intent, 1);
//                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 666:
                String la, lo;

                la = data.getStringExtra("la");
                lo = data.getStringExtra("lo");

                Intent passIntent = new Intent();
                passIntent.putExtra("la", la);
                passIntent.putExtra("lo", lo);

                Log.i("666", " " + la);

                setResult(666, data);
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
}
