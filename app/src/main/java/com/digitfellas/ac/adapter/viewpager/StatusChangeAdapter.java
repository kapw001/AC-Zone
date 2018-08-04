package com.digitfellas.ac.adapter.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.digitfellas.ac.R;
import com.digitfellas.ac.data.model.response.CreateService;
import com.digitfellas.ac.data.model.response.ServiceManServicesDetails;

import java.util.List;

public class StatusChangeAdapter extends ArrayAdapter<ServiceManServicesDetails.Data.ServiceStatus> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<ServiceManServicesDetails.Data.ServiceStatus> items;

    public StatusChangeAdapter(@NonNull Context context, @NonNull List<ServiceManServicesDetails.Data.ServiceStatus> objects) {
        super(context, android.R.layout.simple_spinner_item,objects);
//        super(context, R.layout.row_item_spinner, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        items = objects;

    }

    public void updateBrand(List<ServiceManServicesDetails.Data.ServiceStatus> brandList) {

        addAll(brandList);
//        notifyDataSetChanged();

    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return dropItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(R.layout.row_item_spinner, parent, false);

        TextView item_view = (TextView) view.findViewById(R.id.spinner_item);
        item_view.setTextColor(Color.BLACK);
        ServiceManServicesDetails.Data.ServiceStatus category = items.get(position);
//        if (position==0){
//            item_view.setTextColor(ContextCompat.getColor(mContext,android.R.color.darker_gray));
//        }

//        item_view.setTextSize(12);

        item_view.setText(category.getSstatus());

//        Log.e(TAG, "createItemView: "+new Gson().toJson(category));

        return view;
    }

    private View dropItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(R.layout.row_item_spinner, parent, false);

//        view.setBackgroundColor(Color.WHITE);

        TextView item_view = (TextView) view.findViewById(R.id.spinner_item);
        item_view.setTextColor(Color.BLACK);
        item_view.setPadding(16,26,16,26);

        ServiceManServicesDetails.Data.ServiceStatus category = items.get(position);

        item_view.setText(category.getSstatus());

//        Log.e(TAG, "createItemView: "+new Gson().toJson(category));

        return view;
    }
}
