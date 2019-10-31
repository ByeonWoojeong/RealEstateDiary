package app.cosmos.ghrealestatediary;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import app.cosmos.ghrealestatediary.DTO.Member;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MemberListAdapter extends BaseAdapter {

    Context context;
    int layout;
    ArrayList<Member> data;
    ListView listView;
    AQuery aQuery = null;
    MemberListAdapter mainListAdapter = this;
    String groupNumber;
    TwoBtnDialog twoBtnDialog;
    int type;

    public MemberListAdapter(Context context, int layout, ArrayList<Member> data, ListView listView, String group, int type) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
        this.groupNumber = group;
        this.type = type;
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
        TextView boss, name, phone, exile;
        LinearLayout exile_con;

    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder vh = null;
        aQuery = new AQuery(context);

        if (view == null) {
            view = View.inflate(context, layout, null);
            vh = new ViewHolder();
            vh.name = view.findViewById(R.id.name);
            vh.boss = view.findViewById(R.id.boss);
            vh.phone = view.findViewById(R.id.phone);
            vh.exile_con = view.findViewById(R.id.exile_con);
            vh.exile = view.findViewById(R.id.exile);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final Member item = data.get(i);
        vh.name.setText(item.getName());
        vh.phone.setText(item.getPhone());
        final ViewHolder finalVh = vh;
        vh.exile_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalVh.exile.callOnClick();
            }
        });
        vh.exile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //강퇴 Dialog
                twoBtnDialog = new TwoBtnDialog(context, item.getName(), item, item.getIdx(), item.getGroupNumber());
                twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                twoBtnDialog.setCancelable(false);
                twoBtnDialog.show();
            }
        });


        if (9 == item.getType()) {
            vh.boss.setVisibility(View.VISIBLE);

            vh.exile_con.setVisibility(View.GONE);
            vh.exile.setVisibility(View.GONE);
        }else if (1 == item.getType()) {
            vh.boss.setVisibility(View.GONE);

            vh.exile.setVisibility(View.VISIBLE);
            vh.exile_con.setVisibility(View.VISIBLE);
        }else if(7 == item.getType()) {
            vh.boss.setVisibility(View.GONE);
            vh.exile_con.setVisibility(View.GONE);
            vh.exile.setVisibility(View.GONE);
        }
        return view;
    }

    public class TwoBtnDialog extends Dialog {
        TwoBtnDialog twoBtnDialog = this;
        Context context;

        public TwoBtnDialog(final Context context, final String name, final Member item, final String idx, final String groupNumber) {
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
            title1.setText(name + " 님을 \n그룹에서 강제탈퇴\n 하시겠습니까?");
            btn1.setText("취소");
            btn2.setText("확인");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnDialog.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnDialog.dismiss();

                    SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    String url = UrlManager.getBaseUrl() + "group/kick";
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
                                .add("group", groupNumber)
                                .add("member", item.getIdx())
                                .build();
                        Request request = new Request.Builder().url(url).header("GHsoft-Agent", "" + getToken).header("User-Agent", "android").post(body).build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) { //통신 실패
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String res = response.body().string();
                                ((MemberActivity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = new JSONObject(res);
                                            if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                                data.remove(item);
                                                mainListAdapter.notifyDataSetChanged();
                                                Toast.makeText(context, name + " 님을 그룹에서 강제탈퇴 하였습니다.", Toast.LENGTH_SHORT).show();
                                            } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                                Toast.makeText(context, name + " 님의 강제탈퇴를 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                if ("pay".equals(jsonObject.getString("type"))) {
                                                    //기간 만료
                                                    twoBtnDialog.dismiss();
                                                    Intent intent = new Intent(context, BillingListActivity.class);
                                                    context.startActivity(intent);
                                                    ((MemberActivity) context).finish();
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
//                    params.put("group", groupNumber);
//                    params.put("member", item.getIdx());
//                    Log.i("MEMBERLISTADAPTER", "params " + params);
//                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            try {
//                                Log.i("MEMBERLISTADAPTER", "jsonObject " + jsonObject);
//                                if (jsonObject.getBoolean("return")) {
//                                    data.remove(item);
//                                    mainListAdapter.notifyDataSetChanged();
//                                    Toast.makeText(context, name + " 님을 그룹에서 강제탈퇴 하였습니다.", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    data.remove(item);
//                                    mainListAdapter.notifyDataSetChanged();
//
//                                    if("pay".equals(jsonObject.getString("type"))){
//                                        //기간 만료
//                                    }
//
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

}
