package app.cosmos.ghrealestatediary;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import app.cosmos.ghrealestatediary.CustomTabLayout.TabLayoutWithArrow;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class ToSActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayoutWithArrow tabLayout;
    PagerAdapterToS adapter;
    FragmentManager fragmentManager;
    View view1, view2, view3;
    TextView tab_text1, tab_text2, tab_text3, subtitle;
    String tab_selected;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_s);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(ToSActivity.this, true);
            }
        }
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        subtitle = (TextView) findViewById(R.id.subtitle);
        subtitle.setText("이용약관에 대해 알려드립니다.");
        tabLayout = (TabLayoutWithArrow) findViewById(R.id.tabsMain);
        view1 = getLayoutInflater().inflate(R.layout.custom_tab_text, null);
        tab_text1 = (TextView) view1.findViewById(R.id.tab_text);
        tab_text1.setText("이용약관");
        tab_text1.setTextColor(Color.parseColor("#7199ff"));
        tab_selected = "0";
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));

        view2 = getLayoutInflater().inflate(R.layout.custom_tab_text, null);
        tab_text2 = (TextView) view2.findViewById(R.id.tab_text);
        tab_text2.setText("개인정보처리방침");
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));

        view3 = getLayoutInflater().inflate(R.layout.custom_tab_text, null);
        tab_text3 = (TextView) view3.findViewById(R.id.tab_text);
        tab_text3.setText("위치기반서비스");
        tabLayout.addTab(tabLayout.newTab().setCustomView(view3));

        tabLayout.setTabGravity(TabLayoutWithArrow.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.viewpager_tos);
        fragmentManager = getSupportFragmentManager();
        adapter = new PagerAdapterToS(fragmentManager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayoutWithArrow.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setOnTabSelectedListener(new TabLayoutWithArrow.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayoutWithArrow.Tab tab) {
                tab_selected = tab.getPosition()+"";
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    subtitle.setText("이용약관에 대해 알려드립니다.");
                    tab_text1.setTextColor(Color.parseColor("#7199ff"));
                    tab_text2.setTextColor(Color.parseColor("#acacac"));
                    tab_text3.setTextColor(Color.parseColor("#acacac"));
                } else if (tab.getPosition() == 1) {
                    subtitle.setText("개인정보 처리방침에 대해 알려드립니다.");
                    tab_text1.setTextColor(Color.parseColor("#acacac"));
                    tab_text2.setTextColor(Color.parseColor("#7199ff"));
                    tab_text3.setTextColor(Color.parseColor("#acacac"));
                } else if (tab.getPosition() == 2) {
                    subtitle.setText("위치기반 서비스에 대해 알려드립니다.");
                    tab_text1.setTextColor(Color.parseColor("#acacac"));
                    tab_text2.setTextColor(Color.parseColor("#acacac"));
                    tab_text3.setTextColor(Color.parseColor("#7199ff"));
                }
            }
            @Override
            public void onTabUnselected(TabLayoutWithArrow.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayoutWithArrow.Tab tab) {
                tab_selected = tab.getPosition()+"";
            }
        });
    }

}
