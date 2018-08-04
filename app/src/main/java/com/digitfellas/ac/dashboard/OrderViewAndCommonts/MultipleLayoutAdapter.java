package com.digitfellas.ac.dashboard.OrderViewAndCommonts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseAdapter;
import com.digitfellas.ac.base.VH;
import com.digitfellas.ac.data.model.response.CommentsCount;
import com.digitfellas.ac.data.model.response.OrderDetailsInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MultipleLayoutAdapter extends BaseAdapter<Object, RecyclerView.ViewHolder> {

    protected MultipleLayoutAdapter(List<Object> list) {
        super(list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (getValue(viewType) instanceof OrderDetailsInfo.Data.OrderDetail) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_product_first, parent, false);

            return new ViewHolderProduct(view);

        } else if (getValue(viewType) instanceof OrderDetailsInfo.Data.OrderComment) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comments, parent, false);

            return new ViewHolderComments(view);

        } else if (getValue(viewType) instanceof CommentsCount) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comments_count, parent, false);

            return new ViewHolderCommentsCount(view);

        }


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (getValue(position) instanceof OrderDetailsInfo.Data.OrderDetail) {

            OrderDetailsInfo.Data.OrderDetail detail = (OrderDetailsInfo.Data.OrderDetail) getList().get(position);

            ((ViewHolderProduct) holder).price.setText(detail.getOrderPrice() + "");
            ((ViewHolderProduct) holder).productName.setText(detail.getProductName() + "");
            ((ViewHolderProduct) holder).quantity.setText(detail.getOrderQuantity() + "");
            ((ViewHolderProduct) holder).sku.setText(detail.getSku() + "");
            ((ViewHolderProduct) holder).subTotal.setText(detail.getOrderSubTotal() + "");


        } else if (getValue(position) instanceof OrderDetailsInfo.Data.OrderComment) {
            OrderDetailsInfo.Data.OrderComment comment = (OrderDetailsInfo.Data.OrderComment) getList().get(position);

            ((ViewHolderComments) holder).description.setText(comment.getComment());
            ((ViewHolderComments) holder).name.setText(comment.getName());

        } else if (getValue(position) instanceof CommentsCount) {

            CommentsCount commentsCount = (CommentsCount) getList().get(position);

            ((ViewHolderCommentsCount) holder).commentscount.setText("Comments (" + commentsCount.getCount() + ")");

        }


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return getCount();
    }


    static class ViewHolderProduct extends VH {
        @BindView(R.id.product_name)
        TextView productName;
        @BindView(R.id.sku)
        TextView sku;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.quantity)
        TextView quantity;
        @BindView(R.id.sub_total)
        TextView subTotal;

        public ViewHolderProduct(View itemView) {
            super(itemView);
        }
    }

    static class ViewHolderComments extends VH {
        @BindView(R.id.image_view)
        ImageView imageView;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.description)
        TextView description;

        public ViewHolderComments(View itemView) {
            super(itemView);
        }
    }

    static class ViewHolderCommentsCount extends VH {
        @BindView(R.id.commentscount)
        TextView commentscount;

        public ViewHolderCommentsCount(View itemView) {
            super(itemView);
        }
    }


}

