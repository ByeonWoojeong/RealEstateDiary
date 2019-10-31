package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

public class ConsultWriteActivity extends AppCompatActivity {

    private static Context context;

    int CAMERA = 700, GALLERY = 800, totalImageCount = 0, addImageCount = 0, getImageCount = 0;
    AQuery aQuery = null;
    InputMethodManager ipm;
    ImageView back, camera;
    LinearLayout image_con, name_con, phone_con, first_date_con, target_date_con;
    FrameLayout next_con, content_con;
    TextView toolbar_title, next, first_date, target_date;
    EditText name, phone, content;
    ScrollView scrollView;
    HorizontalScrollView horizontalScrollView;
    String getIdx, getName, getPhone, getFirst_date, getTarget_date, getContent, getImages;
    FrameLayout imageview_con[] = new FrameLayout[3];
    ImageView imageView[] = new ImageView[3];
    ImageView closeImageView[] = new ImageView[3];
    ArrayList<Bitmap> originalBitmap =  new ArrayList<Bitmap>();
    ArrayList<Bitmap> resizeBitmap =  new ArrayList<Bitmap>();
    ArrayList<String> imagePath =  new ArrayList<String>();
    ArrayList<File> file =  new ArrayList<File>();
    ArrayList<String> getImageList =  new ArrayList<String>();
    ContentResolver contentResolver;
    ProgressDialog progressDialog;
    OneBtnDialog oneBtnDialog;
    TwoBtnDialog twoBtnDialog;
    DatePickerDialog datePickerDialog;
    boolean isKeyBoardVisible;

    boolean checkPhoneNumber(String number){
        String getNumber = number.replaceAll("-", "").replaceAll(" ", "").replaceAll("[+]82", "0");
        boolean checkPhoneNumber = Pattern.matches("(01[016789])(\\d{3,4})(\\d{4})", getNumber);
        return checkPhoneNumber;
    }

