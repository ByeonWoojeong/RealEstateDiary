package app.cosmos.ghrealestatediary;

import java.text.DecimalFormat;

import android.graphics.Color;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;


public class HyphenTextWathcer implements TextWatcher {

    @SuppressWarnings("unused")
    private EditText mEditText;
    private String strAmount = "";

    public HyphenTextWathcer(EditText e) {
        this.mEditText = e;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (!s.toString().equals(strAmount)) {
            strAmount = makeStringComma(s.toString().replace(",", ""));
            mEditText.setText(strAmount);
            Editable e = mEditText.getText();
            Selection.setSelection(e, strAmount.length());
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    protected String makeStringComma(String str) {
        if (str.length() == 0) {
            return "";
        }
        long value = Long.parseLong(str);
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(value);
    }
}