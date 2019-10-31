package app.cosmos.ghrealestatediary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;


import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import app.cosmos.ghrealestatediary.CustomTabLayout.TabLayoutWithArrow;
import app.cosmos.ghrealestatediary.DTO.AddressDTO;
import app.cosmos.ghrealestatediary.DTO.Filter;
import app.cosmos.ghrealestatediary.DTO.Main;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.victor.loading.rotate.RotateLoading;
import com.wunderlist.slidinglayer.SlidingLayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static Context context;

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private Location location;
    private Location location2;
    AQuery aQuery = null;
    LinearLayout filter_con_con, filter_con, addView;
    PorterDuffColorFilter porterDuffColorFilter;
    LocationManager locManager;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    double StartLatitude, StartLongitude;
    InputMethodManager ipm;
    BackPressCloseHandler backPressCloseHandler;
    View view1, view2, view3, view4, view5, view6, view7;
    TextView tab_text1, tab_text2, tab_text3, tab_text4, tab_text5, tab_text6, tab_text7, sae_all, maemae, junsae, woalsae, under_text, capture_text;
    ImageView back_btn, refresh_btn, setting, search, refresh_map, share, ggalddegi, x, left_menu1, left_menu21, left_menu2, left_menu3, left_menu4,
            left_side_menu1, left_side_menu2, left_side_menu3, left_side_menu4, left_side_menu5, left_side_menu6, left_side_menu7,
            left_side_bank, left_side_mart, left_side_drugstore, left_side_oilbank, left_side_cafe, left_side_convenience,
            right_menu1, right_menu2, right_menu3, right_menu4,
            capture_back_btn, capture_refresh, capture_eraser, capture_brush;
    FrameLayout layout_condition1, title_con1, title_con2, listView_con, ggalddegi_con, share_con, x_con, under_text_con, rangebar1_0con, rangebar2_0con, rangebar3_0con, rangebar4_0con, rangebar5_0con, rangebar6_0con, rangebar7_0con, rangebar1_con, rangebar2_con, rangebar3_con, rangebar4_con, rangebar5_1con, rangebar5_2con, rangebar5_3con, rangebar6_1con, rangebar6_2con, rangebar6_3con, rangebar7_1con, rangebar7_2con, rangebar7_3con, capture_text_con, capture_con, drawing_icon_con, capture_refresh_con;
    LinearLayout layout_condition2, title_con0;
    RelativeLayout tab_gradation_1, tab_gradation_2;
    DrawingView drawView;
    ScrollView filter;
    ArrayList<Main> data;
    MainListAdapter mainListAdapter;
    ListView listView;
    SwipeRefreshLayout mSwipeRefresh;
    ProgressBar progressBar;
    Parcelable state;
    ViewPager viewPager;
    TabLayoutWithArrow tabLayout;
    PagerAdapterMain adapter;
    FragmentManager fragmentManager;
    String getLa, getLo;
    String rangebar1_left, rangebar1_0left, rangebar1_right, rangebar1_0right, rangebar2_left, rangebar2_0left, rangebar2_right, rangebar2_0right, rangebar3_left, rangebar3_0left, rangebar3_right, rangebar3_0right, rangebar4_left, rangebar4_0left, rangebar4_right, rangebar4_0right, rangebar5_0left, rangebar5_0right, rangebar5_1left, rangebar5_1right, rangebar5_2left, rangebar5_2right, rangebar5_3left, rangebar5_3right, rangebar6_0left, rangebar6_0right, rangebar6_1left, rangebar6_1right, rangebar6_2left, rangebar6_2right, rangebar6_3left, rangebar6_3right, rangebar7_0left, rangebar7_0right, rangebar7_1left, rangebar7_1right, rangebar7_2left, rangebar7_2right, rangebar7_3left, rangebar7_3right;
    SlidingLayer slidingLayer, slidingLayer2, slidingLayer21, slidingLayer4;
    AlphaAnimation fade_in, fade_out;
    RotateLoading rotateLoading;
    OneBtnDialog oneBtnDialog;
    TwoBtnDialog twoBtnDialog;
    boolean under_list, write_selected, side_menu1_selected, side_menu2_selected, side_menu3_selected,
            menu_bank_selected, menu_mart_selected, menu_drugstore_selected, menu_oilbank_selected, menu_cafe_selected, menu_convenience_selected,
            share_clicked;
    boolean isErase = true;
    Handler handler = new Handler();
    static String tab_selected, sae_selected, rangebar1_0left_f, rangebar1_left_f, rangebar1_0right_f, rangebar1_right_f, rangebar2_0left_f, rangebar2_left_f, rangebar2_0right_f, rangebar2_right_f, rangebar3_0left_f, rangebar3_left_f, rangebar3_0right_f, rangebar3_right_f, rangebar4_0left_f, rangebar4_left_f, rangebar4_0right_f, rangebar4_right_f, rangebar5_0left_f, rangebar5_1left_f, rangebar5_0right_f, rangebar5_1right_f, rangebar5_2left_f, rangebar5_2right_f, rangebar5_3left_f, rangebar5_3right_f, rangebar6_0left_f, rangebar6_1left_f, rangebar6_0right_f, rangebar6_1right_f, rangebar6_2left_f, rangebar6_2right_f, rangebar6_3left_f, rangebar6_3right_f, rangebar7_0left_f, rangebar7_1left_f, rangebar7_0right_f, rangebar7_1right_f, rangebar7_2left_f, rangebar7_2right_f, rangebar7_3left_f, rangebar7_3right_f;
    static boolean roadview_selected;
    WebView webView;

    ArrayList<Integer> status;

    ArrayList<Filter> filterData;   //리스트 뷰
    ArrayList<EditText> find = new ArrayList<>();

    JsonObject defaultFilter;
    JsonObject allFilter;
    JsonObject maemaeFilter;
    JsonObject jenseFilter;
    JsonObject wallseFilter;

    JsonObject filterJson;

    InputMethodManager imm;

    void search() {

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);

        ArrayList<Integer> status2 = new ArrayList<>();
        if (status.get(0) == 1) status2.add(0);
        if (status.get(1) == 1) status2.add(1);
        if (status.get(2) == 1) status2.add(2);

        filterJson.addProperty("status", status2.toString());
        Log.i("SEARCH", "" + filterJson.toString());

        if (filter.getVisibility() != View.VISIBLE && listView_con.getVisibility() != View.VISIBLE) {
            webView.loadUrl("javascript:search(" + filterJson.toString() + ")");
        }

        List<String> checkFilter =
                Arrays.asList("maemae", "bojeung", "wallse", "jense", "geunri", "gaunri", "area1");
        for (int i = 0; i < checkFilter.size(); i++) {

            Log.i("CHECK", " " + checkFilter.get(i));

            if (filterJson.has(checkFilter.get(i))) {
                left_menu2.setSelected(true);
                return;
            }
        }

        left_menu2.setSelected(false);
    }

    void initFilter() {
        //onCreate할 때 시킨다

        status = new ArrayList<>();
        status.add(0);
        status.add(0);
        status.add(0);

        filterJson = new JsonObject();

        defaultFilter = new JsonObject();
        defaultFilter.addProperty("maemae1", 0);
        defaultFilter.addProperty("maemae2", 10000);
        defaultFilter.addProperty("jense1", "0");
        defaultFilter.addProperty("jense2", "500");
        defaultFilter.addProperty("bojeung1", "0");
        defaultFilter.addProperty("bojeung2", "100");
        defaultFilter.addProperty("wallse1", "0");
        defaultFilter.addProperty("wallse2", "100");
        defaultFilter.addProperty("gaunri1", "0");
        defaultFilter.addProperty("gaunri2", "100");
        defaultFilter.addProperty("geunri1", "0");
        defaultFilter.addProperty("geunri2", "100");
        defaultFilter.addProperty("area1", "0");
        defaultFilter.addProperty("area2", "60");

        allFilter = new JsonObject();
        allFilter.addProperty("maemae1", 0);
        allFilter.addProperty("maemae2", 10000);
        allFilter.addProperty("jense1", "0");
        allFilter.addProperty("jense2", "500");
        allFilter.addProperty("bojeung1", "0");
        allFilter.addProperty("bojeung2", "100");
        allFilter.addProperty("wallse1", "0");
        allFilter.addProperty("wallse2", "100");
        allFilter.addProperty("gaunri1", "0");
        allFilter.addProperty("gaunri2", "100");
        allFilter.addProperty("geunri1", "0");
        allFilter.addProperty("geunri2", "100");
        allFilter.addProperty("area1", "0");
        allFilter.addProperty("area2", "60");


        maemaeFilter = new JsonObject();
        maemaeFilter.addProperty("maemae1", "0");
        maemaeFilter.addProperty("maemae2", "10000");
        maemaeFilter.addProperty("gaunri1", "0");
        maemaeFilter.addProperty("gaunri2", "100");
        maemaeFilter.addProperty("geunri1", "0");
        maemaeFilter.addProperty("geunri2", "100");
        maemaeFilter.addProperty("area1", "0");
        maemaeFilter.addProperty("area2", "60");


        jenseFilter = new JsonObject();
        jenseFilter.addProperty("jense1", "0");
        jenseFilter.addProperty("jense2", "500");
        jenseFilter.addProperty("gaunri1", "0");
        jenseFilter.addProperty("gaunri2", "100");
        jenseFilter.addProperty("geunri1", "0");
        jenseFilter.addProperty("geunri2", "100");
        jenseFilter.addProperty("area1", "0");
        jenseFilter.addProperty("area2", "60");

        wallseFilter = new JsonObject();
        wallseFilter.addProperty("bojeung1", "0");
        wallseFilter.addProperty("bojeung2", "100");
        wallseFilter.addProperty("wallse1", "0");
        wallseFilter.addProperty("wallse2", "100");
        wallseFilter.addProperty("gaunri1", "0");
        wallseFilter.addProperty("gaunri2", "100");
        wallseFilter.addProperty("geunri1", "0");
        wallseFilter.addProperty("geunri2", "100");
        wallseFilter.addProperty("area1", "0");
        wallseFilter.addProperty("area2", "60");

    }


    class AndroidBridge {
        @JavascriptInterface
        public void save(final double[] arg) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences saveLocation = getSharedPreferences("saveLocation", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = saveLocation.edit();
                    editor.putString("la", arg[0] + "");
                    editor.putString("lo", arg[1] + "");
                    editor.putString("level", ((int) arg[2]) + "");
                    editor.commit();
                    getLa = arg[0] + "";
                    getLo = arg[1] + "";
                    right_menu1.setSelected(false);
                    slidingLayer2.closeLayer(true);
                    slidingLayer21.closeLayer(true);
                    slidingLayer4.closeLayer(true);
                    if (left_menu1.getVisibility() == View.GONE) {
                        left_menu1.startAnimation(fade_in);
                        left_menu1.setVisibility(View.VISIBLE);
                    }
                    if (side_menu1_selected || side_menu2_selected || side_menu3_selected) {
                        left_menu1.setSelected(true);
                    }
                    if (left_menu21.getVisibility() == View.GONE) {
                        left_menu21.startAnimation(fade_in);
                        left_menu21.setVisibility(View.VISIBLE);
                    }
                    if (left_side_menu4.isSelected()) {
                        left_menu21.setSelected(false);
                    } else {
                        left_menu21.setSelected(true);
                    }
                    if (left_menu4.getVisibility() == View.GONE) {
                        left_menu4.startAnimation(fade_in);
                        left_menu4.setVisibility(View.VISIBLE);
                    }
                    if (menu_bank_selected || menu_mart_selected || menu_drugstore_selected || menu_oilbank_selected || menu_cafe_selected || menu_convenience_selected) {
                        left_menu4.setSelected(true);
                    }
                }
            });
        }

        @JavascriptInterface
        public void addLocation(final double[] arg) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String url = "https://api2.sktelecom.com/tmap/geo/reversegeocoding?version=1&appKey=9241461c-cece-4c80-a03f-03da0124f1fe&lat=" + arg[0] + "&lon=" + arg[1] + "&coordType=WGS84GEO&addressType=A02";
                    aQuery.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            if (jsonObject != null) {
                                try {
                                    Gson gson = new Gson();
                                    AddressDTO getAddress = gson.fromJson(jsonObject.getString("addressInfo"), AddressDTO.class);
                                    twoBtnDialog = new TwoBtnDialog(MainActivity.this, getAddress.getFullAddress().toString(), arg[0] + "", arg[1] + "");
                                    twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    twoBtnDialog.setCancelable(false);
                                    twoBtnDialog.show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            });
        }

        @JavascriptInterface
        public void gps(final String gps) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        final JSONObject jsonObject = new JSONObject(gps);
                        final String lat = jsonObject.getString("lat"), lng = jsonObject.getString("lng");
                        String url = "https://api2.sktelecom.com/tmap/geo/reversegeocoding?version=1&appKey=9241461c-cece-4c80-a03f-03da0124f1fe&lat=" + lat + "&lon=" + lng + "&coordType=WGS84GEO&addressType=A02";

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
                            Request request = new Request.Builder().url(url).post(body).build();
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
                                            if (jsonObject != null) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(res);
                                                    Gson gson = new Gson();
                                                    AddressDTO getAddress = gson.fromJson(jsonObject.getString("addressInfo"), AddressDTO.class);
                                                    twoBtnDialog = new TwoBtnDialog(MainActivity.this, getAddress.getFullAddress().toString(), lat + "", lng + "");
                                                    twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    twoBtnDialog.setCancelable(false);
                                                    twoBtnDialog.show();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
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

