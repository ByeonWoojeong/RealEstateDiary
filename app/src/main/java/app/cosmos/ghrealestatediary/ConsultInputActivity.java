package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class ConsultInputActivity extends AppCompatActivity {

    private static Context context;

    AQuery aQuery = null;
    static String idx;
    ImageView back, callBtn;
    TextView next, name, phone, first_date, target_date, content;
    LinearLayout image_con;
    FrameLayout next_con, call_con;
    ImageView image1, image2, image3;
    HorizontalScrollView horizontalScrollView;
    ProgressBar progressBar;
    boolean isUpdate;
    String getName, getPhone, getFirst_date, getTarget_date, getContent, getImages;


    @Override
    protected void onResume() {
        super.onResume();
        isUpdate = false;
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = UrlManager.getBaseUrl() + "memo/view";
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
                    .add("idx", idx)
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
                                progressBar.setVisibility(View.GONE);
                                if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                    getName = jsonObject.getJSONObject("data").getString("name");
                                    if("null".equals(getName)){
                                        getName="";
                                    }
                                    getPhone = jsonObject.getJSONObject("data").getString("phone");
                                    if("null".equals(getPhone)){
                                        getPhone="";
                                    }
                                    getFirst_date = jsonObject.getJSONObject("data").getString("f_date");
                                    getTarget_date = jsonObject.getJSONObject("data").getString("m_date");
                                    getContent = jsonObject.getJSONObject("data").getString("content");
                                    if("null".equals(getContent)){
                                        getContent="";
                                    }
                                    getImages = jsonObject.getJSONObject("data").getString("image");
                                    isUpdate = true;
                                    name.setText(getName);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        phone.setText(PhoneNumberUtils.formatNumber(getPhone, Locale.getDefault().getCountry()));
                                    } else {
                                        phone.setText(PhoneNumberUtils.formatNumber(getPhone));
                                    }
                                    if (!"".equals(phone.getText().toString().trim())) {
                                        call_con.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                callBtn.callOnClick();
                                            }
                                        });
                                        callBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.getText().toString().trim()));
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                    if (!"null".equals(jsonObject.getJSONObject("data").getString("f_date"))) {
                                        first_date.setText(getFirst_date);
                                    }
                                    if (!"null".equals(jsonObject.getJSONObject("data").getString("m_date"))) {
                                        target_date.setText(getTarget_date);
                                    }
                                    content.setText(getContent);
                                    if (!"null".equals(jsonObject.getJSONObject("data").getString("image"))) {
                                        image_con.setVisibility(View.VISIBLE);
                                        image1.setImageBitmap(null);
                                        image2.setImageBitmap(null);
                                        image3.setImageBitmap(null);
                                        final String[] getImages = jsonObject.getJSONObject("data").getString("image").split(",");
                                        if (1 == getImages.length) {
                                            image1.setVisibility(View.VISIBLE);
                                            Glide.with(ConsultInputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).crossFade().bitmapTransform(new CenterCrop(ConsultInputActivity.this), new RoundedCornersTransformation(ConsultInputActivity.this, 50, 0)).into(image1);
                                            image1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(ConsultInputActivity.this, InputImagePathActivity.class);
                                                    intent.putExtra("path", UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else if (2 == getImages.length) {
                                            image1.setVisibility(View.VISIBLE);
                                            image2.setVisibility(View.VISIBLE);
                                            Glide.with(ConsultInputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).crossFade().bitmapTransform(new CenterCrop(ConsultInputActivity.this), new RoundedCornersTransformation(ConsultInputActivity.this, 50, 0)).into(image1);
                                            Glide.with(ConsultInputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).crossFade().bitmapTransform(new CenterCrop(ConsultInputActivity.this), new RoundedCornersTransformation(ConsultInputActivity.this, 50, 0)).into(image2);
                                            image1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(ConsultInputActivity.this, InputImagePathActivity.class);
                                                    intent.putExtra("path", UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]);
                                                    startActivity(intent);
                                                }
                                            });
                                            image2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(ConsultInputActivity.this, InputImagePathActivity.class);
                                                    intent.putExtra("path", UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else if (3 == getImages.length) {
                                            image1.setVisibility(View.VISIBLE);
                                            image2.setVisibility(View.VISIBLE);
                                            image3.setVisibility(View.VISIBLE);
                                            Glide.with(ConsultInputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).crossFade().bitmapTransform(new CenterCrop(ConsultInputActivity.this), new RoundedCornersTransformation(ConsultInputActivity.this, 50, 0)).into(image1);
                                            Glide.with(ConsultInputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).crossFade().bitmapTransform(new CenterCrop(ConsultInputActivity.this), new RoundedCornersTransformation(ConsultInputActivity.this, 50, 0)).into(image2);
                                            Glide.with(ConsultInputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).crossFade().bitmapTransform(new CenterCrop(ConsultInputActivity.this), new RoundedCornersTransformation(ConsultInputActivity.this, 50, 0)).into(image3);
                                            image1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(ConsultInputActivity.this, InputImagePathActivity.class);
                                                    intent.putExtra("path", UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]);
                                                    startActivity(intent);
                                                }
                                            });
                                            image2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(ConsultInputActivity.this, InputImagePathActivity.class);
                                                    intent.putExtra("path", UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]);
                                                    startActivity(intent);
                                                }
                                            });
                                            image3.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(ConsultInputActivity.this, InputImagePathActivity.class);
                                                    intent.putExtra("path", UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    }

                                } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                    if ("pay".equals(jsonObject.getString("type"))) {
                                        Intent intent = new Intent(ConsultInputActivity.this, BillingListActivity.class);
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
        
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("idx", idx);
//        progressBar.setVisibility(View.VISIBLE);
//        aQuery.progress(progressBar).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                try {
//                    progressBar.setVisibility(View.GONE);
//                    if (jsonObject.getBoolean("return")) {
//                        getName = jsonObject.getJSONObject("data").getString("name");
//                        getPhone = jsonObject.getJSONObject("data").getString("phone");
//                        getFirst_date = jsonObject.getJSONObject("data").getString("f_date");
//                        getTarget_date = jsonObject.getJSONObject("data").getString("m_date");
//                        getContent = jsonObject.getJSONObject("data").getString("content");
//                        getImages = jsonObject.getJSONObject("data").getString("image");
//                        isUpdate = true;
//                        name.setText(jsonObject.getJSONObject("data").getString("name"));
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            phone.setText(PhoneNumberUtils.formatNumber(jsonObject.getJSONObject("data").getString("phone"), Locale.getDefault().getCountry()));
//                        } else {
//                            phone.setText(PhoneNumberUtils.formatNumber(jsonObject.getJSONObject("data").getString("phone")));
//                        }
//                        if (!"".equals(phone.getText().toString().trim())) {
//                            call_con.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    call.callOnClick();
//                                }
//                            });
//                            call.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.getText().toString().trim()));
//                                    startActivity(intent);
//                                }
//                            });
//                        }
//                        if (!"null".equals(jsonObject.getJSONObject("data").getString("f_date"))) {
//                            first_date.setText(jsonObject.getJSONObject("data").getString("f_date"));
//                        }
//                        if (!"null".equals(jsonObject.getJSONObject("data").getString("m_date"))) {
//                            target_date.setText(jsonObject.getJSONObject("data").getString("m_date"));
//                        }
//                        content.setText(jsonObject.getJSONObject("data").getString("content"));
//                        if (!"null".equals(jsonObject.getJSONObject("data").getString("image"))) {
//                            image_con.setVisibility(View.VISIBLE);
//                            image1.setImageBitmap(null);
//                            image2.setImageBitmap(null);
//                            image3.setImageBitmap(null);
//                            final String[] getImages = jsonObject.getJSONObject("data").getString("image").split(",");
//                            if (1 == getImages.length) {
//                                image1.setVisibility(View.VISIBLE);
//                                Glide.with(ConsultInputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).crossFade().bitmapTransform(new CenterCrop(ConsultInputActivity.this), new RoundedCornersTransformation(ConsultInputActivity.this, 50, 0)).into(image1);
//                                image1.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(ConsultInputActivity.this, InputImagePathActivity.class);
//                                        intent.putExtra("path", UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]);
//                                        startActivity(intent);
//                                    }
//                                });
//                            } else if (2 == getImages.length) {
//                                image1.setVisibility(View.VISIBLE);
//                                image2.setVisibility(View.VISIBLE);
//                                Glide.with(ConsultInputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).crossFade().bitmapTransform(new CenterCrop(ConsultInputActivity.this), new RoundedCornersTransformation(ConsultInputActivity.this, 50, 0)).into(image1);
//                                Glide.with(ConsultInputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).crossFade().bitmapTransform(new CenterCrop(ConsultInputActivity.this), new RoundedCornersTransformation(ConsultInputActivity.this, 50, 0)).into(image2);
//                                image1.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(ConsultInputActivity.this, InputImagePathActivity.class);
//                                        intent.putExtra("path", UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]);
//                                        startActivity(intent);
//                                    }
//                                });
//                                image2.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(ConsultInputActivity.this, InputImagePathActivity.class);
//                                        intent.putExtra("path", UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]);
//                                        startActivity(intent);
//                                    }
//                                });
//                            } else if (3 == getImages.length) {
//                                image1.setVisibility(View.VISIBLE);
//                                image2.setVisibility(View.VISIBLE);
//                                image3.setVisibility(View.VISIBLE);
//                                Glide.with(ConsultInputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).crossFade().bitmapTransform(new CenterCrop(ConsultInputActivity.this), new RoundedCornersTransformation(ConsultInputActivity.this, 50, 0)).into(image1);
//                                Glide.with(ConsultInputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).crossFade().bitmapTransform(new CenterCrop(ConsultInputActivity.this), new RoundedCornersTransformation(ConsultInputActivity.this, 50, 0)).into(image2);
//                                Glide.with(ConsultInputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).crossFade().bitmapTransform(new CenterCrop(ConsultInputActivity.this), new RoundedCornersTransformation(ConsultInputActivity.this, 50, 0)).into(image3);
//                                image1.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(ConsultInputActivity.this, InputImagePathActivity.class);
//                                        intent.putExtra("path", UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]);
//                                        startActivity(intent);
//                                    }
//                                });
//                                image2.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(ConsultInputActivity.this, InputImagePathActivity.class);
//                                        intent.putExtra("path", UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]);
//                                        startActivity(intent);
//                                    }
//                                });
//                                image3.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent intent = new Intent(ConsultInputActivity.this, InputImagePathActivity.class);
//                                        intent.putExtra("path", UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]);
//                                        startActivity(intent);
//                                    }
//                                });
//                            }
//                        }
//                    } else if (!jsonObject.getBoolean("return")) {
////                        if("logout".equals(jsonObject.getString("type"))){
////                            Toast.makeText(ConsultInputActivity.this, "다른 기기에서 로그인 하였습니다.", Toast.LENGTH_SHORT).show();
////                            SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
////                            SharedPreferences.Editor editor = prefLoginChecked.edit();
////                            editor.clear();
////                            editor.commit();
////                            Intent intent = new Intent(ConsultInputActivity.this, LoginActivity.class);
////                            finish();
////                            startActivity(intent);
////                        }
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
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_input);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(ConsultInputActivity.this, true);
            }
        }
        aQuery = new AQuery(this);
        Intent intent = getIntent();
        idx = intent.getStringExtra("idx");
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#7199ff"), android.graphics.PorterDuff.Mode.SRC_IN);
        image_con = (LinearLayout) findViewById(R.id.image_con);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        next_con = (FrameLayout) findViewById(R.id.next_con);
        next = (TextView) findViewById(R.id.next);
        back = (ImageView) findViewById(R.id.back);
        call_con = (FrameLayout) findViewById(R.id.call_con);
        callBtn = (ImageView) findViewById(R.id.call);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        first_date = (TextView) findViewById(R.id.first_date);
        target_date = (TextView) findViewById(R.id.target_date);
        content = (TextView) findViewById(R.id.content);
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.callOnClick();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpdate) {
                    Intent intent = new Intent(ConsultInputActivity.this, ConsultWriteActivity.class);
                    intent.putExtra("idx", idx);
                    intent.putExtra("name", getName);
                    intent.putExtra("phone", getPhone);
                    intent.putExtra("f_date", getFirst_date);
                    intent.putExtra("m_date", getTarget_date);
                    intent.putExtra("content", getContent);
                    intent.putExtra("images", getImages);
                    startActivityForResult(intent, 1);
                    finish();
                }
            }
        });
    }

}
