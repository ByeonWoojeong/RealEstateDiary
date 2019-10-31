package app.cosmos.ghrealestatediary;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import app.cosmos.ghrealestatediary.DTO.AddressDTO;
import app.cosmos.ghrealestatediary.DTO.PoiDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainSearchListAdapter extends BaseAdapter {

    Context context;
    int layout;
    ArrayList<PoiDTO> data;
    ListView listView;
    AQuery aQuery = null;
    int checkAccumulator;
    String la, lo, address;
    Gson gson;
    Animation animation;
    MainSearchListAdapter mainSearchListAdapter = this;

    public MainSearchListAdapter(Context context, int layout, ArrayList<PoiDTO> data, ListView listView) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.listView = listView;
        checkAccumulator = 0;
        gson = new Gson();
    }

    public void countCheck(boolean isChecked) {
        checkAccumulator += isChecked ? 1 : -1;
    }

    public void resetCount() {
        checkAccumulator = 0;
    }

    public String getLa() {
        return la;
    }

    public String getLo() {
        return lo;
    }

    public String getAddress() {
        return address;
    }

    public int getCountCheck() {
        return checkAccumulator;
    }

    public View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        if (position < firstListItemPosition || position > lastListItemPosition) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
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
        TextView title1;
        TextView title2;
        CheckBox checkbox;
        AddressDTO getAddress;
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
            vh.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final PoiDTO item = data.get(i);
        final ViewHolder finalVh = vh;
        String url = "https://api2.sktelecom.com/tmap/geo/reversegeocoding?version=1&appKey=9241461c-cece-4c80-a03f-03da0124f1fe&lat=" + item.getNoorLat() + "&lon=" + item.getNoorLon() + "&coordType=WGS84GEO&addressType=A02";
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
                    .build();
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) { //통신 실패
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String res = response.body().string();
                    ((MainSearchActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                if (jsonObject != null) {
                                    try {
                                        finalVh.getAddress = gson.fromJson(jsonObject.getString("addressInfo"), AddressDTO.class);
                                        if (!"".equals(item.getTelNo().trim())) {
                                            finalVh.title1.setText(finalVh.getAddress.getFullAddress() + " (" + item.getTelNo() + ")");
                                        } else {
                                            finalVh.title1.setText(finalVh.getAddress.getFullAddress());
                                        }
                                        finalVh.title2.setText("(" + item.getLowerBizName() + ") " + item.getBizName() + " " + item.getName());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
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

//        aQuery.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                if (jsonObject != null) {
//                    try {
//                        finalVh.getAddress = gson.fromJson(jsonObject.getString("addressInfo"), AddressDTO.class);
//                        if (!"".equals(item.getTelNo().trim())) {
//                            finalVh.title1.setText(finalVh.getAddress.getFullAddress() + " (" + item.getTelNo() +")");
//                        } else {
//                            finalVh.title1.setText(finalVh.getAddress.getFullAddress());
//                        }
//                        finalVh.title2.setText("("+item.getLowerBizName()+") "+item.getBizName()+" "+item.getName());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
        vh.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                countCheck(isChecked);
            }
        });
        vh.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("la", item.getNoorLat());
                intent.putExtra("lo", item.getNoorLon());
                ((Activity) context).setResult(666, intent);
                ((Activity) context).finish();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mainSearchListAdapter.getCount(); i++) {
                    CheckBox getCheckbox = (CheckBox) getViewByPosition(i, listView).findViewById(R.id.checkbox);
                    getCheckbox.setChecked(false);
                }
                finalVh.checkbox.setChecked(true);
                SharedPreferences searchCheck = context.getSharedPreferences("searchCheck", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = searchCheck.edit();
                editor.clear();
                editor.putBoolean(i + "", true);
                editor.commit();
                la = item.getNoorLat();
                lo = item.getNoorLon();
                address = finalVh.getAddress.getFullAddress();
            }
        });
        SharedPreferences searchCheck = context.getSharedPreferences("searchCheck", Activity.MODE_PRIVATE);
        vh.checkbox.setChecked(searchCheck.getBoolean(i + "", false));
        animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        view.startAnimation(animation);
        return view;
    }
}
