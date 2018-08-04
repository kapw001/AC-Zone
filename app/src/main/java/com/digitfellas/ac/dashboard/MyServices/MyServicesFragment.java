package com.digitfellas.ac.dashboard.MyServices;


import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.digitfellas.ac.R;
import com.digitfellas.ac.adapter.viewpager.ViewPagerAdapter;
import com.digitfellas.ac.base.BaseFragment;
import com.digitfellas.ac.customview.CustomMessageView;
import com.digitfellas.ac.dashboard.customerservices.MyServiceDetailsActivity;
import com.digitfellas.ac.dashboard.customerservices.PostServiceActivity;
import com.digitfellas.ac.dashboard.myorder.open.OpenFragment;
import com.digitfellas.ac.dashboard.myorder.open.OpenFragmentCompleted;
import com.digitfellas.ac.data.listener.DataListener;
import com.digitfellas.ac.data.model.request.Products;
import com.digitfellas.ac.data.model.response.CustomerServices;
import com.digitfellas.ac.utility.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyServicesFragment extends BaseFragment {


    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.pager)
    ViewPager viewPager;

    private ViewPagerAdapter viewPagerAdapter;

    public MyServicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_services, container, false);
        setButterKnife(this, view);


        viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.white));
            drawable.setSize(5, 1);
            ((LinearLayout) root).setDividerPadding(40);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }
//        viewPager.setOffscreenPageLimit(1);
        viewPagerAdapter.addFragment(MyServicesStatusFragment.getInstance(1),"Open");
        viewPagerAdapter.addFragment(MyServicesStatusFragment.getInstance(2),"Attended");
        viewPagerAdapter.addFragment(MyServicesStatusFragment.getInstance(3),"Closed");

        return view;
    }


    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);

        CustomerServices customerServices= (CustomerServices) object;

        if (customerServices.getSuccess()){


        }else {

            showToast(customerServices.getMessage());

        }


    }

//    @OnClick(R.id.fab)
//    public void onClick(View view){
//
//
//        startActivity(new Intent(getContext(),PostServiceActivity.class));
//
//
//
//
//
//    }

}
