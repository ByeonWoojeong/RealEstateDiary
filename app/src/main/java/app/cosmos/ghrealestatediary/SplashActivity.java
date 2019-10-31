package app.cosmos.ghrealestatediary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    String token;

    Handler toastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(getApplicationContext(), "최신 버전으로 업데이트 해주세요.", Toast.LENGTH_SHORT).show();
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(Message msg){
            SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
            Boolean loginChecked = prefLoginChecked.getBoolean("loginChecked", false);
            if (loginChecked) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                finish();
                startActivityForResult(intent, 1);
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                finish();
                startActivityForResult(intent, 1);
            }
        }
    };

    Runnable token_load = new Runnable() {
        public void run() {
            SharedPreferences prefToken = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
            token = prefToken.getString("Token", "");
            handler.postDelayed(this, 500);
            if (!"".equals(token.toString())){
                handler.removeCallbacks(token_load);
                handler.sendEmptyMessageDelayed(0, 500);
            }
        }
    };

    InputMethodManager methodManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        methodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        methodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        setContentView(R.layout.activity_splash);
        registerForContextMenu(findViewById(R.id.imageView));   //지정된 뷰에 대해 표시할 컨텍스트 메뉴를 등록.
        final PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //ConnectivityManager : 네트워크 연결 상태에 대한 쿼리에 응답, 네트워크 연결이 변경될 때 알림.
                ConnectivityManager connectivityManager = (ConnectivityManager) SplashActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                //connectivityManager.getActiveNetworkInfo() : 현재 네트워크 연결을 나타내는 인스턴스를 가져오는 데 사용.
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null) {

                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String store_version = MarketVersionChecker.getMarketVersion(getPackageName());     //PlayStore 버전 체크
                                if (store_version != null) {
                                    try {
                                        String device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                                        if (store_version.compareTo(device_version) > 0) {  //1(긍정값)이면?
                                            Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                                            marketLaunch.setData(Uri.parse("market://details?id=app.cosmos.ghrealestatediary"));
                                            startActivity(marketLaunch);
                                            Message msg = toastHandler.obtainMessage();
                                            toastHandler.sendMessage(msg);
                                            moveTaskToBack(true);
                                            ActivityCompat.finishAffinity(SplashActivity.this);
                                            finish();
                                        } else {
                                            SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
                                            Boolean loginChecked = prefLoginChecked.getBoolean("loginChecked", false);
                                            String getLoginPhoneNumber = prefLoginChecked.getString("loginPhoneNumber", "");



                                            token_load.run();


                                        }
                                    } catch (PackageManager.NameNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
                                    Boolean loginChecked = prefLoginChecked.getBoolean("loginChecked", false);
                                    String getLoginPhoneNumber = prefLoginChecked.getString("loginPhoneNumber", "");
                                    //-   String getPhoneNumber = phoneNumber.replaceAll("-", "").replaceAll(" ", "").replaceAll("[+]82", "0");


                                    token_load.run();

                                }
                            }
                        }).start();

                    } else {
                        Toast.makeText(SplashActivity.this, "인터넷이 연결되어 있지 않아 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
                        setResult(999);
                        finish();
                    }
                } else {
                    Toast.makeText(SplashActivity.this, "인터넷이 연결되어 있지 않아 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
                    setResult(999);
                    finish();
                }
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                finish();
            }
        };

        new TedPermission(SplashActivity.this)
                .setPermissionListener(permissionlistener)
                .setPermissions(android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS, android.Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_CANCELED:
                break;
            case 999:
                setResult(999);
                finish();
                break;
        }
    }
}