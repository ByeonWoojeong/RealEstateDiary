package app.cosmos.ghrealestatediary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import java.util.ArrayList;

import app.cosmos.ghrealestatediary.DTO.Address;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class Addr1SelectActivity extends AppCompatActivity {

    ArrayList<Address> data;
    Addr1ListAdapter addr1ListAdapter;
    GridView grid_view;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addr1_select);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(Addr1SelectActivity.this, true);
            }
        }
        findViewById (R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        grid_view = (GridView) findViewById (R.id.grid_view);
        data = new ArrayList<Address>();
        addr1ListAdapter = new Addr1ListAdapter(Addr1SelectActivity.this, R.layout.list_addr, data, grid_view);
        data.add(new Address("서울특별시", false));
        data.add(new Address("부산광역시", false));
        data.add(new Address("인천광역시", false));
        data.add(new Address("광주광역시", false));
        data.add(new Address("대구광역시", false));
        data.add(new Address("대전광역시", false));
        data.add(new Address("울산광역시", false));
        data.add(new Address("세종특별자치시", false));
        data.add(new Address("경기도", false));
        data.add(new Address("강원도", false));
        data.add(new Address("충청북도", false));
        data.add(new Address("충청남도", false));
        data.add(new Address("전라북도", false));
        data.add(new Address("전라남도", false));
        data.add(new Address("경상북도", false));
        data.add(new Address("경상남도", false));
        data.add(new Address("제주도", false));
        grid_view.setAdapter(addr1ListAdapter);
        addr1ListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_CANCELED:
                break;
            case 999:
                setResult(777);
                finish();
                break;
        }
    }
}