//                        aQuery.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
//                            @Override
//                            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                                if (jsonObject != null) {
//                                    try {
//                                        Gson gson = new Gson();
//                                        AddressDTO getAddress = gson.fromJson(jsonObject.getString("addressInfo"), AddressDTO.class);
//                                        twoBtnDialog = new TwoBtnDialog(MainActivity.this, getAddress.getFullAddress().toString(), lat + "", lng + "");
//                                        twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                        twoBtnDialog.setCancelable(false);
//                                        twoBtnDialog.show();
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @JavascriptInterface
        public void detail(final String arg) {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    try {
                        JSONObject jsonObject = new JSONObject(arg);
                        String idx = jsonObject.getString("idx");
                        if (write_selected) {
                            webView.loadUrl("javascript:add()");
                            write_selected = false;
                            right_menu3.setSelected(false);
                        }
                        Intent intent = new Intent(MainActivity.this, InputActivity.class);
                        intent.putExtra("idx", idx + "");
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @JavascriptInterface
        public void pay(final String[] arg) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i("KILLAPP", " 브릿지 pay");
                    myPay("1");
                }
            });
        }

        @JavascriptInterface
        public void close(final String[] arg) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i("KILLAPP", " 브릿지 logout");
                    Toast.makeText(MainActivity.this, "다른 기기에서 로그인 하였습니다.", Toast.LENGTH_SHORT).show();
                    SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefLoginChecked.edit();
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
            });
        }
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location2 = locationList.get(locationList.size() - 1);
                location = location2;

                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());

                Log.d("GPS", "CALL BACK :: onLocationResult : " + markerSnippet);
            }

            Log.d("GPS", "CALL BACK :: Location : " + location);
            Log.d("GPS", "CALL BACK :: onLocationChanged Call");
            onLocationChanged(location);
            Log.d("GPS", "CALL BACK :: onLocationChanged Called");
        }
    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("GPS", "onConnected START");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(10000); //3000(위치 업데이트 반복 주기)
        mLocationRequest.setFastestInterval(10000);  //1500
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            CheckPermission();
        }
        Log.i("GPS", "onConnected -> startLocationUpdates START");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("GPS", "onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("GPS", "onLocationChanged START");
        List<String> providers = locManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                location = locManager.getLastKnownLocation(provider);
                Log.i("GPS", " location " + location);
            }
            if (location == null) {
                Log.i("GPS", "location: " + location + " 위치가 동일할 수 있습니다.");
                continue;
            }
            if (bestLocation == null || location.getAccuracy() < location.getAccuracy()) {
                bestLocation = location;
            }
        }

        //getLatitude 널 처리할 것.!

        StartLatitude = location.getLatitude();
        StartLongitude = location.getLongitude();

        //홍대역
//        StartLatitude = 37.556719;
//        StartLongitude = 126.923673;

        Log.i("GPS", " lat: " + StartLatitude + " lon: " + StartLongitude);
        mGoogleApiClient.disconnect();
        rotateLoading.stop();
        String url = "https://api2.sktelecom.com/tmap/geo/reversegeocoding?version=1&appKey=9241461c-cece-4c80-a03f-03da0124f1fe&lat=" + StartLatitude + "&lon=" + StartLongitude + "&coordType=WGS84GEO&addressType=A02";
        aQuery.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                if (jsonObject != null) {
                    try {
                        Gson gson = new Gson();
                        AddressDTO getAddress = gson.fromJson(jsonObject.getString("addressInfo"), AddressDTO.class);
                        Toast.makeText(MainActivity.this, "현위치는 " + getAddress.getFullAddress() + " 입니다.", Toast.LENGTH_SHORT).show();
                        webView.loadUrl("javascript:myGPS('" + StartLatitude + "','" + StartLongitude + "')");
                        SharedPreferences saveLocation = getSharedPreferences("saveLocation", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = saveLocation.edit();
                        editor.putString("myla", StartLatitude + "");
                        editor.putString("mylo", StartLongitude + "");
                        editor.commit();
                        if (roadview_selected) {
                            webView.loadUrl("javascript:roadView()");
                            webView.loadUrl("javascript:roadView()");
                        }
                        right_menu1.setSelected(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    } //onLocationChanged().....

    private void CheckPermission() {
        Log.i("GPS", "CheckPermission START");
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                return;
            }
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_CONTACTS}, 2);
            return;
        }
        hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                return;
            }
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_CONTACTS}, 2);
            return;
        }
    } //CheckPermission().....

    protected void startLocationUpdates() {
        Log.i("GPS", "startLocationUpdates START");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());

        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.i("GPS", " GPS 서비스 활성화 해야함.2");
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(intent, GPS_ENABLE_REQUEST_CODE);

        } else {
            Log.i("GPS", " GPS 서비스 활성화");
            onLocationChanged(location);
        }


