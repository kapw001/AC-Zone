package com.digitfellas.ac.dashboard.serviceman;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.digitfellas.ac.R;
import com.digitfellas.ac.adapter.viewpager.ViewPagerAdapter;
import com.digitfellas.ac.base.BaseActivity;
import com.digitfellas.ac.dashboard.dealer.DealerDashboardActivity;
import com.digitfellas.ac.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceManActivity extends BaseActivity {

//    @BindView(R.id.tabLayout)
//    TabLayout tabLayout;
//    @BindView(R.id.pager)
//    ViewPager pager;

//    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_man);
        setButterKnife();

        setTitle("My Services");

//        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
//
//        pager.setAdapter(viewPagerAdapter);
//
//        tabLayout.setupWithViewPager(pager);
//
//        viewPagerAdapter.addFragment(new MyServiceOpenFragment(),"Open");
//        viewPagerAdapter.addFragment(new MyServiceOpenFragment(),"Attended");
//        viewPagerAdapter.addFragment(new MyServiceOpenFragment(),"Completed");


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
//            Intent intent = new Intent(ServiceManActivity.this, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
