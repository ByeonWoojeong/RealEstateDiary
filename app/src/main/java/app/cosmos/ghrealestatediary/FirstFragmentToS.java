package app.cosmos.ghrealestatediary;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class FirstFragmentToS extends Fragment {

    Context context;
    View view;
    WebView webView, childView;
    ProgressBar progressBar;
    TextView first_tos_txt;
    AQuery aQuery = null;

    public boolean checkUrl(String url) {
        if (url.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            startActivity(intent);
            return true;
        } else if (url.startsWith("sms:")) {
            Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            startActivity(i);
            return true;
        } else if (url.startsWith("mailto:")) {
            Intent it = new Intent(Intent.ACTION_SEND);
            String[] tos = {url.substring(7)};
            String[] ccs = {"자신의 이메일을 작성해 주세요."};
            it.putExtra(Intent.EXTRA_EMAIL, tos);
            it.putExtra(Intent.EXTRA_CC, ccs);
            it.putExtra(Intent.EXTRA_TEXT, "내용을 작성해 주세요.");
            it.putExtra(Intent.EXTRA_SUBJECT, "제목을 작성해 주세요.");
            it.setType("message/rfc822");
            startActivity(Intent.createChooser(it, "보내실 이메일을 선택하여 주세요."));
            return true;
        } else if (url.endsWith(".mp4") || url.endsWith(".swf")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), "video/*");
            startActivity(intent);
            return true;
        } else if (url.startsWith("intent:")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent intent = null;
                try {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (webView.canGoBack()) {
                        webView.clearHistory();
                    }
                    if (intent == null) {
                        return false;
                    } else {
                        if (intent.getPackage() != null) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + intent.getPackage())));
                            return true;
                        }
                    }
                }
            } else {
                Intent intent = null;
                try {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (webView.canGoBack()) {
                        webView.clearHistory();
                    }
                    if (intent == null) {
                        return false;
                    } else {
                        if (intent.getPackage() != null) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + intent.getPackage())));
                            return true;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first_tos, container, false);
        SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        String getToken = get_token.getString("Token", "");

        first_tos_txt = view.findViewById(R.id.first_tos_txt);

        aQuery = new AQuery(context);
        String url = UrlManager.getBaseUrl() + "main/policy/1";
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
                                    first_tos_txt.setText(jsonObject.getJSONObject("data").getString("content"));
                                }else if(!jsonObject.getBoolean("return")){//return이 false 면?
                                    Toast.makeText(context, "정보를 불러오지 못하였습니다.", Toast.LENGTH_SHORT).show();
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



//        Map<String, Object> params = new HashMap<>();
//        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//            @Override
//            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                Log.i("POLICY", " " + jsonObject);
//                try {
//                    if (jsonObject.getBoolean("return")) {
//                        first_tos_txt.setText(jsonObject.getJSONObject("data").getString("content"));
//                    } else {
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));

        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

//    private class MyWebViewClient extends WebViewClient {
//        @SuppressWarnings("deprecation")
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            return checkUrl(url);
//        }
//
//        @TargetApi(Build.VERSION_CODES.N)
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            String url = request.getUrl().toString();
//            return checkUrl(url);
//        }
//
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//            progressBar.setVisibility(View.VISIBLE);
//            progressBar.setProgress(0);
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            progressBar.setVisibility(View.GONE);
//            progressBar.setProgress(100);
//        }
//    }

//    private class MyWebChromeClient extends WebChromeClient {
//        @Override
//        public void onCloseWindow(WebView w) {
//            super.onCloseWindow(w);
//        }
//
//        @Override
//        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//            new AlertDialog.Builder(context).setTitle("알림").setMessage(message).setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    result.confirm();
//                }
//            }).setCancelable(false).create().show();
//            return true;
//        }
//
//        @Override
//        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
//            new AlertDialog.Builder(context).setTitle("알림").setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    result.confirm();
//                }
//            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    result.cancel();
//                }
//            }).setCancelable(false).create().show();
//            return true;
//        }
//
//        @Override
//        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
//            childView = new WebView(view.getContext());
//            childView.getSettings().setJavaScriptEnabled(true);
//            childView.getSettings().setDomStorageEnabled(true);
//            childView.getSettings().setAllowFileAccess(true);
//            childView.getSettings().setAllowContentAccess(true);
//            childView.getSettings().setLoadWithOverviewMode(true);
//            childView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//            childView.setInitialScale(1);
//            childView.getSettings().setUseWideViewPort(true);
//            childView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//            childView.setWebChromeClient(this);
//            childView.setWebViewClient(new MyWebViewClient(){});
//            childView.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
//            childView.findFocus();
//            view.addView(childView);
//            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
//            transport.setWebView(childView);
//            resultMsg.sendToTarget();
//            return true;
//        }
//
//        @Override
//        public void onProgressChanged(WebView view, int newProgress) {
//            super.onProgressChanged(view, newProgress);
//            progressBar.setProgress(newProgress);
//        }
//    }
}