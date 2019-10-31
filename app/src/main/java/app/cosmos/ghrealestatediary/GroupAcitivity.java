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
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.EditText;
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

import app.cosmos.ghrealestatediary.DTO.Consult;
import app.cosmos.ghrealestatediary.DTO.Group;
import app.cosmos.ghrealestatediary.DTO.GroupInvite;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class GroupAcitivity extends AppCompatActivity {

    private static Context context;

    ImageView back;
    FrameLayout make_con;
    TextView make, no_invite, no_group_list;
    ListView group_invite_listview, my_group_listview;
    ArrayList<GroupInvite> inviteData;
    ArrayList<Group> groupData;
    GroupInviteListAdapter groupInviteListAdapter;
    GroupListAdapter groupListAdapter;
    SwipeRefreshLayout mSwipeRefresh;
    ProgressBar progressBar;
    TwoBtnMakeDialog twoBtnRecDialog;
    Parcelable state1, state2;
    OneBtnDialog oneBtnDialog;
    InputMethodManager imm;
    int myType;

    AQuery aQuery = null;

    void refreshInviteList() {
        state1 = null;
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = UrlManager.getBaseUrl() + "group/list";
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
                    inviteData.clear();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                progressBar.setVisibility(View.GONE);
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                    JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                                    if (jsonArray.length() == 0) {
                                        group_invite_listview.setVisibility(View.GONE);
                                    } else {
                                        group_invite_listview.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                            Log.i("GroupActivity", "여기로  getJsonObject  :: " + i + " 번째 " + getJsonObject);
                                            inviteData.add(new GroupInvite(getJsonObject.getString("group"), getJsonObject.getString("name"), getJsonObject.getString("phone"), getJsonObject.getInt("type")));
                                        }
                                        group_invite_listview.setAdapter(groupInviteListAdapter);
                                        groupInviteListAdapter.notifyDataSetChanged();
                                    }
                                } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
