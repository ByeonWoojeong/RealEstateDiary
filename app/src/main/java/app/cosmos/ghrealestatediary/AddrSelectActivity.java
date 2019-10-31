package app.cosmos.ghrealestatediary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

@SuppressLint("SetJavaScriptEnabled")
public class AddrSelectActivity extends AppCompatActivity {

    ImageView back;
    WebView webView;
    Handler handler = new Handler();
    String addr1, addr2, la, lo;

    class AndroidBridge {
        @JavascriptInterface
        public void addr(final String arg1, final String arg2) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    addr1 = arg1;
                    addr2 = arg2;
                    if (addr1 != null && addr2 != null && la != null && lo != null) {
                        Intent intent = new Intent();
                        intent.putExtra("addr", addr2);
                        intent.putExtra("la", la);
                        intent.putExtra("lo", lo);
                        setResult(666, intent);
                        finish();
                    }
                }
            });
        }
        @JavascriptInterface
        public void gps(final double arg1, final double arg2) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    la = arg1+"";
                    lo = arg2+"";
                    if (addr1 != null && addr2 != null && la != null && lo != null) {
                        Intent intent = new Intent();
                        intent.putExtra("addr", addr2);
                        intent.putExtra("la", la);
                        intent.putExtra("lo", lo);
                        setResult(666, intent);
                        finish();
                    }
                }
            });
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    @JavascriptInterface
    @SuppressLint( "AddJavascriptInterface")
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addr_select);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(AddrSelectActivity.this, true);
            }
        }
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        String getToken = get_token.getString("Token", "");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webView  = (WebView) findViewById(R.id.webview);
        webView.addJavascriptInterface(new AndroidBridge(), "buda");
        String userAgent = new WebView(getBaseContext()).getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(userAgent + "" + getToken);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            webView.getSettings().setDisplayZoomControls(false);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            webView.getSettings().setTextZoom(100);
        }
        webView.setWebChromeClient(new WebChromeClient(){
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
        });
        webView.setWebViewClient(new WebViewClient());
        String postData = "&package=buda&addr=abc&gps=abc";
        char buf[] = postData.toCharArray();
        byte[] data = new byte[buf.length];
        for (int i = 0; i < buf.length; i++) {
            data[i] = (byte) buf[i];
        }
        webView.postUrl("http://cdn.globalhu.kr/daum/", data);
    }

}
