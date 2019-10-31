package app.cosmos.ghrealestatediary;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;

import app.cosmos.ghrealestatediary.DTO.Address;


public class Addr1ListAdapter extends BaseAdapter{

    Context context;
    int layout;
    ArrayList<Address> data;
    GridView gridView;
    AQuery aQuery = null;
    int checkAccumulator;
    Addr1ListAdapter addr1ListAdapter = this;

    public Addr1ListAdapter(Context context, int layout, ArrayList<Address> data, GridView gridView) {
        this.context = context;
        this.layout = layout;
        this.data = data;
        this.gridView = gridView;
        checkAccumulator = 0;
    }

    public void countCheck(boolean isChecked) {
        checkAccumulator += isChecked ? 1 : -1 ;
    }

    public int getCountCheck() {
        return checkAccumulator;
    }

    public View getViewByPosition(int position, GridView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        if (position < firstListItemPosition || position > lastListItemPosition) {
            return  listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
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
                } else {
                    finalVh.list_con.setBackgroundColor(Color.parseColor("#ffffff"));
                    finalVh.name.setTextColor(Color.parseColor("#787878"));
                }
            }
        });
        vh.name.setText(item.getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0; i < addr1ListAdapter.getCount(); i++) {
                    CheckBox getCheckbox = (CheckBox) getViewByPosition(i, gridView).findViewById(R.id.checkbox);
                    getCheckbox.setChecked(false);
                }
                finalVh.checkbox.setChecked(true);
                SharedPreferences addr1CheckList = context.getSharedPreferences("addr1CheckList", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = addr1CheckList.edit();
                editor.clear();
                editor.putString("addr1", item.getName());
                editor.commit();
                Intent intent = new Intent(context, Addr2SelectActivity.class);
                intent.putExtra("name", item.getName());
                ((Activity)context).startActivityForResult(intent, 1);
            }
        });
        return view;
    }
}
