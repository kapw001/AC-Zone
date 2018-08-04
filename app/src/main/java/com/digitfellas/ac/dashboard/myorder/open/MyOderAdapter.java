package com.digitfellas.ac.dashboard.myorder.open;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digitfellas.ac.R;

public class MyOderAdapter extends RecyclerView.Adapter<MyOderAdapter.ProductVH>{


    @NonNull
    @Override
    public ProductVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_my_order,parent,false);

        return new ProductVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ProductVH extends RecyclerView.ViewHolder{


        public ProductVH(View itemView) {
            super(itemView);
        }
    }
}
