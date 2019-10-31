package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
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

public class InputActivity extends AppCompatActivity {

    Context context;
    AQuery aQuery = null;
    static String idx;
    ImageView back, callIcon, finish_date_icon;
    TextView next, count_text, toolbar_title, title, dong, ho, now_floor, all_floor, address, address2, area, area2, area_unit, area2_unit, maemae, maemae_unit, junsae, junsae_unit, bojeung, bojeung_unit, woalsae, woalsae_unit, geunri, geunri_unit, gaunri, gaunri_unit, door_lock, door_lock2, damdang, damdang_phone, sangse, spinner_text, finish_date;
    SpinnerReselect spinner;
    LinearLayout image_con, finish_date_con;
    FrameLayout next_con, input_count, call_con, left_scroll_con, right_scroll_con;
    ImageView main_image, left_scroll, right_scroll, image1, image2, image3, image4, image5, image6;
    HorizontalScrollView horizontalScrollView;
    ProgressBar progressBar;
    boolean isUpdate, isUpdated, isUpdatedStatus, userIsInteracting;
    String getStatus, la, lo, getLa, getLo, getType, getTitle, getAddress, getAddress2, getArea, getArea2, getMaemae, getJunsae, getWoalsae, getBojeung, getGeunri, getGaunri, getDoor_lock, getDoor_lock2, getDamdang, getDamdang_phone, getSangse, getImages, getFinishDate, getDong, getHo, getNowFloor, getAllFloor;
    String IMG;


