package app.cosmos.ghrealestatediary;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
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

import app.cosmos.ghrealestatediary.DTO.Main;
import app.cosmos.ghrealestatediary.DTO.ShareBuilding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ShareBuildingListAdapter extends BaseAdapter {

    Context context;
    int layout;
    ArrayList<ShareBuilding> data;
    ListView listView;
    AQuery aQuery = null;
    ShareBuildingListAdapter mainListAdapter;
    TwoBtnDialog twoBtnDialog;
    ShareBuildingListAdapter shareBuildingListAdapter = this;
    String group;
    int checkAccumulator;

    public ShareBuildingListAdapter(Context context, int layout, ArrayList<ShareBuilding> data, ListView listView, String group) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
        this.group = group;
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
        ImageView map;
        TextView name;
        TextView title;
        TextView status;
        FrameLayout delete_btn_con;
        TextView delete_btn;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder vh = null;
        aQuery = new AQuery(context);
        if (view == null) {
            view = View.inflate(context, layout, null);
            vh = new ViewHolder();
            vh.map = (ImageView) view.findViewById(R.id.map);
            vh.name = (TextView) view.findViewById(R.id.name);
            vh.title = (TextView) view.findViewById(R.id.title);
            vh.status = (TextView) view.findViewById(R.id.status);
            vh.delete_btn_con = (FrameLayout) view.findViewById(R.id.delete_btn_con);
            vh.delete_btn = (TextView) view.findViewById(R.id.delete_btn);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final ShareBuilding item = data.get(i);
        vh.name.setText(item.getName());
        vh.title.setText(item.getTitle());
        if ("0".equals(item.getStatus())) {
            vh.status.setText("거래대기");
            vh.status.setTextColor(Color.parseColor("#ff5656"));
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {

            } else {

            }
        } else if ("1".equals(item.getStatus())) {
            vh.status.setText("거래중");
            vh.status.setTextColor(Color.parseColor("#44cd1a"));
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {

            } else {

            }
        } else if ("2".equals(item.getStatus())) {
            vh.status.setText("거래완료");
            vh.status.setTextColor(Color.parseColor("#7199ff"));
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {

            } else {

            }
        }
        vh.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("la", item.getLat());
                intent.putExtra("lo", item.getLng());
                ((Activity) context).setResult(666, intent);
                ((Activity) context).finish();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InputActivity.class);
                intent.putExtra("idx", item.getIdx());
                intent.putExtra("status", item.getStatus());
                ((Activity) context).startActivityForResult(intent, 1);
            }
        });
        final ViewHolder finalVh = vh;

        if ("true".equals(item.getMy())) {
            vh.delete_btn_con.setVisibility(View.VISIBLE);
            vh.delete_btn_con.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalVh.delete_btn.callOnClick();
                }
            });
            vh.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnDialog = new TwoBtnDialog(context, "해당 매물 공유를\n취소 하시겠습니까?", item, item.getIdx());
                    twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    twoBtnDialog.setCancelable(false);
                    twoBtnDialog.show();
                }
            });
        } else {
            vh.delete_btn_con.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    public class TwoBtnDialog extends Dialog {
        TwoBtnDialog twoBtnDialog = this;
        Context context;

        public TwoBtnDialog(final Context context, final String text, final ShareBuilding item, final String idx) {
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
            btn2.setText("삭제");
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
                    String url = UrlManager.getBaseUrl() + "group/sub";
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
                                .add("group", group)
                                .add("idx[0]", idx)
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
                                                shareBuildingListAdapter.notifyDataSetChanged();
                                                Toast.makeText(context, "공유를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                                            } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                                Toast.makeText(context, "공유를 취소할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                            twoBtnDialog.dismiss();
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
//                    params.put("group", group);
//                    params.put("idx[0]", idx);
//                    Log.i("Adapter", " params:: " + params);
//                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            try {
//                                Log.i("Adapter", " jsonObject:: " + jsonObject);
//                                if (jsonObject.getBoolean("return")) {
//                                    data.remove(item);
//                                    shareBuildingListAdapter.notifyDataSetChanged();
//                                    Toast.makeText(context, "공유를 취소하였습니다.", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(context, "공유를 취소할 수 없습니다.", Toast.LENGTH_SHORT).show();
//                                }
//                                twoBtnDialog.dismiss();
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