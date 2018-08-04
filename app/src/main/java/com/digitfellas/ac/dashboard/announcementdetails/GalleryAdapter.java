package com.digitfellas.ac.dashboard.announcementdetails;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseAdapter;
import com.digitfellas.ac.base.VH;
import com.digitfellas.ac.data.model.response.Anouncement;
import com.digitfellas.ac.data.model.response.AnouncementDetails;
import com.digitfellas.ac.utility.Utils;

import java.util.List;

import butterknife.BindView;

public class GalleryAdapter extends BaseAdapter<AnouncementDetails.Data.AnnouncementImage, GalleryAdapter.ProductVH> {


    protected GalleryAdapter(OnItemClick<AnouncementDetails.Data.AnnouncementImage> onItemClick, List<AnouncementDetails.Data.AnnouncementImage> list) {
        super(onItemClick, list);
    }

    @NonNull
    @Override
    public ProductVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_gallery_view, parent, false);

        return new ProductVH(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductVH holder, final int position) {

        AnouncementDetails.Data.AnnouncementImage image = getValue(position);


        holder.progressBar.setVisibility(View.VISIBLE);

        Utils.loadImagewithProgressBar(holder.mImageView, holder.progressBar, image.getImgUrl());


        if (onItemClick != null) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onItemClick.OnItemClickListener(v, getValue(position), position);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    public class ProductVH extends VH {

        @BindView(R.id.image)
        ImageView mImageView;

        @BindView(R.id.progressbar)
        ProgressBar progressBar;

        public ProductVH(View itemView) {
            super(itemView);
        }
    }
}
