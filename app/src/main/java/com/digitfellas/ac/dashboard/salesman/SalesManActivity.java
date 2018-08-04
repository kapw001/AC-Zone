package com.digitfellas.ac.dashboard.salesman;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.digitfellas.ac.R;
import com.digitfellas.ac.adapter.viewpager.ViewPagerAdapter;
import com.digitfellas.ac.base.BaseActivity;
import com.digitfellas.ac.dashboard.MyServices.MyServicesStatusFragment;
import com.digitfellas.ac.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SalesManActivity extends BaseActivity {

    public static final int KEY_REQUEST_CODE = 123;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager pager;

    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_man);
        ButterKnife.bind(this);
        setTitle("My Tasks");
        initViews();

    }


    private void initViews() {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);


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
        adapter.addFragment(SalesManTasksFragment.getInstance(1), "Open");
        adapter.addFragment(SalesManTasksFragment.getInstance(2), "Pending");
        adapter.addFragment(SalesManTasksFragment.getInstance(3), "Completed");
//        adapter.addFragment(MyServicesStatusFragment.getInstance(3),"Closed");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logout) {


            logout();

//            dataSource.clear();
//
//            Intent intent = new Intent(this, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onClick(View v) {


        Intent intent = new Intent(this, AddTaskActivity.class);

        startActivityForResult(intent, KEY_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SalesManTasksFragment fragment = (SalesManTasksFragment) adapter.getItem(pager.getCurrentItem());
        fragment.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == resultCode) {
//
//            showToast("Call Back");
//
//        }


    }
}
