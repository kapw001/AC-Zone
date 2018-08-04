package com.digitfellas.ac.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public class VH extends RecyclerView.ViewHolder {

    public VH(View itemView) {
        super(itemView);

        ButterKnife.bind(this,itemView);

    }
}
