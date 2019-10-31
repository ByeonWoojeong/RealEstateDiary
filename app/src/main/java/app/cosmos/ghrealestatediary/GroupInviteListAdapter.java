package app.cosmos.ghrealestatediary;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import app.cosmos.ghrealestatediary.DTO.GroupInvite;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class GroupInviteListAdapter extends BaseAdapter {

    Context context;
    int layout;
    ArrayList<GroupInvite> data;
    ListView listView;
    AQuery aQuery = null;
    GroupInviteListAdapter groupInviteListAdapter = this;
    OneBtnDialog oneBtnDialog;
    TwoBtnDialog twoBtnDialog;
    int type;

    public GroupInviteListAdapter(Context context, int layout, ArrayList<GroupInvite> data, ListView listView) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class ViewHolder {
        TextView groupName;
        TextView bossPhone;
        Button yes;
        Button no;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder vh = null;
        aQuery = new AQuery(context);
        if (view == null) {
            view = View.inflate(context, layout, null);
            vh = new ViewHolder();
            vh.groupName = (TextView) view.findViewById(R.id.group_name);
            vh.bossPhone = (TextView) view.findViewById(R.id.boss_phone);
            vh.yes = view.findViewById(R.id.yes);
            vh.no = view.findViewById(R.id.no);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final GroupInvite item = data.get(i);
        vh.groupName.setText(item.getGroupName());
        vh.bossPhone.setText(item.getBossPhone());
        vh.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                final String url = UrlManager.getBaseUrl() + "group/join";
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
                            .add("type", "1")
                            .add("group", item.getGroupNumber())
                            .build();
                    Request request = new Request.Builder().url(url).header("GHsoft-Agent", "" + getToken).header("User-Agent", "android").post(body).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) { //통신 실패
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String res = response.body().string();
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(res);
                                        if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                            data.remove(item);
                                            groupInviteListAdapter.notifyDataSetChanged();
                                            Toast.makeText(context, item.getGroupName() + " 그룹의 초대를 수락 하였습니다.", Toast.LENGTH_SHORT).show();
                                        } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                            if ("pay".equals(jsonObject.getString("type"))) {
                                                Intent intent = new Intent(context, BillingListActivity.class);
                                                context.startActivity(intent);
                                                ((Activity) context).finish();
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
//                params.put("type", 1);
//                params.put("group", item.getGroupNumber());
//                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                    @Override
//                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                        LogManager.print("? "+jsonObject);
//                        try {
//                            if (jsonObject.getBoolean("return")) {
//                                data.remove(item);
//                                groupInviteListAdapter.notifyDataSetChanged();
//                                Toast.makeText(context, item.getGroupName() + " 그룹의 초대를 수락 하였습니다.", Toast.LENGTH_SHORT).show();
//                            } else {
//                                if("pay".equals(jsonObject.getString("type"))){
//                                    //기간 만료
//                                    data.remove(item);
//                                    groupInviteListAdapter.notifyDataSetChanged();
//                                }
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
            }
        });
        vh.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                final String url = UrlManager.getBaseUrl() + "group/join";
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
                            .add("type", "0")
                            .add("group", item.getGroupNumber())
                            .build();
                    Request request = new Request.Builder().url(url).header("GHsoft-Agent", "" + getToken).header("User-Agent", "android").post(body).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) { //통신 실패
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String res = response.body().string();
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(res);
                                        if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                            data.remove(item);
                                            groupInviteListAdapter.notifyDataSetChanged();
                                            Toast.makeText(context, item.getGroupName() + " 의 초대를 거절 하였습니다.", Toast.LENGTH_SHORT).show();
                                        } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                            Toast.makeText(context, item.getGroupName() + " 의 초대 거절을\n실패 하였습니다.", Toast.LENGTH_SHORT).show();
                                            if ("pay".equals(jsonObject.getString("type"))) {
                                                Intent intent = new Intent(context, BillingListActivity.class);
                                                context.startActivity(intent);
                                                ((Activity) context).finish();
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
//                params.put("type", 0);
//                params.put("group", item.getGroupNumber());
//                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                    @Override
//                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                        try {
//                            if (jsonObject.getBoolean("return")) {
//                                data.remove(item);
//                                new Thread() {
//                                    public void run() {
//                                        data.remove(item);
//                                        Message msg = noHandler.obtainMessage();
//                                        noHandler.sendMessage(msg);
//                                    }
//                                }.start();
//                            } else {
//                                if ("pay".equals(jsonObject.getString("type"))) {
//                                    //기간 만료
//                                    data.remove(item);
//                                    groupInviteListAdapter.notifyDataSetChanged();
//                                }
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
            }
        });
        return view;
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


    //그룹 초대 수락
    public class TwoBtnDialog extends Dialog {
        TwoBtnDialog twoBtnDialog = this;
        Context context;

        public TwoBtnDialog(final Context context, final String groupName, final GroupInvite item, final String idx) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_two_btn_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title1 = (TextView) findViewById(R.id.title1);
            TextView title2 = (TextView) findViewById(R.id.title2);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            title2.setVisibility(View.GONE);
            title1.setText("해당 그룹의 초대를\n수락하시겠습니까?");
            btn1.setText("취소");
            btn2.setText("가입");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnDialog.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    String url = UrlManager.getBaseUrl() + "group/join/" + idx;
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("type", "1");
                    params.put("group", idx);
                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            try {
                                if (jsonObject.getBoolean("return")) {
                                    ((Activity) context).finish();
                                    Toast.makeText(context, groupName + " 그룹에 가입 하였습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    if ("full".equals(jsonObject.getString("type"))) {
                                        oneBtnDialog = new OneBtnDialog(context, groupName + " 그룹의 정원이 가득 찼습니다 !", "확인");
                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        oneBtnDialog.setCancelable(false);
                                        oneBtnDialog.show();
                                    } else if ("pay".equals(jsonObject.getString("type"))) {
                                        //기간 만료
                                    } else {
                                        data.remove(item);
                                        groupInviteListAdapter.notifyDataSetChanged();
                                        oneBtnDialog = new OneBtnDialog(context, groupName + " 그룹이 존재하지 않습니다 !", "확인");
                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        oneBtnDialog.setCancelable(false);
                                        oneBtnDialog.show();
                                    }
                                }
                                twoBtnDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
                }
            });
        }
    }
}