//        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
//
//        Log.i("GPS", " locationAvailability " + locationAvailability);
//        Log.i("GPS", " locationAvailability.isLocationAvailable() : " + locationAvailability.isLocationAvailable());
//
//        if (locationAvailability.isLocationAvailable()) {
//            Log.i("GPS", " locationAvailability.isLocationAvailable() ");
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//        } else {
//            Log.i("GPS", " GPS 서비스 활성화 해야함.1");
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            intent.addCategory(Intent.CATEGORY_DEFAULT);
//            startActivityForResult(intent, GPS_ENABLE_REQUEST_CODE);
//
//            if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                Log.i("GPS", " GPS 서비스 활성화 해야함.2");
//            }else{
//                Log.i("GPS", " GPS 서비스 활성화");
//            }
//        }

    } //startLocationUpdates().....

    //배열을 String 으로 전환
    public void arraysToString(String[] str) {
        Arrays.toString(str);
    }

    void refreshList(String share) {
        mainListAdapter = new MainListAdapter(MainActivity.this, R.layout.list_main, data, listView, MainActivity.this, share);
        state = null;
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = UrlManager.getBaseUrl() + "item/applist";

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
                    .add("lat", getLa)
                    .add("lng", getLo)
                    .add("json", filterJson.toString())
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
                                data.clear();
                                JSONObject jsonObject = new JSONObject(res);
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                    mSwipeRefresh.setVisibility(View.VISIBLE);
                                    JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                                    if (jsonArray.length() == 0) {
                                        listView.setVisibility(View.GONE);
                                        SharedPreferences shareCheckList = getSharedPreferences("shareCheckList", Activity.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = shareCheckList.edit();
                                        editor.clear();
                                        editor.commit();
                                    } else {
                                        listView.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                            data.add(new Main(getJsonObject.getString("idx"), getJsonObject.getString("lat"), getJsonObject.getString("lng"), getJsonObject.getString("name"), getJsonObject.getString("area1"), getJsonObject.getString("area2"), getJsonObject.getString("my"), getJsonObject.getString("distance"), getJsonObject.getString("status")));
                                        }
                                        listView.setAdapter(mainListAdapter);
                                        mainListAdapter.notifyDataSetChanged();
                                    }
                                } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                    if ("nodata".equals(jsonObject.getString("type"))) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mSwipeRefresh.setVisibility(View.GONE);
                                                listView.setAdapter(mainListAdapter);
                                                mainListAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    } else if ("pay".equals(jsonObject.getString("type"))) {
                                        Intent intent = new Intent(MainActivity.this, BillingListActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                if (state != null) {
                                    listView.onRestoreInstanceState(state);
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
//        params.put("lat", getLa);
//        params.put("lng", getLo);
//        params.put("json",filterJson.toString());
//
//        progressBar.setVisibility(View.VISIBLE);
//
//        aQuery.progress(progressBar).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                try {
//                    progressBar.setVisibility(View.GONE);
//                    data.clear();
//                    if (jsonObject.getBoolean("return")) {
//                        mSwipeRefresh.setVisibility(View.VISIBLE);
//                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
//                        if(jsonArray.length()==0){
//                            listView.setVisibility(View.GONE);
//                            SharedPreferences shareCheckList = getSharedPreferences("shareCheckList", Activity.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = shareCheckList.edit();
//                            editor.clear();
//                            editor.commit();
//                        }else{
//                            listView.setVisibility(View.VISIBLE);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
//                                data.add(new Main(getJsonObject.getString("idx"), getJsonObject.getString("lat"), getJsonObject.getString("lng"), getJsonObject.getString("name"), getJsonObject.getString("area1"), getJsonObject.getString("area2"), getJsonObject.getString("my"), getJsonObject.getString("distance"), getJsonObject.getString("status")));
//                            }
//                            listView.setAdapter(mainListAdapter);
//                            mainListAdapter.notifyDataSetChanged();
//                        }
//                    } else if (!jsonObject.getBoolean("return")) {
//                        if ("nodata".equals(jsonObject.getString("type"))) {
//                            mSwipeRefresh.setVisibility(View.GONE);
//                            listView.setAdapter(mainListAdapter);
//                            mainListAdapter.notifyDataSetChanged();
//                        } else if ("pay".equals(jsonObject.getString("type"))) {
//                            Intent intent = new Intent(MainActivity.this, BillingListActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }
//                    if (state != null) {
//                        listView.onRestoreInstanceState(state);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));

    } //refreshList().....

    void setMoveTo(String la, String lo) {
        webView.loadUrl("javascript:gps('" + la + "','" + lo + "')");
        if (la == null || lo == null || "".equals(la) || "".equals(lo)) {
            webView.loadUrl("javascript:gps()");
        }

        if (write_selected) {
            webView.loadUrl("javascript:add()");
            write_selected = false;
            right_menu3.setSelected(false);
        }
        if (roadview_selected) {
            webView.loadUrl("javascript:roadView()");
            webView.loadUrl("javascript:roadView()");
        }

    } //setMoveTo()......

    Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        if (view instanceof SurfaceView) {
            SurfaceView surfaceView = (SurfaceView) view;
            surfaceView.setZOrderOnTop(true);
            surfaceView.draw(canvas);
            surfaceView.setZOrderOnTop(false);
            return bitmap;
        } else {
            view.draw(canvas);
            return bitmap;
        }
    } //viewToBitmap().....

    void myPay(String why_bill) {
        Toast.makeText(this, "결제 후 사용하실 수 있습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, BillingListActivity.class);
        intent.putExtra("why_bill", why_bill);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, 1);
    }

    void category(int index) {
//        Button bank = findViewById(R.id.left_side_bank);
//        Button mart = findViewById(R.id.left_side_mart);
//        Button drugstore = findViewById(R.id.left_side_bank);
//        Button  = findViewById(R.id.left_side_mart);
//        Button bank = findViewById(R.id.left_side_bank);
//        Button mart = findViewById(R.id.left_side_mart);

        ArrayList<View> category = new ArrayList<>();
        category.add(findViewById(R.id.left_side_bank));
        category.add(findViewById(R.id.left_side_mart));
        category.add(findViewById(R.id.left_side_drugstore));
        category.add(findViewById(R.id.left_side_oilbank));
        category.add(findViewById(R.id.left_side_cafe));
        category.add(findViewById(R.id.left_side_convenience));

        Boolean touch = category.get(index).isSelected();
        if (touch) {
            category.get(index).setSelected(false);
        } else {
            for (int i = 0; i < category.size(); i++) {
                category.get(i).setSelected(false);
            }
            category.get(index).setSelected(true);
        }
        webView.loadUrl("javascript:place(" + index + ")");
    }

    @Override
    public void onPause() {
        super.onPause();
        state = listView.onSaveInstanceState();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences shareCheckList = getSharedPreferences("shareCheckList", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = shareCheckList.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    public void finish() {
        super.finish();
        SharedPreferences shareCheckList = getSharedPreferences("shareCheckList", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = shareCheckList.edit();
        editor.clear();
        editor.commit();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    @SuppressLint("AddJavascriptInterface")
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(MainActivity.this, true);
            }
        }

        initFilter();
        context = this;

        SharedPreferences shareCheckList = getSharedPreferences("shareCheckList", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = shareCheckList.edit();
        editor.clear();
        editor.commit();

        /* 자동 로그인 서버 통신 */
        aQuery = new AQuery(this);
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = UrlManager.getBaseUrl() + "member";
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
                                Log.i("서버통신", " " + jsonObject);
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                    SharedPreferences saveLocation = getSharedPreferences("saveLocation", Activity.MODE_PRIVATE);
                                    String getMyX = saveLocation.getString("myla", "");
                                    String getMyY = saveLocation.getString("mylo", "");
                                    getLa = saveLocation.getString("la", "");
                                    getLo = saveLocation.getString("lo", "");
                                    String getLevel = saveLocation.getString("level", "");
                                    if (!"".equals(getLa)) {
                                        String postData = "&mylat=" + getMyX + "&mylng=" + getMyY + "&lat=" + getLa + "&lng=" + getLo + "&level=" + getLevel;
                                        webView.loadUrl("https://map.buda.globalhu.kr/");
                                    } else {
                                        webView.loadUrl("https://map.buda.globalhu.kr/");
                                    }
                                } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                    if ("login".equals(jsonObject.getString("type"))) {
                                        Toast.makeText(MainActivity.this, "로그인을 해주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        finish();
                                        startActivity(intent);
                                    } else if ("permission".equals(jsonObject.getString("type"))) {
                                        Toast.makeText(MainActivity.this, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else if ("pay".equals(jsonObject.getString("type"))) {
                                        Toast.makeText(MainActivity.this, "기간이 만료되었습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, BillingListActivity.class);
                                        startActivity(intent);
                                        finish();
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

//        final Map<String, Object> params = new HashMap<String, Object>();
//        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                try {
//                    Log.i("최초", " " + jsonObject);
//
//                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
//
//                        SharedPreferences saveLocation = getSharedPreferences("saveLocation", Activity.MODE_PRIVATE);
//                        String getMyX = saveLocation.getString("myla", "");
//                        String getMyY = saveLocation.getString("mylo", "");
//                        getLa = saveLocation.getString("la", "");
//                        getLo = saveLocation.getString("lo", "");
//                        String getLevel = saveLocation.getString("level", "");
//                        if (!"".equals(getLa)) {
//                            String postData = "&mylat=" + getMyX + "&mylng=" + getMyY + "&lat=" + getLa + "&lng=" + getLo + "&level=" + getLevel;
//                            webView.loadUrl("https://map.buda.globalhu.kr/");
//
//                        } else {
//                            webView.loadUrl("https://map.buda.globalhu.kr/");
//                        }
//
//                    } else if (!jsonObject.getBoolean("return")) {
//                        if ("login".equals(jsonObject.getString("type"))) {
//                            Toast.makeText(getApplicationContext(), "로그인 해주세요.", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                            finish();
//                            startActivity(intent);
//                        }else if("permission".equals(jsonObject.getString("type"))){
//                            Toast.makeText(getApplicationContext(), "권한이 없습니다.", Toast.LENGTH_SHORT).show();
//                            finish();
//                        }else if("pay".equals(jsonObject.getString("type"))){
//                            Toast.makeText(getApplicationContext(), "기간이 만료되었습니다.", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(MainActivity.this, BillingListActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));


//        aQuery = new AQuery(this);
//        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
//        String getToken = get_token.getString("Token", "");
//        SharedPreferences mainDialogDate = getSharedPreferences("mainDialogDate", Activity.MODE_PRIVATE);
//        String getDate = mainDialogDate.getString("mainDialogDate", "");
//        if (!"".equals(getDate)) {
//            try {
//                Date date = new Date(System.currentTimeMillis());
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                simpleDateFormat.format(date);
//                Date last_day = simpleDateFormat.parse(getDate);
//                if (date.after(last_day)) {
//                    String url = UrlManager.getBaseUrl() + "/main/popup";
//                    Map<String, Object> params = new HashMap<String, Object>();
//                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            try {
//                                if (jsonObject.getBoolean("return")) {
//                                    Intent intent = new Intent(MainActivity.this, NoticeDialogActivity.class);
//                                    intent.putExtra("idx", jsonObject.getString("idx"));
//                                    intent.putExtra("image", jsonObject.getString("image"));
//                                    startActivityForResult(intent, 444);
//                                } else if (!jsonObject.getBoolean("return")) {
////                                if("logout".equals(jsonObject.getString("type"))){
////                                    Log.i("KILLAPP", " onCreate() aQuery /main/popup - logout 1");
////                                    Toast.makeText(MainActivity.this, "다른 기기에서 로그인 하였습니다.", Toast.LENGTH_SHORT).show();
////                                    SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
////                                    SharedPreferences.Editor editor = prefLoginChecked.edit();
////                                    editor.clear();
////                                    editor.commit();
////                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
////                                    finish();
////                                    startActivity(intent);
////                                }
//                            }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        } else {
//            String url = UrlManager.getBaseUrl() + "/main/popup";
//            Map<String, Object> params = new HashMap<String, Object>();
//            aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                @Override
//                public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                    try {
//                        if (jsonObject.getBoolean("return")) {
//                            Intent intent = new Intent(MainActivity.this, NoticeDialogActivity.class);
//                            intent.putExtra("idx", jsonObject.getString("idx"));
//                            intent.putExtra("image", jsonObject.getString("image"));
//                            startActivityForResult(intent, 444);
//                        }else if (!jsonObject.getBoolean("return")) {
////                            if("logout".equals(jsonObject.getString("type"))){
////                                Log.i("KILLAPP", " onCreate() aQuery /main/popup - logout 2");
////                                Toast.makeText(MainActivity.this, "다른 기기에서 로그인 하였습니다.", Toast.LENGTH_SHORT).show();
////                                SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
////                                SharedPreferences.Editor editor = prefLoginChecked.edit();
////                                editor.clear();
////                                editor.commit();
////                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
////                                finish();
////                                startActivity(intent);
////                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
//        }
        ipm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        backPressCloseHandler = new BackPressCloseHandler(this);
        rotateLoading = (RotateLoading) findViewById(R.id.rotateloading);
        rotateLoading.setLoadingColor(Color.parseColor("#7199ff"));
        filter = (ScrollView) findViewById(R.id.filter);
        title_con0 = (LinearLayout) findViewById(R.id.title_con0);
        title_con1 = (FrameLayout) findViewById(R.id.title_con1);
        tab_gradation_1 = (RelativeLayout) findViewById(R.id.tab_gradation_1);
        tab_gradation_2 = (RelativeLayout) findViewById(R.id.tab_gradation_2);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listView_con.getVisibility() == View.VISIBLE) {
                    SharedPreferences shareCheckList = getSharedPreferences("shareCheckList", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shareCheckList.edit();
                    editor.clear();
                    editor.commit();
                    under_text.setText("매물 목록보기");
                    listView_con.setVisibility(View.GONE);
                    title_con0.setVisibility(View.VISIBLE);
                    title_con1.setVisibility(View.GONE);
                    title_con2.setVisibility(View.GONE);
                } else if (filter.getVisibility() == View.VISIBLE) {
                    ipm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    under_text.setText("매물 목록보기");
                    filter.setVisibility(View.GONE);
                    title_con0.setVisibility(View.VISIBLE);
                    title_con1.setVisibility(View.GONE);
                    title_con2.setVisibility(View.GONE);
                    if (under_list) {
                        title_con0.setVisibility(View.GONE);
                        title_con1.setVisibility(View.GONE);
                        title_con2.setVisibility(View.VISIBLE);
                        share_con.setVisibility(View.VISIBLE);
                        ggalddegi_con.setVisibility(View.VISIBLE);
                        x_con.setVisibility(View.GONE);
                        listView_con.setVisibility(View.VISIBLE);
                        refreshList("none");
                        under_list = false;
                        under_text.setText("매물 등록하기");
                    }
                }
            }
        });
        refresh_btn = (ImageView) findViewById(R.id.refresh_btn);
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left_menu2.setSelected(false);
                int k = 0;
                for (int i = 0; i < filterData.size(); i++) {
                    if (i % 2 == 0) {
                        LinearLayout get_filter_con_con = (LinearLayout) filter_con_con.getChildAt(k);
                        EditText start_price0 = get_filter_con_con.getChildAt(0).findViewById(R.id.start_price);
                        EditText end_price0 = get_filter_con_con.getChildAt(0).findViewById(R.id.end_price);
                        EditText start_price1 = get_filter_con_con.getChildAt(1).findViewById(R.id.start_price);
                        EditText end_price1 = get_filter_con_con.getChildAt(1).findViewById(R.id.end_price);
                        start_price0.setText("");
                        end_price0.setText("");
                        start_price1.setText("");
                        end_price1.setText("");
                        k++;
                    }
                }
            }
        });
        setting = (ImageView) findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyDiaryActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (write_selected) {
                    webView.loadUrl("javascript:add()");
                    write_selected = false;
                    right_menu3.setSelected(false);
                }
                Intent intent = new Intent(MainActivity.this, MainSearchActivity.class);
                intent.putExtra("intent", "1");
                startActivityForResult(intent, 1);
            }
        });
        refresh_map = (ImageView) findViewById(R.id.refresh_map);
        refresh_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                webView.loadUrl("javascript:refresh()");
                webView.loadUrl("https://map.buda.globalhu.kr/");
                left_menu1.setSelected(true);
                left_side_menu1.setSelected(true);
                side_menu1_selected = true;
                left_side_menu2.setSelected(false);
                side_menu2_selected = false;
                left_side_menu3.setSelected(false);
                side_menu3_selected = false;
                side_menu3_selected = false;

            }
        });
        title_con2 = (FrameLayout) findViewById(R.id.title_con2);
        listView_con = (FrameLayout) findViewById(R.id.listView_con);
        x_con = (FrameLayout) findViewById(R.id.x_con);
        x = (ImageView) findViewById(R.id.x);
        share_con = (FrameLayout) findViewById(R.id.share_con);
        share = (ImageView) findViewById(R.id.share);
        share_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share.callOnClick();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //매물 리스트 '공유' 버튼 이벤트
                share_clicked = true;
                refreshList("share");
                under_text.setText("공유하기");
                share_con.setVisibility(View.GONE);
                ggalddegi_con.setVisibility(View.GONE);
                x_con.setVisibility(View.VISIBLE);
            }
        });
        ggalddegi_con = (FrameLayout) findViewById(R.id.ggalddegi_con);
        ggalddegi = (ImageView) findViewById(R.id.ggalddegi);
        ggalddegi_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ggalddegi.callOnClick();
            }
        });
        ggalddegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left_menu2.callOnClick();
            }
        });
        x_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x.callOnClick();
            }
        });
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_clicked = false;
                SharedPreferences shareCheckList = getSharedPreferences("shareCheckList", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = shareCheckList.edit();
                editor.clear();
                editor.commit();
                refreshList("none");
                x_con.setVisibility(View.GONE);
                share_con.setVisibility(View.VISIBLE);
                ggalddegi_con.setVisibility(View.VISIBLE);
                under_text.setText("매물 등록하기");
            }
        });
        listView = (ListView) findViewById(R.id.memberListView);
        data = new ArrayList<Main>();
