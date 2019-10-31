package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.cosmos.ghrealestatediary.DTO.Payment;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class PayHistoryActivity extends AppCompatActivity {

    AQuery aQuery = null;
    TextView day;
    String getDate;
    RelativeLayout detail_service_layout, no_service_layout;
    ListView payment_list = null;
    PayListAdapter payListAdapter;
    ArrayList<Payment> data;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_history);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(PayHistoryActivity.this, true);
            }
        }

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#e3e3e3"), android.graphics.PorterDuff.Mode.SRC_IN);
        day = findViewById(R.id.day);
        aQuery = new AQuery(this);
        no_service_layout = (RelativeLayout) findViewById(R.id.no_service_layout);
        detail_service_layout = (RelativeLayout) findViewById(R.id.detail_service_layout);
        payment_list = (ListView) findViewById (R.id.payment_list);
        data = new ArrayList<Payment>();
        payListAdapter = new PayListAdapter(PayHistoryActivity.this, R.layout.list_pay_history_item, data, payment_list);
    }

    public void infoRefresh() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        String url = UrlManager.getBaseUrl() + "member/history";
        Map<String, Object> params = new HashMap<String, Object>();
        progressBar.setVisibility(View.VISIBLE);
        aQuery.progress(progressBar).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                try {
                    Log.i("PAYHISTORY", " " + jsonObject);
                    progressBar.setVisibility(View.GONE);
                    data.clear();

                    if (jsonObject.getBoolean("return")) {
                        payment_list.setVisibility(View.VISIBLE);

                        getDate = jsonObject.getJSONObject("data").getString("date");   //남은 날짜
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));

                        day.setText(getDate);
                        if (jsonArray.length() == 0) {
                            no_service_layout.setVisibility(View.VISIBLE);
                            detail_service_layout.setVisibility(View.GONE);
                        } else {
                            no_service_layout.setVisibility(View.GONE);
                            detail_service_layout.setVisibility(View.VISIBLE);

                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                data.add(new Payment(getJsonObject.getString("date"), getJsonObject.getString("cost") + "원"));
                            }
                            payment_list.setAdapter(payListAdapter);
                            payListAdapter.notifyDataSetChanged();
                        }

                    } else if (!jsonObject.getBoolean("return")) {

                        if("logout".equals(jsonObject.getString("type"))){
                            Toast.makeText(PayHistoryActivity.this, "다른 기기에서 로그인 하였습니다.", Toast.LENGTH_SHORT).show();
                            SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefLoginChecked.edit();
                            editor.clear();
                            editor.commit();
                            Intent intent = new Intent(PayHistoryActivity.this, LoginActivity.class);
                            finish();
                            startActivity(intent);
                        }else{
                            Toast.makeText(PayHistoryActivity.this, "결제 내역을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        infoRefresh();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
}
