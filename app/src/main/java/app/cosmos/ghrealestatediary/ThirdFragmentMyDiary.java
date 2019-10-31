package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
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


public class ThirdFragmentMyDiary extends Fragment {

    AQuery aQuery = null;
    Context context;
    View view;
    String token;
    FrameLayout menu3_0_con, menu3_1_con, menu3_2_con, menu3_3_con, menu3_4_con, menu3_5_con, menu3_6_con, menu3_7_con, menu3_8_con;
    TextView menu3_0, menu3_1, menu3_2, menu3_3, menu3_4, menu3_5, menu3_6, menu3_7, menu3_8;
    OneBtnDialog oneBtnDialog;
    TwoBtnRecDialog1 twoBtnRecDialog1;
    TwoBtnRecDialog2 twoBtnRecDialog2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_third_my_diary, container, false);
        SharedPreferences prefToken = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        token = prefToken.getString("Token", "");
        aQuery = new AQuery(context);
        menu3_0_con = (FrameLayout) view.findViewById(R.id.menu3_0_con);
        menu3_1_con = (FrameLayout) view.findViewById(R.id.menu3_1_con);
        menu3_2_con = (FrameLayout) view.findViewById(R.id.menu3_2_con);
        menu3_3_con = (FrameLayout) view.findViewById(R.id.menu3_3_con);
        menu3_4_con = (FrameLayout) view.findViewById(R.id.menu3_4_con);
        menu3_5_con = (FrameLayout) view.findViewById(R.id.menu3_5_con);
//        menu3_6_con = (FrameLayout) view.findViewById(R.id.menu3_6_con);
        menu3_7_con = (FrameLayout) view.findViewById(R.id.menu3_7_con);
        menu3_8_con = (FrameLayout) view.findViewById(R.id.menu3_8_con);
        menu3_0 = (TextView) view.findViewById(R.id.menu3_0);
        menu3_1 = (TextView) view.findViewById(R.id.menu3_1);
        menu3_2 = (TextView) view.findViewById(R.id.menu3_2);
        menu3_3 = (TextView) view.findViewById(R.id.menu3_3);
        menu3_4 = (TextView) view.findViewById(R.id.menu3_4);
        menu3_5 = (TextView) view.findViewById(R.id.menu3_5);
//        menu3_6 = (TextView) view.findViewById(R.id.menu3_6);
        menu3_7 = (TextView) view.findViewById(R.id.menu3_7);
        menu3_8 = (TextView) view.findViewById(R.id.menu3_8);
        menu3_0_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu3_0.callOnClick();
            }
        });
        menu3_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NoticeActivity.class);
                startActivity(intent);
            }
        });
        menu3_1_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu3_1.callOnClick();
            }
        });
        menu3_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.kakao.com/o/sJ0Ec6D"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        menu3_2_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu3_2.callOnClick();
            }
        });
        menu3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FaQActivity.class);
                startActivity(intent);
            }
        });

        //결제 내역
        menu3_3_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu3_3.callOnClick();
            }
        });
        menu3_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneBtnDialog = new OneBtnDialog(context, "서비스 준비 중 입니다!", "확인");
                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                oneBtnDialog.setCancelable(false);
                oneBtnDialog.show();
//                Intent intent = new Intent(context, PayHistoryActivity.class);
//                startActivity(intent);
            }
        });
//        버전 정보 다이얼로그
//        menu3_4_con.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                menu3_4.callOnClick();
//            }
//        });
//        menu3_4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                oneBtnDialog = new OneBtnDialog(context, getResources().getString(R.string.get_version), "닫기");
//                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                oneBtnDialog.setCancelable(false);
//                oneBtnDialog.show();
//            }
//        });
        menu3_5_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu3_5.callOnClick();
            }
        });
        menu3_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AboutActivity.class);
                startActivity(intent);
            }
        });
