package app.cosmos.ghrealestatediary;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.JsResult;
import android.widget.TextView;

/**
 * Created by admin on 2018-04-25.
 */

public class WebDialog {
    public Dialog alertDialog(Context context, String content, final JsResult result) {
        View customView = View.inflate(context, R.layout.popup_alert, null);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(customView);
        TextView tv_content = (TextView) customView.findViewById(R.id.tv_content);
        tv_content.setText(content);
        TextView bt_confirm = (TextView) customView.findViewById(R.id.bt_confirm);
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.confirm();
                dialog.dismiss();
            }
        });
        ViewGroup.LayoutParams size = dialog.getWindow().getAttributes();
        size.width = ViewGroup.LayoutParams.MATCH_PARENT;
        size.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) size);
        dialog.setCancelable(false);
        return dialog;
    }
    public Dialog confirmDialog(Context context, String content, final JsResult result) {
        View customView = View.inflate(context, R.layout.popup_alert, null);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(customView);
        TextView tv_content = (TextView) customView.findViewById(R.id.tv_content);
        tv_content.setText(content);
        TextView bt_confirm = (TextView) customView.findViewById(R.id.bt_confirm);
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.confirm();
                dialog.dismiss();
            }
        });
        TextView bt_cancel=(TextView)customView.findViewById(R.id.bt_cancel);
        bt_cancel.setVisibility(View.VISIBLE);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.cancel();
                dialog.dismiss();
            }
        });
        ViewGroup.LayoutParams size = dialog.getWindow().getAttributes();
        size.width = ViewGroup.LayoutParams.MATCH_PARENT;
        size.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) size);
        dialog.setCancelable(false);
        return dialog;
    }
}
