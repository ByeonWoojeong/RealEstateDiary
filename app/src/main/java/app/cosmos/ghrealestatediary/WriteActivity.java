package app.cosmos.ghrealestatediary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;


import org.json.JSONArray;
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
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import app.cosmos.ghrealestatediary.DTO.Main;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static app.cosmos.ghrealestatediary.GlobalApplication.setDarkMode;

@SuppressLint("SetJavaScriptEnabled")
public class WriteActivity extends AppCompatActivity {

    Context context;
    int CAMERA = 700, GALLERY = 800, totalImageCount = 0, addImageCount = 0, getImageCount = 0;
    AQuery aQuery = null;
    InputMethodManager ipm;
    boolean isKeyBoardVisible;
    ImageView back, camera, finish_date_icon;
    LinearLayout activity_write, address_con, image_con, title_con, dong_con, ho_con, now_floor_con, all_floor_con, address2_con, area1_con, area2_con, maemae_con, junsae_con, bojeung_con, woalsae_con, geunri_con, gaunri_con, door_lock1_con, door_lock2_con, damdang_con, damdang_phone_con, finish_date_con;
    FrameLayout next_con, sangse_con;
    TextView toolbar_title, next, spinner_text, address, area1_unit, area2_unit, maemae_unit, junsae_unit, bojeung_unit, woalsae_unit, geunri_unit, gaunri_unit, finish_date;
    EditText title, dong, ho, now_floor, all_floor, address2, area1, area2, maemae, junsae, bojeung, woalsae, geunri, gaunri, door_lock1, door_lock2, damdang, damdang_phone, sangse;
    ScrollView scrollView;
    HorizontalScrollView horizontalScrollView;
    String addr, la, lo, getIdx, getType, getTitle, getAddress, getAddress2, getArea, getArea2, getMaemae, getJunsae, getWoalsae, getBojeung, getGeunri, getGaunri, getDoor_lock, getDoor_lock2, getDamdang, getDamdang_phone, getSangse, getImages, getFinishDate, getDong, getHo, getNowFloor, getAllFloor;
    boolean userIsInteracting;
    SpinnerReselect spinner;
    FrameLayout imageview_con[] = new FrameLayout[6];
    ImageView imageView[] = new ImageView[6];
    ImageView closeImageView[] = new ImageView[6];
    ArrayList<Bitmap> originalBitmap = new ArrayList<Bitmap>();
    ArrayList<Bitmap> resizeBitmap = new ArrayList<Bitmap>();
    ArrayList<String> imagePath = new ArrayList<String>();
    ArrayList<File> file = new ArrayList<File>();
    ArrayList<String> getImageList = new ArrayList<String>();
    ContentResolver contentResolver;
    ProgressDialog progressDialog;
    OneBtnDialog oneBtnDialog;
    TwoBtnDialog twoBtnDialog;
    NextDialog nextDialog;
    DatePickerDialog datePickerDialog;

    boolean isCategory;