//        mainListAdapter = new MainListAdapter(MainActivity.this, R.layout.list_main, data, listView, MainActivity.this, "none");
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#e3e3e3"), android.graphics.PorterDuff.Mode.SRC_IN);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefresh.setColorSchemeResources(R.color.baseColor, R.color.baseColor);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (share_clicked) {
                    refreshList("share");
                } else {
                    refreshList("none");
                }
                mSwipeRefresh.setRefreshing(false);
            }
        });
        porterDuffColorFilter = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        tabLayout = (TabLayoutWithArrow) findViewById(R.id.tabsMain);
        layout_condition1 = (FrameLayout) findViewById(R.id.layout_condition1);
        layout_condition2 = (LinearLayout) findViewById(R.id.layout_condition2);
        view1 = getLayoutInflater().inflate(R.layout.custom_tab_text, null);
        tab_text1 = (TextView) view1.findViewById(R.id.tab_text);
        tab_text1.setText("전체");
        tab_text1.setTextColor(Color.parseColor("#7199ff"));
        tab_selected = "0";
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));

        view2 = getLayoutInflater().inflate(R.layout.custom_tab_text, null);
        tab_text2 = (TextView) view2.findViewById(R.id.tab_text);
        tab_text2.setText("원룸");
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));

        view3 = getLayoutInflater().inflate(R.layout.custom_tab_text, null);
        tab_text3 = (TextView) view3.findViewById(R.id.tab_text);
        tab_text3.setText("투/쓰리룸");
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));

        view4 = getLayoutInflater().inflate(R.layout.custom_tab_text, null);
        tab_text4 = (TextView) view4.findViewById(R.id.tab_text);
        tab_text4.setText("아파트");
        tabLayout.addTab(tabLayout.newTab().setCustomView(view4));

        view5 = getLayoutInflater().inflate(R.layout.custom_tab_text, null);
        tab_text5 = (TextView) view5.findViewById(R.id.tab_text);
        tab_text5.setText("사무실");
        tabLayout.addTab(tabLayout.newTab().setCustomView(view5));

        view6 = getLayoutInflater().inflate(R.layout.custom_tab_text, null);
        tab_text6 = (TextView) view6.findViewById(R.id.tab_text);
        tab_text6.setText("주택");
        tabLayout.addTab(tabLayout.newTab().setCustomView(view6));

        view7 = getLayoutInflater().inflate(R.layout.custom_tab_text, null);
        tab_text7 = (TextView) view7.findViewById(R.id.tab_text);
        tab_text7.setText("상가");
        tabLayout.addTab(tabLayout.newTab().setCustomView(view7));

        tabLayout.setTabGravity(TabLayoutWithArrow.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.viewpager_main);
        fragmentManager = getSupportFragmentManager();
        adapter = new PagerAdapterMain(fragmentManager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayoutWithArrow.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(6);


        /****************************** TAB 건물 종류 **************************************/
        tabLayout.setOnTabSelectedListener(new TabLayoutWithArrow.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayoutWithArrow.Tab tab) {
                tab_selected = tab.getPosition() + "";
                viewPager.setCurrentItem(tab.getPosition());
                ipm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);

                //전체 (원룸 투쓰리 아파트 사무실 주택 상가)
                if (tab.getPosition() == 0) {
                    tab_text1.setTextColor(Color.parseColor("#7199ff"));
                    tab_text2.setTextColor(Color.parseColor("#acacac"));
                    tab_text3.setTextColor(Color.parseColor("#acacac"));
                    tab_text4.setTextColor(Color.parseColor("#acacac"));
                    tab_text5.setTextColor(Color.parseColor("#acacac"));
                    tab_text6.setTextColor(Color.parseColor("#acacac"));
                    tab_text7.setTextColor(Color.parseColor("#acacac"));

                    filterJson.remove("type");

                    //원룸
                } else if (tab.getPosition() == 1) {
                    tab_text1.setTextColor(Color.parseColor("#acacac"));
                    tab_text2.setTextColor(Color.parseColor("#7199ff"));
                    tab_text3.setTextColor(Color.parseColor("#acacac"));
                    tab_text4.setTextColor(Color.parseColor("#acacac"));
                    tab_text5.setTextColor(Color.parseColor("#acacac"));
                    tab_text6.setTextColor(Color.parseColor("#acacac"));
                    tab_text7.setTextColor(Color.parseColor("#acacac"));

                    filterJson.addProperty("type", "1");

                    //투쓰리
                } else if (tab.getPosition() == 2) {
                    tab_text1.setTextColor(Color.parseColor("#acacac"));
                    tab_text2.setTextColor(Color.parseColor("#acacac"));
                    tab_text3.setTextColor(Color.parseColor("#7199ff"));
                    tab_text4.setTextColor(Color.parseColor("#acacac"));
                    tab_text5.setTextColor(Color.parseColor("#acacac"));
                    tab_text6.setTextColor(Color.parseColor("#acacac"));
                    tab_text7.setTextColor(Color.parseColor("#acacac"));

                    filterJson.addProperty("type", "2");
                    //아파트
                } else if (tab.getPosition() == 3) {
                    tab_text1.setTextColor(Color.parseColor("#acacac"));
                    tab_text2.setTextColor(Color.parseColor("#acacac"));
                    tab_text3.setTextColor(Color.parseColor("#acacac"));
                    tab_text4.setTextColor(Color.parseColor("#7199ff"));
                    tab_text5.setTextColor(Color.parseColor("#acacac"));
                    tab_text6.setTextColor(Color.parseColor("#acacac"));
                    tab_text7.setTextColor(Color.parseColor("#acacac"));
                    filterJson.addProperty("type", "3");

                    //사무실
                } else if (tab.getPosition() == 4) {
                    tab_text1.setTextColor(Color.parseColor("#acacac"));
                    tab_text2.setTextColor(Color.parseColor("#acacac"));
                    tab_text3.setTextColor(Color.parseColor("#acacac"));
                    tab_text4.setTextColor(Color.parseColor("#acacac"));
                    tab_text5.setTextColor(Color.parseColor("#7199ff"));
                    tab_text6.setTextColor(Color.parseColor("#acacac"));
                    tab_text7.setTextColor(Color.parseColor("#acacac"));
                    filterJson.addProperty("type", "4");

                    //주택
                } else if (tab.getPosition() == 5) {
                    tab_text1.setTextColor(Color.parseColor("#acacac"));
                    tab_text2.setTextColor(Color.parseColor("#acacac"));
                    tab_text3.setTextColor(Color.parseColor("#acacac"));
                    tab_text4.setTextColor(Color.parseColor("#acacac"));
                    tab_text5.setTextColor(Color.parseColor("#acacac"));
                    tab_text6.setTextColor(Color.parseColor("#7199ff"));
                    tab_text7.setTextColor(Color.parseColor("#acacac"));
                    filterJson.addProperty("type", "5");

                    //상가
                } else if (tab.getPosition() == 6) {
                    tab_text1.setTextColor(Color.parseColor("#acacac"));
                    tab_text2.setTextColor(Color.parseColor("#acacac"));
                    tab_text3.setTextColor(Color.parseColor("#acacac"));
                    tab_text4.setTextColor(Color.parseColor("#acacac"));
                    tab_text5.setTextColor(Color.parseColor("#acacac"));
                    tab_text6.setTextColor(Color.parseColor("#acacac"));
                    tab_text7.setTextColor(Color.parseColor("#7199ff"));
                    filterJson.addProperty("type", "6");
                }

                if (listView_con.getVisibility() == View.VISIBLE) {
                    if (share_clicked) {
                        refreshList("share");
                    } else {
                        refreshList("none");
                    }
                }
                search();
            }

            @Override
            public void onTabUnselected(TabLayoutWithArrow.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayoutWithArrow.Tab tab) {
                tab_selected = tab.getPosition() + "";
            }
        });
        sae_all = (TextView) findViewById(R.id.sae_all);
        maemae = (TextView) findViewById(R.id.maemae);
        junsae = (TextView) findViewById(R.id.junsae);
        woalsae = (TextView) findViewById(R.id.woalsae);
        filterData = new ArrayList<Filter>();

        SharedPreferences sae_all_pre = getSharedPreferences("sae_all", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sae_all_pre.edit();
        editor1.clear();
        editor1.commit();
        SharedPreferences maemae_pre = getSharedPreferences("maemae", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = maemae_pre.edit();
        editor2.clear();
        editor2.commit();
        SharedPreferences junsae_pre = getSharedPreferences("junsae", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = junsae_pre.edit();
        editor3.clear();
        editor3.commit();
        SharedPreferences woalsae_pre = getSharedPreferences("woalsae", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor4 = woalsae_pre.edit();
        editor4.clear();
        editor4.commit();
        sae_selected = "0";
        filter_con_con = findViewById(R.id.filter_con_con);
        sae_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sae_selected = "0";
                filterJson.remove("sale");
                sae_all.setTextColor(Color.parseColor("#ffffff"));
                maemae.setTextColor(Color.parseColor("#80ffffff"));
                junsae.setTextColor(Color.parseColor("#80ffffff"));
                woalsae.setTextColor(Color.parseColor("#80ffffff"));

                filterData.clear();
                filterData.add(new Filter("매매 (백만원)", "0", "10000"));
                filterData.add(new Filter("전세 (백만원)", "0", "500"));
                filterData.add(new Filter("보증금 (백만원)", "0", "100"));
                filterData.add(new Filter("월세 (만원)", "0", "100"));
                filterData.add(new Filter("권리금 (백만원)", "0", "100"));
                filterData.add(new Filter("관리비 (만원)", "0", "100"));
                filterData.add(new Filter("면적 (평)", "0", "60"));
                filter_con_con.removeAllViews();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < filterData.size(); i++) {

                    final EditText start_price, end_price;

                    if (i % 2 == 0) {
                        filter_con = (LinearLayout) inflater.inflate(R.layout.filter_con, null);
                        filter_con.removeAllViews();
                        addView = (LinearLayout) inflater.inflate(R.layout.filter_grid_item, null);
                        final LinearLayout item_con = (LinearLayout) addView.findViewById(R.id.item_con);
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        param.weight = 0.5f;
                        item_con.setLayoutParams(param);
                        final TextView filter_title = (TextView) addView.findViewById(R.id.filter_title);
                        start_price = (EditText) addView.findViewById(R.id.start_price);
                        end_price = (EditText) addView.findViewById(R.id.end_price);
                        filter_title.setText(filterData.get(i).getFilter_title() + "");
                        SharedPreferences get_filter_data = context.getSharedPreferences("sae_all", Activity.MODE_PRIVATE);
                        if (0 == get_filter_data.getInt("menu" + (i + 1) + "_start_price", 0)) {
                            start_price.setText("");
                        } else {
                            start_price.setText(get_filter_data.getInt("menu" + (i + 1) + "_start_price", 0) + "");
                        }
                        if (0 == get_filter_data.getInt("menu" + (i + 1) + "_end_price", 0)) {
                            end_price.setText("");
                        } else {
                            end_price.setText(get_filter_data.getInt("menu" + (i + 1) + "_end_price", 0) + "");
                        }
                        filter_con.addView(addView);
                        if ((filterData.size() - 1) == i) {
                            View make_view = inflater.inflate(R.layout.filter_con, null);
                            make_view.setLayoutParams(param);
                            filter_con.addView(make_view);
                            filter_con_con.addView(filter_con);
                        }
                    } else {
                        addView = (LinearLayout) inflater.inflate(R.layout.filter_grid_item, null);
                        final LinearLayout item_con = (LinearLayout) addView.findViewById(R.id.item_con);
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        param.weight = 0.5f;
                        item_con.setLayoutParams(param);
                        final TextView filter_title = (TextView) addView.findViewById(R.id.filter_title);
                        start_price = (EditText) addView.findViewById(R.id.start_price);
                        end_price = (EditText) addView.findViewById(R.id.end_price);
                        filter_title.setText(filterData.get(i).getFilter_title() + "");
                        SharedPreferences get_filter_data = context.getSharedPreferences("sae_all", Activity.MODE_PRIVATE);
                        if (0 == get_filter_data.getInt("menu" + (i + 1) + "_start_price", 0)) {
                            start_price.setText("");
                        } else {
                            start_price.setText(get_filter_data.getInt("menu" + (i + 1) + "_start_price", 0) + "");
                        }
                        if (0 == get_filter_data.getInt("menu" + (i + 1) + "_end_price", 0)) {
                            end_price.setText("");
                        } else {
                            end_price.setText(get_filter_data.getInt("menu" + (i + 1) + "_end_price", 0) + "");
                        }
                        filter_con.addView(addView);
                        filter_con_con.addView(filter_con);
                    }
                }

                if (listView_con.getVisibility() == View.VISIBLE) {
                    if (share_clicked) {
                        refreshList("share");
                    } else {
                        refreshList("none");
                    }
                }
                search();
            }
        });
        maemae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sae_selected = "1";
                filterJson.addProperty("sale", "[1]");
                sae_all.setTextColor(Color.parseColor("#80ffffff"));
                maemae.setTextColor(Color.parseColor("#ffffff"));
                junsae.setTextColor(Color.parseColor("#80ffffff"));
                woalsae.setTextColor(Color.parseColor("#80ffffff"));

                filterData.clear();
                filterData.add(new Filter("매매 (백만원)", "0", "10000"));
                filterData.add(new Filter("권리금 (백만원)", "0", "100"));
                filterData.add(new Filter("관리비 (만원)", "0", "100"));
                filterData.add(new Filter("면적 (평)", "0", "60"));
                filter_con_con.removeAllViews();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < filterData.size(); i++) {
                    final EditText start_price;
                    final EditText end_price;
                    if (i % 2 == 0) {
                        filter_con = (LinearLayout) inflater.inflate(R.layout.filter_con, null);
                        filter_con.removeAllViews();
                        addView = (LinearLayout) inflater.inflate(R.layout.filter_grid_item, null);
                        final LinearLayout item_con = (LinearLayout) addView.findViewById(R.id.item_con);
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        param.weight = 0.5f;
                        item_con.setLayoutParams(param);
                        final TextView filter_title = (TextView) addView.findViewById(R.id.filter_title);
                        start_price = (EditText) addView.findViewById(R.id.start_price);
                        end_price = (EditText) addView.findViewById(R.id.end_price);
                        filter_title.setText(filterData.get(i).getFilter_title() + "");
                        SharedPreferences get_filter_data = context.getSharedPreferences("maemae", Activity.MODE_PRIVATE);
                        if (0 == get_filter_data.getInt("menu" + (i + 1) + "_start_price", 0)) {
                            start_price.setText("");
                        } else {
                            start_price.setText(get_filter_data.getInt("menu" + (i + 1) + "_start_price", 0) + "");
                        }
                        if (0 == get_filter_data.getInt("menu" + (i + 1) + "_end_price", 0)) {
                            end_price.setText("");
                        } else {
                            end_price.setText(get_filter_data.getInt("menu" + (i + 1) + "_end_price", 0) + "");
                        }
                        filter_con.addView(addView);
                        if ((filterData.size() - 1) == i) {
                            View make_view = inflater.inflate(R.layout.filter_con, null);
                            make_view.setLayoutParams(param);
                            filter_con.addView(make_view);
                            filter_con_con.addView(filter_con);
                        }

                    } else {
                        addView = (LinearLayout) inflater.inflate(R.layout.filter_grid_item, null);
                        final LinearLayout item_con = (LinearLayout) addView.findViewById(R.id.item_con);
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        param.weight = 0.5f;
                        item_con.setLayoutParams(param);
                        final TextView filter_title = (TextView) addView.findViewById(R.id.filter_title);
                        start_price = (EditText) addView.findViewById(R.id.start_price);
                        end_price = (EditText) addView.findViewById(R.id.end_price);
                        filter_title.setText(filterData.get(i).getFilter_title() + "");
                        SharedPreferences get_filter_data = context.getSharedPreferences("maemae", Activity.MODE_PRIVATE);
                        if (0 == get_filter_data.getInt("menu" + (i + 1) + "_start_price", 0)) {
                            start_price.setText("");
                        } else {
                            start_price.setText(get_filter_data.getInt("menu" + (i + 1) + "_start_price", 0) + "");
                        }
                        if (0 == get_filter_data.getInt("menu" + (i + 1) + "_end_price", 0)) {
                            end_price.setText("");
                        } else {
                            end_price.setText(get_filter_data.getInt("menu" + (i + 1) + "_end_price", 0) + "");
                        }
                        filter_con.addView(addView);
                        filter_con_con.addView(filter_con);
                    }
                }

                if (listView_con.getVisibility() == View.VISIBLE) {
                    if (share_clicked) {
                        refreshList("share");
                    } else {
                        refreshList("none");
                    }
                }
                search();
            }
        });
        junsae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sae_selected = "2";
                filterJson.addProperty("sale", "[2]");
                sae_all.setTextColor(Color.parseColor("#80ffffff"));
                maemae.setTextColor(Color.parseColor("#80ffffff"));
                junsae.setTextColor(Color.parseColor("#ffffff"));
                woalsae.setTextColor(Color.parseColor("#80ffffff"));

                filterData.clear();
                filterData.add(new Filter("전세 (백만원)", "0", "500"));
                filterData.add(new Filter("권리금 (백만원)", "0", "100"));
                filterData.add(new Filter("관리비 (만원)", "0", "100"));
                filterData.add(new Filter("면적 (평)", "0", "60"));
                filter_con_con.removeAllViews();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < filterData.size(); i++) {
                    final EditText start_price;
                    final EditText end_price;
                    if (i % 2 == 0) {
                        filter_con = (LinearLayout) inflater.inflate(R.layout.filter_con, null);
                        filter_con.removeAllViews();
                        addView = (LinearLayout) inflater.inflate(R.layout.filter_grid_item, null);
                        final LinearLayout item_con = (LinearLayout) addView.findViewById(R.id.item_con);
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        param.weight = 0.5f;
                        item_con.setLayoutParams(param);
                        final TextView filter_title = (TextView) addView.findViewById(R.id.filter_title);
                        start_price = (EditText) addView.findViewById(R.id.start_price);
                        end_price = (EditText) addView.findViewById(R.id.end_price);
                        filter_title.setText(filterData.get(i).getFilter_title() + "");
                        SharedPreferences get_filter_data = context.getSharedPreferences("junsae", Activity.MODE_PRIVATE);
                        if (0 == get_filter_data.getInt("menu" + (i + 1) + "_start_price", 0)) {
                            start_price.setText("");
                        } else {
                            start_price.setText(get_filter_data.getInt("menu" + (i + 1) + "_start_price", 0) + "");
                        }
                        if (0 == get_filter_data.getInt("menu" + (i + 1) + "_end_price", 0)) {
                            end_price.setText("");
                        } else {
                            end_price.setText(get_filter_data.getInt("menu" + (i + 1) + "_end_price", 0) + "");
                        }
                        filter_con.addView(addView);
                        if ((filterData.size() - 1) == i) {
                            View make_view = inflater.inflate(R.layout.filter_con, null);
                            make_view.setLayoutParams(param);
                            filter_con.addView(make_view);
                            filter_con_con.addView(filter_con);
                        }
                    } else {
                        addView = (LinearLayout) inflater.inflate(R.layout.filter_grid_item, null);
                        final LinearLayout item_con = (LinearLayout) addView.findViewById(R.id.item_con);
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        param.weight = 0.5f;
                        item_con.setLayoutParams(param);
                        final TextView filter_title = (TextView) addView.findViewById(R.id.filter_title);
                        start_price = (EditText) addView.findViewById(R.id.start_price);
                        end_price = (EditText) addView.findViewById(R.id.end_price);
                        filter_title.setText(filterData.get(i).getFilter_title() + "");
                        SharedPreferences get_filter_data = context.getSharedPreferences("junsae", Activity.MODE_PRIVATE);
                        if (0 == get_filter_data.getInt("menu" + (i + 1) + "_start_price", 0)) {
                            start_price.setText("");
                        } else {
                            start_price.setText(get_filter_data.getInt("menu" + (i + 1) + "_start_price", 0) + "");
                        }
                        if (0 == get_filter_data.getInt("menu" + (i + 1) + "_end_price", 0)) {
                            end_price.setText("");
                        } else {
                            end_price.setText(get_filter_data.getInt("menu" + (i + 1) + "_end_price", 0) + "");
                        }
                        filter_con.addView(addView);
                        filter_con_con.addView(filter_con);
                    }

                }

                if (listView_con.getVisibility() == View.VISIBLE) {
                    if (share_clicked) {
                        refreshList("share");
                    } else {
                        refreshList("none");
                    }
                }
                search();
            }
        });
        woalsae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sae_selected = "3";
                filterJson.addProperty("sale", "[3]");
                sae_all.setTextColor(Color.parseColor("#80ffffff"));
                maemae.setTextColor(Color.parseColor("#80ffffff"));
                junsae.setTextColor(Color.parseColor("#80ffffff"));
                woalsae.setTextColor(Color.parseColor("#ffffff"));

                filterData.clear();
                filterData.add(new Filter("보증금 (백만원)", "0", "100"));
                filterData.add(new Filter("월세 (만원)", "0", "100"));
                filterData.add(new Filter("권리금 (백만원)", "0", "100"));
                filterData.add(new Filter("관리비 (만원)", "0", "100"));
                filterData.add(new Filter("면적 (평)", "0", ""));
                filter_con_con.removeAllViews();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < filterData.size(); i++) {
                    final EditText start_price;
                    final EditText end_price;
                    if (i % 2 == 0) {
                        filter_con = (LinearLayout) inflater.inflate(R.layout.filter_con, null);
                        filter_con.removeAllViews();
                        addView = (LinearLayout) inflater.inflate(R.layout.filter_grid_item, null);
                        final LinearLayout item_con = (LinearLayout) addView.findViewById(R.id.item_con);
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        param.weight = 0.5f;
                        item_con.setLayoutParams(param);
                        final TextView filter_title = (TextView) addView.findViewById(R.id.filter_title);
                        start_price = (EditText) addView.findViewById(R.id.start_price);
                        end_price = (EditText) addView.findViewById(R.id.end_price);
                        filter_title.setText(filterData.get(i).getFilter_title() + "");
                        SharedPreferences get_filter_data = context.getSharedPreferences("woalsae", Activity.MODE_PRIVATE);
                        if (0 == get_filter_data.getInt("menu" + (i + 1) + "_start_price", 0)) {
                            start_price.setText("");
                        } else {
                            start_price.setText(get_filter_data.getInt("menu" + (i + 1) + "_start_price", 0) + "");
                        }
                        if (0 == get_filter_data.getInt("menu" + (i + 1) + "_end_price", 0)) {
                            end_price.setText("");
                        } else {
                            end_price.setText(get_filter_data.getInt("menu" + (i + 1) + "_end_price", 0) + "");
                        }
                        filter_con.addView(addView);
                        if ((filterData.size() - 1) == i) {
                            View make_view = inflater.inflate(R.layout.filter_con, null);
                            make_view.setLayoutParams(param);
                            filter_con.addView(make_view);
                            filter_con_con.addView(filter_con);
                        }
                    } else {
                        addView = (LinearLayout) inflater.inflate(R.layout.filter_grid_item, null);
                        final LinearLayout item_con = (LinearLayout) addView.findViewById(R.id.item_con);
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        param.weight = 0.5f;
                        item_con.setLayoutParams(param);
                        final TextView filter_title = (TextView) addView.findViewById(R.id.filter_title);
                        start_price = (EditText) addView.findViewById(R.id.start_price);
                        end_price = (EditText) addView.findViewById(R.id.end_price);
                        filter_title.setText(filterData.get(i).getFilter_title() + "");
                        SharedPreferences get_filter_data = context.getSharedPreferences("woalsae", Activity.MODE_PRIVATE);
                        if (0 == get_filter_data.getInt("menu" + (i + 1) + "_start_price", 0)) {
                            start_price.setText("");
                        } else {
                            start_price.setText(get_filter_data.getInt("menu" + (i + 1) + "_start_price", 0) + "");
                        }
                        if (0 == get_filter_data.getInt("menu" + (i + 1) + "_end_price", 0)) {
                            end_price.setText("");
                        } else {
                            end_price.setText(get_filter_data.getInt("menu" + (i + 1) + "_end_price", 0) + "");
                        }
                        filter_con.addView(addView);
                        filter_con_con.addView(filter_con);
                    }

                }

                if (listView_con.getVisibility() == View.VISIBLE) {
                    if (share_clicked) {
                        refreshList("share");
                    } else {
                        refreshList("none");
                    }
                }
                search();
            }
        });
        slidingLayer = (SlidingLayer) findViewById(R.id.slidingLayer);
        slidingLayer2 = (SlidingLayer) findViewById(R.id.slidingLayer2);
        slidingLayer21 = (SlidingLayer) findViewById(R.id.slidingLayer21);
        slidingLayer4 = (SlidingLayer) findViewById(R.id.slidingLayer4);
        FrameLayout.LayoutParams rlp = (FrameLayout.LayoutParams) slidingLayer.getLayoutParams();
        FrameLayout.LayoutParams rlp2 = (FrameLayout.LayoutParams) slidingLayer2.getLayoutParams();
        FrameLayout.LayoutParams rlp21 = (FrameLayout.LayoutParams) slidingLayer21.getLayoutParams();
        FrameLayout.LayoutParams rlp4 = (FrameLayout.LayoutParams) slidingLayer4.getLayoutParams();
        slidingLayer.setStickTo(SlidingLayer.STICK_TO_LEFT);
        slidingLayer2.setStickTo(SlidingLayer.STICK_TO_LEFT);
        slidingLayer21.setStickTo(SlidingLayer.STICK_TO_LEFT);
        slidingLayer4.setStickTo(SlidingLayer.STICK_TO_LEFT);
        rlp.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        rlp.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        rlp2.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        rlp2.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        rlp21.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        rlp21.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        rlp4.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        rlp4.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        slidingLayer.setLayoutParams(rlp);
        slidingLayer2.setLayoutParams(rlp2);
        slidingLayer21.setLayoutParams(rlp21);
        slidingLayer4.setLayoutParams(rlp4);
        slidingLayer.setShadowSize(0);
        slidingLayer2.setShadowSize(0);
        slidingLayer21.setShadowSize(0);
        slidingLayer4.setShadowSize(0);
        slidingLayer.setShadowDrawable(null);
        slidingLayer2.setShadowDrawable(null);
        slidingLayer21.setShadowDrawable(null);
        slidingLayer4.setShadowDrawable(null);
        slidingLayer.setSlidingEnabled(false);
        slidingLayer2.setSlidingEnabled(false);
        slidingLayer21.setSlidingEnabled(false);
        slidingLayer4.setSlidingEnabled(false);
        slidingLayer.openLayer(true);
        fade_in = new AlphaAnimation(0.0f, 1.0f);
        fade_out = new AlphaAnimation(1.0f, 0.0f);
        fade_in.setDuration(300);
        fade_out.setDuration(300);
        left_menu1 = (ImageView) findViewById(R.id.left_menu1);
        left_menu21 = (ImageView) findViewById(R.id.left_menu21);
        left_menu2 = (ImageView) findViewById(R.id.left_menu2);
        left_menu3 = (ImageView) findViewById(R.id.left_menu3);
        left_menu4 = (ImageView) findViewById(R.id.left_menu4);
        left_side_menu1 = (ImageView) findViewById(R.id.left_side_menu1);
        left_side_menu2 = (ImageView) findViewById(R.id.left_side_menu2);
        left_side_menu3 = (ImageView) findViewById(R.id.left_side_menu3);
        left_side_menu4 = (ImageView) findViewById(R.id.left_side_menu4);
        left_side_menu5 = (ImageView) findViewById(R.id.left_side_menu5);
        left_side_menu6 = (ImageView) findViewById(R.id.left_side_menu6);
        left_side_menu7 = (ImageView) findViewById(R.id.left_side_menu7);
        left_side_bank = (ImageView) findViewById(R.id.left_side_bank);
        left_side_drugstore = (ImageView) findViewById(R.id.left_side_drugstore);
        left_side_mart = (ImageView) findViewById(R.id.left_side_mart);
        left_side_oilbank = (ImageView) findViewById(R.id.left_side_oilbank);
        left_side_cafe = (ImageView) findViewById(R.id.left_side_cafe);
        left_side_convenience = (ImageView) findViewById(R.id.left_side_convenience);
        left_menu21.setSelected(false);
        left_menu4.setSelected(false);
        left_side_menu4.setSelected(true);
        left_side_menu5.setSelected(false);
        left_side_menu6.setSelected(false);
        left_side_menu7.setSelected(false);
        left_menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left_menu1.startAnimation(fade_out);
                left_menu1.setVisibility(View.GONE);
                slidingLayer2.openLayer(true);
            }
        });
        left_menu21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left_menu21.startAnimation(fade_out);
                left_menu21.setVisibility(View.GONE);
                slidingLayer21.openLayer(true);
            }
        });
        left_menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left_menu4.startAnimation(fade_out);
                left_menu4.setVisibility(View.GONE);
                slidingLayer4.openLayer(true);
            }
        });
        rangebar1_0left = "0";
        rangebar1_left = "0";
        rangebar1_0right = "21";
        rangebar1_right = "21";
        rangebar2_0left = "0";
        rangebar2_left = "0";
        rangebar2_0right = "51";
        rangebar2_right = "51";
        rangebar3_0left = "0";
        rangebar3_left = "0";
        rangebar3_0right = "21";
        rangebar3_right = "21";
        rangebar4_0left = "0";
        rangebar4_left = "0";
        rangebar4_0right = "21";
        rangebar4_right = "21";
        rangebar5_0left = "0";
        rangebar5_1left = "0";
        rangebar5_2left = "0";
        rangebar5_3left = "0";
        rangebar5_0right = "11";
        rangebar5_1right = "11";
        rangebar5_2right = "11";
        rangebar5_3right = "11";
        rangebar6_0left = "0";
        rangebar6_1left = "0";
        rangebar6_2left = "0";
        rangebar6_3left = "0";
        rangebar6_0right = "11";
        rangebar6_1right = "11";
        rangebar6_2right = "11";
        rangebar6_3right = "11";
        rangebar7_0left = "0";
        rangebar7_1left = "0";
        rangebar7_2left = "0";
        rangebar7_3left = "0";
        rangebar7_0right = "7";
        rangebar7_1right = "7";
        rangebar7_2right = "7";
        rangebar7_3right = "7";
        rangebar1_0left_f = "0";
        rangebar1_left_f = "0";
        rangebar1_0right_f = "21";
        rangebar1_right_f = "21";
        rangebar2_0left_f = "0";
        rangebar2_left_f = "0";
        rangebar2_0right_f = "51";
        rangebar2_right_f = "51";
        rangebar3_0left_f = "0";
        rangebar3_left_f = "0";
        rangebar3_0right_f = "21";
        rangebar3_right_f = "21";
        rangebar4_0left_f = "0";
        rangebar4_left_f = "0";
        rangebar4_0right_f = "21";
        rangebar4_right_f = "21";
        rangebar5_0left_f = "0";
        rangebar5_1left_f = "0";
        rangebar5_2left_f = "0";
        rangebar5_3left_f = "0";
        rangebar5_0right_f = "11";
        rangebar5_1right_f = "11";
        rangebar5_2right_f = "11";
        rangebar5_3right_f = "11";
        rangebar6_0left_f = "0";
        rangebar6_1left_f = "0";
        rangebar6_2left_f = "0";
        rangebar6_3left_f = "0";
        rangebar6_0right_f = "11";
        rangebar6_1right_f = "11";
        rangebar6_2right_f = "11";
        rangebar6_3right_f = "11";
        rangebar7_0left_f = "0";
        rangebar7_1left_f = "0";
        rangebar7_2left_f = "0";
        rangebar7_3left_f = "0";
        rangebar7_0right_f = "7";
        rangebar7_1right_f = "7";
        rangebar7_2right_f = "7";
        rangebar7_3right_f = "7";