    @Override
    protected void onResume() {
        super.onResume();
        isUpdate = false;
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        String url = UrlManager.getBaseUrl() + "item/view";
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
                                    idx = jsonObject.getJSONObject("data").getString("idx");
                                    getStatus = jsonObject.getJSONObject("data").getString("status");
                                    getLa = jsonObject.getJSONObject("data").getString("lat");
                                    getLo = jsonObject.getJSONObject("data").getString("lng");
                                    getType = jsonObject.getJSONObject("data").getString("type");
                                    getTitle = jsonObject.getJSONObject("data").getString("name");
                                    getDong = jsonObject.getJSONObject("data").getString("dong");
                                    getHo = jsonObject.getJSONObject("data").getString("ho");
                                    getNowFloor = jsonObject.getJSONObject("data").getString("nfloor");
                                    getAllFloor = jsonObject.getJSONObject("data").getString("afloor");
                                    getAddress = jsonObject.getJSONObject("data").getString("addr1");
                                    getAddress2 = jsonObject.getJSONObject("data").getString("addr2");
                                    getArea = jsonObject.getJSONObject("data").getString("area1");
                                    getArea2 = jsonObject.getJSONObject("data").getString("area2");
                                    getMaemae = jsonObject.getJSONObject("data").getString("maemae");
                                    getJunsae = jsonObject.getJSONObject("data").getString("jense");
                                    getWoalsae = jsonObject.getJSONObject("data").getString("wallse");
                                    getBojeung = jsonObject.getJSONObject("data").getString("bojeung");
                                    getGeunri = jsonObject.getJSONObject("data").getString("geunri");
                                    getGaunri = jsonObject.getJSONObject("data").getString("gaunri");
                                    getDoor_lock = jsonObject.getJSONObject("data").getString("pass1");
                                    getDoor_lock2 = jsonObject.getJSONObject("data").getString("pass2");
                                    getDamdang = jsonObject.getJSONObject("data").getString("admin_name");
                                    getDamdang_phone = jsonObject.getJSONObject("data").getString("admin_phone");
                                    getFinishDate = jsonObject.getJSONObject("data").getString("enddate");
                                    getSangse = jsonObject.getJSONObject("data").getString("memo");
                                    getImages = jsonObject.getJSONObject("data").getString("image");
                                    if ("true".equals(jsonObject.getJSONObject("data").getString("my"))) {
                                        next_con.setVisibility(View.VISIBLE);
                                    }
                                    isUpdate = true;
                                    if ("1".equals(jsonObject.getJSONObject("data").getString("type"))) {
                                        toolbar_title.setText("매물(원룸)");
                                    } else if ("2".equals(jsonObject.getJSONObject("data").getString("type"))) {
                                        toolbar_title.setText("매물(투/쓰리룸)");
                                    } else if ("3".equals(jsonObject.getJSONObject("data").getString("type"))) {
                                        toolbar_title.setText("매물(아파트)");
                                    } else if ("4".equals(jsonObject.getJSONObject("data").getString("type"))) {
                                        toolbar_title.setText("매물(사무실)");
                                    } else if ("5".equals(jsonObject.getJSONObject("data").getString("type"))) {
                                        toolbar_title.setText("매물(주택)");
                                    } else if ("6".equals(jsonObject.getJSONObject("data").getString("type"))) {
                                        toolbar_title.setText("매물(상가)");
                                    }
                                    if ("0".equals(getStatus)) {
                                        spinner_text.setText("거래대기");
                                    } else if ("1".equals(getStatus)) {
                                        spinner_text.setText("거래중");
                                    } else if ("2".equals(getStatus)) {
                                        spinner_text.setText("거래완료");
                                    }
                                    title.setText(jsonObject.getJSONObject("data").getString("name"));
                                    dong.setText(jsonObject.getJSONObject("data").getString("dong"));
                                    if ("null".equals(jsonObject.getJSONObject("data").getString("dong"))) {
                                        Log.i("INPUTACTIVITY", " NULL " + jsonObject.getJSONObject("data").getString("dong"));
                                        dong.setText("");
                                    }

                                    ho.setText(jsonObject.getJSONObject("data").getString("ho"));
                                    if ("null".equals(jsonObject.getJSONObject("data").getString("ho"))) {
                                        ho.setText("");
                                    }

                                    now_floor.setText(jsonObject.getJSONObject("data").getString("nfloor"));
                                    if ("null".equals(jsonObject.getJSONObject("data").getString("nfloor"))) {
                                        now_floor.setText("");
                                    }

                                    all_floor.setText(jsonObject.getJSONObject("data").getString("afloor"));
                                    if ("null".equals(jsonObject.getJSONObject("data").getString("afloor"))) {
                                        all_floor.setText("");
                                    }

                                    address.setText(jsonObject.getJSONObject("data").getString("addr1"));
                                    address2.setText(jsonObject.getJSONObject("data").getString("addr2"));
                                    area.setText(jsonObject.getJSONObject("data").getString("area1"));
                                    if ("null".equals(jsonObject.getJSONObject("data").getString("area1"))) {
                                        area.setText("");
                                    }
                                    finish_date.setText(jsonObject.getJSONObject("data").getString("enddate"));
                                    if ("0000-00-00".equals(jsonObject.getJSONObject("data").getString("enddate"))) {
                                        finish_date.setText("");
                                    }
                                    if ("null".equals(jsonObject.getJSONObject("data").getString("enddate"))) {
                                        finish_date.setText("");
                                    }

                                    if (!"".equals(area.getText().toString().trim())) {
                                        area_unit.setVisibility(View.VISIBLE);
                                    }
                                    area2.setText(jsonObject.getJSONObject("data").getString("area2"));
                                    if ("null".equals(jsonObject.getJSONObject("data").getString("area2"))) {
                                        area2.setText("");
                                    }
                                    if (!"".equals(area2.getText().toString().trim())) {
                                        area2_unit.setVisibility(View.VISIBLE);
                                    }
                                    NumberFormat numberFormat = NumberFormat.getInstance();
                                    if (!"".equals(jsonObject.getJSONObject("data").getString("maemae"))) {
                                        maemae.setText(numberFormat.format(Integer.parseInt(jsonObject.getJSONObject("data").getString("maemae"))));
                                        maemae_unit.setVisibility(View.VISIBLE);
                                    }
                                    if (!"".equals(jsonObject.getJSONObject("data").getString("jense"))) {
                                        junsae.setText(numberFormat.format(Integer.parseInt(jsonObject.getJSONObject("data").getString("jense"))));
                                        junsae_unit.setVisibility(View.VISIBLE);
                                    }
                                    if (!"".equals(jsonObject.getJSONObject("data").getString("bojeung"))) {
                                        bojeung.setText(numberFormat.format(Integer.parseInt(jsonObject.getJSONObject("data").getString("bojeung"))));
                                        bojeung_unit.setVisibility(View.VISIBLE);
                                    }
                                    if (!"".equals(jsonObject.getJSONObject("data").getString("wallse"))) {
                                        woalsae.setText(numberFormat.format(Integer.parseInt(jsonObject.getJSONObject("data").getString("wallse"))));
                                        woalsae_unit.setVisibility(View.VISIBLE);
                                    }
                                    if (!"".equals(jsonObject.getJSONObject("data").getString("geunri"))) {
                                        geunri.setText(numberFormat.format(Integer.parseInt(jsonObject.getJSONObject("data").getString("geunri"))));
                                        geunri_unit.setVisibility(View.VISIBLE);
                                    }
                                    if (!"".equals(jsonObject.getJSONObject("data").getString("gaunri"))) {
                                        gaunri.setText(numberFormat.format(Integer.parseInt(jsonObject.getJSONObject("data").getString("gaunri"))));
                                        gaunri_unit.setVisibility(View.VISIBLE);
                                    }
                                    door_lock.setText(jsonObject.getJSONObject("data").getString("pass1"));
                                    door_lock2.setText(jsonObject.getJSONObject("data").getString("pass2"));
                                    damdang.setText(jsonObject.getJSONObject("data").getString("admin_name"));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        damdang_phone.setText(PhoneNumberUtils.formatNumber(jsonObject.getJSONObject("data").getString("admin_phone"), Locale.getDefault().getCountry()));
                                    } else {
                                        damdang_phone.setText(PhoneNumberUtils.formatNumber(jsonObject.getJSONObject("data").getString("admin_phone")));
                                    }
                                    if (!"".equals(damdang_phone.getText().toString().trim())) {
                                        call_con.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                callIcon.callOnClick();
                                            }
                                        });
                                        callIcon.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + damdang_phone.getText().toString().trim()));
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                    sangse.setText(jsonObject.getJSONObject("data").getString("memo"));
                                    if ("null".equals(jsonObject.getJSONObject("data").getString("memo"))) {
                                        sangse.setText("");
                                    }
                                    if (!"null".equals(jsonObject.getJSONObject("data").getString("image"))) {
                                        image_con.setVisibility(View.VISIBLE);
                                        image1.setImageBitmap(null);
                                        image2.setImageBitmap(null);
                                        image3.setImageBitmap(null);
                                        image4.setImageBitmap(null);
                                        image5.setImageBitmap(null);
                                        image6.setImageBitmap(null);
                                        left_scroll_con.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                left_scroll.callOnClick();
                                            }
                                        });
                                        left_scroll.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                horizontalScrollView.smoothScrollTo(0, 0);
                                            }
                                        });
                                        right_scroll_con.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                right_scroll.callOnClick();
                                            }
                                        });
                                        right_scroll.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                horizontalScrollView.smoothScrollTo(horizontalScrollView.getWidth(), 0);
                                            }
                                        });
                                        final String[] getImages = jsonObject.getJSONObject("data").getString("image").split(",");
                                        if (getImages.length == 1) {
                                            input_count.setVisibility(View.GONE);
                                        } else {
                                            input_count.setVisibility(View.VISIBLE);
                                            count_text.setText((getImages.length - 1) + "");
                                        }
