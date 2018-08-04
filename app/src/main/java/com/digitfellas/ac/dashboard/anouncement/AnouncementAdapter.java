package com.digitfellas.ac.dashboard.anouncement;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseAdapter;
import com.digitfellas.ac.base.VH;
import com.digitfellas.ac.data.model.response.Anouncement;
import com.digitfellas.ac.utility.Utils;

import java.util.List;

import butterknife.BindView;

public class AnouncementAdapter extends BaseAdapter<Anouncement.Datum,AnouncementAdapter.ProductVH>{


    protected AnouncementAdapter(OnItemClick<Anouncement.Datum> onItemClick, List<Anouncement.Datum> list) {
        super(onItemClick, list);
    }

    @NonNull
    @Override
    public ProductVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_anouncement,parent,false);

        return new ProductVH(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductVH holder, final int position) {

        Anouncement.Datum anouncement=getValue(position);

        setText(holder.mTitle,anouncement.getTitle());

        holder.mImageView.setImageDrawable(Utils.getTextDrawable(anouncement.getTitle()));

        if (onItemClick!=null){

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onItemClick.OnItemClickListener(v,getValue(position),position);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    public class ProductVH extends VH {

        @BindView(R.id.image_view)
        ImageView mImageView;

        @BindView(R.id.title)
        TextView mTitle;

        public ProductVH(View itemView) {
            super(itemView);
        }
    }
}