//        rangebar1_0 = (RangeBar) findViewById(R.id.rangebar1_0);
//        rangebar2_0 = (RangeBar) findViewById(R.id.rangebar2_0);
//        rangebar3_0 = (RangeBar) findViewById(R.id.rangebar3_0);
//        rangebar4_0 = (RangeBar) findViewById(R.id.rangebar4_0);
//        rangebar5_0 = (RangeBar) findViewById(R.id.rangebar5_0);
//        rangebar6_0 = (RangeBar) findViewById(R.id.rangebar6_0);
//        rangebar7_0 = (RangeBar) findViewById(R.id.rangebar7_0);
//        rangebar1 = (RangeBar) findViewById(R.id.rangebar1);
//        rangebar2 = (RangeBar) findViewById(R.id.rangebar2);
//        rangebar3 = (RangeBar) findViewById(R.id.rangebar3);
//        rangebar4 = (RangeBar) findViewById(R.id.rangebar4);
//        rangebar5_1 = (RangeBar) findViewById(R.id.rangebar5_1);
//        rangebar5_2 = (RangeBar) findViewById(R.id.rangebar5_2);
//        rangebar5_3 = (RangeBar) findViewById(R.id.rangebar5_3);
//        rangebar6_1 = (RangeBar) findViewById(R.id.rangebar6_1);
//        rangebar6_2 = (RangeBar) findViewById(R.id.rangebar6_2);
//        rangebar6_3 = (RangeBar) findViewById(R.id.rangebar6_3);
//        rangebar7_1 = (RangeBar) findViewById(R.id.rangebar7_1);
//        rangebar7_2 = (RangeBar) findViewById(R.id.rangebar7_2);
//        rangebar7_3 = (RangeBar) findViewById(R.id.rangebar7_3);

        //Filter ------------------------------------------------------------------------
        left_menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Filter", " Click");
                under_text.setText("적용하기");
                title_con0.setVisibility(View.GONE);
                title_con1.setVisibility(View.VISIBLE);
                title_con2.setVisibility(View.GONE);
                filter.setVisibility(View.VISIBLE);
                filter_con_con.setVisibility(View.VISIBLE);

                if ("0" == sae_selected) {
                    sae_all.callOnClick();
                } else if ("1" == sae_selected) {
                    maemae.callOnClick();
                } else if ("2" == sae_selected) {
                    junsae.callOnClick();
                } else if ("3" == sae_selected) {
                    woalsae.callOnClick();
                }

                if (listView_con.getVisibility() == View.VISIBLE) {
                    SharedPreferences shareCheckList = getSharedPreferences("shareCheckList", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shareCheckList.edit();
                    editor.clear();
                    editor.commit();
                    title_con0.setVisibility(View.GONE);
                    title_con1.setVisibility(View.VISIBLE);
                    title_con2.setVisibility(View.GONE);
                    listView_con.setVisibility(View.GONE);
                    under_list = true;
                }

                filter.scrollTo(0, 0);
                if (!left_menu2.isSelected()) {
                    Log.i("Filter", " !left_menu2.isSelected()");
                }
            }
        });

        left_menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConsultListActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        left_side_menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (side_menu1_selected) {
                    Log.i("스카이뷰", "1 " + side_menu1_selected);
                    left_side_menu1.setSelected(false);
                    side_menu1_selected = false;
                    webView.loadUrl("javascript:map('sky')");
                    if (!side_menu1_selected && !side_menu2_selected && !side_menu3_selected) {
                        left_menu1.setSelected(false);
                    }
                } else {
                    Log.i("스카이뷰", "2 " + side_menu1_selected);
                    left_side_menu1.setSelected(true);
                    side_menu1_selected = true;
                    if (!side_menu1_selected && !side_menu2_selected && !side_menu3_selected) {
                        left_menu1.setSelected(false);
                    }
                    webView.loadUrl("javascript:map('sky')");
                }
            }
        });
        left_menu1.setSelected(true);
        side_menu1_selected = true;
        left_side_menu1.setSelected(true);

        left_side_menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (side_menu2_selected) {
                    left_side_menu2.setSelected(false);
                    side_menu2_selected = false;
                    webView.loadUrl("javascript:map('ter')");
                    if (!side_menu1_selected && !side_menu2_selected && !side_menu3_selected) {
                        left_menu1.setSelected(false);
                    }
                } else {
                    left_side_menu2.setSelected(true);
                    side_menu2_selected = true;
                    webView.loadUrl("javascript:map('ter')");
                }
            }
        });
        left_side_menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (side_menu3_selected) {
                    left_side_menu3.setSelected(false);
                    side_menu3_selected = false;
                    webView.loadUrl("javascript:map('dis')");
                    if (!side_menu1_selected && !side_menu2_selected && !side_menu3_selected) {
                        left_menu1.setSelected(false);
                    }
                } else {
                    left_side_menu3.setSelected(true);
                    side_menu3_selected = true;
                    webView.loadUrl("javascript:map('dis')");
                }
            }
        });

        /*******************************    거래 상태 필터링     ********************************/



        /* 거래 상태 (전체) */
        left_side_menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                left_menu21.setSelected(false);
                left_side_menu4.setSelected(true);
                left_side_menu5.setSelected(false);
                left_side_menu6.setSelected(false);
                left_side_menu7.setSelected(false);
                status.set(0, 0);
                status.set(1, 0);
                status.set(2, 0);

                search();
            }
        });

        /* 거래 상태 (거래 대기) */
        left_side_menu5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wait, ing, end;

                wait = status.get(0) == 1;  //true
                ing = status.get(1) == 1;
                end = status.get(2) == 1;

                // wait 가 트루일때 - if ing, end 가 둘다 false - 전체 on   else   wait off

                if (wait) {
                    if (!ing && !end) {
                        left_side_menu4.setSelected(true);
                        left_menu21.setSelected(false);
                    }
                    left_side_menu5.setSelected(false);
                    status.set(0, 0);
                } else {
                    if (ing && end) {
                        left_side_menu4.setSelected(true);
                        left_menu21.setSelected(false);
                        left_side_menu6.setSelected(false);
                        left_side_menu7.setSelected(false);
                        status.set(1, 0);
                        status.set(2, 0);
                    } else if (!ing && !end) {
                        left_side_menu4.setSelected(false);
                        left_menu21.setSelected(true);
                        left_side_menu5.setSelected(true);
                        status.set(0, 1);
                    } else {
                        //둘중에 하나라도 켜져있을 때
                        left_side_menu5.setSelected(true);
                        status.set(0, 1);
                    }
                }
                search();
            }
        });

        /* 거래 상태 (거래 중) */
        left_side_menu6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean an1, me, an2;

                an1 = status.get(0) == 1;  //true
                me = status.get(1) == 1;
                an2 = status.get(2) == 1;

                // wait 가 트루일때 - if ing, end 가 둘다 false - 전체 on   else   wait off

                if (me) {
                    if (!an1 && !an2) {
                        left_side_menu4.setSelected(true);
                        left_menu21.setSelected(false);
                    }
                    left_side_menu6.setSelected(false);
                    status.set(1, 0);
                } else {
                    if (an1 && an2) {
                        left_side_menu4.setSelected(true);
                        left_menu21.setSelected(false);
                        left_side_menu5.setSelected(false);
                        left_side_menu7.setSelected(false);
                        status.set(0, 0);
                        status.set(2, 0);
                    } else if (!an1 && !an2) {
                        left_side_menu4.setSelected(false);
                        left_menu21.setSelected(true);
                        left_side_menu6.setSelected(true);
                        status.set(1, 1);
                    } else {
                        //둘중에 하나라도 켜져있을 때
                        left_side_menu6.setSelected(true);
                        status.set(1, 1);
                    }
                }
                search();
            }
        });

        /* 거래 상태 (거래 완료) */
        left_side_menu7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean an1, me, an2;

                an1 = status.get(0) == 1;  //true
                an2 = status.get(1) == 1;
                me = status.get(2) == 1;

                // wait 가 트루일때 - if ing, end 가 둘다 false - 전체 on   else   wait off

                if (me) {
                    if (!an1 && !an2) {
                        left_side_menu4.setSelected(true);
                        left_menu21.setSelected(false);
                    }
                    left_side_menu7.setSelected(false);
                    status.set(2, 0);
                } else {
                    if (an1 && an2) {
                        left_side_menu4.setSelected(true);
                        left_menu21.setSelected(false);
                        left_side_menu5.setSelected(false);
                        left_side_menu6.setSelected(false);
                        status.set(0, 0);
                        status.set(1, 0);
                    } else if (!an1 && !an2) {
                        left_side_menu4.setSelected(false);
                        left_menu21.setSelected(true);
                        left_side_menu7.setSelected(true);
                        status.set(2, 1);
                    } else {
                        //둘중에 하나라도 켜져있을 때
                        left_side_menu7.setSelected(true);
                        status.set(2, 1);
                    }
                }
                search();
            }
        });


        /*********************************** 편의 시설 ******************************************/
        left_side_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category(0);
            }
        });
        left_side_mart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category(1);
            }
        });
        left_side_drugstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category(2);
            }
        });
        left_side_oilbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category(3);
            }
        });
        left_side_cafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category(4);
            }
        });
        left_side_convenience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category(5);
            }
        });

        right_menu1 = (ImageView) findViewById(R.id.right_menu1);
        right_menu2 = (ImageView) findViewById(R.id.right_menu2);
        right_menu3 = (ImageView) findViewById(R.id.right_menu3);
        right_menu4 = (ImageView) findViewById(R.id.right_menu4);
        right_menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    right_menu1.setSelected(false);
                    Toast.makeText(MainActivity.this, "GPS가 꺼져있습니다.'위치 서비스’에서 활성화 해주세요.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 123);
                } else {
                    if (write_selected) {
                        webView.loadUrl("javascript:gps()");
                        write_selected = false;
                        right_menu3.setSelected(false);
                    }
                    right_menu1.setSelected(true);
//                    mGoogleApiClient.connect();
                    rotateLoading.start();
                    webView.loadUrl("javascript:gps()");
                    rotateLoading.stop();
                    right_menu1.setSelected(false);
                }
            }
        });
        right_menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roadview_selected) {
                    webView.loadUrl("javascript:roadView()");
                    roadview_selected = false;
                    right_menu2.setSelected(false);
                    slidingLayer.openLayer(true);
                    right_menu3.setClickable(true);
                    right_menu4.setClickable(true);
                    right_menu3.getBackground().clearColorFilter();
                    right_menu4.getBackground().clearColorFilter();
                } else {
                    webView.loadUrl("javascript:roadView()");
                    roadview_selected = true;
                    right_menu2.setSelected(true);
                    slidingLayer.closeLayer(true);
                    right_menu3.setClickable(false);
                    right_menu4.setClickable(false);
                    right_menu3.getBackground().setColorFilter(porterDuffColorFilter);
                    right_menu4.getBackground().setColorFilter(porterDuffColorFilter);
                    if (write_selected) {
                        webView.loadUrl("javascript:add()");
                        write_selected = false;
                        right_menu3.setSelected(false);
                    }
                }
            }
        });
        right_menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (write_selected) {
                    webView.loadUrl("javascript:add()");
                    write_selected = false;
                    right_menu3.setSelected(false);
                } else {
                    webView.loadUrl("javascript:add()");
                    write_selected = true;
                    right_menu3.setSelected(true);
                    Toast.makeText(MainActivity.this, "위치등록 모드가 활성화 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        capture_con = (FrameLayout) findViewById(R.id.capture_con);
        drawView = (DrawingView) findViewById(R.id.drawView);
        capture_text_con = (FrameLayout) findViewById(R.id.capture_text_con);
        capture_text = (TextView) findViewById(R.id.capture_text);
        capture_back_btn = (ImageView) findViewById(R.id.capture_back_btn);
        capture_refresh_con = (FrameLayout) findViewById(R.id.capture_refresh_con);
        drawing_icon_con = (FrameLayout) findViewById(R.id.drawing_icon_con);
        capture_refresh = (ImageView) findViewById(R.id.capture_refresh);
        capture_eraser = (ImageView) findViewById(R.id.capture_eraser);
        capture_brush = (ImageView) findViewById(R.id.capture_brush);
        right_menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_condition1.setVisibility(View.GONE);
                layout_condition2.setVisibility(View.GONE);
                capture_con.setVisibility(View.VISIBLE);
                setting.setClickable(false);
                search.setClickable(false);
                Drawable drawable = new BitmapDrawable(getResources(), viewToBitmap(webView));
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    drawView.setBackgroundDrawable(drawable);
                } else {
                    drawView.setBackground(drawable);
                }
                if (drawView.drawCanvas != null) {
                    drawView.startNew();
                }
            }
        });
        capture_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture_con.setVisibility(View.GONE);
                layout_condition1.setVisibility(View.VISIBLE);
                layout_condition2.setVisibility(View.VISIBLE);
                setting.setClickable(true);
                search.setClickable(true);
            }
        });
        capture_refresh_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture_refresh.callOnClick();
            }
        });
        capture_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isErase = false;
                drawView.startNew();
            }
        });
        drawing_icon_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (capture_eraser.getVisibility() == View.VISIBLE) {
                    capture_eraser.callOnClick();
                } else if (capture_brush.getVisibility() == View.VISIBLE) {
                    capture_brush.callOnClick();
                }
            }
        });
        capture_eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isErase) {
                    capture_eraser.setVisibility(View.GONE);
                    capture_brush.setVisibility(View.VISIBLE);
                    drawView.setErase(isErase);
                    isErase = false;
                } else {
                    capture_eraser.setVisibility(View.VISIBLE);
                    capture_brush.setVisibility(View.GONE);
                    drawView.setErase(isErase);
                    isErase = true;
                }