    boolean checkPhoneNumber(String number) {
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

    Uri getFileUri() {
        File dir = new File(getFilesDir(), "Picture");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, System.currentTimeMillis() + ".png");
        imagePath.add(file.getAbsolutePath() + "");
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

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void hideKeyboard() {
        ipm.hideSoftInputFromWindow(title.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(dong.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(ho.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(now_floor.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(all_floor.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(address2.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(area1.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(area2.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(maemae.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(junsae.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(bojeung.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(woalsae.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(geunri.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(gaunri.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(door_lock1.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(door_lock2.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(damdang.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(damdang_phone.getWindowToken(), 0);
        ipm.hideSoftInputFromWindow(sangse.getWindowToken(), 0);
    }

    @Override
    public void finish() {
        super.finish();
//            ipm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        hideKeyboard();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//            ipm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        hideKeyboard();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ffffff"));
            window.setBackgroundDrawable(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setDarkMode(WriteActivity.this, true);
            }
        }
        aQuery = new AQuery(this);
        activity_write = (LinearLayout) findViewById(R.id.activity_write);
        ipm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        contentResolver = getContentResolver();
        Intent intent = getIntent();
        addr = intent.getStringExtra("addr");
        la = intent.getStringExtra("la");
        lo = intent.getStringExtra("lo");
        getIdx = intent.getStringExtra("idx");
        getType = intent.getStringExtra("type");
        getTitle = intent.getStringExtra("title");
        getDong = intent.getStringExtra("dong");
        getHo = intent.getStringExtra("ho");
        getNowFloor = intent.getStringExtra("nfloor");
        getAllFloor = intent.getStringExtra("afloor");
        getAddress = intent.getStringExtra("address");
        getAddress2 = intent.getStringExtra("address2");
        getArea = intent.getStringExtra("area");
        getArea2 = intent.getStringExtra("area2");
        getMaemae = intent.getStringExtra("maemae");
        getJunsae = intent.getStringExtra("junsae");
        getWoalsae = intent.getStringExtra("woalsae");
        getBojeung = intent.getStringExtra("bojeung");
        getGeunri = intent.getStringExtra("geunri");
        getGaunri = intent.getStringExtra("gaunri");
        getDoor_lock = intent.getStringExtra("door_lock");
        getDoor_lock2 = intent.getStringExtra("door_lock2");
        getDamdang = intent.getStringExtra("damdang");
        getDamdang_phone = intent.getStringExtra("damdang_phone");
        getFinishDate = intent.getStringExtra("enddate");
        getSangse = intent.getStringExtra("sangse");
        getImages = intent.getStringExtra("images");
        back = (ImageView) findViewById(R.id.back);
        next_con = (FrameLayout) findViewById(R.id.next_con);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        next = (TextView) findViewById(R.id.next);
        title = (EditText) findViewById(R.id.title);
        title.addTextChangedListener(textWatcherTitle);
        address = (TextView) findViewById(R.id.address);
        address.addTextChangedListener(textWatcherAddress);
        dong = (EditText) findViewById(R.id.dong);
        ho = (EditText) findViewById(R.id.ho);
        now_floor = (EditText) findViewById(R.id.now_floor);
        all_floor = (EditText) findViewById(R.id.all_floor);
        address2 = (EditText) findViewById(R.id.address2);
        address2.addTextChangedListener(textWatcherAddDetail);
        area1 = (EditText) findViewById(R.id.area1);
        area2 = (EditText) findViewById(R.id.area2);
        area1_unit = (TextView) findViewById(R.id.area1_unit);
        area2_unit = (TextView) findViewById(R.id.area2_unit);
        maemae = (EditText) findViewById(R.id.maemae);
        maemae.addTextChangedListener(new HyphenTextWathcer(maemae));
        maemae_unit = (TextView) findViewById(R.id.maemae_unit);
        junsae = (EditText) findViewById(R.id.junsae);
        junsae.addTextChangedListener(new HyphenTextWathcer(junsae));
        junsae_unit = (TextView) findViewById(R.id.junsae_unit);
        bojeung = (EditText) findViewById(R.id.imdae);
        bojeung.setEnabled(false);
        bojeung.addTextChangedListener(new HyphenTextWathcer(bojeung));
        bojeung_unit = (TextView) findViewById(R.id.bojeung_unit);
        woalsae = (EditText) findViewById(R.id.woalsae);
        woalsae.addTextChangedListener(new HyphenTextWathcer(woalsae));
        woalsae_unit = (TextView) findViewById(R.id.woalsae_unit);
        geunri = (EditText) findViewById(R.id.geunri);
        geunri.addTextChangedListener(new HyphenTextWathcer(geunri));
        geunri_unit = (TextView) findViewById(R.id.geunri_unit);
        gaunri = (EditText) findViewById(R.id.gaunri);
        gaunri.addTextChangedListener(new HyphenTextWathcer(gaunri));
        gaunri_unit = (TextView) findViewById(R.id.gaunri_unit);
        finish_date = (TextView) findViewById(R.id.finish_date);
        finish_date_icon = (ImageView) findViewById(R.id.finish_date_icon);
        door_lock1 = (EditText) findViewById(R.id.door_lock1);
        door_lock2 = (EditText) findViewById(R.id.door_lock2);
        damdang = (EditText) findViewById(R.id.damdang);
        damdang.addTextChangedListener(textWatcherManager);
        damdang_phone = (EditText) findViewById(R.id.damdang_phone);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            damdang_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher("KR"));
            damdang_phone.addTextChangedListener(textWatcherManagerTel);
        } else {
            damdang_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
            damdang_phone.addTextChangedListener(textWatcherManagerTel);
        }
        sangse = (EditText) findViewById(R.id.sangse);
        image_con = (LinearLayout) findViewById(R.id.image_con);
        title_con = (LinearLayout) findViewById(R.id.title_con);
        dong_con = (LinearLayout) findViewById(R.id.dong_con);
        ho_con = (LinearLayout) findViewById(R.id.ho_con);
        now_floor_con = (LinearLayout) findViewById(R.id.now_floor_con);
        all_floor_con = (LinearLayout) findViewById(R.id.all_floor_con);
        address_con = (LinearLayout) findViewById(R.id.address_con);
        address2_con = (LinearLayout) findViewById(R.id.address2_con);
        area1_con = (LinearLayout) findViewById(R.id.area1_con);
        area2_con = (LinearLayout) findViewById(R.id.area2_con);
        maemae_con = (LinearLayout) findViewById(R.id.maemae_con);
        junsae_con = (LinearLayout) findViewById(R.id.junsae_con);
        bojeung_con = (LinearLayout) findViewById(R.id.imdae_con);
        woalsae_con = (LinearLayout) findViewById(R.id.woalsae_con);
        geunri_con = (LinearLayout) findViewById(R.id.geunri_con);
        gaunri_con = (LinearLayout) findViewById(R.id.gaunri_con);
        door_lock1_con = (LinearLayout) findViewById(R.id.door_lock1_con);
        door_lock2_con = (LinearLayout) findViewById(R.id.door_lock2_con);
        damdang_con = (LinearLayout) findViewById(R.id.damdang_con);
        damdang_phone_con = (LinearLayout) findViewById(R.id.damdang_phone_con);
        finish_date_con = (LinearLayout) findViewById(R.id.finish_date_con);
        sangse_con = (FrameLayout) findViewById(R.id.sangse_con);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        spinner = (SpinnerReselect) findViewById(R.id.spinner);
        spinner_text = (TextView) findViewById(R.id.spinner_text);
        String[] kategorie = new String[]{
                "원룸",
                "투/쓰리룸",
                "아파트",
                "사무실",
                "주택",
                "상가"
        };
        final ArrayAdapter<String> kategorieAdapter = new ArrayAdapter<String>(WriteActivity.this, R.layout.my_spinner, kategorie);
        kategorieAdapter.setDropDownViewResource(R.layout.my_spinner);
        spinner.setAdapter(kategorieAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (userIsInteracting) {
                    spinner_text.setText(spinner.getSelectedItem().toString());
                    getType = (spinner.getSelectedItemPosition() + 1) + "";
                    isCategory = true;
                    if (!"".equals(title.getText().toString().trim()) && !"".equals(address.getText().toString().trim())
                            && !"".equals(damdang.getText().toString().trim()) && !"".equals(damdang_phone.getText().toString().trim()) && isCategory) {

                        if (!"".equals(maemae.getText().toString().trim()) || !"".equals(junsae.getText().toString().trim()) || !"".equals(woalsae.getText().toString().trim())) {

                            next.setTextColor(getResources().getColor(R.color.baseColor));
                        } else {
                            next.setTextColor(getResources().getColor(R.color.grayColor));
                        }
                    } else {
                        next.setTextColor(getResources().getColor(R.color.grayColor));
                    }
                    Log.i("WriteActivity", "IS: " + isCategory);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        camera = (ImageView) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipm.hideSoftInputFromWindow(camera.getWindowToken(), 0);
                if (totalImageCount >= 6) {
                    oneBtnDialog = new OneBtnDialog(WriteActivity.this, "이미지는 최대 6장까지\n첨부 가능합니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                twoBtnDialog = new TwoBtnDialog(WriteActivity.this);
                twoBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                twoBtnDialog.show();
            }
        });

        if (addr != null) {
            address.setText(addr);
        } else {
            address_con.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    address.callOnClick();
                }
            });
            address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WriteActivity.this, AddrSelectActivity.class);
                    startActivityForResult(intent, 1);
                }
            });
            title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    switch (actionId) {
                        case EditorInfo.IME_ACTION_NEXT:
                            address.callOnClick();
                            break;
                        case EditorInfo.IME_ACTION_GO:
                            address.callOnClick();
                            break;
                        case EditorInfo.IME_ACTION_SEND:
                            address.callOnClick();
                            break;
                        case EditorInfo.IME_ACTION_SEARCH:
                            address.callOnClick();
                            break;
                        case EditorInfo.IME_ACTION_DONE:
                            address.callOnClick();
                            break;
                        default:
                            return false;
                    }
                    return true;
                }
            });
        }
        dong.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        ho.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        ho.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        ho.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        ho.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        ho.requestFocus();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        ho.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        now_floor.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        now_floor.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        now_floor.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        now_floor.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        now_floor.requestFocus();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        now_floor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        all_floor.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        all_floor.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        all_floor.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        all_floor.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        all_floor.requestFocus();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        all_floor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        address2.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        address2.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        address2.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        address2.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        address2.requestFocus();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        address2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        area1.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        area1.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        area1.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        area1.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        area1.requestFocus();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        area1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals(area1.getText().toString().trim())) {
                    area1_unit.setVisibility(View.GONE);
                } else {
                    area1_unit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        area1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        area2.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        area2.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        area2.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        area2.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        area2.requestFocus();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        area2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals(area1.getText().toString().trim())) {
                    area2_unit.setVisibility(View.GONE);
                } else {
                    area2_unit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        area2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        maemae.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        maemae.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        maemae.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        maemae.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        maemae.requestFocus();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        maemae.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!"".equals(title.getText().toString().trim()) && !"".equals(address.getText().toString().trim()) &&
                        !"".equals(address2.getText().toString().trim()) && !"".equals(damdang.getText().toString().trim()) &&
                        !"".equals(damdang_phone.getText().toString().trim()) && isCategory) {

                    next.setTextColor(getResources().getColor(R.color.baseColor));
                } else {
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }

                if ("".equals(maemae.getText().toString().trim())) {
                    maemae_unit.setVisibility(View.GONE);
                } else {
                    maemae_unit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        maemae.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        if ("".equals(junsae.getText().toString().trim())) {
                            junsae.requestFocus();
                        } else {
                            woalsae.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        if ("".equals(junsae.getText().toString().trim())) {
                            junsae.requestFocus();
                        } else {
                            woalsae.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        if ("".equals(junsae.getText().toString().trim())) {
                            junsae.requestFocus();
                        } else {
                            woalsae.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        if ("".equals(junsae.getText().toString().trim())) {
                            junsae.requestFocus();
                        } else {
                            woalsae.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        if ("".equals(junsae.getText().toString().trim())) {
                            junsae.requestFocus();
                        } else {
                            woalsae.requestFocus();
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        junsae.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!"".equals(title.getText().toString().trim()) && !"".equals(address.getText().toString().trim()) &&
                        !"".equals(address2.getText().toString().trim()) && !"".equals(damdang.getText().toString().trim()) &&
                        !"".equals(damdang_phone.getText().toString().trim()) && isCategory) {

                    next.setTextColor(getResources().getColor(R.color.baseColor));

                } else {
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }

                if ("".equals(junsae.getText().toString().trim())) {
                    junsae_unit.setVisibility(View.GONE);
                } else {
                    junsae_unit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        junsae.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        if ("".equals(woalsae.getText().toString().trim())) {
                            woalsae.requestFocus();
                        } else {
                            bojeung.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        if ("".equals(woalsae.getText().toString().trim())) {
                            woalsae.requestFocus();
                        } else {
                            bojeung.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        if ("".equals(woalsae.getText().toString().trim())) {
                            woalsae.requestFocus();
                        } else {
                            bojeung.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        if ("".equals(woalsae.getText().toString().trim())) {
                            woalsae.requestFocus();
                        } else {
                            bojeung.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        if ("".equals(woalsae.getText().toString().trim())) {
                            woalsae.requestFocus();
                        } else {
                            bojeung.requestFocus();
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        bojeung.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals(bojeung.getText().toString().trim())) {
                    bojeung_unit.setVisibility(View.GONE);
                } else {
                    bojeung_unit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        bojeung.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        if ("".equals(geunri.getText().toString().trim())) {
                            geunri.requestFocus();
                        } else {
                            gaunri.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        if ("".equals(geunri.getText().toString().trim())) {
                            geunri.requestFocus();
                        } else {
                            gaunri.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        if ("".equals(geunri.getText().toString().trim())) {
                            geunri.requestFocus();
                        } else {
                            gaunri.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        if ("".equals(geunri.getText().toString().trim())) {
                            geunri.requestFocus();
                        } else {
                            gaunri.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        if ("".equals(geunri.getText().toString().trim())) {
                            geunri.requestFocus();
                        } else {
                            gaunri.requestFocus();
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        woalsae.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!"".equals(title.getText().toString().trim()) && !"".equals(address.getText().toString().trim()) &&
                        !"".equals(address2.getText().toString().trim()) && !"".equals(damdang.getText().toString().trim()) &&
                        !"".equals(damdang_phone.getText().toString().trim()) && isCategory) {

                    next.setTextColor(getResources().getColor(R.color.baseColor));
                } else {
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }

                if ("".equals(woalsae.getText().toString().trim())) {
                    woalsae_unit.setVisibility(View.GONE);
                } else {
                    woalsae_unit.setVisibility(View.VISIBLE);
                }

                if (s.length() > 0) {
                    bojeung.setEnabled(true);
                } else {
                    bojeung.setEnabled(false);
                    bojeung.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        woalsae.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        if ("".equals(bojeung.getText().toString().trim())) {
                            bojeung.requestFocus();
                        } else {
                            geunri.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        if ("".equals(bojeung.getText().toString().trim())) {
                            bojeung.requestFocus();
                        } else {
                            geunri.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        if ("".equals(bojeung.getText().toString().trim())) {
                            bojeung.requestFocus();
                        } else {
                            geunri.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        if ("".equals(bojeung.getText().toString().trim())) {
                            bojeung.requestFocus();
                        } else {
                            geunri.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        if ("".equals(bojeung.getText().toString().trim())) {
                            bojeung.requestFocus();
                        } else {
                            geunri.requestFocus();
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        geunri.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals(geunri.getText().toString().trim())) {
                    geunri_unit.setVisibility(View.GONE);
                } else {
                    geunri_unit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        geunri.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        if ("".equals(gaunri.getText().toString().trim())) {
                            gaunri.requestFocus();
                        } else {
                            door_lock1.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        if ("".equals(gaunri.getText().toString().trim())) {
                            gaunri.requestFocus();
                        } else {
                            door_lock1.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        if ("".equals(gaunri.getText().toString().trim())) {
                            gaunri.requestFocus();
                        } else {
                            door_lock1.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        if ("".equals(gaunri.getText().toString().trim())) {
                            gaunri.requestFocus();
                        } else {
                            door_lock1.requestFocus();
                        }
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        if ("".equals(gaunri.getText().toString().trim())) {
                            gaunri.requestFocus();
                        } else {
                            door_lock1.requestFocus();
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        gaunri.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals(gaunri.getText().toString().trim())) {
                    gaunri_unit.setVisibility(View.GONE);
                } else {
                    gaunri_unit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        gaunri.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        door_lock1.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        door_lock1.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        door_lock1.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        door_lock1.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        door_lock1.requestFocus();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        door_lock1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        door_lock2.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        door_lock2.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        door_lock2.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        door_lock2.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        door_lock2.requestFocus();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        door_lock2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        damdang.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        damdang.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        damdang.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        damdang.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        damdang.requestFocus();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        damdang.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        damdang_phone.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        damdang_phone.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        damdang_phone.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        damdang_phone.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        damdang_phone.requestFocus();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        damdang_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        finish_date.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_GO:
                        finish_date.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        finish_date.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        finish_date.requestFocus();
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        finish_date.requestFocus();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        for (int i = 0; i < imageView.length; i++) {
            int res1 = getResources().getIdentifier("imageview_con" + i, "id", getPackageName());
            int res2 = getResources().getIdentifier("imageview" + i, "id", getPackageName());
            int res3 = getResources().getIdentifier("close_imageview" + i, "id", getPackageName());
            imageview_con[i] = (FrameLayout) findViewById(res1);
            imageView[i] = (ImageView) findViewById(res2);
            closeImageView[i] = (ImageView) findViewById(res3);
            final int finalI = i;
            imageView[finalI].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WriteActivity.this, InputImagePathActivity.class);
                    intent.putExtra("path", imagePath.get(finalI));
                    startActivityForResult(intent, 900);
                }
            });
            closeImageView[finalI].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    totalImageCount--;
                    next.setTextColor(getResources().getColor(R.color.baseColor));
                    if (totalImageCount <= 0) {
                        image_con.setVisibility(View.VISIBLE);
                    }
                    imageview_con[totalImageCount].setVisibility(View.GONE);
                    imageView[totalImageCount].setImageBitmap(null);
                    imagePath.remove(finalI);
                    if (getImageList.size() > 0 && finalI < getImageList.size()) {
                        getImageCount--;
                        getImageList.remove(finalI);
                    }
                    if (addImageCount > 0) {
                        if (finalI > getImageList.size()) {
                            addImageCount--;
                            originalBitmap.remove(finalI - getImageCount);
                            resizeBitmap.remove(finalI - getImageCount);
                        }
                    }
                    for (int i = finalI; i < totalImageCount; i++) {
                        imageView[i].setImageBitmap(null);
                        Glide.with(WriteActivity.this).load(imagePath.get(i)).crossFade().bitmapTransform(new CenterCrop(WriteActivity.this), new RoundedCornersTransformation(WriteActivity.this, dpToPx(11), 0)).into(imageView[i]);
                    }
                }
            });
        }
        title_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        dong_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dong.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        ho_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ho.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        now_floor_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                now_floor.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        all_floor_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_floor.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        address2_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address2.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        area1_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                area1.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        area2_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                area2.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        maemae_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maemae.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        junsae_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                junsae.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        bojeung_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bojeung.requestFocus();

                if (bojeung.isEnabled()) {
                    ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
                } else {
                    hideKeyboard();
                }
            }
        });
        woalsae_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                woalsae.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        geunri_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geunri.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        gaunri_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gaunri.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        door_lock1_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                door_lock1.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        door_lock2_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                door_lock2.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        damdang_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                damdang.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        damdang_phone_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                damdang_phone.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        sangse_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sangse.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
            }
        });
        next_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.callOnClick();
            }
        });
        finish_date_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_date.callOnClick();
            }
        });
        finish_date_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_date.callOnClick();
            }
        });
        finish_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ipm.isAcceptingText()) {
                    ipm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                datePickerDialog = new DatePickerDialog(WriteActivity.this, "1");
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                if ("선택".equals(spinner_text.getText().toString()) || "선택" == spinner_text.getText().toString()) {
                    oneBtnDialog = new OneBtnDialog(WriteActivity.this, "카테고리를 선택해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if ("".equals(title.getText().toString()) || "" == title.getText().toString()) {
                    oneBtnDialog = new OneBtnDialog(WriteActivity.this, "제목을 입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if ("".equals(address.getText().toString()) || "" == address.getText().toString()) {
                    oneBtnDialog = new OneBtnDialog(WriteActivity.this, "주소를 선택해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if ("".equals(address2.getText().toString()) || "" == address2.getText().toString()) {
                    oneBtnDialog = new OneBtnDialog(WriteActivity.this, "상세주소를 입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if ("".equals(maemae.getText().toString()) && "".equals(junsae.getText().toString()) && "".equals(woalsae.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(WriteActivity.this, "매매, 전세, 월세 중\n하나는 입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if ("".equals(damdang.getText().toString()) || "" == damdang.getText().toString()) {
                    oneBtnDialog = new OneBtnDialog(WriteActivity.this, "담당자를 입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if ("".equals(damdang_phone.getText().toString()) || "" == damdang_phone.getText().toString()) {
                    oneBtnDialog = new OneBtnDialog(WriteActivity.this, "담당자 전화번호를\n입력해 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if (!checkPhoneNumber(damdang_phone.getText().toString())) {
                    oneBtnDialog = new OneBtnDialog(WriteActivity.this, "전화번호 형식을\n맞춰 주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
                if (getIdx != null) {
                    progressDialog = new ProgressDialog(WriteActivity.this, "수정 중 입니다.\n잠시만 기다려 주세요 !");
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                } else {
//                    progressDialog = new ProgressDialog(WriteActivity.this, "매물 등록 중 입니다.\n잠시만 기다려 주세요 !");
//                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
                    nextDialog = new NextDialog(WriteActivity.this);
                    nextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    nextDialog.setCancelable(false);
                    nextDialog.show();
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
                                Log.i("매물등록", " runOnUiThread");
                                SharedPreferences get_token = getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                                final String getToken = get_token.getString("Token", "");
                                String url = null;

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
                                                url = UrlManager.getBaseUrl() + "item/update";

                                                FormBody.Builder formBuilder = new FormBody.Builder()
                                                        .add("idx", getIdx)
                                                        .add("type", getType)
                                                        .add("name", title.getText().toString().trim())
                                                        .add("lat", la)
                                                        .add("lng", lo)
                                                        .add("area1", area1.getText().toString().trim())
                                                        .add("area2", area2.getText().toString().trim())
                                                        .add("addr1", address.getText().toString().trim())
                                                        .add("addr2", address2.getText().toString().trim())
                                                        .add("maemae", maemae.getText().toString().trim().replaceAll(",", ""))
                                                        .add("jense", junsae.getText().toString().trim().replaceAll(",", ""))
                                                        .add("bojeung", bojeung.getText().toString().trim().replaceAll(",", ""))
                                                        .add("wallse", woalsae.getText().toString().trim().replaceAll(",", ""))
                                                        .add("geunri", geunri.getText().toString().trim().replaceAll(",", ""))
                                                        .add("gaunri", gaunri.getText().toString().trim().replaceAll(",", ""))
                                                        .add("pass1", door_lock1.getText().toString().trim())
                                                        .add("pass2", door_lock2.getText().toString().trim())
                                                        .add("admin_name", damdang.getText().toString().trim())
                                                        .add("admin_phone", damdang_phone.getText().toString().trim().replaceAll("-", "").replaceAll(" ", ""))
                                                        .add("memo", sangse.getText().toString().trim())
                                                        .add("enddate", finish_date.getText().toString().trim())
                                                        .add("dong", dong.getText().toString().trim())
                                                        .add("ho", ho.getText().toString().trim())
                                                        .add("nfloor", now_floor.getText().toString().trim())
                                                        .add("afloor", all_floor.getText().toString().trim());
                                                file.clear();
                                                for (int i = 0; i < addImageCount; i++) {
                                                    Uri fileUri = getImageUri(WriteActivity.this, resizeBitmap.get(i));
                                                    String filePath = getImageRealPathFromURI(WriteActivity.this.getContentResolver(), fileUri);
                                                    File makeFile = new File(filePath);
                                                    file.add(makeFile);
                                                    formBuilder.add("image[" + i + "]", file.get(i).toString());
                                                }
                                                for (int i = 0; i < getImageCount; i++) {
                                                    formBuilder.add("image_list[" + i + "]", getImageList.get(i));
                                                }
                                                Log.i("매물등록", " formBuilder " + formBuilder);
                                                RequestBody body = formBuilder.build();
                                                Request request = new Request.Builder()
                                                        .url(url)
                                                        .header("GHsoft-Agent", "" + getToken).header("User-Agent", "android")
                                                        .post(body)
                                                        .build();
                                                try {
                                                    Response response = client.newCall(request).execute();
                                                    String responseStr = response.body().string();
                                                    Log.i("매물등록", " responseStr " + responseStr);
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(responseStr);
                                                        Log.i("매물등록", " jsonObject " + jsonObject);

//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                progressDialog.dismiss();
//                                                            }
//                                                        });
                                                        for (int i = 0; i < addImageCount; i++) {
                                                            contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.ImageColumns.DATA + "=?", new String[]{file.get(i).toString()});
                                                            file.get(i).delete();
                                                        }

                                                        if (jsonObject.getBoolean("return")) {    //return이 true 면?

                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Intent intent = new Intent();
                                                                    intent.putExtra("la", la);
                                                                    intent.putExtra("lo", lo);
                                                                    setResult(777, intent);
                                                                    finish();
                                                                    Toast.makeText(WriteActivity.this, "수정을 완료 하였습니다.", Toast.LENGTH_LONG).show();
                                                                }
                                                            });

                                                        } else if (!jsonObject.getBoolean("return")) {

                                                            if ("pay".equals(jsonObject.getString("type"))) {
                                                                Toast.makeText(WriteActivity.this, "기간이 만료되었습니다.", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                                Intent intent = new Intent(WriteActivity.this, BillingListActivity.class);
                                                                startActivity(intent);
                                                            }

                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    oneBtnDialog = new OneBtnDialog(WriteActivity.this, "수정을 할 수 없습니다 !", "확인");
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
                                                url = UrlManager.getBaseUrl() + "item/add";
                                                FormBody.Builder formBuilder = new FormBody.Builder()
                                                        .add("type", getType)
                                                        .add("lat", la)
                                                        .add("lng", lo)
                                                        .add("name", title.getText().toString().trim())
                                                        .add("area1", area1.getText().toString().trim())
                                                        .add("area2", area2.getText().toString().trim())
                                                        .add("addr1", address.getText().toString().trim())
                                                        .add("addr2", address2.getText().toString().trim())
                                                        .add("maemae", maemae.getText().toString().trim().replaceAll(",", ""))
                                                        .add("jense", junsae.getText().toString().trim().replaceAll(",", ""))
                                                        .add("bojeung", bojeung.getText().toString().trim().replaceAll(",", ""))
                                                        .add("wallse", woalsae.getText().toString().trim().replaceAll(",", ""))
                                                        .add("geunri", geunri.getText().toString().trim().replaceAll(",", ""))
                                                        .add("gaunri", gaunri.getText().toString().trim().replaceAll(",", ""))
                                                        .add("pass1", door_lock1.getText().toString().trim())
                                                        .add("pass2", door_lock2.getText().toString().trim())
                                                        .add("admin_name", damdang.getText().toString().trim())
                                                        .add("admin_phone", damdang_phone.getText().toString().trim().replaceAll("-", "").replaceAll(" ", ""))
                                                        .add("memo", sangse.getText().toString().trim())
                                                        .add("enddate", finish_date.getText().toString().trim())
                                                        .add("dong", dong.getText().toString().trim())
                                                        .add("ho", ho.getText().toString().trim())
                                                        .add("nfloor", now_floor.getText().toString().trim())
                                                        .add("afloor", all_floor.getText().toString().trim());

                                                file.clear();
                                                for (int i = 0; i < addImageCount; i++) {
                                                    Uri fileUri = getImageUri(WriteActivity.this, resizeBitmap.get(i));
                                                    String filePath = getImageRealPathFromURI(WriteActivity.this.getContentResolver(), fileUri);
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
                                                    Log.i("매물등록", " responseStr " + responseStr);

                                                    try {
                                                        JSONObject jsonObject = new JSONObject(responseStr);
                                                        Log.i("매물등록", " jsonObject " + jsonObject);

//                                                        runOnUiThread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                progressDialog.dismiss();
//                                                            }
//                                                        });
                                                        for (int i = 0; i < addImageCount; i++) {
                                                            contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.ImageColumns.DATA + "=?", new String[]{file.get(i).toString()});
                                                            file.get(i).delete();
                                                        }

                                                        if (jsonObject.getBoolean("return")) {    //return이 true 면?

//                                                                    progressDialog.dismiss();
//                                                                    Intent intent = new Intent();
//                                                                    intent.putExtra("la", la);
//                                                                    intent.putExtra("lo", lo);
//                                                                    setResult(666, intent);
//                                                                    finish();
//                                                                    Toast.makeText(WriteActivity.this, "위치 등록을 완료 하였습니다.", Toast.LENGTH_LONG).show();



                                                        } else if (!jsonObject.getBoolean("return")) {

                                                            if ("pay".equals(jsonObject.getString("type"))) {
                                                                Toast.makeText(WriteActivity.this, "기간이 만료되었습니다.", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                                Intent intent = new Intent(WriteActivity.this, BillingListActivity.class);
                                                                startActivity(intent);
                                                            }

                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    oneBtnDialog = new OneBtnDialog(WriteActivity.this, "위치 등록을 할 수 없습니다 !", "확인");
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


//                                Map<String, Object> params = new HashMap<String, Object>();
//                                String url = null;
//                                if (getIdx != null) {
//                                    url = UrlManager.getBaseUrl() + "item/update";
//                                    Log.i("매물등록", " url " + url);
//                                    params.put("idx", getIdx);
//                                } else {
//                                    url = UrlManager.getBaseUrl() + "item/add";
//                                    Log.i("매물등록", " url " + url);
//                                }
//
//                                params.put("type", getType);
//                                params.put("name", title.getText().toString().trim());
//                                params.put("lat", la);
//                                params.put("lng", lo);
//                                params.put("area1", area1.getText().toString().trim());
//                                params.put("area2", area2.getText().toString().trim());
//                                params.put("addr1", address.getText().toString().trim());
//                                params.put("addr2", address2.getText().toString().trim());
//                                params.put("maemae", maemae.getText().toString().trim().replaceAll(",", ""));
//                                params.put("jense", junsae.getText().toString().trim().replaceAll(",", ""));
//                                params.put("bojeung", bojeung.getText().toString().trim().replaceAll(",", ""));
//                                params.put("wallse", woalsae.getText().toString().trim().replaceAll(",", ""));
//                                params.put("geunri", geunri.getText().toString().trim().replaceAll(",", ""));
//                                params.put("gaunri", gaunri.getText().toString().trim().replaceAll(",", ""));
//                                params.put("pass1", door_lock1.getText().toString().trim());
//                                params.put("pass2", door_lock2.getText().toString().trim());
//                                params.put("admin_name", damdang.getText().toString().trim());
//                                params.put("admin_phone", damdang_phone.getText().toString().trim().replaceAll("-", "").replaceAll(" ", ""));
//                                params.put("memo", sangse.getText().toString().trim());
//                                params.put("enddate", finish_date.getText().toString().trim());
//                                params.put("dong", dong.getText().toString().trim());
//                                params.put("ho", ho.getText().toString().trim());
//                                params.put("nfloor", now_floor.getText().toString().trim());
//                                params.put("afloor", all_floor.getText().toString().trim());
//                                file.clear();
//
//                                for (int i = 0; i < addImageCount; i++){
//                                    Uri fileUri = getImageUri(WriteActivity.this, resizeBitmap.get(i));
//                                    String filePath = getImageRealPathFromURI(WriteActivity.this.getContentResolver(), fileUri);
//                                    File makeFile = new File(filePath);
//                                    file.add(makeFile);
//                                    Log.i("FILE", "" + makeFile);
//                                    params.put("image["+i+"]", file.get(i));
//
//                                }
//                                for (int i = 0; i < getImageCount; i++){
//                                    params.put("image_list["+i+"]", getImageList.get(i));
//                                }
//                                Log.i("매물등록", " params " + params);
//                                aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
//                                    @Override
//                                    public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
//                                        Log.i("매물등록", " jsonObject " + jsonObject.toString());
//                                        Log.i("매물등록", " status " + status);
//
//                                        for (int i = 0; i < addImageCount; i++){
//                                            contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.ImageColumns.DATA + "=?" , new String[]{ file.get(i).toString() });
//                                            file.get(i).delete();
//                                        }
//                                        try {
//                                            if (jsonObject.getBoolean("return")) {
//                                                if (getIdx != null) {
//                                                    Intent intent = new Intent();
//                                                    intent.putExtra("la", la);
//                                                    intent.putExtra("lo", lo);
//                                                    setResult(777, intent);
//                                                    finish();
//                                                    Toast.makeText(WriteActivity.this, "수정을 완료 하였습니다." , Toast.LENGTH_LONG).show();
//                                                } else {
//                                                    progressDialog.dismiss();
//                                                    Intent intent = new Intent();
//                                                    intent.putExtra("la", la);
//                                                    intent.putExtra("lo", lo);
//                                                    setResult(666, intent);
//                                                    finish();
//                                                    Toast.makeText(WriteActivity.this, "위치 등록을 완료 하였습니다." , Toast.LENGTH_LONG).show();
//                                                }
//                                            } else if (!jsonObject.getBoolean("return")) {
//
//                                                if("pay".equals(jsonObject.getString("type"))){
//                                                    Toast.makeText(WriteActivity.this, "기간이 만료되었습니다.", Toast.LENGTH_SHORT).show();
//                                                    finish();
//                                                    Intent intent = new Intent(WriteActivity.this, BillingListActivity.class);
//                                                    startActivity(intent);
//                                                }
//
//                                                if (getIdx != null) {
//                                                    oneBtnDialog = new OneBtnDialog(WriteActivity.this, "수정을 할 수 없습니다 !", "확인", "yes");
//                                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                                    oneBtnDialog.setCancelable(false);
//                                                    oneBtnDialog.show();
//                                                } else {
//                                                    oneBtnDialog = new OneBtnDialog(WriteActivity.this, "위치 등록을 할 수 없습니다 !", "확인", null);
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
        if (getIdx != null) {
            toolbar_title.setText("수정");
            if ("1".equals(getType)) {
                spinner_text.setText("원룸");
            } else if ("2".equals(getType)) {
                spinner_text.setText("투/쓰리룸");
            } else if ("3".equals(getType)) {
                spinner_text.setText("아파트");
            } else if ("4".equals(getType)) {
                spinner_text.setText("사무실");
            } else if ("5".equals(getType)) {
                spinner_text.setText("주택");
            } else if ("6".equals(getType)) {
                spinner_text.setText("상가");
            }

            title.setText(getTitle);
            title.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!title.equals(getTitle)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            address.setText(getAddress);
            address.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!address.equals(getAddress)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            dong.setText(getDong);
            if ("null".equals(getDong)) {
                dong.setText("");
            }
            dong.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!dong.equals(getDong)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            ho.setText(getHo);
            if ("null".equals(getHo)) {
                ho.setText("");
            }
            ho.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!ho.equals(getHo)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            now_floor.setText(getNowFloor);
            if ("null".equals(getNowFloor)) {
                now_floor.setText("");
            }
            now_floor.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!now_floor.equals(getNowFloor)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            all_floor.setText(getAllFloor);
            if ("null".equals(getAllFloor)) {
                all_floor.setText("");
            }
            all_floor.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!all_floor.equals(getAllFloor)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            address2.setText(getAddress2);
            address2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!address2.equals(getAddress2)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            area1.setText(getArea);
            if ("null".equals(getArea)) {
                area1.setText("");
            }
            area1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!area1.equals(getArea)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            area2.setText(getArea2);
            if ("null".equals(getArea2)) {
                area2.setText("");
            }
            area2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!area2.equals(getArea2)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            maemae.setText(getMaemae);
            maemae.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!maemae.equals(getMaemae)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            junsae.setText(getJunsae);
            junsae.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!junsae.equals(getJunsae)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            woalsae.setText(getWoalsae);
            woalsae.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!woalsae.equals(getWoalsae)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                        bojeung.setEnabled(true);
                    }
                    if (s.length() > 0) {
                        bojeung.setEnabled(true);
                    } else {
                        bojeung.setEnabled(false);
                        bojeung.setText("");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            bojeung.setText(getBojeung);
            bojeung.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!bojeung.equals(getBojeung)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            geunri.setText(getGeunri);
            geunri.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!geunri.equals(getGeunri)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            gaunri.setText(getGaunri);
            gaunri.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!gaunri.equals(getGaunri)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            door_lock1.setText(getDoor_lock);
            door_lock1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!door_lock1.equals(getDoor_lock)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            door_lock2.setText(getDoor_lock2);
            door_lock2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!door_lock2.equals(getDoor_lock2)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            damdang.setText(getDamdang);
            damdang.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!damdang.equals(getDamdang)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            damdang_phone.setText(getDamdang_phone);
            damdang_phone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!damdang_phone.equals(getDamdang_phone)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            finish_date.setText(getFinishDate);
            if ("null".equals(getFinishDate)) {
                finish_date.setText("");
            } else if ("0000-00-00".equals(getFinishDate)) {
                finish_date.setText("");
            }
            finish_date.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!finish_date.equals(getFinishDate)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            sangse.setText(getSangse);
            if ("null".equals(getSangse)) {
                sangse.setText("");
            }
            sangse.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!sangse.equals(getSangse)) {
                        next.setTextColor(getResources().getColor(R.color.baseColor));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            if (!"null".equals(getImages)) {
                image_con.setVisibility(View.VISIBLE);
                final String[] images = getImages.split(",");
                totalImageCount = images.length;
                getImageCount = images.length;

                Log.i("INPUTACTIVITY", " getImages " + getImages);
                Log.i("INPUTACTIVITY", " totalImageCount " + totalImageCount);
                Log.i("INPUTACTIVITY", " images.length " + images.length);

                if (getImageCount != images.length) {
                    next.setTextColor(getResources().getColor(R.color.baseColor));
                }

                for (int i = 0; i < images.length; i++) {
                    imagePath.add(i, UrlManager.getBaseUrl() + "uploads/images/origin/" + images[i]);
                    getImageList.add(i, images[i]);
                    imageview_con[i].setVisibility(View.VISIBLE);
                    Glide.with(WriteActivity.this).load(UrlManager.getBaseUrl() + "uploads/images/origin/" + images[i]).crossFade().bitmapTransform(new CenterCrop(WriteActivity.this), new RoundedCornersTransformation(WriteActivity.this, dpToPx(11), 0)).into(imageView[i]);
                }

            }

        } else {
            toolbar_title.setText("등록");
        }
    }

    TextWatcher textWatcherTitle = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!"".equals(address.getText().toString().trim()) && !"".equals(address2.getText().toString().trim())
                    && !"".equals(damdang.getText().toString().trim()) && !"".equals(damdang_phone.getText().toString().trim()) && isCategory) {

                if (!"".equals(maemae.getText().toString().trim()) || !"".equals(junsae.getText().toString().trim()) || !"".equals(woalsae.getText().toString().trim())) {

                    next.setTextColor(getResources().getColor(R.color.baseColor));
                } else {
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
            } else {
                next.setTextColor(getResources().getColor(R.color.grayColor));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher textWatcherAddress = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!"".equals(title.getText().toString().trim()) && !"".equals(address2.getText().toString().trim())
                    && !"".equals(damdang.getText().toString().trim()) && !"".equals(damdang_phone.getText().toString().trim()) && isCategory) {

                if (!"".equals(maemae.getText().toString().trim()) || !"".equals(junsae.getText().toString().trim()) || !"".equals(woalsae.getText().toString().trim())) {

                    next.setTextColor(getResources().getColor(R.color.baseColor));
                } else {
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
            } else {
                next.setTextColor(getResources().getColor(R.color.grayColor));
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher textWatcherAddDetail = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!"".equals(title.getText().toString().trim()) && !"".equals(address.getText().toString().trim())
                    && !"".equals(damdang.getText().toString().trim()) && !"".equals(damdang_phone.getText().toString().trim()) && isCategory) {

                if (!"".equals(maemae.getText().toString().trim()) || !"".equals(junsae.getText().toString().trim()) || !"".equals(woalsae.getText().toString().trim())) {

                    next.setTextColor(getResources().getColor(R.color.baseColor));
                } else {
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
            } else {
                next.setTextColor(getResources().getColor(R.color.grayColor));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher textWatcherManager = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!"".equals(title.getText().toString().trim()) && !"".equals(address.getText().toString().trim())
                    && !"".equals(address2.getText().toString().trim()) && !"".equals(damdang_phone.getText().toString().trim()) && isCategory) {

                if (!"".equals(maemae.getText().toString().trim()) || !"".equals(junsae.getText().toString().trim()) || !"".equals(woalsae.getText().toString().trim())) {

                    next.setTextColor(getResources().getColor(R.color.baseColor));
                } else {
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
            } else {
                next.setTextColor(getResources().getColor(R.color.grayColor));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher textWatcherManagerTel = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!"".equals(title.getText().toString().trim()) && !"".equals(address.getText().toString().trim())
                    && !"".equals(address2.getText().toString().trim()) && !"".equals(damdang.getText().toString().trim()) && isCategory) {

                if (!"".equals(maemae.getText().toString().trim()) || !"".equals(junsae.getText().toString().trim()) || !"".equals(woalsae.getText().toString().trim())) {

                    next.setTextColor(getResources().getColor(R.color.baseColor));
                } else {
                    next.setTextColor(getResources().getColor(R.color.grayColor));
                }
            } else {
                next.setTextColor(getResources().getColor(R.color.grayColor));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    //상태바 높이 구하기
    public int getStatusBarHeight() {
        int statusHeight = 0;
        int screenSizeType = (getApplicationContext().getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK);

        if (screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            int resourceId = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");

            if (resourceId > 0) {
                statusHeight = getApplicationContext().getResources().getDimensionPixelSize(resourceId);
            }
        }
        return statusHeight;
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        Log.i("WWW", "123");
        userIsInteracting = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("INPUTACTIVITY", "onActivityResult requestCode: " + requestCode);
        Log.i("INPUTACTIVITY", "onActivityResult resultCode : " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                Log.i("INPUTACTIVITY", " onActivityResult requestCode case 1:");
                break;
            case 700:
                if (resultCode == RESULT_CANCELED) {
                    return;
                } else {
                    try {
                        if (Build.VERSION.SDK_INT < 21) {
                            Uri imgUri = data.getData();
                            imagePath.add(getImageRealPathFromURI(WriteActivity.this.getContentResolver(), imgUri));
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
                    scrollView.smoothScrollTo(0, 0);
                    if (totalImageCount < 6) {
                        imageview_con[totalImageCount].setVisibility(View.VISIBLE);

                    }
                    Glide.with(WriteActivity.this).load(imagePath.get(totalImageCount)).crossFade().bitmapTransform(new CenterCrop(WriteActivity.this),new RoundedCornersTransformation(WriteActivity.this, dpToPx(11), 0)).into(imageView[totalImageCount]);
                    totalImageCount++;
                    addImageCount++;
                }
                break;
            case 800:
                if (resultCode == RESULT_CANCELED) {
                    return;
                } else {
                    try {
                        Uri imgUri = data.getData();
                        imagePath.add(getImageRealPathFromURI(WriteActivity.this.getContentResolver(), imgUri));
                        ExifInterface exif = new ExifInterface(imagePath.get(totalImageCount));
                        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        originalBitmap.add(rotate(BitmapFactory.decodeFile(imagePath.get(totalImageCount)), exifDegree));
                        resizeBitmap.add(rotate(resizeBitmap(imagePath.get(totalImageCount), 1080, 1920), exifDegree));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image_con.setVisibility(View.VISIBLE);
                    if (totalImageCount < 6) {
                        imageview_con[totalImageCount].setVisibility(View.VISIBLE);
                    }
                    Glide.with(WriteActivity.this).load(imagePath.get(totalImageCount)).crossFade().bitmapTransform(new CenterCrop(WriteActivity.this), new RoundedCornersTransformation(WriteActivity.this, dpToPx(11), 0)).into(imageView[totalImageCount]);
                    totalImageCount++;
                    addImageCount++;
                    next.setTextColor(getResources().getColor(R.color.baseColor));
                }
                break;
            case 900:
                break;
            case RESULT_CANCELED:
                break;
        }
        switch (resultCode) {
            case 1:
                Log.i("INPUTACTIVITY", " onActivityResult resultCode case 1:");
                break;
            case RESULT_CANCELED:
                break;
            case 666:
                address.setText(data.getStringExtra("addr"));
                la = data.getStringExtra("la");
                lo = data.getStringExtra("lo");
                dong.requestFocus();
                ipm.toggleSoftInput(ipm.SHOW_FORCED, ipm.HIDE_IMPLICIT_ONLY);
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
//                    if ("yes".equals(modify)) {
//                        progressDialog.dismiss();
//                    }
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
                    if (Build.VERSION.SDK_INT > 21) {
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

    public class NextDialog extends Dialog {
        NextDialog nextDialog = this;
        Context context;

        public NextDialog(final Context context) {
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
            title1.setText("동일한 주소에 추가 등록하시겠습니까?");
            btn1.setText("아니요");
            btn1.setTextColor(Color.parseColor("#7199ff"));
            btn2.setText("네");

            btn1.setOnClickListener(new View.OnClickListener() {    //아니요
                @Override
                public void onClick(View v) {
                    nextDialog.dismiss();
                    progressDialog = new ProgressDialog(WriteActivity.this, "매물 등록 중 입니다.\n잠시만 기다려 주세요 !");
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Intent intent = new Intent();
                    intent.putExtra("la", la);
                    intent.putExtra("lo", lo);
                    setResult(666, intent);
                    finish();
                    Toast.makeText(WriteActivity.this, "위치 등록을 완료 하였습니다.", Toast.LENGTH_LONG).show();

                    progressDialog.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {   //네
                @Override
                public void onClick(View v) {
                    nextDialog.dismiss();
//                    Intent intent = new Intent(WriteActivity.this, WriteActivity.class);
//                    intent.putExtra("type", getType);
//                    intent.putExtra("addr", addr);
//                    intent.putExtra("la", la);
//                    intent.putExtra("lo", lo);
//                    startActivityForResult(intent, 666);
//                    finish();

                    //스크롤 위로

                    List<EditText> clearText =
                            Arrays.asList(title, dong, ho, now_floor, all_floor, address2, area1, area2, maemae, junsae, bojeung, woalsae, geunri, gaunri, door_lock1, door_lock2, damdang, damdang_phone, sangse);
                    for (int i = 0; i < clearText.size(); i++) {
                        clearText.get(i).setText("");
                    }
                    finish_date.setText("");
                    scrollView.smoothScrollTo(0, 0);
                    spinner_text.requestFocus();
                    imagePath.clear();

                    for (int x = 0; x < totalImageCount; x++) {
                        imageview_con[x].setVisibility(View.GONE);
                        imageView[x].setImageBitmap(null);
                    }

                    totalImageCount = 0;

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
                        finish_date.setText(datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
                    } else if ("2".equals(type)) {
                        finish_date.setText(datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
                    }
                }
            });
        }
    }
}
