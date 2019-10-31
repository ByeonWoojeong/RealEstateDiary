package app.cosmos.ghrealestatediary;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static app.cosmos.ghrealestatediary.MyDiaryActivity.getEmail;
import static app.cosmos.ghrealestatediary.MyDiaryActivity.group;
import static app.cosmos.ghrealestatediary.MyDiaryActivity.group_idx;


public class SecondFragmentMyDiary extends Fragment {

    AQuery aQuery = null;
    Context context;
    View view;
    String token;
    LinearLayout menu2_1_con, menu2_2_con, menu2_3_con, menu2_4_con, menu2_5_con;
    TextView menu2_1, menu2_2, menu2_3, menu2_4, menu2_5;
    OneBtnDialog oneBtnDialog;
    TwoBtnCreateDialog twoBtnCreateDialog;
    TwoBtnLeaveDialog twoBtnLeaveDialog;
    TwoBtnRecDialog twoBtnRecDialog;

    boolean checkEmail(String email){
        Pattern pattern = Pattern.compile("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }

    boolean checkPhoneNumber(String number){
        boolean checkPhoneNumber = Pattern.matches("(01[016789])(\\d{3,4})(\\d{4})", number);
        return checkPhoneNumber;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_second_my_diary, container, false);
        SharedPreferences prefToken = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        token = prefToken.getString("Token", "");
        aQuery = new AQuery(context);
        menu2_1_con = (LinearLayout) view.findViewById(R.id.menu2_1_con);
        menu2_2_con = (LinearLayout) view.findViewById(R.id.menu2_2_con);
        menu2_3_con = (LinearLayout) view.findViewById(R.id.menu2_3_con);
        menu2_4_con = (LinearLayout) view.findViewById(R.id.menu2_4_con);
        menu2_5_con = (LinearLayout) view.findViewById(R.id.menu2_5_con);
        menu2_1 = (TextView) view.findViewById(R.id.menu2_1);
        menu2_2 = (TextView) view.findViewById(R.id.menu2_2);
        menu2_3 = (TextView) view.findViewById(R.id.menu2_3);
        menu2_4 = (TextView) view.findViewById(R.id.menu2_4);
        menu2_5 = (TextView) view.findViewById(R.id.menu2_5);
        menu2_1_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu2_1.callOnClick();
            }
        });
        menu2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(group_idx)) {
                    twoBtnCreateDialog = new TwoBtnCreateDialog(context);
                    twoBtnCreateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    twoBtnCreateDialog.setCancelable(false);
                    twoBtnCreateDialog.show();
                } else {
                    oneBtnDialog = new OneBtnDialog(context, "이미 그룹이 있습니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                }
            }
        });
        menu2_2_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu2_2.callOnClick();
            }
        });
        menu2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(group_idx)) {
                    oneBtnDialog = new OneBtnDialog(context, "그룹이 없습니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                } else {
                    if ("0".equals(group)) {
                        oneBtnDialog = new OneBtnDialog(context, "그룹장만 초대 가능합니다 !", "확인");
                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        oneBtnDialog.setCancelable(false);
                        oneBtnDialog.show();
                    } else {
                        twoBtnRecDialog = new TwoBtnRecDialog(context);
                        twoBtnRecDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        twoBtnRecDialog.setCancelable(false);
                        twoBtnRecDialog.show();
                    }
                }
            }
        });
        menu2_3_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu2_3.callOnClick();
            }
        });
        menu2_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(group_idx)) {
                    Intent intent = new Intent(context, GroupInviteActivity.class);
                    intent.putExtra("email", getEmail);
                    startActivityForResult(intent, 1);
                } else {
                    oneBtnDialog = new OneBtnDialog(context, "이미 그룹이 있습니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                }
            }
        });
        menu2_4_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu2_4.callOnClick();
            }
        });
        menu2_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(group_idx)) {
                    oneBtnDialog = new OneBtnDialog(context, "그룹이 없습니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                } else {
//                    Intent intent = new Intent(context, ListActivity.class);
//                    intent.putExtra("isGroup", group);
//                    startActivityForResult(intent, 1);
                }
            }
        });
        menu2_5_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu2_5.callOnClick();
            }
        });
        menu2_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(group_idx)) {
                    oneBtnDialog = new OneBtnDialog(context, "그룹이 없습니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                } else {
                    if ("0".equals(group)) {
                        twoBtnLeaveDialog = new TwoBtnLeaveDialog(context, "0");
                        twoBtnLeaveDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        twoBtnLeaveDialog.setCancelable(false);
                        twoBtnLeaveDialog.show();
                    } else {
                        twoBtnLeaveDialog = new TwoBtnLeaveDialog(context, "1");
                        twoBtnLeaveDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        twoBtnLeaveDialog.setCancelable(false);
                        twoBtnLeaveDialog.show();
                    }
                }
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

    public class TwoBtnCreateDialog extends Dialog {
        TwoBtnCreateDialog twoBtnCreateDialog = this;
        Context context;
        public TwoBtnCreateDialog(final Context context) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_two_btn_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title1 = (TextView) findViewById(R.id.title1);
            TextView title2 = (TextView) findViewById(R.id.title2);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            title1.setText("그룹을 생성하시겠습니까?");
            title2.setText("그룹원은 최대 10명까지\n가능합니다.");
            btn1.setText("취소");
            btn2.setText("확인");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnCreateDialog.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    String url = UrlManager.getBaseUrl() + "/group/make";
                    Map<String, Object> params = new HashMap<String, Object>();
                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            twoBtnCreateDialog.dismiss();
                            try {
                                if (jsonObject.getBoolean("return")) {
                                    ((MyDiaryActivity) getActivity()).infoRefresh();
                                    oneBtnDialog = new OneBtnDialog(context, "그룹이 생성 되었습니다.", "확인");
                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    oneBtnDialog.setCancelable(false);
                                    oneBtnDialog.show();
                                } else if (!jsonObject.getBoolean("return")) {
                                    oneBtnDialog = new OneBtnDialog(context, "이미 그룹이 있습니다 !", "확인");
                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    oneBtnDialog.setCancelable(false);
                                    oneBtnDialog.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
                }
            });
        }
    }

    public class TwoBtnLeaveDialog extends Dialog {
        TwoBtnLeaveDialog twoBtnLeaveDialog = this;
        Context context;
        public TwoBtnLeaveDialog(final Context context, final String group) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_two_btn_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title1 = (TextView) findViewById(R.id.title1);
            TextView title2 = (TextView) findViewById(R.id.title2);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            title1.setText("그룹을 탈퇴하시겠습니까?");
            if ("1".equals(group)) {
                title2.setText("그룹장이 탈퇴하면\n그룹이 해체됩니다.\n신중히 선택해주시기 바랍니다.");
            } else {
                title2.setVisibility(View.GONE);
            }
            btn1.setText("취소");
            btn2.setText("확인");
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    twoBtnLeaveDialog.dismiss();
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    String url = UrlManager.getBaseUrl() + "/group/leave";
                    Map<String, Object> params = new HashMap<String, Object>();
                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            ((MyDiaryActivity) getActivity()).infoRefresh();
                            twoBtnLeaveDialog.dismiss();
                            oneBtnDialog = new OneBtnDialog(context, "그룹을 탈퇴 하였습니다.", "확인");
                            oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            oneBtnDialog.setCancelable(false);
                            oneBtnDialog.show();
                        }
                    }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
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
            final TextView title = (TextView) findViewById(R.id.title);
            final EditText content = (EditText) findViewById(R.id.content);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            TextView btn2 = (TextView) findViewById(R.id.btn2);
            title.setText("그룹에 초대할 회원의\n휴대폰 번호를 입력해주세요.");
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
                    if ("".equals(content.getText().toString()) || "" == content.getText().toString()) {
                        oneBtnDialog = new OneBtnDialog(context, "이메일을 입력해 주세요 !", "확인");
                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        oneBtnDialog.setCancelable(false);
                        oneBtnDialog.show();
                        return;
                    }
