package app.cosmos.ghrealestatediary;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class InputImagePathActivity extends AppCompatActivity {

    String path;
    PhotoView image;
    PhotoViewAttacher attacher;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_input_image_path);


        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        image = (PhotoView) findViewById(R.id.originalImage);
        attacher = new PhotoViewAttacher(image);
        Glide.with(this).load(path.toString()).crossFade().centerCrop().into(image);
        attacher.update();
    }

    @Override
    public void onStart(){
        Log.i("IMG", "onStart");
        super.onStart();
    }

    @Override
    public void finish() {
        Log.i("IMG", "onFinish");
        super.finish();
        statusbarVisibility(true);
    }

    @Override
    protected void onDestroy() {
        Log.i("IMG", "onDestroy");
        super.onDestroy();
    }

    public void statusbarVisibility(boolean setVisibility){
        if(setVisibility){
            if (Build.VERSION.SDK_INT < 16) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            else {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }else{
            if (Build.VERSION.SDK_INT < 16) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            else {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }
}
