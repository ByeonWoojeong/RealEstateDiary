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
import android.widget.FrameLayout;
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

import app.cosmos.ghrealestatediary.DTO.Consult;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ConsultListAdapter extends BaseAdapter {

    Context context;
    int layout;
    ArrayList<Consult> data;
    ListView listView;
    AQuery aQuery = null;
    ConsultListAdapter consultListAdapter = this;
    TwoBtnDialog twoBtnDialog;

    public ConsultListAdapter(Context context, int layout, ArrayList<Consult> data, ListView listView) {
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
        TextView name;
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
            vh.name = (TextView) view.findViewById(R.id.name);
            vh.delete_btn_con = (FrameLayout) view.findViewById(R.id.delete_btn_con);
            vh.delete_btn = (TextView) view.findViewById(R.id.delete_btn);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final Consult item = data.get(i);
        vh.name.setText(item.getName());
        final ViewHolder finalVh = vh;
        vh.delete_btn_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalVh.delete_btn.callOnClick();
            }
        });
        vh.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoBtnDialog = new TwoBtnDialog(context, item.getIdx(), item);
                twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                twoBtnDialog.setCancelable(false);
                twoBtnDialog.show();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConsultInputActivity.class);
                intent.putExtra("idx", item.getIdx());
                ((Activity) context).startActivityForResult(intent, 1);
            }
        });
        return view;
    }

    public class TwoBtnDialog extends Dialog {
        TwoBtnDialog twoBtnDialog = this;
        Context context;

        public TwoBtnDialog(final Context context, final String idx, final Consult item) {
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
            title1.setText("해당 메모를\n삭제 하시겠습니까?");
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
                    final String url = UrlManager.getBaseUrl() + "memo/delete";
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
                        twoBtnDialog.dismiss();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) { //통신 실패
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String res = response.body().string();
                                ((ConsultListActivity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = new JSONObject(res);
                                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                                data.remove(item);
                                                consultListAdapter.notifyDataSetChanged();
                                                Toast.makeText(context, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                            } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                                consultListAdapter.notifyDataSetChanged();
                                                Toast.makeText(context, "삭제를 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
//                    params.put("idx", idx);
//                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            try {
//                                if (jsonObject.getBoolean("return")) {
//                                    data.remove(item);
//                                    consultListAdapter.notifyDataSetChanged();
//                                    Toast.makeText(context, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    data.remove(item);
//                                    consultListAdapter.notifyDataSetChanged();
//                                    Toast.makeText(context, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
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

    @SuppressLint("HandlerLeak")
    private Handler successHandler = new Handler() {
        public void handleMessage(Message msg) {
            consultListAdapter.notifyDataSetChanged();
            Toast.makeText(context, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
        }
    };
    @SuppressLint("HandlerLeak")
    private Handler failHandler = new Handler() {
        public void handleMessage(Message msg) {
            consultListAdapter.notifyDataSetChanged();
            Toast.makeText(context, "삭제를 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    };
}
