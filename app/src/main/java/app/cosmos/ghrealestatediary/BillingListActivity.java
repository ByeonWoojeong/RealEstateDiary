package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;


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
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class BillingListActivity extends AppCompatActivity {

    private final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr8uRjq5uZ0WxFeGhF941oNh25YJR3Lr43xxs8uvrIyYXCDfy4383YVV0Gr0WgO7wr4rY6BZAQZ6csnydJBatsG3v5INJGGRAyjKtRH01kCA078RM2S3wUZwuTdYQD8ngrKhMU0M66LeJh5SuLQ3vnNxLu/2dgXQL9+wfiiZjfg+1ciiCNY3pG+q0Ut1SmHV4QTSC3doaOJaBg9HmIBiU6esg/gM8xRGi8Q2dGvzhYIZrq0U90Fg+TByQyM+GGP9LbLjajfl51AietjZGCAHo4O9+b+DF79459qdNHGJdmJh/24Qe4QrGGrSjOeKwGfDhjSfdet43iBIlKCeSXAtbiwIDAQAB";
    Context context;

    BillingProcessor billingProcessor = null;
    private String ITEM_1MONTH = "1month";
    private String ITEM_2MONTH = "2month";
    private String ITEM_3MONTH = "3month";

    AQuery aQuery = null;
    ImageView back;
    ProgressBar progressBar;
    boolean menuClick, bill_overlap;
    TextView subtitle, card, phone, next;
    String why_bill, getDetail, getCost, type, skuId, skuTitle;
    long skuPriceLong;
    ArrayList<FrameLayout> containerArrayList;  //서비스 이용료 1개월, 2개월, 3개월
    ArrayList<TextView> titleArrayList;
    ArrayList<TextView> priceArrayList;
    ArrayList<TextView> priceSideArrayList;
    View addView;
    LinearLayout list_con;
    FrameLayout next_con;
//    ImageView card_img, phone_img;
    ScrollView scrollView;
    OneBtnDialog oneBtnDialog;

    void refreshList() {
        list_con.removeAllViews();
        containerArrayList.clear();
        titleArrayList.clear();
        priceArrayList.clear();
        priceSideArrayList.clear();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView = inflater.inflate(R.layout.list_billing, null);
        final FrameLayout container = (FrameLayout) addView.findViewById(R.id.container);
        final TextView title = (TextView) addView.findViewById(R.id.title);
        final TextView price = (TextView) addView.findViewById(R.id.price);
        final TextView price_side = (TextView) addView.findViewById(R.id.price_side);
        containerArrayList.add(0, container);
        titleArrayList.add(0, title);
        priceArrayList.add(0, price);
        priceSideArrayList.add(0, price_side);
        title.setText("서비스 이용료 (1개월)");  // 상품 이름
        price.setText("9900");    // 가격

        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 리스트 꾸미기
                for (int j = 0; j <1 ; j++) {
                    containerArrayList.get(j).setBackgroundColor(Color.parseColor("#ffffff"));
                    titleArrayList.get(j).setTextColor(Color.parseColor("#5a5a5a"));
                    priceArrayList.get(j).setTextColor(Color.parseColor("#7199ff"));
                    priceSideArrayList.get(j).setTextColor(Color.parseColor("#5a5a5a"));
                }

                // 클릭 시에 리스트 당 ui 변경
                container.setBackgroundColor(Color.parseColor("#5a5a5a"));
                title.setTextColor(Color.parseColor("#ffffff"));
                price.setTextColor(Color.parseColor("#ffffff"));
                price_side.setTextColor(Color.parseColor("#ffffff"));
                next_con.setBackgroundColor(Color.parseColor("#7199ff"));
                getCost = "9900";
            }
        });
        list_con.addView(addView);



