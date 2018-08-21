package com.rto_driving_test_rajasthan.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rto_driving_test_rajasthan.Models.MainCat;
import com.rto_driving_test_rajasthan.R;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends ArrayAdapter<MainCat> {
    Context context;
    private ArrayList<MainCat> items;

    public CustomSpinnerAdapter(Context context,
                                int resourceId, ArrayList<MainCat> aritems) {
        super(context, resourceId, aritems);
        this.context = context;
        this.items=aritems;
    }
    private class ViewHolder {
        TextView itemName;
        TextView itemDropdown;
    }

    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public MainCat getItem(int position) {
//		Log.v("", "items.get("+position+")= "+items.get(position));
        return items.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
//		final BloodGroupPojo mytempojo = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.convert_spinner_item, null);
            holder = new ViewHolder();
            holder.itemName =  (TextView) convertView.findViewById(R.id.tv_item_name_drop_down);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(items.get(position).getCatName().length()<38)
        {
            holder.itemName.setText(items.get(position).getCatName());
        }
        else
        {
            holder.itemName.setText(items.get(position).getCatName().substring(0,38)+"...");
        }

        return convertView;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.result_spinner_item, null);
            holder = new ViewHolder();
            holder.itemDropdown =  (TextView) convertView.findViewById(R.id.tv_item_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemDropdown.setText(items.get(position).getCatName());
//        if(items.get(position).getCatName().length()<40)
//        {
//            holder.itemDropdown.setText(items.get(position).getCatName());
//        }
//        else
//        {
//            holder.itemDropdown.setText(items.get(position).getCatName().substring(0,40)+"...");
//        }

        return convertView;
    }

}