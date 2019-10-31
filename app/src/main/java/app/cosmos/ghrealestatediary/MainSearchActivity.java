package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.google.gson.JsonArray;


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

import app.cosmos.ghrealestatediary.DTO.PoiDTO;
import app.cosmos.ghrealestatediary.DTO.SearchPoiDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class MainSearchActivity extends AppCompatActivity {

    Context context;
    AQuery aQuery = null;
    InputMethodManager ipm;
    boolean isKeyBoardVisible;
    LinearLayout main_search;
    EditText search_text;
    ImageView search_btn;
    ListView list_view;
    FrameLayout write_con;
    TextView write;
    ArrayList<PoiDTO> poiData;
    MainSearchListAdapter mainSearchListAdapter;
    OneBtnDialog oneBtnDialog;
    Gson gson;
    String getLa, getLo;

    @Override
    public void finish() {
        super.finish();
        if (isKeyBoardVisible) {
            ipm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isKeyBoardVisible) {
            ipm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    boolean keyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(MainSearchActivity.this, true);
            }
        }
        aQuery = new AQuery(this);
        main_search = (LinearLayout) findViewById(R.id.main_search);
        ipm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        search_text = (EditText) findViewById(R.id.search_text);
        search_btn = (ImageView) findViewById(R.id.search_btn);
        list_view = (ListView) findViewById(R.id.list_view);
        write_con = (FrameLayout) findViewById(R.id.write_con);
        write = (TextView) findViewById(R.id.write);
        search_text.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyboardShown(search_text.getRootView())) {
                    isKeyBoardVisible = true;
                } else {
                    isKeyBoardVisible = false;
                }
            }
        });
        gson = new Gson();
        poiData = new ArrayList<PoiDTO>();
        mainSearchListAdapter = new MainSearchListAdapter(MainSearchActivity.this, R.layout.list_search, poiData, list_view);
        search_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        search_btn.callOnClick();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        search_btn.callOnClick();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        search_btn.callOnClick();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        search_btn.callOnClick();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        search_btn.callOnClick();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                write_con.setVisibility(View.VISIBLE);

                if ("".equals(search_text.getText().toString().trim()) || "" == search_text.getText().toString().trim()) {
                    oneBtnDialog = new OneBtnDialog(MainSearchActivity.this, "검색 내용을 입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                String url = "https://api2.sktelecom.com/tmap/pois?version=1&appKey=9241461c-cece-4c80-a03f-03da0124f1fe&page=1&count=30&searchKeyword=" + search_text.getText() + "&resCoordType=WGS84GEO&searchType=all&searchtypCd=A";
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
                        @Override public void onFailure(Call call, IOException e) { //통신 실패
                        }

                        @Override public void onResponse(Call call, Response response) throws IOException {
                            final String res = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(res);
                                        if (isKeyBoardVisible) {
                                            ipm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                        }
                                        mainSearchListAdapter.resetCount();
                                        SharedPreferences searchCheck = getSharedPreferences("searchCheck", Activity.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = searchCheck.edit();
                                        editor.clear();
                                        editor.commit();
                                        search_text.clearFocus();
                                        poiData.clear();
                                        if (jsonObject != null) {
                                            SearchPoiDTO getSearchPoi = gson.fromJson(jsonObject.getString("searchPoiInfo"), SearchPoiDTO.class);
                                            JsonArray list = getSearchPoi.getPois().getAsJsonArray("poi");
                                            for (int i = 0; i < list.size(); i++) {
                                                poiData.add(gson.fromJson(list.get(i), PoiDTO.class));
                                            }
                                            list_view.setAdapter(mainSearchListAdapter);
                                            mainSearchListAdapter.notifyDataSetChanged();
                                        } else {
                                            poiData.clear();
                                            list_view.setAdapter(mainSearchListAdapter);
                                            mainSearchListAdapter.notifyDataSetChanged();
                                            oneBtnDialog = new OneBtnDialog(MainSearchActivity.this, "검색 결과가 없습니다 !", "확인");
                                            oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            oneBtnDialog.setCancelable(false);
                                            oneBtnDialog.show();
                                        }
                                        search_text.setText("");
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

//                aQuery.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
//                    @Override
//                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                        try {
//                            if (isKeyBoardVisible) {
//                                ipm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//                            }
//                            mainSearchListAdapter.resetCount();
//                            SharedPreferences searchCheck = getSharedPreferences("searchCheck", Activity.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = searchCheck.edit();
//                            editor.clear();
//                            editor.commit();
//                            search_text.clearFocus();
//                            poiData.clear();
//                            if (jsonObject != null) {
//                                SearchPoiDTO getSearchPoi = gson.fromJson(jsonObject.getString("searchPoiInfo"), SearchPoiDTO.class);
//                                JsonArray list = getSearchPoi.getPois().getAsJsonArray("poi");
//                                for (int i = 0; i < list.size(); i++) {
//                                    poiData.add(gson.fromJson(list.get(i), PoiDTO.class));
//                                }
//                                list_view.setAdapter(mainSearchListAdapter);
//                                mainSearchListAdapter.notifyDataSetChanged();
//                            } else {
//                                poiData.clear();
//                                list_view.setAdapter(mainSearchListAdapter);
//                                mainSearchListAdapter.notifyDataSetChanged();
//                                oneBtnDialog = new OneBtnDialog(MainSearchActivity.this, "검색 결과가 없습니다 !", "확인");
//                                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                oneBtnDialog.setCancelable(false);
//                                oneBtnDialog.show();
//                            }
//                            search_text.setText("");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
            }
        });
        write_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write.callOnClick();
            }
        });
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 >= mainSearchListAdapter.getCountCheck()) {
                    oneBtnDialog = new OneBtnDialog(MainSearchActivity.this, "선택 항목이 없습니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                } else {
                    if (isKeyBoardVisible) {
                        ipm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }
                    Intent intent = new Intent(MainSearchActivity.this, WriteActivity.class);
                    intent.putExtra("addr", mainSearchListAdapter.getAddress());
                    getLa = mainSearchListAdapter.getLa();
                    getLo = mainSearchListAdapter.getLo();
                    intent.putExtra("la", getLa);
                    intent.putExtra("lo", getLo);
                    startActivityForResult(intent, 1);
                }
            }
        });
        Intent intent = getIntent();
        String getIntent = intent.getStringExtra("intent");
        if (getIntent != null) {
            search_text.requestFocus();
            ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_CANCELED:
                break;
            case 666:
                Intent intent = new Intent();
                intent.putExtra("la", getLa);
                intent.putExtra("lo", getLo);
                setResult(666, intent);
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
