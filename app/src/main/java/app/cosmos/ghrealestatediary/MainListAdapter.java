package app.cosmos.ghrealestatediary;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainListAdapter extends BaseAdapter {

    Context context;
    int layout;
    String share;
    ArrayList<Main> data;
    ListView listView;
    AQuery aQuery = null;
    MainListAdapter mainListAdapter = this;
    MainActivity mainActivity;
    TwoBtnDialog twoBtnDialog;
    int checkAccumulator;

    public MainListAdapter(Context context, int layout, ArrayList<Main> data, ListView listView, MainActivity mainActivity, String share) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
        this.mainActivity = mainActivity;
        this.share = share;
        checkAccumulator = 0;
    }

    public MainListAdapter(Context context, int layout, String share, ArrayList<Main> data, ListView listView) {
        this.context = context;
        this.layout = layout;
        this.share = share;
        this.data = data;
        this.listView = listView;
        checkAccumulator = 0;
    }

    public void countCheck(boolean isChecked) {
        checkAccumulator += isChecked ? 1 : -1 ;
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

    class ViewHolder{
        ImageView map;
        TextView title1;
        TextView title2;
        TextView status;
        FrameLayout delete_btn_con;
        TextView delete_btn;
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
            vh.map = (ImageView) view.findViewById(R.id.map);
            vh.title1 = (TextView) view.findViewById(R.id.title1);
            vh.title2 = (TextView) view.findViewById(R.id.title2);
            vh.status = (TextView) view.findViewById(R.id.status);
            vh.delete_btn_con = (FrameLayout) view.findViewById(R.id.delete_btn_con);
            vh.delete_btn = (TextView) view.findViewById(R.id.delete_btn);
            vh.share_check_con = view.findViewById(R.id.share_check_con);
            vh.share_check = view.findViewById(R.id.share_check);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final Main item = data.get(i);
        vh.title1.setText(item.getArea1()+"평");
        vh.title2.setText(item.getTitle());
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
                mainActivity.back_btn.callOnClick();
                mainActivity.setMoveTo(item.getLa(), item.getLo());
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InputActivity.class);
                intent.putExtra("idx", item.getIdx());
                intent.putExtra("status", item.getStatus());
                ((Activity)context).startActivityForResult(intent, 1);
            }
        });
        final ViewHolder finalVh = vh;

        if("none".equals(share)){
            vh.share_check_con.setVisibility(View.GONE);
            vh.share_check.setVisibility(View.GONE);

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
                        twoBtnDialog = new TwoBtnDialog(context, "해당 매물을\n삭제 하시겠습니까?", item, item.getIdx());
                        twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        twoBtnDialog.setCancelable(false);
                        twoBtnDialog.show();
                    }
                });
            } else {
                vh.delete_btn_con.setVisibility(View.GONE);
            }
        }else if("share".equals(share)){
            vh.share_check_con.setVisibility(View.VISIBLE);
            vh.share_check.setVisibility(View.VISIBLE);
            vh.delete_btn_con.setVisibility(View.GONE);
            vh.delete_btn.setVisibility(View.GONE);
            vh.share_check_con.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalVh.share_check.callOnClick();
                }
            });
            vh.share_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    countCheck(isChecked);
                    if(isChecked){
                        SharedPreferences shareCheckList = context.getSharedPreferences("shareCheckList", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shareCheckList.edit();
                        editor.putString(""+i, item.getIdx());
                        editor.commit();
//                        Toast.makeText(context, "Check! " + i +" "+ item.getIdx(), Toast.LENGTH_SHORT).show();

                    }else{
                        SharedPreferences shareCheckList = context.getSharedPreferences("shareCheckList", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shareCheckList.edit();
                        editor.remove(""+i);
                        editor.commit();
//                        Toast.makeText(context, "Off! " + i, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        return view;
    }

    public class TwoBtnDialog extends Dialog {
        TwoBtnDialog twoBtnDialog = this;
        Context context;
        public TwoBtnDialog(final Context context, final String text, final Main item, final String idx) {
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
                    String url = UrlManager.getBaseUrl() + "item/delete";
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
                                .add("idx", idx)
                                .build();
                        Request request = new Request.Builder().url(url).header("GHsoft-Agent", "" + getToken).header("User-Agent", "android").post(body).build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override public void onFailure(Call call, IOException e) { //통신 실패
                            }

                            @Override public void onResponse(Call call, Response response) throws IOException {
                                final String res = response.body().string();
                                ((MainActivity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = new JSONObject(res);
                                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                                data.remove(item);
                                                mainListAdapter.notifyDataSetChanged();
                                                Toast.makeText(context, "삭제 하였습니다.", Toast.LENGTH_SHORT).show();
                                            }else if(!jsonObject.getBoolean("return")){//return이 false 면?
                                                Toast.makeText(context, "삭제를 실패 하였습니다.", Toast.LENGTH_SHORT).show();
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
                    twoBtnDialog.dismiss();
//                    Map<String, Object> params = new HashMap<String, Object>();
//                    params.put("idx", idx);
//                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            try {
//
//                                Log.i("DELETE", "jsonObject : " + jsonObject);
//                                Log.i("DELETE", "return : " + jsonObject.getBoolean("return"));
//
//                                if (jsonObject.getBoolean("return")) {
//                                    data.remove(item);
//                                    mainListAdapter.notifyDataSetChanged();
//                                    Toast.makeText(context, "삭제 하였습니다.", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(context, "삭제를 할 수 없습니다.", Toast.LENGTH_SHORT).show();
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