//                drawView.unDo();

            }
        });
        capture_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isErase) {
                    capture_eraser.setVisibility(View.GONE);
                    capture_brush.setVisibility(View.VISIBLE);
                    drawView.setErase(isErase);
                    isErase = false;
                } else {
                    capture_eraser.setVisibility(View.VISIBLE);
                    capture_brush.setVisibility(View.GONE);
                    drawView.setErase(isErase);
                    isErase = true;
                }
//                drawView.unDo();
            }
        });
        capture_text_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture_text.callOnClick();
            }
        });
        capture_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setDrawingCacheEnabled(true);
                MediaStore.Images.Media.insertImage(getContentResolver(), drawView.getDrawingCache(), UUID.randomUUID().toString() + ".png", "drawing");
                drawView.setDrawingCacheEnabled(false);
                drawView.destroyDrawingCache();
                capture_con.setVisibility(View.GONE);
                layout_condition1.setVisibility(View.VISIBLE);
                layout_condition2.setVisibility(View.VISIBLE);
                oneBtnDialog = new OneBtnDialog(MainActivity.this, "갤러리 'Pictures' 폴더에\n저장 되었습니다.", "확인");
                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                oneBtnDialog.setCancelable(false);
                oneBtnDialog.show();
            }
        });
        under_text_con = (FrameLayout) findViewById(R.id.under_text_con);
        under_text = (TextView) findViewById(R.id.under_text);
        under_text_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                under_text.callOnClick();
            }
        });
        under_text.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if ("매물 등록하기".equals(under_text.getText().toString())) {
                    Intent intent = new Intent(MainActivity.this, WriteActivity.class);
                    startActivityForResult(intent, 1);
                } else if ("공유하기".equals(under_text.getText().toString())) {

                    SharedPreferences shareCheckList = getSharedPreferences("shareCheckList", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shareCheckList.edit();
                    Map<String, ?> keys = shareCheckList.getAll();
                    Log.i("공유하기", "keys " + keys.size());

                    ArrayList<String> idx = new ArrayList<String>();
                    if (0 != keys.size()) {
                        String getShareIdx = "";
                        int i = 0;
                        for (Map.Entry<String, ?> entry : keys.entrySet()) {
                            idx.add(i, entry.getValue().toString());
                            if (i != 0) {
                                getShareIdx += " ";
                            }
                            getShareIdx += entry.getValue().toString();
                            i++;
                        }
                    }

                    if (0 == mainListAdapter.getCountCheck()) {
                        oneBtnDialog = new OneBtnDialog(MainActivity.this, "한가지 매물 이라도\n 선택해 주세요!", "확인");
                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        oneBtnDialog.setCancelable(false);
                        oneBtnDialog.show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, ShareGroupActivity.class);
                        intent.putStringArrayListExtra("listIdx", idx);
                        startActivity(intent);
                        Log.i("공유하기", " " + idx);
                    }
                } else {
                    if (filter.getVisibility() == View.VISIBLE) {
                        int k = 0;
                        filterJson.remove("maemae");
                        filterJson.remove("bojeung");
                        filterJson.remove("wallse");
                        filterJson.remove("jense");
                        filterJson.remove("geunri");
                        filterJson.remove("gaunri");
                        filterJson.remove("area1");

                        for (int i = 0; i < filterData.size(); i++) {
                            if (i % 2 == 0) {
                                LinearLayout get_filter_con_con = (LinearLayout) filter_con_con.getChildAt(k);
                                EditText start_price0 = get_filter_con_con.getChildAt(0).findViewById(R.id.start_price);
                                EditText end_price0 = get_filter_con_con.getChildAt(0).findViewById(R.id.end_price);
                                EditText start_price1 = get_filter_con_con.getChildAt(1).findViewById(R.id.start_price);
                                EditText end_price1 = get_filter_con_con.getChildAt(1).findViewById(R.id.end_price);
                                String a = "", b = "";

                                if ("0".equals(sae_selected)) {
                                    if (i == 0) {
                                        a = "maemae";
                                        b = "jense";
                                    } else if (i == 2) {
                                        a = "bojeung";
                                        b = "wallse";
                                    } else if (i == 4) {
                                        a = "geunri";
                                        b = "gaunri";
                                    } else if (i == 6) {
                                        a = "area1";
                                        b = "";
                                    }
                                } else if ("1".equals(sae_selected)) {
                                    if (i == 0) {
                                        a = "maemae";
                                        b = "geunri";
                                    } else if (i == 2) {
                                        a = "gaunri";
                                        b = "area1";
                                    }
                                } else if ("2".equals(sae_selected)) {
                                    if (i == 0) {
                                        a = "jense";
                                        b = "geunri";
                                    } else if (i == 2) {
                                        a = "gaunri";
                                        b = "area1";
                                    }
                                } else if ("3".equals(sae_selected)) {
                                    if (i == 0) {
                                        a = "bojeung";
                                        b = "wallse";
                                    } else if (i == 2) {
                                        a = "geunri";
                                        b = "gaunri";
                                    } else if (i == 4) {
                                        a = "area1";
                                        b = "";
                                    }
                                }

                                if (!"".equals(start_price0.getText().toString()) && !"".equals(end_price0.getText().toString())) {
                                    Log.i("STARTPRICE", "(" + start_price0.getText().toString() + ")");
                                    filterJson.addProperty(a, "[" + start_price0.getText() + "," + end_price0.getText() + "]");
                                }

                                if (b != "" && !"".equals(start_price1.getText().toString()) && !"".equals(end_price1.getText().toString())) {
                                    filterJson.addProperty(b, "[" + start_price1.getText() + "," + end_price1.getText() + "]");
                                }
                                k++;
                            }
                        }
                        search();
                        SharedPreferences get_type = null;
                        if ("0".equals(sae_selected)) {
                            get_type = getSharedPreferences("sae_all", Activity.MODE_PRIVATE);
                        } else if ("1".equals(sae_selected)) {
                            get_type = getSharedPreferences("maemae", Activity.MODE_PRIVATE);
                        } else if ("2".equals(sae_selected)) {
                            get_type = getSharedPreferences("junsae", Activity.MODE_PRIVATE);
                        } else if ("3".equals(sae_selected)) {
                            get_type = getSharedPreferences("woalsae", Activity.MODE_PRIVATE);
                        }
                        SharedPreferences.Editor editor = get_type.edit();
                        editor.clear();
                        k = 0;
                        for (int i = 0; i < filterData.size(); i++) {
                            if (i % 2 == 0) {
                                LinearLayout get_filter_con_con = (LinearLayout) filter_con_con.getChildAt(k);
                                EditText start_price0 = get_filter_con_con.getChildAt(0).findViewById(R.id.start_price);
                                EditText end_price0 = get_filter_con_con.getChildAt(0).findViewById(R.id.end_price);
                                EditText start_price1 = get_filter_con_con.getChildAt(1).findViewById(R.id.start_price);
                                EditText end_price1 = get_filter_con_con.getChildAt(1).findViewById(R.id.end_price);
                                editor.putInt("menu" + (i + 1) + "_start_price", "".equals(start_price0.getText().toString()) ? 0 : Integer.parseInt(start_price0.getText().toString()));
                                editor.putInt("menu" + (i + 1) + "_end_price", "".equals(end_price0.getText().toString()) ? 0 : Integer.parseInt(end_price0.getText().toString()));
                                editor.putInt("menu" + (i + 2) + "_start_price", "".equals(start_price1.getText().toString()) ? 0 : Integer.parseInt(start_price1.getText().toString()));
                                editor.putInt("menu" + (i + 2) + "_end_price", "".equals(end_price1.getText().toString()) ? 0 : Integer.parseInt(end_price1.getText().toString()));
                                k++;
                            }
                        }
                        editor.commit();
                        under_text.setText("매물 목록보기");
                        filter.setVisibility(View.GONE);
                        title_con0.setVisibility(View.VISIBLE);
                        title_con1.setVisibility(View.GONE);
                        title_con2.setVisibility(View.GONE);

                        if (under_list) {
                            title_con0.setVisibility(View.GONE);
                            title_con1.setVisibility(View.GONE);
                            title_con2.setVisibility(View.VISIBLE);
                            share_con.setVisibility(View.VISIBLE);
                            ggalddegi_con.setVisibility(View.VISIBLE);
                            x_con.setVisibility(View.GONE);
                            listView_con.setVisibility(View.VISIBLE);
                            refreshList("none");
                            under_list = false;
                            under_text.setText("매물 등록하기");
                        }
                    } else {
                        under_text.setText("매물 등록하기");
                        title_con0.setVisibility(View.GONE);
                        title_con1.setVisibility(View.GONE);
                        title_con2.setVisibility(View.VISIBLE);
                        share_con.setVisibility(View.VISIBLE);
                        ggalddegi_con.setVisibility(View.VISIBLE);
                        x_con.setVisibility(View.GONE);
                        listView_con.setVisibility(View.VISIBLE);
                        refreshList("none");
                    }
                }
            }
        });
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Log.i("GPS", " 위치 서비스 상태 체크: " + locManager);
        webView = (WebView) findViewById(R.id.webview);
        webView.addJavascriptInterface(new AndroidBridge(), "buda");
        String userAgent = new WebView(getBaseContext()).getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(userAgent + "" + getToken);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        userAgent = webView.getSettings().getUserAgentString();
