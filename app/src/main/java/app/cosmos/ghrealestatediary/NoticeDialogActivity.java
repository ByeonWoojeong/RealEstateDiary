package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class NoticeDialogActivity extends AppCompatActivity {

    AQuery aQuery = null;
    String idx, image;
    ImageView img;
    Button btn1, btn2;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(NoticeDialogActivity.this, true);
            }
        }
        aQuery = new AQuery(this);
        Intent intent = getIntent();
        idx = intent.getStringExtra("idx");
        image = intent.getStringExtra("image");
        img = (ImageView) findViewById(R.id.img);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        Glide.with(NoticeDialogActivity.this).load(image).centerCrop().crossFade().into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("idx", idx);
                setResult(444, intent);
                finish();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date(System.currentTimeMillis());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DATE, 7);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String getDate = simpleDateFormat.format(calendar.getTime());
                SharedPreferences mainDialogDate = NoticeDialogActivity.this.getSharedPreferences("mainDialogDate", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mainDialogDate.edit();
                editor.clear();
                editor.putString("mainDialogDate", getDate);
                editor.commit();
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