//                    if (checkPhoneNumber(content.getText().toString())) {
//                        oneBtnDialog = new OneBtnDialog(context, "휴대폰 번호 형식을 맞춰주세요 !", "확인");
//                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                        oneBtnDialog.setCancelable(false);
//                        oneBtnDialog.show();
//                        return;
//                    }
                    if (getEmail.equals(content.getText().toString())) {
                        oneBtnDialog = new OneBtnDialog(context, "자신을 초대할 수 없습니다 !", "확인");
                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        oneBtnDialog.setCancelable(false);
                        oneBtnDialog.show();
                        return;
                    }
                    twoBtnRecDialog.dismiss();
                    SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
                    final String getToken = get_token.getString("Token", "");
                    String url = UrlManager.getBaseUrl() + "/group/invite";
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("phone", content.getText().toString());
                    Log.i("GROUPActivity", "phone " + content.getText().toString());
                    Log.i("GROUPActivity", "params " + params);
                    aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                            try {
                                if (jsonObject.getBoolean("return")) {
                                    oneBtnDialog = new OneBtnDialog(context, "그룹에 초대 하였습니다.", "확인");
                                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    oneBtnDialog.setCancelable(false);
                                    oneBtnDialog.show();
                                } else if (!jsonObject.getBoolean("return")) {
                                    if ("full".equals(jsonObject.getString("type"))) {
                                        oneBtnDialog = new OneBtnDialog(context, "인원이 꽉 찼습니다 !", "확인");
                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        oneBtnDialog.setCancelable(false);
                                        oneBtnDialog.show();
                                    } else {
                                        oneBtnDialog = new OneBtnDialog(context, "존재하지 않는 휴대폰 번호\n이거나, 이미 그룹에\n가입되어 있습니다 !", "확인");
                                        oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        oneBtnDialog.setCancelable(false);
                                        oneBtnDialog.show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.header("GHsoft-Agent", "" + getToken).header("User-Agent", "android"));
                }
            });
        }
    }
}