package app.cosmos.ghrealestatediary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;


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
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import app.cosmos.ghrealestatediary.DTO.FaQ;
import app.cosmos.ghrealestatediary.DTO.Main;
import app.cosmos.ghrealestatediary.DTO.ShareBuilding;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/*******************************************그룹 관리 (공유 매물)******************************************************/

public class SecondFragmentGroup extends Fragment {

    AQuery aQuery = null;
    TextView spinner_text1, spinner_text2, spinner_text3;
    SpinnerReselect spinner1, spinner2, spinner3;
    ImageView down1, down2, down3;
    Boolean userIsInteracting;
    String state, groupNumber;
    String getType1 = "0", getType2 = "", getType3 = "";
    Context context;
    View view;
    ListView buildingListView;
    String token;
    OneBtnDialog oneBtnDialog;

    ArrayList<ShareBuilding> data;
    ShareBuildingListAdapter shareBuildingListAdapter;


    void refreshList(final String type1, final String type2, final String type3) {

        shareBuildingListAdapter = new ShareBuildingListAdapter(context, R.layout.list_share_building, data, buildingListView, groupNumber);
        state = null;
        SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = UrlManager.getBaseUrl() + "group/locationlist";
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
                    .add("my", type1)
                    .add("status", type2)
                    .add("type", type3)
                    .build();
            Request request = new Request.Builder().url(url).header("GHsoft-Agent", "" + getToken).header("User-Agent", "android").post(body).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) { //통신 실패
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String res = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                data.clear();
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                    JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                                    if (jsonArray.length() == 0) {
                                        buildingListView.setVisibility(View.GONE);
                                        SharedPreferences shareCheckList = context.getSharedPreferences("shareCheckList", Activity.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = shareCheckList.edit();
                                        editor.clear();
                                        editor.commit();
                                    } else {
                                        buildingListView.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                            data.add(new ShareBuilding(getJsonObject.getString("idx"), getJsonObject.getString("lat"), getJsonObject.getString("lng"), getJsonObject.getString("name"), getJsonObject.getString("my"), getJsonObject.getString("status"), getJsonObject.getString("title")));
                                        }
                                        buildingListView.setAdapter(shareBuildingListAdapter);
                                        shareBuildingListAdapter.notifyDataSetChanged();
                                    }

                                } else if (!jsonObject.getBoolean("return")) {//return이 false 면?

                                    buildingListView.setAdapter(shareBuildingListAdapter);
                                    shareBuildingListAdapter.notifyDataSetChanged();

                                    if ("pay".equals(jsonObject.getString("type"))) {
                                        Intent intent = new Intent(context, BillingListActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
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


//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("group", groupNumber);
//        params.put("my", type1);
//        params.put("status", type2);
//        params.put("type", type3);
//        Log.i("SecondFragmentGroup", " params:: " + params);
//        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                try {
//                    Log.i("SecondFragmentGroup", " jsonObject:: " + jsonObject);
//                    data.clear();
//                    if (jsonObject.getBoolean("return")) {
//                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
//                        if(jsonArray.length()==0){
//                            buildingListView.setVisibility(View.GONE);
//                            SharedPreferences shareCheckList = context.getSharedPreferences("shareCheckList", Activity.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = shareCheckList.edit();
//                            editor.clear();
//                            editor.commit();
//                        }else{
//                            buildingListView.setVisibility(View.VISIBLE);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
//                                data.add(new ShareBuilding(getJsonObject.getString("idx"), getJsonObject.getString("lat"), getJsonObject.getString("lng"), getJsonObject.getString("name"), getJsonObject.getString("my"), getJsonObject.getString("status"), getJsonObject.getString("title")));
//                            }
//                            buildingListView.setAdapter(shareBuildingListAdapter);
//                            shareBuildingListAdapter.notifyDataSetChanged();
//                        }
//                    } else if (!jsonObject.getBoolean("return")) {
//
//                        buildingListView.setAdapter(shareBuildingListAdapter);
//                        shareBuildingListAdapter.notifyDataSetChanged();
//
//                        if ("pay".equals(jsonObject.getString("type"))) {
//                            Intent intent = new Intent(context, BillingListActivity.class);
//                            startActivity(intent);
//                            getActivity().finish();
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_second_group, container, false);
        SharedPreferences prefToken = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        token = prefToken.getString("Token", "");
        aQuery = new AQuery(context);

        Intent intent = getActivity().getIntent();
        groupNumber = intent.getStringExtra("group");

        spinner1 = (SpinnerReselect) view.findViewById(R.id.spinner1);
        spinner_text1 = (TextView) view.findViewById(R.id.spinner_text1);
        down1 = (ImageView) view.findViewById(R.id.down1);
        spinner2 = (SpinnerReselect) view.findViewById(R.id.spinner2);
        spinner_text2 = (TextView) view.findViewById(R.id.spinner_text2);
        down2 = (ImageView) view.findViewById(R.id.down2);
        spinner3 = (SpinnerReselect) view.findViewById(R.id.spinner3);
        spinner_text3 = (TextView) view.findViewById(R.id.spinner_text3);
        down3 = (ImageView) view.findViewById(R.id.down3);

        buildingListView = (ListView) view.findViewById(R.id.buildingListView);
        data = new ArrayList<ShareBuilding>();


        String[] filter1 = new String[]{
                "최신",
                "내 매물"
        };
        String[] filter2 = new String[]{
                "전체",
                "거래 대기",
                "거래 중",
                "거래 완료"
        };
        String[] filter3 = new String[]{
                "전체",
                "원룸",
                "투/쓰리룸",
                "아파트",
                "사무실",
                "주택",
                "상가"
        };


        refreshList(getType1, getType2, getType3);

        ArrayAdapter<String> filter1Adapter = new ArrayAdapter<String>(context, R.layout.my_spinner, filter1);
        filter1Adapter.setDropDownViewResource(R.layout.my_spinner);
        spinner1.setAdapter(filter1Adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_text1.setText(spinner1.getSelectedItem().toString());
                getType1 = (spinner1.getSelectedItemPosition()) + "";

                refreshList(getType1, getType2, getType3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(context, R.layout.my_spinner, filter2);
        filterAdapter.setDropDownViewResource(R.layout.my_spinner);
        spinner2.setAdapter(filterAdapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_text2.setText(spinner2.getSelectedItem().toString());
                getType2 = (spinner2.getSelectedItemPosition() - 1) + "";
                if ("-1".equals(getType2)) {
                    getType2 = "";
                }
                Log.i("getType2", " " + getType2);

                refreshList(getType1, getType2, getType3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> filter3Adapter = new ArrayAdapter<String>(context, R.layout.my_spinner, filter3);
        filter3Adapter.setDropDownViewResource(R.layout.my_spinner);
        spinner3.setAdapter(filter3Adapter);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_text3.setText(spinner3.getSelectedItem().toString());
                getType3 = (spinner3.getSelectedItemPosition()) + "";
                if ("0".equals(getType3)) {
                    getType3 = "";
                }
                Log.i("getType3", " " + getType3);

                refreshList(getType1, getType2, getType3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    @SuppressLint("HandlerLeak")
    private Handler gone = new Handler() {
        public void handleMessage(Message msg) {
            buildingListView.setVisibility(View.GONE);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler visible = new Handler() {
        public void handleMessage(Message msg) {
            buildingListView.setVisibility(View.VISIBLE);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler listHandler = new Handler() {
        public void handleMessage(Message msg) {
            buildingListView.setAdapter(shareBuildingListAdapter);
            shareBuildingListAdapter.notifyDataSetChanged();
        }
    };

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
