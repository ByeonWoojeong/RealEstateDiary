package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import app.cosmos.ghrealestatediary.DTO.Member;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class MemberListActivity extends AppCompatActivity {

    AQuery aQuery = null;
    ImageView back;
    String isGroup;
    ArrayList<Member> data;
    MemberListAdapter groupListAdapter;
    ListView listView;
    SwipeRefreshLayout mSwipeRefresh;
    ProgressBar progressBar;
    InputMethodManager imm;


    void refreshList() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        String url = UrlManager.getBaseUrl() + "/group/mlist";
        Map<String, Object> params = new HashMap<String, Object>();
        progressBar.setVisibility(View.VISIBLE);
        aQuery.progress(progressBar).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                try {
                    progressBar.setVisibility(View.GONE);
                    data.clear();
                    if (jsonObject.getBoolean("return")) {
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject getJsonObject = jsonArray.getJSONObject(i);
//                            data.add(new Member(getJsonObject.getString("name"), getJsonObject.getString("phone"), getJsonObject.getString("idx"), getJsonObject.getInt("type")));
                        }
                        listView.setAdapter(groupListAdapter);
                        groupListAdapter.notifyDataSetChanged();
                    } else if (!jsonObject.getBoolean("return")) {
                        listView.setAdapter(groupListAdapter);
                        groupListAdapter.notifyDataSetChanged();

                        if ("pay".equals(jsonObject.getString("type"))) {
                          //기간 만료
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(MemberListActivity.this, true);
            }
        }
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        aQuery = new AQuery(this);
        Intent intent = getIntent();
//        isGroup = intent.getStringExtra("isGroup");
        back = (ImageView) findViewById (R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (ListView) findViewById (R.id.memberListView);
        data = new ArrayList<>();
//        groupListAdapter = new MemberListAdapter(MemberListActivity.this, R.layout.list_member, data, listView, isGroup);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#e3e3e3"), android.graphics.PorterDuff.Mode.SRC_IN);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefresh.setColorSchemeResources(R.color.baseColor, R.color.baseColor);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
                mSwipeRefresh.setRefreshing(false);
            }
        });
        refreshList();
    }

}