//        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
//        final String getToken = get_token.getString("Token", "");
//        String url = UrlManager.getBaseUrl() + "pay/list";
//        Map<String, Object> params = new HashMap<String, Object>();
//        progressBar.setVisibility(View.VISIBLE);
//        aQuery.progress(progressBar).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                try {
//                    progressBar.setVisibility(View.GONE);
//                    if (jsonObject.getBoolean("return")) {
////                        type_con.setVisibility(View.VISIBLE);
//                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
//                        final int getLength = jsonArray.length();
//                        list_con.removeAllViews();
//                        containerArrayList.clear();
//                        titleArrayList.clear();
//                        priceArrayList.clear();
//                        priceSideArrayList.clear();
//                        for (int i = 0; i < getLength; i++) {
//                            final JSONObject jsonObjectList = jsonArray.getJSONObject(i);
//                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                            addView = inflater.inflate(R.layout.list_billing, null);
//                            final FrameLayout container = (FrameLayout) addView.findViewById(R.id.container);
//                            final TextView title = (TextView) addView.findViewById(R.id.title);
//                            final TextView price = (TextView) addView.findViewById(R.id.price);
//                            final TextView price_side = (TextView) addView.findViewById(R.id.price_side);
//                            containerArrayList.add(i, container);
//                            titleArrayList.add(i, title);
//                            priceArrayList.add(i, price);
//                            priceSideArrayList.add(i, price_side);
//                            title.setText(jsonObjectList.getString("detail"));  // 상품 이름
//                            price.setText(String.format("%,d", Integer.parseInt(jsonObjectList.getString("cost"))));    // 가격
//                            addView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                    // 리스트 꾸미기
//                                    for (int j = 0; j < getLength; j++) {
//                                        containerArrayList.get(j).setBackgroundColor(Color.parseColor("#ffffff"));
//                                        titleArrayList.get(j).setTextColor(Color.parseColor("#5a5a5a"));
//                                        priceArrayList.get(j).setTextColor(Color.parseColor("#7199ff"));
//                                        priceSideArrayList.get(j).setTextColor(Color.parseColor("#5a5a5a"));
//                                    }
//
//                                    // 클릭 시에 리스트 당 ui 변경
//                                    container.setBackgroundColor(Color.parseColor("#5a5a5a"));
//                                    title.setTextColor(Color.parseColor("#ffffff"));
//                                    price.setTextColor(Color.parseColor("#ffffff"));
//                                    price_side.setTextColor(Color.parseColor("#ffffff"));
//                                    next_con.setBackgroundColor(Color.parseColor("#7199ff"));
//
//                                    try {
//                                        getDetail = jsonObjectList.getString("detail");
//                                        getCost = jsonObjectList.getString("cost");
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
//                            list_con.addView(addView);
//                        }
//                        scrollView.post(new Runnable() {
//                            public void run() {
//                                scrollView.fullScroll(scrollView.FOCUS_UP);
//                            }
//                        });
//                    } else if (!jsonObject.getBoolean("return")) {
//
//                        list_con.removeAllViews();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
    }


    @Override
    public void finish() {
        super.finish();
        if (menuClick) {
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        } else {
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_list);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(BillingListActivity.this, true);
            }
        }
        aQuery = new AQuery(this);
        subtitle = (TextView) findViewById(R.id.subtitle);
        Intent intent = getIntent();
        why_bill = intent.getStringExtra("why_bill");
        if ("1".equals(why_bill)) {
            subtitle.setText("기간 만료");
        } else if ("2".equals(why_bill)) {
            subtitle.setText("서비스 연장");
        }
        back = (ImageView) findViewById (R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list_con = (LinearLayout) findViewById (R.id.list_con);
//        type_con = (LinearLayout) findViewById (R.id.type_con);
//        card_con = (LinearLayout) findViewById (R.id.card_con);
//        phone_con = (LinearLayout) findViewById (R.id.phone_con);
//        card_img = (ImageView) findViewById (R.id.card_img);
//        phone_img = (ImageView) findViewById (R.id.phone_img);
//        card = (TextView) findViewById (R.id.card);
//        phone = (TextView) findViewById (R.id.phone);
        next_con = (FrameLayout) findViewById (R.id.next_con);
        next = (TextView) findViewById (R.id.next);
        containerArrayList = new ArrayList<FrameLayout>();
        titleArrayList = new ArrayList<TextView>();
        priceArrayList = new ArrayList<TextView>();
        priceSideArrayList = new ArrayList<TextView>();
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#e3e3e3"), android.graphics.PorterDuff.Mode.SRC_IN);


        if(!BillingProcessor.isIabServiceAvailable(this)){
            Toast.makeText(this, "인앱 결제 서비스가 지원되지 않습니다.", Toast.LENGTH_LONG).show();
            finish();
        }

        /* 인앱 결제 구현 */
        billingProcessor = new BillingProcessor(BillingListActivity.this, LICENSE_KEY, new BillingProcessor.IBillingHandler() {

            /* BillingProcessor가 초기화되고, 구매 준비가 되면 호출된다. 이 부분에서 구매할 아이템들을 리스트로 구성해서 보여주는 코드를 구현하면 된다. */
            @Override
            public void onBillingInitialized() {
                Log.i("BillingListActivity", "onBillingInitialized():: 구매 준비 완료");
            }

            /* 특정 제품 ID를 가진 아이템의 구매 성공시 호출된다. */
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {

                // 구매한 아이템 정보
                SkuDetails skuDetails = billingProcessor.getPurchaseListingDetails(productId);
                skuTitle = skuDetails.title;  //해당 아이템의 이름
                String skuPriceText = skuDetails.priceText; //아이템 가격의 현지 화폐 단위. ex 0.99$
                skuPriceLong = skuDetails.priceLong; //아이템 가격을 long으로 리턴. ex 0.99
                skuId = skuDetails.productId;    //아이템 ID를 가지고 옴. 어떤 아이템을 구매했는지 판별 가능
                String skuDescription = skuDetails.description;

                Log.i("BillingListActivity", "onProductPurchased():: 특정 제품 ID 구매 성공 시 호출");
                Log.i("BillingListActivity", "onProductPurchased():: TransactionDetails:: " + details);
                Log.i("BillingListActivity", "onProductPurchased():: SkuDetails:: " + skuDetails);
                Log.i("BillingListActivity", "onProductPurchased():: skuId:: " + skuId);
                Log.i("BillingListActivity", "onProductPurchased():: SkuPriceText:: " + skuPriceText);
                Log.i("BillingListActivity", "onProductPurchased():: SkuPriceLong:: " + skuPriceLong);
                Log.i("BillingListActivity", "onProductPurchased():: SkuTitle:: " + skuTitle);
                Log.i("BillingListActivity", "onProductPurchased():: SkuDescription:: " + skuDescription);

                // 구매 처리
                setResult(Activity.RESULT_OK);

            }

            /* 구매 이력이 있는지 확인하는 메소드 */
            @Override
            public void onPurchaseHistoryRestored() {
                Log.i("BillingListActivity", "onPurchaseHistoryRestored():: - 구매 이력이 있는지 확인");
            }

            /* 구매시 어떤 오류가 발생했을 때 호출된다. */
            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {

                Log.i("BillingListActivity", "onBillingError():: 에러 코드: " + errorCode);

                if(errorCode == Constants.BILLING_RESPONSE_RESULT_USER_CANCELED){   // errorCode == 1
                    Toast.makeText(BillingListActivity.this, "구매 과정에서 취소하셨습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == Constants.BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE){
                    Toast.makeText(BillingListActivity.this, "네트워크 연결이 끊겼습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == Constants.BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE){
                    Toast.makeText(BillingListActivity.this, "요청한 제품을 구매할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(BillingListActivity.this, "구매 중 오류가 발생했습니다.\nerrorCode : " + errorCode, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        });


//        card_con.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                card.callOnClick();
//            }
//        });
//        card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                card.setTextColor(Color.parseColor("#ffffff"));
//                card_con.setBackgroundColor(Color.parseColor("#5a5a5a"));
//                card_img.setSelected(true);
//                phone.setTextColor(Color.parseColor("#5a5a5a"));
//                phone_con.setBackgroundColor(Color.parseColor("#ffffff"));
//                phone_img.setSelected(false);
//                type = "card";
//            }
//        });
//        phone_con.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                phone.callOnClick();
//            }
//        });
//        phone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                card.setTextColor(Color.parseColor("#5a5a5a"));
//                card_con.setBackgroundColor(Color.parseColor("#ffffff"));
//                card_img.setSelected(false);
//                phone.setTextColor(Color.parseColor("#ffffff"));
//                phone_con.setBackgroundColor(Color.parseColor("#5a5a5a"));
//                 type = "phone";
//            }
//        });
        next_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next.callOnClick();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (getDetail == null || getCost == null) {
//                    oneBtnDialog = new OneBtnDialog(BillingListActivity.this, "서비스 이용기간을\n선택해 주세요.", "확인", 1);
//                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    oneBtnDialog.setCancelable(false);
//                    oneBtnDialog.show();
//                    return;
//                }
                if (getCost == null) {
                    oneBtnDialog = new OneBtnDialog(BillingListActivity.this, "서비스 이용기간을\n선택해 주세요.", "확인", 1);
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
//                if (type == null) {
//                    oneBtnDialog = new OneBtnDialog(BillingListActivity.this, "결제 수단을\n선택해 주세요.", "확인");
//                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    oneBtnDialog.setCancelable(false);
//                    oneBtnDialog.show();
//                    return;
//                }

                if (bill_overlap) {
                    return;
                }

                if ("9900".equals(getCost)) {
                    purchaseProduct(ITEM_1MONTH);
                    billingProcessor.getPurchaseTransactionDetails(ITEM_1MONTH);    //구매 거래 세부사항 얻기
                } else if ("19800".equals(getCost)) {
                    purchaseProduct(ITEM_2MONTH);
                    billingProcessor.getPurchaseTransactionDetails(ITEM_1MONTH);    //구매 거래 세부사항 얻기
                } else if ("29700".equals(getCost)) {
                    purchaseProduct(ITEM_3MONTH);
                    billingProcessor.getPurchaseTransactionDetails(ITEM_1MONTH);    //구매 거래 세부사항 얻기
                }

                bill_overlap = true;
                Log.i("BillingListActivity", "onCreate():: next.onClick():: " + " getDetail: " + getDetail + " getCost: " + getCost);

//                Intent intent = new Intent(BillingListActivity.this, 빌링Activity.class);
//                intent.putExtra("detail", getDetail);
//                intent.putExtra("cost", getCost);
//                intent.putExtra("type",type);
//                startActivityForResult(intent, 1);
            }
        });
        refreshList();
    }

    @Override
    public void onDestroy(){
        if(billingProcessor != null){
            billingProcessor.release(); //billingProcessor 해제
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("BillingListActivity", "onActivityResult()");

        switch (resultCode) {
            case RESULT_CANCELED:
                bill_overlap = false;
                break;
            case 444:
                finish();
                break;
        }

        if(billingProcessor.handleActivityResult(requestCode, resultCode, data)){
            bill_overlap = false;
            Log.i("BillingListActivity", "onActivityResult()" + " : RESULT_OK");

            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            try{

                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                final String getToken = get_token.getString("Token", "");
                String url = UrlManager.getBaseUrl() + "/inapp";

                Map<String, Object> params = new HashMap<String, Object>();
                if(purchaseData == null){   // 구매 과정에서 취소하면 여기로 떨어짐
                    Log.i("BillingListActivity", "onActivityResult() :: " +"purchaseData : " + purchaseData);
                    bill_overlap = false;
                }else if(purchaseData != null){
                    JSONObject purchaseDataJson = new JSONObject(purchaseData);
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
                        progressBar.setVisibility(View.VISIBLE);
                        RequestBody body = new FormBody.Builder()
                                .add("skuId", skuId)
                                .add("SkuPriceLong", Long.toString(skuPriceLong))
                                .add("SkuTitle", skuTitle)
                                .add("receipt", purchaseDataJson.toString())
                                .build();
                        Request request = new Request.Builder().url(url).header("GHsoft-Agent", "" + getToken).header("User-Agent", "android").post(body).build();
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
                                            progressBar.setVisibility(View.GONE);
                                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                                oneBtnDialog = new OneBtnDialog(BillingListActivity.this, skuTitle + "\n구매에 성공하였습니다.", "확인", 0);
                                                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                oneBtnDialog.setCancelable(false);
                                                oneBtnDialog.show();
                                            }else if(!jsonObject.getBoolean("return")){//return이 false 면?
                                                Toast.makeText(BillingListActivity.this, "구매에 실패하였습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(context, "서버 통신에 실패하였습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show();
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


//                    JSONObject purchaseDataJson = new JSONObject(purchaseData);
//                    params.put("skuId", skuId);
//                    params.put("SkuPriceLong", skuPriceLong);
//                    params.put("SkuTitle", skuTitle);
//                    params.put("receipt", purchaseDataJson);
//                    Log.i("BillingListActivity", "Params " + params);
//                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            if(jsonObject != null){
//                                try {
//                                    if (jsonObject.getBoolean("return")) {
//
//                                        Log.i("BillingListActivity", "onActivityResult() :: " + " return TRUE");
//
//                                        // 구매에 성공하였습니다. 다이얼로그 띄우기 (성공시 - finish())
//                                        oneBtnDialog = new OneBtnDialog(BillingListActivity.this, skuTitle + "\n구매에 성공하였습니다.", "확인", 0);
//                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                        oneBtnDialog.setCancelable(false);
//                                        oneBtnDialog.show();
//
//                                    } else if (!jsonObject.getBoolean("return")) {
//                                        Log.i("BillingListActivity", "onActivityResult() :: " + " return FALSE");
//                                        Toast.makeText(BillingListActivity.this, "구매에 실패하였습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show();
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));

                }

            }catch(JSONException e){
                Toast.makeText(this, "서버 통신 실패\n다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

    public class OneBtnDialog extends Dialog {
        OneBtnDialog oneBtnDialog = this;
        Context context;
        public OneBtnDialog(final Context context, final String text, final String btnText, final int finish) {
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
                    if(finish == 1){
                        oneBtnDialog.dismiss();
                    }else if(finish == 0){
                        oneBtnDialog.dismiss();
                        finish();
                    }
                }
            });
        }
    }

    //구매하기 함수
    public void purchaseProduct(final String productId) {
        if (billingProcessor.isPurchased(productId)) {
            // 구매하였으면 소비하여 없앤 후 다시 구매하게 하는 로직. 만약 1번 구매 후 계속 이어지게 할 것이면 아래 함수는 주석처리.
            billingProcessor.consumePurchase(productId);    //(주석 처리하면 재구매할 수 없음.)
            Log.i("BillingListActivity", "purchaseProduct():: consumPurchase() - 다시 구매하게 하는 로직");
        }
        billingProcessor.purchase(this, productId);
        Log.i("BillingListActivity", "purchaseProduct():: purchase() - 구매");
    }
}