//                            count_text.setText(getImages.length+"");
                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(main_image);
                                        main_image.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Log.i("INPUTACTIVITY", " IMG " + IMG);
                                                Intent intent = new Intent(InputActivity.this, InputImagePathActivity.class);
                                                if (IMG == null) {
                                                    Log.i("INPUTACTIVITY", " IMG null " + IMG);
                                                    intent.putExtra("path", UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]);
                                                } else {
                                                    intent.putExtra("path", UrlManager.getBaseUrl() + "uploads/images/origin/" + IMG);
                                                }

                                                startActivity(intent);
                                            }
                                        });
                                        if (1 == getImages.length) {
                                            image1.setVisibility(View.VISIBLE);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(image1);
                                            image1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[0];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                        } else if (2 == getImages.length) {
                                            image1.setVisibility(View.VISIBLE);
                                            image2.setVisibility(View.VISIBLE);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(image1);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(image2);
                                            image1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[0];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                            image2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[1];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                        } else if (3 == getImages.length) {
                                            image1.setVisibility(View.VISIBLE);
                                            image2.setVisibility(View.VISIBLE);
                                            image3.setVisibility(View.VISIBLE);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(image1);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(image2);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).centerCrop().crossFade().into(image3);
                                            image1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[0];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                            image2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[1];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                            image3.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[2];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                        } else if (4 == getImages.length) {
                                            image1.setVisibility(View.VISIBLE);
                                            image2.setVisibility(View.VISIBLE);
                                            image3.setVisibility(View.VISIBLE);
                                            image4.setVisibility(View.VISIBLE);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(image1);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(image2);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).centerCrop().crossFade().into(image3);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[3]).centerCrop().crossFade().into(image4);
                                            image1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[0];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                            image2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[1];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                            image3.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[2];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                            image4.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[3];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[3]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                        } else if (5 == getImages.length) {
                                            image1.setVisibility(View.VISIBLE);
                                            image2.setVisibility(View.VISIBLE);
                                            image3.setVisibility(View.VISIBLE);
                                            image4.setVisibility(View.VISIBLE);
                                            image5.setVisibility(View.VISIBLE);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(image1);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(image2);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).centerCrop().crossFade().into(image3);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[3]).centerCrop().crossFade().into(image4);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[4]).centerCrop().crossFade().into(image5);
                                            image1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[0];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                            image2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[1];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                            image3.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[2];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                            image4.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[3];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[3]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                            image5.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[4];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[4]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                        } else if (6 == getImages.length) {
                                            image1.setVisibility(View.VISIBLE);
                                            image2.setVisibility(View.VISIBLE);
                                            image3.setVisibility(View.VISIBLE);
                                            image4.setVisibility(View.VISIBLE);
                                            image5.setVisibility(View.VISIBLE);
                                            image6.setVisibility(View.VISIBLE);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(image1);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(image2);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).centerCrop().crossFade().into(image3);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[3]).centerCrop().crossFade().into(image4);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[4]).centerCrop().crossFade().into(image5);
                                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[5]).centerCrop().crossFade().into(image6);
                                            image1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[0];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                            image2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[1];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                            image3.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[2];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                            image4.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[3];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[3]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                            image5.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[4];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[4]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                            image6.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    IMG = getImages[5];
                                                    Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[5]).centerCrop().crossFade().into(main_image);
                                                }
                                            });
                                        }
                                    }
                                } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                    if ("pay".equals(jsonObject.getString("type"))) {
                                        Toast.makeText(InputActivity.this, "기간이 만료되었습니다.", Toast.LENGTH_SHORT).show();
                                        finish();
                                        Intent intent = new Intent(InputActivity.this, BillingListActivity.class);
                                        startActivity(intent);
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
//
//                    if (jsonObject.getBoolean("return")) {
//
//                        idx = jsonObject.getJSONObject("data").getString("idx");
//                        getStatus = jsonObject.getJSONObject("data").getString("status");
//                        getLa = jsonObject.getJSONObject("data").getString("lat");
//                        getLo = jsonObject.getJSONObject("data").getString("lng");
//                        getType = jsonObject.getJSONObject("data").getString("type");
//                        getTitle = jsonObject.getJSONObject("data").getString("name");
//                        getDong = jsonObject.getJSONObject("data").getString("dong");
//                        getHo = jsonObject.getJSONObject("data").getString("ho");
//                        getNowFloor = jsonObject.getJSONObject("data").getString("nfloor");
//                        getAllFloor = jsonObject.getJSONObject("data").getString("afloor");
//                        getAddress = jsonObject.getJSONObject("data").getString("addr1");
//                        getAddress2 = jsonObject.getJSONObject("data").getString("addr2");
//                        getArea = jsonObject.getJSONObject("data").getString("area1");
//                        getArea2 = jsonObject.getJSONObject("data").getString("area2");
//                        getMaemae = jsonObject.getJSONObject("data").getString("maemae");
//                        getJunsae = jsonObject.getJSONObject("data").getString("jense");
//                        getWoalsae = jsonObject.getJSONObject("data").getString("wallse");
//                        getBojeung = jsonObject.getJSONObject("data").getString("bojeung");
//                        getGeunri = jsonObject.getJSONObject("data").getString("geunri");
//                        getGaunri = jsonObject.getJSONObject("data").getString("gaunri");
//                        getDoor_lock = jsonObject.getJSONObject("data").getString("pass1");
//                        getDoor_lock2 = jsonObject.getJSONObject("data").getString("pass2");
//                        getDamdang = jsonObject.getJSONObject("data").getString("admin_name");
//                        getDamdang_phone = jsonObject.getJSONObject("data").getString("admin_phone");
//                        getFinishDate = jsonObject.getJSONObject("data").getString("enddate");
//                        getSangse = jsonObject.getJSONObject("data").getString("memo");
//                        getImages = jsonObject.getJSONObject("data").getString("image");
//                        if ("true".equals(jsonObject.getJSONObject("data").getString("my"))) {
//                            next_con.setVisibility(View.VISIBLE);
//                        }
//                        isUpdate = true;
//                        if ("1".equals(jsonObject.getJSONObject("data").getString("type"))) {
//                            toolbar_title.setText("매물(원룸)");
//                        } else if ("2".equals(jsonObject.getJSONObject("data").getString("type"))) {
//                            toolbar_title.setText("매물(투/쓰리룸)");
//                        } else if ("3".equals(jsonObject.getJSONObject("data").getString("type"))) {
//                            toolbar_title.setText("매물(아파트)");
//                        } else if ("4".equals(jsonObject.getJSONObject("data").getString("type"))) {
//                            toolbar_title.setText("매물(사무실)");
//                        } else if ("5".equals(jsonObject.getJSONObject("data").getString("type"))) {
//                            toolbar_title.setText("매물(주택)");
//                        } else if ("6".equals(jsonObject.getJSONObject("data").getString("type"))) {
//                            toolbar_title.setText("매물(상가)");
//                        }
//                        if ("0".equals(getStatus)) {
//                            spinner_text.setText("거래대기");
//                        } else if ("1".equals(getStatus)) {
//                            spinner_text.setText("거래중");
//                        } else if ("2".equals(getStatus)) {
//                            spinner_text.setText("거래완료");
//                        }
//                        title.setText(jsonObject.getJSONObject("data").getString("name"));
//                        dong.setText(jsonObject.getJSONObject("data").getString("dong"));
//                        if("null".equals(jsonObject.getJSONObject("data").getString("dong"))){
//                            Log.i("INPUTACTIVITY", " NULL " + jsonObject.getJSONObject("data").getString("dong"));
//                            dong.setText("");
//                        }
//
//                        ho.setText(jsonObject.getJSONObject("data").getString("ho"));
//                        if("null".equals(jsonObject.getJSONObject("data").getString("ho"))){
//                            ho.setText("");
//                        }
//
//                        now_floor.setText(jsonObject.getJSONObject("data").getString("nfloor"));
//                        if("null".equals(jsonObject.getJSONObject("data").getString("nfloor"))){
//                            now_floor.setText("");
//                        }
//
//                        all_floor.setText(jsonObject.getJSONObject("data").getString("afloor"));
//                        if("null".equals(jsonObject.getJSONObject("data").getString("afloor"))){
//                            all_floor.setText("");
//                        }
//
//                        address.setText(jsonObject.getJSONObject("data").getString("addr1"));
//                        address2.setText(jsonObject.getJSONObject("data").getString("addr2"));
//                        area.setText(jsonObject.getJSONObject("data").getString("area1"));
//                        finish_date.setText(jsonObject.getJSONObject("data").getString("enddate"));
//                        if("0000-00-00".equals(jsonObject.getJSONObject("data").getString("enddate"))){
//                            finish_date.setText("");
//                        }
//                        if("null".equals(jsonObject.getJSONObject("data").getString("enddate"))){
//                            finish_date.setText("");
//                        }
//
//                        if (!"".equals(area.getText().toString().trim())) {
//                            area_unit.setVisibility(View.VISIBLE);
//                        }
//                        area2.setText(jsonObject.getJSONObject("data").getString("area2"));
//                        if (!"".equals(area2.getText().toString().trim())) {
//                            area2_unit.setVisibility(View.VISIBLE);
//                        }
//                        NumberFormat numberFormat = NumberFormat.getInstance();
//                        if (!"".equals(jsonObject.getJSONObject("data").getString("maemae"))) {
//                            maemae.setText(numberFormat.format(Integer.parseInt(jsonObject.getJSONObject("data").getString("maemae"))));
//                            maemae_unit.setVisibility(View.VISIBLE);
//                        }
//                        if (!"".equals(jsonObject.getJSONObject("data").getString("jense"))) {
//                            junsae.setText(numberFormat.format(Integer.parseInt(jsonObject.getJSONObject("data").getString("jense"))));
//                            junsae_unit.setVisibility(View.VISIBLE);
//                        }
//                        if (!"".equals(jsonObject.getJSONObject("data").getString("bojeung"))) {
//                            bojeung.setText(numberFormat.format(Integer.parseInt(jsonObject.getJSONObject("data").getString("bojeung"))));
//                            bojeung_unit.setVisibility(View.VISIBLE);
//                        }
//                        if (!"".equals(jsonObject.getJSONObject("data").getString("wallse"))) {
//                            woalsae.setText(numberFormat.format(Integer.parseInt(jsonObject.getJSONObject("data").getString("wallse"))));
//                            woalsae_unit.setVisibility(View.VISIBLE);
//                        }
//                        if (!"".equals(jsonObject.getJSONObject("data").getString("geunri"))) {
//                            geunri.setText(numberFormat.format(Integer.parseInt(jsonObject.getJSONObject("data").getString("geunri"))));
//                            geunri_unit.setVisibility(View.VISIBLE);
//                        }
//                        if (!"".equals(gaunri.getText().toString().trim())) {
//                            gaunri.setText(numberFormat.format(Integer.parseInt(jsonObject.getJSONObject("data").getString("gaunri"))));
//                            gaunri_unit.setVisibility(View.VISIBLE);
//                        }
//                        door_lock.setText(jsonObject.getJSONObject("data").getString("pass1"));
//                        door_lock2.setText(jsonObject.getJSONObject("data").getString("pass2"));
//                        damdang.setText(jsonObject.getJSONObject("data").getString("admin_name"));
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            damdang_phone.setText(PhoneNumberUtils.formatNumber(jsonObject.getJSONObject("data").getString("admin_phone"), Locale.getDefault().getCountry()));
//                        } else {
//                            damdang_phone.setText(PhoneNumberUtils.formatNumber(jsonObject.getJSONObject("data").getString("admin_phone")));
//                        }
//                        if (!"".equals(damdang_phone.getText().toString().trim())) {
//                            call_con.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    call.callOnClick();
//                                }
//                            });
//                            call.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + damdang_phone.getText().toString().trim()));
//                                    startActivity(intent);
//                                }
//                            });
//                        }
//                        sangse.setText(jsonObject.getJSONObject("data").getString("memo"));
//                        if (!"null".equals(jsonObject.getJSONObject("data").getString("image"))) {
//                            image_con.setVisibility(View.VISIBLE);
//                            image1.setImageBitmap(null);
//                            image2.setImageBitmap(null);
//                            image3.setImageBitmap(null);
//                            image4.setImageBitmap(null);
//                            image5.setImageBitmap(null);
//                            image6.setImageBitmap(null);
//                            left_scroll_con.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    left_scroll.callOnClick();
//                                }
//                            });
//                            left_scroll.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    horizontalScrollView.smoothScrollTo(0, 0);
//                                }
//                            });
//                            right_scroll_con.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    right_scroll.callOnClick();
//                                }
//                            });
//                            right_scroll.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    horizontalScrollView.smoothScrollTo(horizontalScrollView.getWidth(), 0);
//                                }
//                            });
//                            final String[] getImages = jsonObject.getJSONObject("data").getString("image").split(",");
//                            if(getImages.length==1){
//                                input_count.setVisibility(View.GONE);
//                            }else{
//                                input_count.setVisibility(View.VISIBLE);
//                                count_text.setText((getImages.length-1)+"");
//                            }
////                            count_text.setText(getImages.length+"");
//                            Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(main_image);
//                            main_image.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Log.i("INPUTACTIVITY", " IMG " + IMG);
//                                    Intent intent = new Intent(InputActivity.this, InputImagePathActivity.class);
//                                    if(IMG == null){
//                                        Log.i("INPUTACTIVITY", " IMG null " + IMG);
//                                        intent.putExtra("path", UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]);
//                                    }else{
//                                        intent.putExtra("path", UrlManager.getBaseUrl() + "uploads/images/origin/" + IMG);
//                                    }
//
//                                    startActivity(intent);
//                                }
//                            });
//                            if (1 == getImages.length) {
//                                image1.setVisibility(View.VISIBLE);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(image1);
//                                image1.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[0];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                            } else if (2 == getImages.length) {
//                                image1.setVisibility(View.VISIBLE);
//                                image2.setVisibility(View.VISIBLE);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(image1);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(image2);
//                                image1.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[0];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                                image2.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[1];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                            } else if (3 == getImages.length) {
//                                image1.setVisibility(View.VISIBLE);
//                                image2.setVisibility(View.VISIBLE);
//                                image3.setVisibility(View.VISIBLE);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(image1);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(image2);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).centerCrop().crossFade().into(image3);
//                                image1.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[0];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                                image2.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[1];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                                image3.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[2];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                            } else if (4 == getImages.length) {
//                                image1.setVisibility(View.VISIBLE);
//                                image2.setVisibility(View.VISIBLE);
//                                image3.setVisibility(View.VISIBLE);
//                                image4.setVisibility(View.VISIBLE);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(image1);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(image2);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).centerCrop().crossFade().into(image3);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[3]).centerCrop().crossFade().into(image4);
//                                image1.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[0];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                                image2.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[1];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                                image3.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[2];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                                image4.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[3];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[3]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                            } else if (5 == getImages.length) {
//                                image1.setVisibility(View.VISIBLE);
//                                image2.setVisibility(View.VISIBLE);
//                                image3.setVisibility(View.VISIBLE);
//                                image4.setVisibility(View.VISIBLE);
//                                image5.setVisibility(View.VISIBLE);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(image1);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(image2);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).centerCrop().crossFade().into(image3);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[3]).centerCrop().crossFade().into(image4);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[4]).centerCrop().crossFade().into(image5);
//                                image1.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[0];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                                image2.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[1];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                                image3.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[2];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                                image4.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[3];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[3]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                                image5.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[4];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[4]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                            } else if (6 == getImages.length) {
//                                image1.setVisibility(View.VISIBLE);
//                                image2.setVisibility(View.VISIBLE);
//                                image3.setVisibility(View.VISIBLE);
//                                image4.setVisibility(View.VISIBLE);
//                                image5.setVisibility(View.VISIBLE);
//                                image6.setVisibility(View.VISIBLE);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(image1);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(image2);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).centerCrop().crossFade().into(image3);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[3]).centerCrop().crossFade().into(image4);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[4]).centerCrop().crossFade().into(image5);
//                                Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[5]).centerCrop().crossFade().into(image6);
//                                image1.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[0];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[0]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                                image2.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[1];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[1]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                                image3.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[2];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[2]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                                image4.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[3];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[3]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                                image5.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[4];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[4]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                                image6.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        IMG = getImages[5];
//                                        Glide.with(InputActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + getImages[5]).centerCrop().crossFade().into(main_image);
//                                    }
//                                });
//                            }
//                        }
//                    } else if (!jsonObject.getBoolean("return")) {
//                        if("pay".equals(jsonObject.getString("type"))){
//                            Toast.makeText(InputActivity.this, "기간이 만료되었습니다.", Toast.LENGTH_SHORT).show();
//                            finish();
//                            Intent intent = new Intent(InputActivity.this, BillingListActivity.class);
//                            startActivity(intent);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
    }

    @Override
    public void finish() {
        if (isUpdated) {
            Intent intent = new Intent();
//            intent.putExtra("idx", idx);
            intent.putExtra("la", la);
            intent.putExtra("lo", lo);
            setResult(666, intent);
        } else {
            if (isUpdatedStatus) {
                Intent intent = new Intent();
                setResult(555, intent);
            }
        }
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(InputActivity.this, true);
            }
        }
        aQuery = new AQuery(this);
        Intent intent = getIntent();
        idx = intent.getStringExtra("idx");
        getStatus = intent.getStringExtra("status");
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#7199ff"), android.graphics.PorterDuff.Mode.SRC_IN);
        image_con = (LinearLayout) findViewById(R.id.image_con);
        input_count = (FrameLayout) findViewById(R.id.input_count);
        finish_date_con = findViewById(R.id.finish_date_con);
        finish_date = findViewById(R.id.finish_date);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        next_con = (FrameLayout) findViewById(R.id.next_con);
        next = (TextView) findViewById(R.id.next);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        back = (ImageView) findViewById(R.id.back);
        call_con = (FrameLayout) findViewById(R.id.call_con);
        callIcon = (ImageView) findViewById(R.id.call);
        title = (TextView) findViewById(R.id.title);
        dong = findViewById(R.id.dong);
        ho = findViewById(R.id.ho);
        now_floor = findViewById(R.id.now_floor);
        all_floor = findViewById(R.id.all_floor);
        address = (TextView) findViewById(R.id.address);
        address2 = (TextView) findViewById(R.id.address2);
        area = (TextView) findViewById(R.id.area1);
        area2 = (TextView) findViewById(R.id.area2);
        area_unit = (TextView) findViewById(R.id.area1_unit);
        area2_unit = (TextView) findViewById(R.id.area2_unit);
        maemae = (TextView) findViewById(R.id.maemae);
        maemae_unit = (TextView) findViewById(R.id.maemae_unit);
        junsae = (TextView) findViewById(R.id.junsae);
        junsae_unit = (TextView) findViewById(R.id.junsae_unit);
        bojeung = (TextView) findViewById(R.id.imdae);
        bojeung_unit = (TextView) findViewById(R.id.bojeung_unit);
        woalsae = (TextView) findViewById(R.id.woalsae);
        woalsae_unit = (TextView) findViewById(R.id.woalsae_unit);
        geunri = (TextView) findViewById(R.id.geunri);
        geunri_unit = (TextView) findViewById(R.id.geunri_unit);
        gaunri = (TextView) findViewById(R.id.gaunri);
        gaunri_unit = (TextView) findViewById(R.id.gaunri_unit);
        door_lock = (TextView) findViewById(R.id.door_lock1);
        door_lock2 = (TextView) findViewById(R.id.door_lock2);
        damdang = (TextView) findViewById(R.id.damdang);
        damdang_phone = (TextView) findViewById(R.id.damdang_phone);
        sangse = (TextView) findViewById(R.id.sangse);
        count_text = (TextView) findViewById(R.id.count_text);
        main_image = (ImageView) findViewById(R.id.main_image);
        left_scroll_con = (FrameLayout) findViewById(R.id.left_scroll_con);
        right_scroll_con = (FrameLayout) findViewById(R.id.right_scroll_con);
        left_scroll = (ImageView) findViewById(R.id.left_scroll);
        right_scroll = (ImageView) findViewById(R.id.right_scroll);
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        image4 = (ImageView) findViewById(R.id.image4);
        image5 = (ImageView) findViewById(R.id.image5);
        image6 = (ImageView) findViewById(R.id.image6);
        spinner = (SpinnerReselect) findViewById(R.id.spinner);
        spinner_text = (TextView) findViewById(R.id.spinner_text);
        String[] kategorie = new String[]{
                "거래대기",
                "거래중",
                "거래완료"
        };
        final ArrayAdapter<String> kategorieAdapter = new ArrayAdapter<String>(InputActivity.this, R.layout.my_spinner, kategorie);
        kategorieAdapter.setDropDownViewResource(R.layout.my_spinner);
        spinner.setAdapter(kategorieAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (userIsInteracting) {
                    spinner_text.setText(spinner.getSelectedItem().toString());
                    getStatus = spinner.getSelectedItemPosition() + "";
                    SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    String url = UrlManager.getBaseUrl() + "item/status";
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
                                .add("status", getStatus)
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
                                            if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                                isUpdatedStatus = true;
                                            } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                                if ("pay".equals(jsonObject.getString("type"))) {
                                                    Toast.makeText(InputActivity.this, "서비스 기간이 만료되었습니다.", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(InputActivity.this, BillingListActivity.class);
                                                    finish();
                                                    startActivity(intent);
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
//                    params.put("idx", idx);
//                    params.put("status", getStatus);
//                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            try {
//                                if (jsonObject.getBoolean("return")) {
//                                    isUpdatedStatus = true;
//                                } else if (!jsonObject.getBoolean("return")) {
//                                    if ("pay".equals(jsonObject.getString("type"))) {
//                                        Toast.makeText(InputActivity.this, "다른 기기에서 로그인 하였습니다.", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(InputActivity.this, BillingListActivity.class);
//                                        finish();
//                                        startActivity(intent);
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                    Intent intent = new Intent(InputActivity.this, WriteActivity.class);
                    intent.putExtra("idx", idx);
                    intent.putExtra("type", getType);
                    intent.putExtra("title", getTitle);
                    intent.putExtra("dong", getDong);
                    intent.putExtra("ho", getHo);
                    intent.putExtra("nfloor", getNowFloor);
                    intent.putExtra("afloor", getAllFloor);
                    intent.putExtra("address", getAddress);
                    intent.putExtra("address2", getAddress2);
                    intent.putExtra("area", getArea);
                    intent.putExtra("area2", getArea2);
                    intent.putExtra("maemae", getMaemae);
                    intent.putExtra("junsae", getJunsae);
                    intent.putExtra("woalsae", getWoalsae);
                    intent.putExtra("bojeung", getBojeung);
                    intent.putExtra("geunri", getGeunri);
                    intent.putExtra("gaunri", getGaunri);
                    intent.putExtra("door_lock", getDoor_lock);
                    intent.putExtra("door_lock2", getDoor_lock2);
                    intent.putExtra("damdang", getDamdang);
                    intent.putExtra("damdang_phone", getDamdang_phone);
                    intent.putExtra("enddate", getFinishDate);
                    intent.putExtra("sangse", getSangse);
                    intent.putExtra("images", getImages);
                    intent.putExtra("la", getLa);
                    intent.putExtra("lo", getLo);
                    startActivityForResult(intent, 1);
                }
            }
        });
        if (getStatus != null) {
            if ("0".equals(getStatus)) {
                spinner_text.setText("거래대기");
            } else if ("1".equals(getStatus)) {
                spinner_text.setText("거래중");
            } else if ("2".equals(getStatus)) {
                spinner_text.setText("거래완료");
            }
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("INPUTACTIVITY", " onStop ");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("INPUTACTIVITY", " onRestart ");
        Log.i("INPUTACTIVITY", " IMG " + IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("INPUTACTIVITY", " onActivityResult requestCode " + requestCode + " " + resultCode);
        switch (resultCode) {
            case RESULT_CANCELED:
                break;
            case 777:
                la = data.getStringExtra("la");
                lo = data.getStringExtra("lo");
                isUpdated = true;
                break;
        }
    }
}
