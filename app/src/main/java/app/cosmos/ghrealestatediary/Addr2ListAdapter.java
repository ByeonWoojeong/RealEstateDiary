package app.cosmos.ghrealestatediary;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import java.util.ArrayList;

import app.cosmos.ghrealestatediary.DTO.Address;


public class Addr2ListAdapter extends BaseAdapter{

    Context context;
    int layout;
    ArrayList<Address> data;
    GridView gridView;
    TextView next;
    AQuery aQuery = null;
    int checkAccumulator;

    public Addr2ListAdapter(Context context, int layout, ArrayList<Address> data, GridView gridView, TextView next) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.gridView = gridView;
        this.next = next;
        checkAccumulator = 0;
    }

    public void countCheck(boolean isChecked) {
        checkAccumulator += isChecked ? 1 : -1 ;
    }

    public int getCountCheck() {
        return checkAccumulator;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class ViewHolder{
        FrameLayout list_con;
        CheckBox checkbox;
        TextView name;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder vh = null;
        aQuery = new AQuery(context);

        if (view == null) {
            view = View.inflate(context, layout, null);
            vh = new ViewHolder();
            vh.list_con = (FrameLayout) view.findViewById(R.id.list_con);
            vh.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            vh.name = (TextView) view.findViewById(R.id.name);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        final Address item = data.get(i);
        final ViewHolder finalVh = vh;

        vh.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                countCheck(isChecked);
                if (isChecked) {
                    finalVh.list_con.setBackgroundColor(Color.parseColor("#7199ff"));
                    finalVh.name.setTextColor(Color.parseColor("#ffffff"));
                    SharedPreferences addr2CheckList = context.getSharedPreferences("addr2CheckList", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = addr2CheckList.edit();
                    editor.putString(""+i, item.getName());
                    editor.commit();
                } else {
                    finalVh.list_con.setBackgroundColor(Color.parseColor("#ffffff"));
                    finalVh.name.setTextColor(Color.parseColor("#787878"));
                    SharedPreferences addr2CheckList = context.getSharedPreferences("addr2CheckList", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = addr2CheckList.edit();
                    editor.remove(""+i);
                    editor.commit();
                }
            }
        });
        vh.name.setText(item.getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAccumulator >= 0) {
                    next.setTextColor(Color.parseColor("#7199ff"));
                }else{
                    next.setTextColor(Color.parseColor("#787878"));
                }

                if (finalVh.checkbox.isChecked()) {
                    finalVh.checkbox.setChecked(false);
                } else {
                    if (3 > checkAccumulator) {
                        finalVh.checkbox.setChecked(true);
                    }
                }
            }
        });
        return view;
    }
}
