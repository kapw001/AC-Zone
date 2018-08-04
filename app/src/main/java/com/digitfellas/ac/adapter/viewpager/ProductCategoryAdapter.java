package com.digitfellas.ac.adapter.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.digitfellas.ac.R;
import com.digitfellas.ac.data.model.response.createorder.Category;
import com.digitfellas.ac.data.model.response.createorder.ProductCategory;
import com.google.gson.Gson;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class ProductCategoryAdapter extends ArrayAdapter<ProductCategory> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<ProductCategory> items;

    public ProductCategoryAdapter(@NonNull Context context, @NonNull List<ProductCategory> objects) {
        super(context, android.R.layout.simple_spinner_item,objects);
//        super(context, R.layout.row_item_spinner, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        items = objects;

    }

    public void upadetProduct(List<ProductCategory> items){

        addAll(items);
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
        ProductCategory category = items.get(position);

        item_view.setText(category.getCategoryName());

//        Log.e(TAG, "createItemView: "+new Gson().toJson(category));

        return view;
    }

    private View dropItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(R.layout.row_item_spinner, parent, false);

//        view.setBackgroundColor(Color.WHITE);

        TextView item_view = (TextView) view.findViewById(R.id.spinner_item);
        item_view.setTextColor(Color.BLACK);
        item_view.setPadding(16,26,0,26);

        ProductCategory category = items.get(position);

        item_view.setText(category.getCategoryName());

//        Log.e(TAG, "createItemView: "+new Gson().toJson(category));

        return view;
    }
}
