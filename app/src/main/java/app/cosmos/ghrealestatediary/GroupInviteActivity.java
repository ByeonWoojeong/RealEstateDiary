package app.cosmos.ghrealestatediary;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import app.cosmos.ghrealestatediary.DTO.GroupInvite;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class GroupInviteActivity extends AppCompatActivity {

    AQuery aQuery = null;
    ImageView back;
    String email;
    ArrayList<GroupInvite> data;
    GroupInviteListAdapter groupInviteListAdapter;
    ListView listView;
    SwipeRefreshLayout mSwipeRefresh;
    ProgressBar progressBar;

    void refreshList() {
        SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        String url = UrlManager.getBaseUrl() + "/group/list";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("email", email);
        progressBar.setVisibility(View.VISIBLE);
        aQuery.progress(progressBar).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                try {
                    progressBar.setVisibility(View.GONE);
                    data.clear();
                    if (jsonObject.getBoolean("return")) {
                        listView.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        for (int i = 0; i<jsonArray.length(); i++) {
                            JSONObject getJsonObject = jsonArray.getJSONObject(i);
//                            data.add(new GroupInvite(getJsonObject.getString("idx"), getJsonObject.getString("email"), getJsonObject.getString("email")));
                        }
                        listView.setAdapter(groupInviteListAdapter);
                        groupInviteListAdapter.notifyDataSetChanged();
                    } else if (!jsonObject.getBoolean("return")) {
                        if ("nodata".equals(jsonObject.getString("type"))) {
                            listView.setVisibility(View.GONE);
                            listView.setAdapter(groupInviteListAdapter);
                            groupInviteListAdapter.notifyDataSetChanged();
                        }
                        if("logout".equals(jsonObject.getString("type"))){
                            Toast.makeText(GroupInviteActivity.this, "다른 기기에서 로그인 하였습니다.", Toast.LENGTH_SHORT).show();
                            SharedPreferences prefLoginChecked = getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefLoginChecked.edit();
                            editor.clear();
                            editor.commit();
                            Intent intent = new Intent(GroupInviteActivity.this, LoginActivity.class);
                            finish();
                            startActivity(intent);
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
        setContentView(R.layout.activity_group_invite);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(GroupInviteActivity.this, true);
            }
        }
        aQuery = new AQuery(this);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        back = (ImageView) findViewById (R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (ListView) findViewById (R.id.memberListView);
        data = new ArrayList<GroupInvite>();
        groupInviteListAdapter = new GroupInviteListAdapter(GroupInviteActivity.this, R.layout.list_group_invite, data, listView);
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
