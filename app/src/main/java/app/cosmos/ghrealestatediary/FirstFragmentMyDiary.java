package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import static app.cosmos.ghrealestatediary.MyDiaryActivity.getAddr1;
import static app.cosmos.ghrealestatediary.MyDiaryActivity.getAddr2;
import static app.cosmos.ghrealestatediary.MyDiaryActivity.getEmail;
import static app.cosmos.ghrealestatediary.MyDiaryActivity.getIdx;
import static app.cosmos.ghrealestatediary.MyDiaryActivity.getName;
import static app.cosmos.ghrealestatediary.MyDiaryActivity.getPhone;
import static app.cosmos.ghrealestatediary.MyDiaryActivity.getRealestate_name;
import static app.cosmos.ghrealestatediary.MyDiaryActivity.getRealestate_tel;
import static app.cosmos.ghrealestatediary.MyDiaryActivity.type;


public class FirstFragmentMyDiary extends Fragment {

    AQuery aQuery = null;
    Context context;
    View view;
    String token;
    LinearLayout menu1_2_con;
    FrameLayout menu1_0_con, menu1_1_con, menu1_3_con, menu1_4_con, menu1_5_con;
    TextView menu1_0, menu1_1, menu1_2, menu1_3, menu1_4, menu1_5;
    OneBtnDialog oneBtnDialog;
    TwoBtnRecDialog twoBtnRecDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first_my_diary, container, false);
        SharedPreferences prefToken = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        token = prefToken.getString("Token", "");
        aQuery = new AQuery(context);
        menu1_0_con = (FrameLayout) view.findViewById(R.id.menu1_0_con);
        menu1_1_con = (FrameLayout) view.findViewById(R.id.menu1_1_con);
        menu1_2_con = (LinearLayout) view.findViewById(R.id.menu1_2_con);
        menu1_3_con = (FrameLayout) view.findViewById(R.id.menu1_3_con);
        menu1_4_con = (FrameLayout) view.findViewById(R.id.menu1_4_con);
        menu1_5_con = (FrameLayout) view.findViewById(R.id.menu1_5_con);
        menu1_0 = (TextView) view.findViewById(R.id.menu1_0);
        menu1_1 = (TextView) view.findViewById(R.id.menu1_1);
        menu1_2 = (TextView) view.findViewById(R.id.menu1_2);
        menu1_3 = (TextView) view.findViewById(R.id.menu1_3);
        menu1_4 = (TextView) view.findViewById(R.id.menu1_4);
        menu1_5 = (TextView) view.findViewById(R.id.menu1_5);

        menu1_0_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu1_0.callOnClick();
            }
        });
        menu1_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //그룹 관리

//                Toast.makeText(context, "서비스 준비 중입니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, GroupAcitivity.class);
                startActivityForResult(intent, 1);
            }
        });

        menu1_1_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu1_1.callOnClick();
            }
        });
        menu1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent intent = new Intent(context, MyProfileActivity.class);
                    startActivityForResult(intent, 1);

            }
        });
        menu1_2_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu1_2.callOnClick();
            }
        });
        menu1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                oneBtnDialog = new OneBtnDialog(context, "준비 중인 메뉴입니다.\n관리자에게 문의해주세요!\n(기타 > 문의하기)", "확인");
//                oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                oneBtnDialog.setCancelable(false);
//                oneBtnDialog.show();

//                if (getIdx != null) {
//                    if ("sns".equals(type)) {
//                        oneBtnDialog = new OneBtnDialog(context, "카카오 아이디는\n비밀번호 변경 할 수 없습니다 !", "확인");
//                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        oneBtnDialog.setCancelable(false);
//                        oneBtnDialog.show();
//                    } else if ("pass".equals(type)){
                        Intent intent = new Intent(context, PassCertifyActivity.class);
//                        intent.putExtra("idx", getIdx);
//                        intent.putExtra("email", getEmail);
//                        intent.putExtra("name", getName);
//                        intent.putExtra("phone", getPhone);
//                        intent.putExtra("address1", getAddr1);
//                        intent.putExtra("address2", getAddr2);
//                        intent.putExtra("realestate_name", getRealestate_name);
//                        intent.putExtra("realestate_tel", getRealestate_tel);
                        startActivityForResult(intent, 1);
//                    }
//                }
            }
        });
        SharedPreferences prefLoginChecked = context.getSharedPreferences("prefLoginChecked", Activity.MODE_PRIVATE);
        if ("sns".equals(prefLoginChecked.getString("type", ""))) {
            menu1_2_con.setVisibility(View.GONE);
        }
        menu1_3_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu1_3.callOnClick();
            }
        });
        menu1_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (getIdx != null) {
                    Intent intent = new Intent(context, PushActivity.class);
                    startActivityForResult(intent,1);
//                }
            }
        });
        menu1_4_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu1_4.callOnClick();
            }
        });
        menu1_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (getIdx != null) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=app.cosmos.ghrealestatediary");
                    sendIntent.setType("text/plain");
                    try {
                        startActivity(Intent.createChooser(sendIntent, "앱 공유하기"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
//            }
        });
        menu1_5_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu1_5.callOnClick();
            }
        });
        menu1_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (getIdx != null) {
                    twoBtnRecDialog = new TwoBtnRecDialog(context);
                    twoBtnRecDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    twoBtnRecDialog.setCancelable(false);
                    twoBtnRecDialog.show();
//                }
            }
        });
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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

    public class TwoBtnRecDialog extends Dialog {
        TwoBtnRecDialog twoBtnRecDialog = this;
        Context context;
        public TwoBtnRecDialog(final Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_two_btn_rec_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title = (TextView) findViewById(R.id.title);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            title.setText("추천인의 휴대폰 번호를\n입력해주세요.");
            btn1.setText("취소");
            btn2.setText("확인");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnRecDialog.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnRecDialog.dismiss();
                }
            });
        }
    }
}