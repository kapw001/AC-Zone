package com.digitfellas.ac.dashboard.myorder;


import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.digitfellas.ac.R;
import com.digitfellas.ac.adapter.viewpager.ViewPagerAdapter;
import com.digitfellas.ac.base.BaseFragment;
import com.digitfellas.ac.dashboard.myorder.open.CompeletedFragment;
import com.digitfellas.ac.dashboard.myorder.open.OpenFragment;
import com.digitfellas.ac.dashboard.myorder.open.OpenFragmentCompleted;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrderFragment extends BaseFragment {


    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.pager)
    ViewPager viewPager;

    private ViewPagerAdapter viewPagerAdapter;

    public MyOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);
        setButterKnife(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        viewPagerAdapter=new ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
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
        viewPagerAdapter.addFragment(OpenFragment.getInstance(1), "Open");
        viewPagerAdapter.addFragment(OpenFragment.getInstance(2), "Completed");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = viewPagerAdapter.getItem(viewPager.getCurrentItem());

        fragment.onActivityResult(requestCode, resultCode, data);

    }
}
