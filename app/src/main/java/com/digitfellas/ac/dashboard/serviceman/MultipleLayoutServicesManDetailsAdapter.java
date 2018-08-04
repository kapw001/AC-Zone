package com.digitfellas.ac.dashboard.serviceman;

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
import com.digitfellas.ac.dashboard.customerservices.MyServiceDetailsActivity;
import com.digitfellas.ac.data.model.response.CommentsCount;
import com.digitfellas.ac.data.model.response.CustomerServicesDetails;
import com.digitfellas.ac.data.model.response.ServiceManServicesDetails;

import java.util.List;

import butterknife.BindView;

public class MultipleLayoutServicesManDetailsAdapter extends BaseAdapter<Object, RecyclerView.ViewHolder> {

    protected MultipleLayoutServicesManDetailsAdapter(List<Object> list) {
        super(list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (getValue(viewType) instanceof ServiceManServiceDetailsActivity.Data) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_myservice_details, parent, false);

            return new ViewHolderProduct(view);

        } else if (getValue(viewType) instanceof ServiceManServicesDetails.Data.ServiceComment) {

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


        if (getValue(position) instanceof ServiceManServiceDetailsActivity.Data) {

            ServiceManServiceDetailsActivity.Data detail = (ServiceManServiceDetailsActivity.Data) getList().get(position);

            ((ViewHolderProduct) holder).title.setText(detail.title + "");
            ((ViewHolderProduct) holder).description.setText(detail.des + "");


        } else if (getValue(position) instanceof ServiceManServicesDetails.Data.ServiceComment) {
            ServiceManServicesDetails.Data.ServiceComment comment = (ServiceManServicesDetails.Data.ServiceComment) getList().get(position);

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

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.description)
        TextView description;

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

