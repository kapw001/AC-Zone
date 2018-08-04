package com.digitfellas.ac.dashboard.serviceman;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseFragment;
import com.digitfellas.ac.customview.CustomMessageView;
import com.digitfellas.ac.dashboard.myorder.PaginationAdapter;
import com.digitfellas.ac.data.model.request.Products;
import com.digitfellas.ac.utility.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyServiceOpenFragment extends BaseFragment implements MyServiceManAdapter.OnItemClicListener {


    @BindView(R.id.listview)
    CustomMessageView customMessageView;
    private MyServiceManAdapter myOderAdapter;


    public MyServiceOpenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_service_open, container, false);
        setButterKnife(this,view);




        myOderAdapter = new MyServiceManAdapter(this,new ArrayList<Products>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        customMessageView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayout.VERTICAL);

        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));

        customMessageView.addItemDecoration(dividerItemDecoration);
//        mMyOrder_ListView.addItemDecoration(new EqualSpacingItemDecoration(16,EqualSpacingItemDecoration.VERTICAL));
        customMessageView.setAdapter(myOderAdapter);

//        customMessageView.addOnScrollListener(Utils.fabShowHide(mFab));

        customMessageView.setMessageTitle("No data");
        customMessageView.setMessageDescription("There is no data");

        customMessageView.showContent();

        return view;
    }


    @Override
    public void OnItemClick(View v, Object o, int position) {


        Intent intent=new Intent(getContext(),ServiceManServiceDetailsActivity.class);

        startActivity(intent);


    }
}
