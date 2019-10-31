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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
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

import app.cosmos.ghrealestatediary.DTO.Group;
import app.cosmos.ghrealestatediary.DTO.Member;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class GroupListAdapter extends BaseAdapter {

    Context context;
    int layout;
    String share;
    ArrayList<Group> data;
    ListView listView;
    AQuery aQuery = null;
    GroupListAdapter mainListAdapter = this;
    TwoBtnDialog twoBtnDialog;
    int type;
    int checkAccumulator;


    public GroupListAdapter(Context context, int layout, ArrayList<Group> data, ListView listView, int type, String share) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
        this.type = type;
        this.share = share;
        checkAccumulator = 0;
    }

    public void countCheck(boolean isChecked) {
        checkAccumulator += isChecked ? 1 : -1;
    }

    public int getCountCheck() {
        return checkAccumulator;
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
        LinearLayout out_con;
        TextView out; //해체, 탈퇴
        FrameLayout share_check_con;
        CheckBox share_check;
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
            vh.out_con = (LinearLayout) view.findViewById(R.id.out_con);
            vh.out = (TextView) view.findViewById(R.id.out);
            vh.share_check_con = (FrameLayout) view.findViewById(R.id.share_check_con);
            vh.share_check = (CheckBox) view.findViewById(R.id.share_check);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final Group item = data.get(i);
        vh.groupName.setText(item.getGroupName());
        vh.bossPhone.setText(item.getBossPhone());
        final ViewHolder finalVh = vh;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MemberActivity.class);
                intent.putExtra("group", item.getGroupNumber());
                ((Activity) context).startActivityForResult(intent, 1);
            }
        });

        vh.out_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalVh.out.callOnClick();
            }
        });
        vh.out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("GroupListAdapter", " out ");
                twoBtnDialog = new TwoBtnDialog(context, "해당 그룹을\n탈퇴 하시겠습니까?", item, item.getGroupNumber(), 9);
                twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                twoBtnDialog.setCancelable(false);
                twoBtnDialog.show();
            }
        });

        if ("none".equals(share)) {
            view.setClickable(true);
            vh.share_check_con.setVisibility(View.GONE);
            vh.share_check.setVisibility(View.GONE);
            vh.out_con.setVisibility(View.VISIBLE);
            vh.out.setVisibility(View.VISIBLE);

            if (1 == item.getType()) {
                vh.share_check_con.setVisibility(View.GONE);
                vh.share_check.setVisibility(View.GONE);
                vh.out.setText("탈퇴");
                vh.out.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("GroupListAdapter", " out ");
                        twoBtnDialog = new TwoBtnDialog(context, "해당 그룹을\n탈퇴 하시겠습니까?", item, item.getGroupNumber(), item.getType());
                        twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        twoBtnDialog.setCancelable(false);
                        twoBtnDialog.show();
                    }
                });
            } else if (9 == item.getType()) {
                vh.share_check_con.setVisibility(View.GONE);
                vh.share_check.setVisibility(View.GONE);
                vh.out.setText("해체");
                vh.out.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("GroupListAdapter", " out ");
                        twoBtnDialog = new TwoBtnDialog(context, "해당 그룹을\n해체 하시겠습니까?", item, item.getGroupNumber(), item.getType());
                        twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        twoBtnDialog.setCancelable(false);
                        twoBtnDialog.show();
                    }
                });
            }
        } else if ("share".equals(share)) {
            view.setClickable(false);
            vh.out_con.setVisibility(View.GONE);
            vh.out.setVisibility(View.GONE);
            vh.share_check_con.setVisibility(View.VISIBLE);
            vh.share_check_con.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalVh.share_check.callOnClick();
                }
            });
            vh.share_check.setVisibility(View.VISIBLE);
            vh.share_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    countCheck(isChecked);
                    if (isChecked) {
                        SharedPreferences shareGroupList = context.getSharedPreferences("shareGroupList", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shareGroupList.edit();
                        editor.putString("" + i, item.getGroupNumber());
                        editor.commit();
//                        Toast.makeText(context, "Check! " + i +" "+ item.getGroupNumber(), Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences shareGroupList = context.getSharedPreferences("shareGroupList", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shareGroupList.edit();
                        editor.remove("" + i);
                        editor.commit();
//                        Toast.makeText(context, "Off! " + i, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        return view;
    }

    //그룹 해체, 탈퇴
    public class TwoBtnDialog extends Dialog {
        TwoBtnDialog twoBtnDialog = this;
        Context context;

        public TwoBtnDialog(final Context context, final String text, final Group item, final String groupNumber, final int type) {
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
            title1.setText(text);
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
                    final String url = UrlManager.getBaseUrl() + "group/remove";
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
                                                    mainListAdapter.notifyDataSetChanged();

                                                    if (type == 1) {
                                                        Toast.makeText(context, "탈퇴 하였습니다.", Toast.LENGTH_SHORT).show();
                                                    } else if (type == 9) {
                                                        Toast.makeText(context, "해체 하였습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                            } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                                if("pay".equals(jsonObject.getString("type"))){
                                                    //기간 만료
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

//                    Map<String, Object> params = new HashMap<String, Object>();
//                    params.put("group", groupNumber);
//                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            try {
//                                if (jsonObject.getBoolean("return")) {
//                                    data.remove(item);
//                                    mainListAdapter.notifyDataSetChanged();
//
//                                    if(type == 1){
//                                        Toast.makeText(context, "탈퇴 하였습니다.", Toast.LENGTH_SHORT).show();
//                                    }else if(type == 9){
//                                        Toast.makeText(context, "해체 하였습니다.", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                } else {
//
//                                    if("pay".equals(jsonObject.getString("type"))){
//                                        //기간 만료
//                                        Intent intent = new Intent(context, BillingListActivity.class);
//                                        context.startActivity(intent);
//                                        ((Activity)context).finish();
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
}