    boolean keyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    private void hideKeyboard()
    {
        ipm.hideSoftInputFromWindow(name.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(phone.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(content.getWindowToken(), 0);
    }

    Uri getFileUri() {
        File dir = new File(getFilesDir(), "Picture");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, System.currentTimeMillis() + ".png");
        imagePath.add(file.getAbsolutePath()+"");
        return FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", file);
    }

    String getImageRealPathFromURI(ContentResolver contentResolver, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = contentResolver.query(contentUri, proj, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            int path = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String tmp = cursor.getString(path);
            cursor.close();
            return tmp;
        }
    }

    int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {

            }
        }
        return bitmap;
    }

    Bitmap resizeBitmap(String file, int width, int height) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }

    Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void finish() {
        super.finish();
        hideKeyboard();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyboard();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_write);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(ConsultWriteActivity.this, true);
            }
        }
        aQuery = new AQuery(this);
        ipm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        contentResolver = getContentResolver();
        Intent intent = getIntent();
        getIdx = intent.getStringExtra("idx");
        getName = intent.getStringExtra("name");
        getPhone = intent.getStringExtra("phone");
        getFirst_date = intent.getStringExtra("f_date");
        getTarget_date = intent.getStringExtra("m_date");
        getContent = intent.getStringExtra("content");
        getImages = intent.getStringExtra("images");
        back = (ImageView) findViewById(R.id.back);
        next_con = (FrameLayout) findViewById(R.id.next_con);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        next = (TextView) findViewById(R.id.next);
        name = (EditText) findViewById(R.id.name);
        name.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyboardShown(name.getRootView())) {
                    isKeyBoardVisible = true;
                } else {
                    isKeyBoardVisible = false;
                }
            }
        });
        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        phone.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        phone.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        phone.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        phone.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        phone.requestFocus();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if( name.length() == 0 && phone.length() == 0 && first_date.length() == 0 && target_date.length() == 0 && content.length() == 0){
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else if(name.getText().toString().equals(getName) && phone.getText().toString().equals(getPhone) && first_date.getText().toString().equals(getFirst_date)
                        && target_date.getText().toString().equals(getTarget_date) && content.getText().toString().equals(getContent)){
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else if(phone.getText().toString().equals(getPhone) && name.length() == 0 && first_date.length() == 0 && target_date.length() == 0 && content.length() == 0){
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else{
                    next.setTextColor(getResources().getColor(R.color.baseColor));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        phone = (EditText) findViewById(R.id.phone);
        phone.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyboardShown(phone.getRootView())) {
                    isKeyBoardVisible = true;
                } else {
                    isKeyBoardVisible = false;
                }
            }
        });
        phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        first_date.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        first_date.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        first_date.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        first_date.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        first_date.requestFocus();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if( name.length() == 0 && phone.length() == 0 && first_date.length() == 0 && target_date.length() == 0 && content.length() == 0){
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else if(name.getText().toString().equals(getName) && phone.getText().toString().equals(getPhone) && first_date.getText().toString().equals(getFirst_date)
                        && target_date.getText().toString().equals(getTarget_date) && content.getText().toString().equals(getContent)){
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else if(phone.getText().toString().equals(getPhone) && name.length() == 0 && first_date.length() == 0 && target_date.length() == 0 && content.length() == 0){
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else{
                    next.setTextColor(getResources().getColor(R.color.baseColor));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher("KR"));
        } else {
            phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        }
        first_date = (TextView) findViewById(R.id.first_date);
        first_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if( name.length() == 0 && phone.length() == 0 && first_date.length() == 0 && target_date.length() == 0 && content.length() == 0){
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else if(name.getText().toString().equals(getName) && phone.getText().toString().equals(getPhone) && first_date.getText().toString().equals(getFirst_date)
                        && target_date.getText().toString().equals(getTarget_date) && content.getText().toString().equals(getContent)){
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else if(phone.getText().toString().equals(getPhone) && name.length() == 0 && first_date.length() == 0 && target_date.length() == 0 && content.length() == 0){
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else{
                    next.setTextColor(getResources().getColor(R.color.baseColor));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        target_date = (TextView) findViewById(R.id.target_date);
        target_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if( name.length() == 0 && phone.length() == 0 && first_date.length() == 0 && target_date.length() == 0 && content.length() == 0){
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else if(name.getText().toString().equals(getName) && phone.getText().toString().equals(getPhone) && first_date.getText().toString().equals(getFirst_date)
                        && target_date.getText().toString().equals(getTarget_date) && content.getText().toString().equals(getContent)){
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else if(phone.getText().toString().equals(getPhone) && name.length() == 0 && first_date.length() == 0 && target_date.length() == 0 && content.length() == 0){
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else{
                    next.setTextColor(getResources().getColor(R.color.baseColor));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        content = (EditText) findViewById(R.id.content);
        content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyboardShown(content.getRootView())) {
                    isKeyBoardVisible = true;
                } else {
                    isKeyBoardVisible = false;
                }
            }
        });
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if( name.length() == 0 && phone.length() == 0 && first_date.length() == 0 && target_date.length() == 0 && content.length() == 0){
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else if(name.getText().toString().equals(getName) && phone.getText().toString().equals(getPhone) && first_date.getText().toString().equals(getFirst_date)
                        && target_date.getText().toString().equals(getTarget_date) && content.getText().toString().equals(getContent)){
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else if(phone.getText().toString().equals(getPhone) && name.length() == 0 && first_date.length() == 0 && target_date.length() == 0 && content.length() == 0){
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else{
                    next.setTextColor(getResources().getColor(R.color.baseColor));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        image_con = (LinearLayout) findViewById(R.id.image_con);
        name_con = (LinearLayout) findViewById(R.id.name_con);
        phone_con = (LinearLayout) findViewById(R.id.phone_con);
        first_date_con = (LinearLayout) findViewById(R.id.first_date_con);
        target_date_con = (LinearLayout) findViewById(R.id.target_date_con);
        content_con = (FrameLayout) findViewById(R.id.content_con);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        camera = (ImageView) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipm.hideSoftInputFromWindow(camera.getWindowToken(), 0);
                if (totalImageCount >= 6) {
                    oneBtnDialog = new OneBtnDialog(ConsultWriteActivity.this, "이미지는 최대 6장까지\n첨부 가능합니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                twoBtnDialog = new TwoBtnDialog(ConsultWriteActivity.this);
                twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                twoBtnDialog.show();
            }
        });
        phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        first_date.callOnClick();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        first_date.callOnClick();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        first_date.callOnClick();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        first_date.callOnClick();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        first_date.callOnClick();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        for (int i = 0; i < imageView.length; i++) {
            int res1 = getResources().getIdentifier("imageview_con"+i, "id", getPackageName());
            int res2 = getResources().getIdentifier("imageview"+i, "id", getPackageName());
            int res3 = getResources().getIdentifier("close_imageview"+i, "id", getPackageName());
            imageview_con[i] = (FrameLayout) findViewById(res1);
            imageView[i] = (ImageView) findViewById(res2);
            closeImageView[i] = (ImageView) findViewById(res3);
            final int finalI = i;
            imageView[finalI].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ConsultWriteActivity.this, InputImagePathActivity.class);
                    intent.putExtra("path", imagePath.get(finalI));
                    startActivityForResult(intent, 900);
                }
            });
            closeImageView[finalI].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalImageCount --;
                    if (totalImageCount <= 0) {
                        image_con.setVisibility(View.GONE);
                    }
                    imageview_con[totalImageCount].setVisibility(View.GONE);
                    imageView[totalImageCount].setImageBitmap(null);
                    imagePath.remove(finalI);
                    if (getImageList.size() > 0 && finalI < getImageList.size()) {
                        getImageCount --;
                        getImageList.remove(finalI);
                    }
                    if (addImageCount > 0) {
                        if (finalI > getImageList.size()) {
                            addImageCount --;
                            originalBitmap.remove(finalI - getImageCount);
                            resizeBitmap.remove(finalI - getImageCount);
                        }
                    }
                    for (int i = finalI; i < totalImageCount; i++){
                        imageView[i].setImageBitmap(null);
                        Glide.with(ConsultWriteActivity.this).load(imagePath.get(i)).crossFade().bitmapTransform(new CenterCrop(ConsultWriteActivity.this), new RoundedCornersTransformation(ConsultWriteActivity.this, 50, 0)).into(imageView[i]);
                    }
                }
            });
        }
        name_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        phone_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        first_date_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_date.callOnClick();
            }
        });
        first_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ipm.isAcceptingText()) {
                    ipm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                datePickerDialog = new DatePickerDialog(ConsultWriteActivity.this, "1");
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });
        target_date_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target_date.callOnClick();
            }
        });
        target_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ipm.isAcceptingText()) {
                    ipm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                datePickerDialog = new DatePickerDialog(ConsultWriteActivity.this, "2");
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });
        content_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        next_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.callOnClick();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isKeyBoardVisible) {
                    ipm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                if ("".equals(name.getText().toString().trim()) && "".equals(phone.getText().toString().trim()) && "".equals(first_date.getText().toString().trim()) && "".equals(target_date.getText().toString().trim()) && "".equals(content.getText().toString().trim())) {
                    oneBtnDialog = new OneBtnDialog(ConsultWriteActivity.this, "한가지 항목 이라도\n입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }

                if (!"".equals(phone.getText().toString().trim())) {
                    if (!checkPhoneNumber(phone.getText().toString())) {
                        oneBtnDialog = new OneBtnDialog(ConsultWriteActivity.this, "연락처(전화번호)\n형식을 맞춰 주세요 !", "확인");
                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        oneBtnDialog.setCancelable(false);
                        oneBtnDialog.show();
                        return;
                    }
                }
                if (getIdx != null) {
                    progressDialog = new ProgressDialog(ConsultWriteActivity.this, "수정 중 입니다.\n잠시만 기다려 주세요 !");
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                } else {
                    progressDialog = new ProgressDialog(ConsultWriteActivity.this, "작성 중 입니다.\n잠시만 기다려 주세요 !");
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                                final String getToken = get_token.getString("Token", "");
//                                final Map<String, Object> params = new HashMap<String, Object>();

                                Thread th = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                                            trustManagerFactory.init((KeyStore) null);
                                            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                                            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                                                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
                                            }
                                            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
                                            SSLContext sslContext = SslUtils.getSslContextForCertificateFile(context, "BPClass2RootCA-sha2.cer");
                                            OkHttpClient client = new OkHttpClient.Builder().sslSocketFactory(sslContext.getSocketFactory(), trustManager).build();

                                            String url = null;
                                            if (getIdx != null) {
                                                url = UrlManager.getBaseUrl() + "memo/update";
                                                FormBody.Builder formBuilder = new FormBody.Builder()
                                                        .add("idx", getIdx)
                                                        .add("name", name.getText().toString().trim())
                                                        .add("phone", phone.getText().toString().trim().replaceAll("-", "").replaceAll(" ", ""))
                                                        .add("f_date", first_date.getText().toString().trim())
                                                        .add("m_date", target_date.getText().toString().trim())
                                                        .add("content", content.getText().toString().trim());
                                                file.clear();
                                                for (int i = 0; i < addImageCount; i++) {
                                                    Uri fileUri = getImageUri(ConsultWriteActivity.this, resizeBitmap.get(i));
                                                    String filePath = getImageRealPathFromURI(ConsultWriteActivity.this.getContentResolver(), fileUri);
                                                    File makeFile = new File(filePath);
                                                    file.add(makeFile);
                                                    formBuilder.add("image[" + i + "]", file.get(i).toString());
                                                }
                                                for (int i = 0; i < getImageCount; i++) {
                                                    formBuilder.add("image_list[" + i + "]", getImageList.get(i));
                                                }
                                                RequestBody body = formBuilder.build();
                                                Request request = new Request.Builder()
                                                        .url(url)
                                                        .header("GHsoft-Agent", "" + getToken).header("User-Agent", "android")
                                                        .post(body)
                                                        .build();
                                                try {
                                                    Response response = client.newCall(request).execute();
                                                    String responseStr = response.body().string();
                                                    Log.i("OKHTTP", " responseStr " + responseStr);
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(responseStr);
                                                        Log.i("OKHTTP", " jsonObject " + jsonObject);

                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                progressDialog.dismiss();
                                                            }
                                                        });
                                                        for (int i = 0; i < addImageCount; i++){
                                                            contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.ImageColumns.DATA + "=?" , new String[]{ file.get(i).toString() });
                                                            file.get(i).delete();
                                                        }

                                                        if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toast.makeText(ConsultWriteActivity.this, "수정을 완료 하였습니다." , Toast.LENGTH_SHORT).show();
                                                                    Intent i = new Intent(ConsultWriteActivity.this, ConsultListActivity.class);
                                                                    startActivity(i);
                                                                    finish();
                                                                }
                                                            });

                                                        } else if (!jsonObject.getBoolean("return")) {

                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    oneBtnDialog = new OneBtnDialog(ConsultWriteActivity.this, "수정을 할 수 없습니다 !", "확인");
                                                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                    oneBtnDialog.setCancelable(false);
                                                                    oneBtnDialog.show();
                                                                }
                                                            });
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                url = UrlManager.getBaseUrl() + "memo/insert";
                                                FormBody.Builder formBuilder = new FormBody.Builder()
                                                        .add("name", name.getText().toString().trim())
                                                        .add("phone", phone.getText().toString().trim().replaceAll("-", "").replaceAll(" ", ""))
                                                        .add("f_date", first_date.getText().toString().trim())
                                                        .add("m_date", target_date.getText().toString().trim())
                                                        .add("content", content.getText().toString().trim());
                                                file.clear();
                                                for (int i = 0; i < addImageCount; i++) {
                                                    Uri fileUri = getImageUri(ConsultWriteActivity.this, resizeBitmap.get(i));
                                                    String filePath = getImageRealPathFromURI(ConsultWriteActivity.this.getContentResolver(), fileUri);
                                                    File makeFile = new File(filePath);
                                                    file.add(makeFile);
                                                    formBuilder.add("image[" + i + "]", file.get(i).toString());
                                                }
                                                for (int i = 0; i < getImageCount; i++) {
                                                    formBuilder.add("image_list[" + i + "]", getImageList.get(i));
                                                }

                                                RequestBody body = formBuilder.build();
                                                Request request = new Request.Builder()
                                                        .url(url)
                                                        .header("GHsoft-Agent", "" + getToken).header("User-Agent", "android")
                                                        .post(body)
                                                        .build();
                                                try {
                                                    Response response = client.newCall(request).execute();
                                                    String responseStr = response.body().string();
                                                    Log.i("OKHTTP", " responseStr " + responseStr);

                                                    try {
                                                        JSONObject jsonObject = new JSONObject(responseStr);
                                                        Log.i("OKHTTP", " jsonObject " + jsonObject);

                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                progressDialog.dismiss();
                                                            }
                                                        });
                                                        for (int i = 0; i < addImageCount; i++){
                                                            contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.ImageColumns.DATA + "=?" , new String[]{ file.get(i).toString() });
                                                            file.get(i).delete();
                                                        }

                                                        if (jsonObject.getBoolean("return")) {    //return이 true 면?
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    finish();
                                                                    Toast.makeText(ConsultWriteActivity.this, "작성을 완료 하였습니다.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });


                                                        } else if (!jsonObject.getBoolean("return")) {

                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    oneBtnDialog = new OneBtnDialog(ConsultWriteActivity.this, "작성을 할 수 없습니다 !", "확인");
                                                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                    oneBtnDialog.setCancelable(false);
                                                                    oneBtnDialog.show();
                                                                }
                                                            });
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        } catch (NoSuchAlgorithmException e) {
                                            e.printStackTrace();
                                        } catch (KeyStoreException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                th.start();



//                                if (getIdx != null) {
//                                    url = UrlManager.getBaseUrl() + "memo/update";
//                                    params.put("idx", getIdx);
//                                } else {
//                                    url = UrlManager.getBaseUrl() + "memo/insert";
//                                }
//                                params.put("name", name.getText().toString().trim());
//                                params.put("phone", phone.getText().toString().trim().replaceAll("-", "").replaceAll(" ", ""));
//                                params.put("f_date", first_date.getText().toString().trim());
//                                params.put("m_date", target_date.getText().toString().trim());
//                                params.put("content", content.getText().toString().trim());
//                                file.clear();
//                                for (int i = 0; i < addImageCount; i++){
//                                    Uri fileUri = getImageUri(ConsultWriteActivity.this, resizeBitmap.get(i));
//                                    String filePath = getImageRealPathFromURI(ConsultWriteActivity.this.getContentResolver(), fileUri);
//                                    File makeFile = new File(filePath);
//                                    file.add(makeFile);
//                                    params.put("image["+i+"]", file.get(i));
//                                }
//                                for (int i = 0; i < getImageCount; i++){
//                                    params.put("image_list["+i+"]", getImageList.get(i));
//                                }
//                                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                                    @Override
//                                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                                        progressDialog.dismiss();
//                                        for (int i = 0; i < addImageCount; i++){
//                                            contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.ImageColumns.DATA + "=?" , new String[]{ file.get(i).toString() });
//                                            file.get(i).delete();
//                                        }
//                                        try {
//                                            if (jsonObject.getBoolean("return")) {
//                                                if (getIdx != null) {
//                                                    Toast.makeText(ConsultWriteActivity.this, "수정을 완료 하였습니다." , Toast.LENGTH_SHORT).show();
//                                                    Intent i = new Intent(ConsultWriteActivity.this, ConsultListActivity.class);
//                                                    startActivity(i);
//                                                    finish();
//                                                } else {
//                                                    finish();
//                                                    Toast.makeText(ConsultWriteActivity.this, "작성을 완료 하였습니다." , Toast.LENGTH_SHORT).show();
//                                                }
//                                            } else if (!jsonObject.getBoolean("return")) {
//                                                if (getIdx != null) {
//                                                    oneBtnDialog = new OneBtnDialog(ConsultWriteActivity.this, "수정을 할 수 없습니다 !", "확인");
//                                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                                    oneBtnDialog.setCancelable(false);
//                                                    oneBtnDialog.show();
//                                                } else {
//                                                    oneBtnDialog = new OneBtnDialog(ConsultWriteActivity.this, "작성을 할 수 없습니다 !", "확인");
//                                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                                    oneBtnDialog.setCancelable(false);
//                                                    oneBtnDialog.show();
//                                                }
//                                            }
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
                            }
                        });
                    }
                }).start();
            }
        });
        if (getIdx != null) {   //수정 페이지
            toolbar_title.setText("수정");
            name.setText(getName);
            phone.setText(getPhone);
            if (!"null".equals(getFirst_date)) {
                first_date.setText(getFirst_date);
            }
            if (!"null".equals(getTarget_date)) {
                target_date.setText(getTarget_date);
            }
            content.setText(getContent);
            if (!"null".equals(getImages)) {
                image_con.setVisibility(View.VISIBLE);
                final String[] images = getImages.split(",");
                totalImageCount = images.length;
                getImageCount = images.length;
                for (int i = 0; i < images.length; i++) {
                    imagePath.add(i, UrlManager.getBaseUrl() + "uploads/images/origin/" + images[i]);
                    getImageList.add(i, images[i]);
                    imageview_con[i].setVisibility(View.VISIBLE);
                    Glide.with(ConsultWriteActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + images[i]).crossFade().bitmapTransform(new CenterCrop(ConsultWriteActivity.this), new RoundedCornersTransformation(ConsultWriteActivity.this, 50, 0)).into(imageView[i]);
                }
            }
        } else {
            toolbar_title.setText("작성");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 700:
                if (resultCode == RESULT_CANCELED){
                    return;
                } else {
                    try {
                        if (Build.VERSION.SDK_INT < 21){
                            Uri imgUri = data.getData();
                            imagePath.add(getImageRealPathFromURI(ConsultWriteActivity.this.getContentResolver(), imgUri));
                        }
                        ExifInterface exif = new ExifInterface(imagePath.get(totalImageCount));
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        originalBitmap.add(rotate(BitmapFactory.decodeFile(imagePath.get(totalImageCount)), exifDegree));
                        resizeBitmap.add(rotate(resizeBitmap(imagePath.get(totalImageCount), 1080, 1920), exifDegree));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image_con.setVisibility(View.VISIBLE);
                    if (totalImageCount < 3) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                        imageview_con[totalImageCount].setVisibility(View.VISIBLE);
                    }
                    Glide.with(ConsultWriteActivity.this).load(imagePath.get(totalImageCount)).crossFade().bitmapTransform(new CenterCrop(ConsultWriteActivity.this), new RoundedCornersTransformation(ConsultWriteActivity.this, 50, 0)).into(imageView[totalImageCount]);
                    totalImageCount ++;
                    addImageCount ++;
                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }, 99);
                }
                break;
            case 800:
                if (resultCode == RESULT_CANCELED){
                    return;
                } else {
                    try {
                        Uri imgUri = data.getData();
                        imagePath.add(getImageRealPathFromURI(ConsultWriteActivity.this.getContentResolver(), imgUri));
                        ExifInterface exif = new ExifInterface(imagePath.get(totalImageCount));
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        originalBitmap.add(rotate(BitmapFactory.decodeFile(imagePath.get(totalImageCount)), exifDegree));
                        resizeBitmap.add(rotate(resizeBitmap(imagePath.get(totalImageCount), 1080, 1920), exifDegree));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image_con.setVisibility(View.VISIBLE);
                    if (totalImageCount < 3) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                        imageview_con[totalImageCount].setVisibility(View.VISIBLE);
                    }
                    Glide.with(ConsultWriteActivity.this).load(imagePath.get(totalImageCount)).crossFade().bitmapTransform(new CenterCrop(ConsultWriteActivity.this), new RoundedCornersTransformation(ConsultWriteActivity.this, 50, 0)).into(imageView[totalImageCount]);
                    totalImageCount ++;
                    addImageCount ++;
                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }, 99);
                }
                break;
            case 900:
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 99);
                break;
            case RESULT_CANCELED:
                break;
        }
    }

    public class ProgressDialog extends Dialog {
        Context context;
        public ProgressDialog(final Context context, final String text) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_progress_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title1 = (TextView) findViewById(R.id.title1);
            TextView title2 = (TextView) findViewById(R.id.title2);
            title2.setVisibility(View.GONE);
            title1.setText(text);
        }
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

    public class TwoBtnDialog extends Dialog {
        TwoBtnDialog twoBtnDialog = this;
        Context context;
        public TwoBtnDialog(final Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_two_btn_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title1 = (TextView) findViewById(R.id.title1);
            TextView title2 = (TextView) findViewById(R.id.title2);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            title2.setVisibility(View.GONE);
            title1.setText("이미지 첨부방식을\n선택해 주세요 !");
            btn1.setText("카메라");
            btn1.setTextColor(Color.parseColor("#7199ff"));
            btn2.setText("갤러리");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnDialog.dismiss();
                    if (Build.VERSION.SDK_INT > 21){
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, getFileUri());
                        startActivityForResult(intent, CAMERA);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA);
                    }
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnDialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY);
                }
            });
        }
    }

    public class DatePickerDialog extends Dialog {
        DatePickerDialog datePickerDialog = this;
        Context context;
        public DatePickerDialog(final Context context, final String type) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_datepicker_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            Calendar calendar = Calendar.getInstance();
            final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            btn1.setText("취소");
            btn2.setText("선택");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerDialog.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerDialog.dismiss();
                    if ("1".equals(type)) {
                        first_date.setText(datePicker.getYear() + "-" + (datePicker.getMonth() +1) + "-" + datePicker.getDayOfMonth());
                    } else if ("2".equals(type)) {
                        target_date.setText(datePicker.getYear() + "-" + (datePicker.getMonth() +1) + "-" + datePicker.getDayOfMonth());
                    }
                }
            });
        }
    }
}
