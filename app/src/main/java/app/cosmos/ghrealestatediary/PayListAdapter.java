package app.cosmos.ghrealestatediary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import app.cosmos.ghrealestatediary.DTO.AddressDTO;
import app.cosmos.ghrealestatediary.DTO.Payment;
import app.cosmos.ghrealestatediary.DTO.PoiDTO;

public class PayListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Payment> data;
    ListView listView;
    private int mLayout;
    AQuery aQuery = null;
    Gson gson;

    public PayListAdapter(Context context, int layout, ArrayList<Payment> data, ListView listView)
    {
        this.mContext = context;
        this.mLayout = layout;
        this.data = data;
        this.listView = listView;
        this.mInflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        gson = new Gson();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PayViewHolder payViewHolder;
        aQuery = new AQuery(mContext);

        if(convertView == null){
            convertView = mInflater.inflate(mLayout, parent, false);
            payViewHolder = new PayViewHolder();
            payViewHolder.pay_date = (TextView)convertView.findViewById(R.id.pay_date);
            payViewHolder.pay_price = (TextView)convertView.findViewById(R.id.pay_price);

            convertView.setTag(payViewHolder);
        }
        else{
            payViewHolder = (PayViewHolder)convertView.getTag();
        }

        payViewHolder.pay_date.setText(data.get(position).getPay_date());
        payViewHolder.pay_price.setText(data.get(position).getPay_price());

        return convertView;
    }

    public class PayViewHolder{
        public TextView pay_date;
        public TextView pay_price;
    }
}
