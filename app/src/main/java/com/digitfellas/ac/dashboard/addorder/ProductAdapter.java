package com.digitfellas.ac.dashboard.addorder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.digitfellas.ac.R;
import com.digitfellas.ac.data.model.request.Products;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductVH> {

    private List<Products> items;
    private ProductChangeCallBack changeCallBack;

    public ProductAdapter(ProductChangeCallBack changeCallBack, List<Products> items) {
        this.changeCallBack = changeCallBack;
        this.items = items;
    }

    public void updateItems(List<Products> items) {
        this.items = new ArrayList<>();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void updateItems(Products items, int position) {

        this.items.set(position, items);
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public ProductVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_product, parent, false);

        return new ProductVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVH holder, final int position) {

        final Products products = items.get(position);

        setValues(holder.mPoductName, products.getProduct_category_name() + "");
        setValues(holder.mSku, products.getSku());
        setValues(holder.mPrice, products.getPrice() + "");
        setValues(holder.mSubTotal, products.getSub_total() + "");
        setValues(holder.mQuantity, products.getQuantity() + "");

        holder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeCallBack != null) {
                    changeCallBack.editProduct(position, products);
//                    items.remove(position);
//                    notifyDataSetChanged();
                }
            }
        });

        holder.mDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (changeCallBack != null) {
                    changeCallBack.deleteProduct(position);
//                    items.remove(position);
//                    notifyDataSetChanged();
                }
            }
        });


    }

    private void setValues(TextView textView, String s) {

        textView.setText(s + " ");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ProductVH extends RecyclerView.ViewHolder {

        @BindView(R.id.sub_total)
        TextView mSubTotal;
        @BindView(R.id.quantity)
        TextView mQuantity;
        @BindView(R.id.price)
        TextView mPrice;
        @BindView(R.id.sku)
        TextView mSku;
        @BindView(R.id.product_name)
        TextView mPoductName;

        @BindView(R.id.delete_product)
        ImageButton mDeleteProduct;

        @BindView(R.id.edit)
        ImageButton mEdit;

        public ProductVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ProductChangeCallBack {

        void deleteProduct(int position);

        void editProduct(int position, Products products);

    }
}
