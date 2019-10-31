package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import app.cosmos.ghrealestatediary.DTO.Address;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class Addr2SelectActivity extends AppCompatActivity {

    ArrayList<Address> data;
    Addr2ListAdapter addr2ListAdapter;
    GridView grid_view;
    String name;
    FrameLayout next_con;
    TextView next;
    OneBtnDialog oneBtnDialog;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addr2_select);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(Addr2SelectActivity.this, true);
            }
        }
        SharedPreferences addr2CheckList = getSharedPreferences("addr2CheckList", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = addr2CheckList.edit();
        editor.clear();
        editor.commit();
        next_con = (FrameLayout) findViewById (R.id.next_con);
        next = (TextView) findViewById (R.id.next);
        findViewById (R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        grid_view = (GridView) findViewById (R.id.grid_view);
        data = new ArrayList<Address>();
        addr2ListAdapter = new Addr2ListAdapter(Addr2SelectActivity.this, R.layout.list_addr, data, grid_view, next);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        if ("서울특별시".equals(name)) {
            data.add(new Address("강남구", false));
            data.add(new Address("강동구", false));
            data.add(new Address("강북구", false));
            data.add(new Address("강서구", false));
            data.add(new Address("관악구", false));
            data.add(new Address("광진구", false));
            data.add(new Address("구로구", false));
            data.add(new Address("금천구", false));
            data.add(new Address("노원구", false));
            data.add(new Address("도봉구", false));
            data.add(new Address("동대문구", false));
            data.add(new Address("동작구", false));
            data.add(new Address("마포구", false));
            data.add(new Address("서대문구", false));
            data.add(new Address("서초구", false));
            data.add(new Address("성동구", false));
            data.add(new Address("성북구", false));
            data.add(new Address("송파구", false));
            data.add(new Address("양천구", false));
            data.add(new Address("영등포구", false));
            data.add(new Address("용산구", false));
            data.add(new Address("은평구", false));
            data.add(new Address("종로구", false));
            data.add(new Address("중구", false));
            data.add(new Address("중랑구", false));
        } else if ("부산광역시".equals(name)) {
            data.add(new Address("강서구", false));
            data.add(new Address("금정구", false));
            data.add(new Address("기장군", false));
            data.add(new Address("남구", false));
            data.add(new Address("동구", false));
            data.add(new Address("동래구", false));
            data.add(new Address("부산진구", false));
            data.add(new Address("북구", false));
            data.add(new Address("사상구", false));
            data.add(new Address("사하구", false));
            data.add(new Address("서구", false));
            data.add(new Address("수영구", false));
            data.add(new Address("연제구", false));
            data.add(new Address("영도구", false));
            data.add(new Address("중구", false));
            data.add(new Address("해운대구", false));
        } else if ("인천광역시".equals(name)) {
            data.add(new Address("강화군", false));
            data.add(new Address("계양구", false));
            data.add(new Address("남구", false));
            data.add(new Address("남동구", false));
            data.add(new Address("동구", false));
            data.add(new Address("부평구", false));
            data.add(new Address("서구", false));
            data.add(new Address("연수구", false));
            data.add(new Address("옹진군", false));
            data.add(new Address("중구", false));
        } else if ("광주광역시".equals(name)) {
            data.add(new Address("광산구", false));
            data.add(new Address("남구", false));
            data.add(new Address("동구", false));
            data.add(new Address("북구", false));
            data.add(new Address("서구", false));
        } else if ("대구광역시".equals(name)) {
            data.add(new Address("남구", false));
            data.add(new Address("달서구", false));
            data.add(new Address("달성군", false));
            data.add(new Address("동구", false));
            data.add(new Address("북구", false));
            data.add(new Address("서구", false));
            data.add(new Address("수성구", false));
            data.add(new Address("중구", false));
        } else if ("대전광역시".equals(name)) {
            data.add(new Address("대덕구", false));
            data.add(new Address("동구", false));
            data.add(new Address("서구", false));
            data.add(new Address("유성구", false));
            data.add(new Address("중구", false));
        } else if ("울산광역시".equals(name)) {
            data.add(new Address("남구", false));
            data.add(new Address("동구", false));
            data.add(new Address("북구", false));
            data.add(new Address("울주군", false));
            data.add(new Address("중구", false));
        } else if ("세종특별자치시".equals(name)) {
            data.add(new Address("세종특별자치시", false));
        } else if ("경기도".equals(name)) {
            data.add(new Address("가평군", false));
            data.add(new Address("고양시", false));
            data.add(new Address("과천시", false));
            data.add(new Address("광명시", false));
            data.add(new Address("광주시", false));
            data.add(new Address("구리시", false));
            data.add(new Address("군포시", false));
            data.add(new Address("김포시", false));
            data.add(new Address("남양주시", false));
            data.add(new Address("동두천시", false));
            data.add(new Address("부천시", false));
            data.add(new Address("성남시", false));
            data.add(new Address("수원시", false));
            data.add(new Address("시흥시", false));
            data.add(new Address("안산시", false));
            data.add(new Address("안성시", false));
            data.add(new Address("안양시", false));
            data.add(new Address("양주시", false));
            data.add(new Address("양평군", false));
            data.add(new Address("여주군", false));
            data.add(new Address("연천군", false));
            data.add(new Address("오산시", false));
            data.add(new Address("용인시", false));
            data.add(new Address("의왕시", false));
            data.add(new Address("의정부시", false));
            data.add(new Address("이천시", false));
            data.add(new Address("파주시", false));
            data.add(new Address("평택시", false));
            data.add(new Address("포천시", false));
            data.add(new Address("하남시", false));
            data.add(new Address("화성시", false));
        } else if ("강원도".equals(name)) {
            data.add(new Address("강릉시", false));
            data.add(new Address("고성군", false));
            data.add(new Address("동해시", false));
            data.add(new Address("삼척시", false));
            data.add(new Address("속초시", false));
            data.add(new Address("양구군", false));
            data.add(new Address("양양군", false));
            data.add(new Address("영월군", false));
            data.add(new Address("원주시", false));
            data.add(new Address("인제군", false));
            data.add(new Address("정선군", false));
            data.add(new Address("철원군", false));
            data.add(new Address("춘천시", false));
            data.add(new Address("태백시", false));
            data.add(new Address("평창군", false));
            data.add(new Address("홍천군", false));
            data.add(new Address("화천군", false));
        } else if ("충청북도".equals(name)) {
            data.add(new Address("괴산군", false));
            data.add(new Address("단양군", false));
            data.add(new Address("보은군", false));
            data.add(new Address("영동군", false));
            data.add(new Address("옥천군", false));
            data.add(new Address("음성군", false));
            data.add(new Address("제천시", false));
            data.add(new Address("증평군", false));
            data.add(new Address("진천군", false));
            data.add(new Address("청원군", false));
            data.add(new Address("청주시", false));
            data.add(new Address("충주시", false));
        } else if ("충청남도".equals(name)) {
            data.add(new Address("계룡시", false));
            data.add(new Address("공주시", false));
            data.add(new Address("금산군", false));
            data.add(new Address("논산시", false));
            data.add(new Address("당진군", false));
            data.add(new Address("보령시", false));
            data.add(new Address("부여군", false));
            data.add(new Address("서산시", false));
            data.add(new Address("서천군", false));
            data.add(new Address("아산시", false));
            data.add(new Address("연기군", false));
            data.add(new Address("예산군", false));
            data.add(new Address("천안시", false));
            data.add(new Address("청양군", false));
            data.add(new Address("태안군", false));
            data.add(new Address("홍성군", false));
        } else if ("전라북도".equals(name)) {
            data.add(new Address("고창군", false));
            data.add(new Address("군산시", false));
            data.add(new Address("김제시", false));
            data.add(new Address("남원시", false));
            data.add(new Address("무주군", false));
            data.add(new Address("부안군", false));
            data.add(new Address("순창군", false));
            data.add(new Address("완주군", false));
            data.add(new Address("익산시", false));
            data.add(new Address("임실군", false));
            data.add(new Address("장수군", false));
            data.add(new Address("전주시", false));
            data.add(new Address("정읍시", false));
            data.add(new Address("진안군", false));
        } else if ("전라남도".equals(name)) {
            data.add(new Address("강진군", false));
            data.add(new Address("고흥군", false));
            data.add(new Address("곡성군", false));
            data.add(new Address("광양시", false));
            data.add(new Address("구례군", false));
            data.add(new Address("나주시", false));
            data.add(new Address("담양군", false));
            data.add(new Address("목포시", false));
            data.add(new Address("무안군", false));
            data.add(new Address("보성군", false));
            data.add(new Address("순천시", false));
            data.add(new Address("신안군", false));
            data.add(new Address("여수시", false));
            data.add(new Address("영광군", false));
            data.add(new Address("영암군", false));
            data.add(new Address("완도군", false));
            data.add(new Address("장성군", false));
            data.add(new Address("장흥군", false));
            data.add(new Address("진도군", false));
            data.add(new Address("함평군", false));
            data.add(new Address("해남군", false));
            data.add(new Address("화순군", false));
        } else if ("경상북도".equals(name)) {
            data.add(new Address("경산시", false));
            data.add(new Address("경주시", false));
            data.add(new Address("고령군", false));
            data.add(new Address("구미시", false));
            data.add(new Address("군위군", false));
            data.add(new Address("김천시", false));
            data.add(new Address("문경시", false));
            data.add(new Address("봉화군", false));
            data.add(new Address("상주시", false));
            data.add(new Address("성주군", false));
            data.add(new Address("안동시", false));
            data.add(new Address("영덕군", false));
            data.add(new Address("영양군", false));
            data.add(new Address("영주시", false));
            data.add(new Address("영천시", false));
            data.add(new Address("예천군", false));
            data.add(new Address("울릉군", false));
            data.add(new Address("울진군", false));
            data.add(new Address("의성군", false));
            data.add(new Address("청도군", false));
            data.add(new Address("청송군", false));
            data.add(new Address("칠곡군", false));
            data.add(new Address("포항시", false));
        } else if ("경상남도".equals(name)) {
            data.add(new Address("거제시", false));
            data.add(new Address("거창군", false));
            data.add(new Address("고성군", false));
            data.add(new Address("김해시", false));
            data.add(new Address("남해군", false));
            data.add(new Address("밀양시", false));
            data.add(new Address("사천시", false));
            data.add(new Address("산청군", false));
            data.add(new Address("양산시", false));
            data.add(new Address("의령군", false));
            data.add(new Address("진주시", false));
            data.add(new Address("창녕군", false));
            data.add(new Address("창원시", false));
            data.add(new Address("통영시", false));
            data.add(new Address("하동군", false));
            data.add(new Address("함안군", false));
            data.add(new Address("함양군", false));
            data.add(new Address("합천군", false));
        } else if ("제주도".equals(name)) {
            data.add(new Address("서귀포시", false));
            data.add(new Address("제주시", false));
        }
        grid_view.setAdapter(addr2ListAdapter);
        addr2ListAdapter.notifyDataSetChanged();
        next_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.callOnClick();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 == addr2ListAdapter.getCountCheck()) {
                    oneBtnDialog = new OneBtnDialog(Addr2SelectActivity.this, "한가지 항목 이라도\n 선택해 주세요!", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                } else {
                    setResult(999);
                    finish();
                }
            }
        });
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
}