//        getToken = get_token.getString("Token", "");
        webView.getSettings().setUserAgentString(userAgent + "gh_mobile{" + getToken + "}");
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            webView.getSettings().setDisplayZoomControls(false);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            webView.getSettings().setTextZoom(100);
        }
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        slidingLayer2.closeLayer(true);
                        slidingLayer21.closeLayer(true);
                        slidingLayer4.closeLayer(true);
                        if (left_menu1.getVisibility() == View.GONE) {
                            left_menu1.startAnimation(fade_in);
                            left_menu1.setVisibility(View.VISIBLE);
                        }
                        if (left_menu21.getVisibility() == View.GONE) {
                            left_menu21.startAnimation(fade_in);
                            left_menu21.setVisibility(View.VISIBLE);
                        }
                        if (left_menu4.getVisibility() == View.GONE) {
                            left_menu4.startAnimation(fade_in);
                            left_menu4.setVisibility(View.VISIBLE);
                        }
                        if (side_menu1_selected || side_menu2_selected || side_menu3_selected) {
                            left_menu1.setSelected(true);
                        }
                        if (menu_bank_selected || menu_mart_selected || menu_drugstore_selected || menu_oilbank_selected || menu_cafe_selected || menu_convenience_selected) {
                            left_menu4.setSelected(true);
                        }
                        if (left_side_menu4.isSelected()) {
                            left_menu21.setSelected(false);
                        } else {
                            left_menu21.setSelected(true);
                        }
                        break;
                }
                return false;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
                Log.i("JSSCRIPT", "onGeolocationPermissionsShowPrompt");
                callback.invoke(origin, true, false);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.i("JSSCRIPT", "onJsAlert");
                WebDialog webDialog = new WebDialog();
                Dialog dialog = webDialog.alertDialog(MainActivity.this, message, result);
                dialog.show();
                return true;

            }

            @Override
            public void onCloseWindow(WebView w) {
                super.onCloseWindow(w);
                finish();
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
                final WebSettings settings = view.getSettings();
                settings.setDomStorageEnabled(true);
                settings.setJavaScriptEnabled(true);
                settings.setAllowFileAccess(true);
                settings.setAllowContentAccess(true);
                view.setWebChromeClient(this);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(view);
                resultMsg.sendToTarget();
                return false;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                Log.i("JSSCRIPT", "onJsConfirm");
                WebDialog webDialog = new WebDialog();
                Dialog dialog = webDialog.confirmDialog(MainActivity.this, message, result);
                dialog.show();
                return true;

            }
        });
        webView.setWebViewClient(new WebViewClient());