//                                    group_invite_listview.setVisibility(View.GONE);
//                                    group_invite_listview.setAdapter(groupInviteListAdapter);
//                                    groupInviteListAdapter.notifyDataSetChanged();

                                    if ("pay".equals(jsonObject.getString("type"))) {
                                        //기간 만료
                                        Toast.makeText(GroupAcitivity.this, "기간이 만료되었습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(GroupAcitivity.this, BillingListActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                if (state1 != null) {
                                    group_invite_listview.onRestoreInstanceState(state1);
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
//                    Log.i("GroupActivity", " groupList 1 " + jsonObject);
//                    inviteData.clear();
//                    if (jsonObject.getBoolean("return")) {
//                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
//                        Log.i("GroupActivity", " INVITE list jsonArray " + jsonArray);
//                        Log.i("GroupActivity", " INVITE list jsonArray LENGTH " + jsonArray.length());
//                        if(jsonArray.length()==0){
//                            group_invite_listview.setVisibility(View.GONE);
//                        }else{
//                            group_invite_listview.setVisibility(View.VISIBLE);
//                            for (int i = 0; i<jsonArray.length(); i++) {
//                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
//                                Log.i("GroupActivity", "여기로  getJsonObject  :: " + i + " 번째 " + getJsonObject);
//                                inviteData.add(new GroupInvite(getJsonObject.getString("group"), getJsonObject.getString("name"), getJsonObject.getString("phone"), getJsonObject.getInt("type")));
//                            }
//                            group_invite_listview.setAdapter(groupInviteListAdapter);
//                            groupInviteListAdapter.notifyDataSetChanged();
//                        }
//
//                    } else if (!jsonObject.getBoolean("return")) {
//                        group_invite_listview.setVisibility(View.GONE);
//                        group_invite_listview.setAdapter(groupInviteListAdapter);
//                        groupInviteListAdapter .notifyDataSetChanged();
//
//                        if("pay".equals(jsonObject.getString("type"))){
//                            //기간 만료
//
//                        }
//                    }
//                    if (state1 != null) {
//                        group_invite_listview.onRestoreInstanceState(state1);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
    }

    void refreshGroupList() {
        state2 = null;
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = UrlManager.getBaseUrl() + "group/list";
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
                                groupData.clear();
                                if (jsonObject.getBoolean("return")) {
                                    JSONArray jsonArray = new JSONArray(jsonObject.getString("mylist"));
                                    Log.i("GroupActivity", " group list jsonArray " + jsonArray);
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

                                } else if (!jsonObject.getBoolean("return")) {

                                    my_group_listview.setVisibility(View.GONE);
                                    my_group_listview.setAdapter(groupListAdapter);
                                    groupListAdapter.notifyDataSetChanged();

                                    if ("pay".equals(jsonObject.getString("type"))) {
                                        //기간 만료
                                    }
                                }
                                if (state2 != null) {
                                    my_group_listview.onRestoreInstanceState(state2);
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
//                    Log.i("GroupActivity", " groupList 2 " + jsonObject);
//                    groupData.clear();
//                    if (jsonObject.getBoolean("return")) {
//                        JSONArray jsonArray = new JSONArray(jsonObject.getString("mylist"));
//                        Log.i("GroupActivity", " group list jsonArray " + jsonArray);
//                        if (jsonArray.length() == 0) {
//                            my_group_listview.setVisibility(View.GONE);
//                        } else {
//                            my_group_listview.setVisibility(View.VISIBLE);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
//                                groupData.add(new Group(getJsonObject.getString("group"), getJsonObject.getString("name"), getJsonObject.getString("phone"), getJsonObject.getInt("type")));
//
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
//                        if ("pay".equals(jsonObject.getString("type"))) {
//                            //기간 만료
//                        }
//                    }
//                    if (state2 != null) {
//                        my_group_listview.onRestoreInstanceState(state2);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
    }

    @Override
    public void onPause() {
        super.onPause();
        state1 = group_invite_listview.onSaveInstanceState();
        state2 = my_group_listview.onSaveInstanceState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(GroupAcitivity.this, true);
            }
        }
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#7199ff"), android.graphics.PorterDuff.Mode.SRC_IN);
        aQuery = new AQuery(this);
        back = findViewById(R.id.back);
        make_con = findViewById(R.id.invite_con);
        make = findViewById(R.id.invite);
        group_invite_listview = findViewById(R.id.group_invite_listview);
        my_group_listview = findViewById(R.id.my_group_listview);
        groupData = new ArrayList<Group>();
        inviteData = new ArrayList<GroupInvite>();
        groupListAdapter = new GroupListAdapter(GroupAcitivity.this, R.layout.list_group, groupData, my_group_listview, myType, "none");
        groupInviteListAdapter = new GroupInviteListAdapter(GroupAcitivity.this, R.layout.list_group_invite, inviteData, group_invite_listview);
//        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
//        mSwipeRefresh.setColorSchemeResources(R.color.baseColor, R.color.baseColor);
//        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mSwipeRefresh.setRefreshing(false);
//            }
//        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        make_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make.callOnClick();
            }
        });

        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoBtnRecDialog = new TwoBtnMakeDialog(GroupAcitivity.this);
                twoBtnRecDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                twoBtnRecDialog.setCancelable(false);
                twoBtnRecDialog.show();
            }
        });

        refreshInviteList();
        refreshGroupList();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
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
                setResult(666, data);
                finish();
                break;
        }
    }

    public class TwoBtnMakeDialog extends Dialog {
        TwoBtnMakeDialog twoBtnDialog = this;
        Context context;

        public TwoBtnMakeDialog(final Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_group_make_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            final TextView title = (TextView) findViewById(R.id.title);
            final EditText name = (EditText) findViewById(R.id.content);
            final TextView statusTxt = (TextView) findViewById(R.id.status);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            title.setText("그룹 생성");
            name.setHint("그룹 이름을 입력해주세요.");
            statusTxt.setText("*8글자 이내로 작성해주세요.");
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
                    final String url = UrlManager.getBaseUrl() + "group/make";
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
                                .add("name", name.getText().toString())
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
                                                // 그룹 생성 성공
                                                twoBtnDialog.dismiss();
//                                              mSwipeRefresh.setVisibility(View.VISIBLE);
                                                Toast.makeText(GroupAcitivity.this, "그룹 \'" + name.getText().toString() + "\'을 생성하였습니다.", Toast.LENGTH_SHORT).show();
                                                refreshGroupList();
                                            } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                                if ("length".equals(jsonObject.getString("type"))) {
                                                    //그룹 이름 길이 초과
                                                    statusTxt.setVisibility(View.VISIBLE);
                                                } else if ("pay".equals(jsonObject.getString("type"))) {
                                                    //기간 만료
                                                    twoBtnDialog.dismiss();
                                                    Intent intent = new Intent(GroupAcitivity.this, BillingListActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
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
//                    params.put("name", name.getText().toString());
//                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            try {
//                                Log.i("GroupActivity", " JSONObject " + jsonObject.toString());
//                                if (jsonObject.getBoolean("return")) {
//                                    //그룹 생성 성공
//                                    twoBtnDialog.dismiss();
////                                    mSwipeRefresh.setVisibility(View.VISIBLE);
//                                    Toast.makeText(GroupAcitivity.this, "그룹 \'" + name.getText().toString() + "\'을 생성하였습니다.", Toast.LENGTH_SHORT).show();
//                                    refreshGroupList();
//
//                                } else if (!jsonObject.getBoolean("return")) {
//                                    //그룹 생성 실패
//
//                                    if ("length".equals(jsonObject.getString("type"))) {
//                                        //그룹 이름 길이 초과
//                                        statusTxt.setVisibility(View.VISIBLE);
//                                    } else if ("pay".equals(jsonObject.getString("type"))) {
//                                        //기간 만료
//                                        twoBtnDialog.dismiss();
//                                        Intent intent = new Intent(GroupAcitivity.this, BillingListActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    } else {
//                                    }
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
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

    public class TwoBtnLeaveDialog extends Dialog {
        TwoBtnLeaveDialog twoBtnLeaveDialog = this;
        Context context;

        public TwoBtnLeaveDialog(final Context context, final String group) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_two_btn_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title1 = (TextView) findViewById(R.id.title1);
            TextView title2 = (TextView) findViewById(R.id.title2);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            title1.setText("그룹을 탈퇴하시겠습니까?");
            if ("1".equals(group)) {
                title2.setText("그룹장이 탈퇴하면\n그룹이 해체됩니다.\n신중히 선택해주시기 바랍니다.");
            } else {
                title2.setVisibility(View.GONE);
            }
            btn1.setText("취소");
            btn2.setText("확인");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnLeaveDialog.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    String url = UrlManager.getBaseUrl() + "/group/leave";
                    Map<String, Object> params = new HashMap<String, Object>();
                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            ((MyDiaryActivity) getActivity()).infoRefresh();
                            twoBtnLeaveDialog.dismiss();
                            oneBtnDialog = new OneBtnDialog(context, "그룹을 탈퇴 하였습니다.", "확인");
                            oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            oneBtnDialog.setCancelable(false);
                            oneBtnDialog.show();
                        }
                    }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
                }
            });
        }
    }
}