//       사업자 정보
//        menu3_6_con.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                menu3_6.callOnClick();
//            }
//        });
//        menu3_6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, CompanyInfoActivity.class);
//                startActivity(intent);
//            }
//        });
        menu3_7_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu3_7.callOnClick();
            }
        });
        menu3_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoBtnRecDialog1 = new TwoBtnRecDialog1(context);
                twoBtnRecDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                twoBtnRecDialog1.setCancelable(false);
                twoBtnRecDialog1.show();
            }
        });
        menu3_8_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu3_8.callOnClick();
            }
        });
        menu3_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twoBtnRecDialog2 = new TwoBtnRecDialog2(context);
                twoBtnRecDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                twoBtnRecDialog2.setCancelable(false);
                twoBtnRecDialog2.show();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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

    public class TwoBtnRecDialog1 extends Dialog {
        TwoBtnRecDialog1 twoBtnRecDialog1 = this;
        Context context;

        public TwoBtnRecDialog1(final Context context) {
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
            title1.setText("로그아웃 하시겠습니까?");
            btn1.setText("취소");
            btn2.setText("확인");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnRecDialog1.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    String url = UrlManager.getBaseUrl() + "logout";
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
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = new JSONObject(res);
                                            if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                                Toast.makeText(context, "로그아웃 하였습니다.", Toast.LENGTH_SHORT).show();
                                                SharedPreferences prefLoginChecked = context.getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = prefLoginChecked.edit();
                                                editor.clear();
                                                editor.commit();
                                                getActivity().setResult(9999);
                                                getActivity().finish();
                                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                                startActivity(intent);
                                            } else if (!jsonObject.getBoolean("return")) {//return이 false 면?
                                                Toast.makeText(context, "로그아웃에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                            twoBtnRecDialog1.dismiss();
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
//                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            try {
//
//                                if (jsonObject.getBoolean("return")) {
//                                    Toast.makeText(context, "로그아웃 하였습니다.", Toast.LENGTH_SHORT).show();
//                                    SharedPreferences prefLoginChecked = context.getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = prefLoginChecked.edit();
//                                    editor.clear();
//                                    editor.commit();
//                                    getActivity().setResult(9999);
//                                    getActivity().finish();
//                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                                    startActivity(intent);
//                                } else if (!jsonObject.getBoolean("return")) {
//                                    Toast.makeText(context, "로그아웃 하였습니다.", Toast.LENGTH_SHORT).show();
//                                    SharedPreferences prefLoginChecked = context.getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = prefLoginChecked.edit();
//                                    editor.clear();
//                                    editor.commit();
//                                    getActivity().setResult(9999);
//                                    getActivity().finish();
//                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                                    startActivity(intent);
//                                }
//                                twoBtnRecDialog1.dismiss();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
                }
            });
        }
    }

    public class TwoBtnRecDialog2 extends Dialog {
        TwoBtnRecDialog2 twoBtnRecDialog2 = this;
        Context context;

        public TwoBtnRecDialog2(final Context context) {
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
            title1.setText("정말로 부동산 다이어리를\n탈퇴 하시겠습니까?");
            btn1.setText("취소");
            btn2.setText("확인");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnRecDialog2.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    String url = UrlManager.getBaseUrl() + "signout";
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
                            @Override public void onFailure(Call call, IOException e) { //통신 실패
                            }

                            @Override public void onResponse(Call call, Response response) throws IOException {
                                final String res = response.body().string();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject jsonObject = new JSONObject(res);
                                            if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                                Toast.makeText(context, "부동산 다이어리를 탈퇴 하였습니다.", Toast.LENGTH_SHORT).show();
                                                SharedPreferences prefLoginChecked = context.getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = prefLoginChecked.edit();
                                                editor.clear();
                                                editor.commit();
                                                getActivity().setResult(9999);
                                                getActivity().finish();
                                            }else if(!jsonObject.getBoolean("return")){//return이 false 면?
                                                Toast.makeText(context, "부동산 다이어리 탈퇴를 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                            twoBtnRecDialog2.dismiss();
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
//                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                        @Override
//                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                            try {
//                                if (jsonObject.getBoolean("return")) {
//                                    Toast.makeText(context, "부동산 다이어리를 탈퇴 하였습니다.", Toast.LENGTH_SHORT).show();
//                                    SharedPreferences prefLoginChecked = context.getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = prefLoginChecked.edit();
//                                    editor.clear();
//                                    editor.commit();
//                                    getActivity().setResult(9999);
//                                    getActivity().finish();
//                                } else if (!jsonObject.getBoolean("return")) {
//                                    Toast.makeText(context, "부동산 다이어리 탈퇴를 실패하였습니다.", Toast.LENGTH_SHORT).show();
////                                    SharedPreferences prefLoginChecked = context.getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
////                                    SharedPreferences.Editor editor = prefLoginChecked.edit();
////                                    editor.clear();
////                                    editor.commit();
////                                    getActivity().setResult(9999);
////                                    getActivity().finish();
//                                }
//                                twoBtnRecDialog2.dismiss();
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