//        SharedPreferences saveLocation = getSharedPreferences("saveLocation", Activity.MODE_PRIVATE);
//        String getMyX = saveLocation.getString("myla", "");
//        String getMyY = saveLocation.getString("mylo", "");
//        getLa = saveLocation.getString("la", "");
//        getLo = saveLocation.getString("lo", "");
//        String getLevel = saveLocation.getString("level", "");
//        if (!"".equals(getLa)) {
//            String postData = "&mylat=" + getMyX + "&mylng=" + getMyY + "&lat=" + getLa + "&lng=" + getLo + "&level=" + getLevel;
////            webView.postUrl(UrlManager.getBaseUrl(), postData.getBytes());
//            webView.loadUrl("https://map.buda.globalhu.kr/");
//
//        } else {
//            webView.loadUrl("https://map.buda.globalhu.kr/");
//        }
    } //onCreate().....

    @Override
    public void onBackPressed() {
        Log.i("BACK", "123");
        if (roadview_selected) {
            webView.loadUrl("javascript:roadView()");
            roadview_selected = false;
            right_menu2.setSelected(false);
            slidingLayer.openLayer(true);
            right_menu3.setClickable(true);
            right_menu4.setClickable(true);
            right_menu3.getBackground().clearColorFilter();
            right_menu4.getBackground().clearColorFilter();
        } else if (listView_con.getVisibility() == View.VISIBLE) {
            back_btn.callOnClick();
        } else if (filter.getVisibility() == View.VISIBLE) {
            back_btn.callOnClick();
        } else if (slidingLayer2.isOpened()) {
            slidingLayer2.closeLayer(true);
            if (left_menu1.getVisibility() == View.GONE) {
                left_menu1.startAnimation(fade_in);
                left_menu1.setVisibility(View.VISIBLE);
            }
            if (side_menu1_selected || side_menu2_selected || side_menu3_selected) {
                left_menu1.setSelected(true);
            }
        } else if (capture_con.getVisibility() == View.VISIBLE) {
            capture_con.setVisibility(View.GONE);
            layout_condition1.setVisibility(View.VISIBLE);
            layout_condition2.setVisibility(View.VISIBLE);
        } else {
            backPressCloseHandler.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("MainResult", " onActivityResult requestCode " + requestCode);
        Log.i("MainResult", " onActivityResult resultCode " + resultCode);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                Log.d("GPS", "onActivityResult : GPS 활성화 되있음");
                onLocationChanged(location);
                break;

            case 1:
                if (listView_con.getVisibility() == View.VISIBLE) {
                    if (share_clicked) {
                        refreshList("share");
                    } else {
                        refreshList("none");
                    }
                }
                break;
        }

        switch (resultCode) {
            case RESULT_CANCELED:
                Log.i("onActivityResult", resultCode + " RESULT_CANCELED");
//                if(data.getStringExtra("la") == null || data.getStringExtra("lo") == null){
//                setMoveTo(data.getStringExtra("la"), data.getStringExtra("lo"));
//                search();
//            }

                if (listView_con.getVisibility() == View.VISIBLE) {
                    if (share_clicked) {
                        refreshList("share");
                    } else {
                        refreshList("none");
                    }
                }
                break;
            case 444:
                Intent intent1 = new Intent(MainActivity.this, NoticeActivity.class);
                intent1.putExtra("idx", data.getStringExtra("idx"));
                startActivity(intent1);
                break;
            case 555:
                if (listView_con.getVisibility() == View.VISIBLE) {
                    if (share_clicked) {
                        refreshList("share");
                    } else {
                        refreshList("none");
                    }
                }
                break;
            case 666:
                setMoveTo(data.getStringExtra("la"), data.getStringExtra("lo"));
                search();
                if (listView_con.getVisibility() == View.VISIBLE) {
                    if (share_clicked) {
                        refreshList("share");
                    } else {
                        refreshList("none");
                    }
                }
                break;
            case 999:
                myPay("1");
                break;
            case 9999:
                Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent2);
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

    public class TwoBtnDialog extends Dialog {
        TwoBtnDialog twoBtnDialog = this;
        Context context;

        public TwoBtnDialog(final Context context, final String address, final String la, final String lo) {
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
            title1.setText(address);
            btn1.setText("취소");
            btn2.setText("등록");
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
                    Intent intent = new Intent(MainActivity.this, WriteActivity.class);
                    intent.putExtra("addr", address);
                    intent.putExtra("la", la);
                    intent.putExtra("lo", lo);
                    startActivityForResult(intent, 1);
                }
            });
        }
    }
}
