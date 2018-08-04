package com.digitfellas.ac.dashboard.dealer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseActivity;
import com.digitfellas.ac.common.BottomNavigationViewBehavior;
import com.digitfellas.ac.dashboard.addorder.AddOrderForCustomerActivity;
import com.digitfellas.ac.dashboard.addorder.AddOrderForDealerActivity;
import com.digitfellas.ac.dashboard.announcementdetails.AnnouncementDeatailsActivity;
import com.digitfellas.ac.dashboard.anouncement.AnouncementFragment;
import com.digitfellas.ac.dashboard.checkstock.CheckStockFragment;
import com.digitfellas.ac.dashboard.MyServices.MyServicesFragment;
import com.digitfellas.ac.dashboard.dashboardscreen.DashboardScreenActivity;
import com.digitfellas.ac.dashboard.downloads.DownloadsFragment;
import com.digitfellas.ac.dashboard.myorder.MyOrderFragment;
import com.digitfellas.ac.login.LoginActivity;

import butterknife.BindView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class DealerDashboardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_LOCATION = 12;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    @BindView(R.id.container)
    FrameLayout frameLayout;

    private Fragment fragment = null;

    TextView mName;
    TextView mRole;

    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_dashboard);

        setButterKnife();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_my_order);
        addFragment(R.id.nav_my_order);

//        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);


        if (dataSource.getRoleType().toLowerCase().equalsIgnoreCase("customer")) {

            navigationView.getMenu().findItem(R.id.nav_services).setVisible(true);
        }

        View header = navigationView.getHeaderView(0);

        mName = (TextView) header.findViewById(R.id.name);
        mRole = (TextView) header.findViewById(R.id.role);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mBottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior(new BottomNavigationViewBehavior.MarginCallBack() {
            @Override
            public void down() {


                Animation animation = new Animation() {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
                        params.setMargins(0, 0, 0, (int) (0 * interpolatedTime));
                        frameLayout.setLayoutParams(params);
                    }
                };
                animation.setDuration(200);
                frameLayout.clearAnimation();
                frameLayout.startAnimation(animation);


            }

            @Override
            public void up() {

                Animation animation = new Animation() {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
                        params.setMargins(0, 0, 0, (int) ((int) getResources().getDimension(R.dimen._60sdp) * interpolatedTime));
                        frameLayout.setLayoutParams(params);
                    }
                };
                animation.setDuration(200);
                frameLayout.clearAnimation();
                frameLayout.startAnimation(animation);
//
//                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams();
//                params.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen._60sdp));
//                frameLayout.setLayoutParams(params);

            }
        }));
//        CoordinatorLayout.LayoutParams layoutParams1 = (CoordinatorLayout.LayoutParams) mFloatingActionButton.getLayoutParams();
//        layoutParams1.setBehavior(new ScrollAwareFABBehaviorWithAnimation());
        setTitle("Dashboard");


        String n = dataSource.getName();
        String role = dataSource.getRoleType();

        mName.setText(n + "");
        mRole.setText(role + "");


        if (getIntent() != null) {

            int id = getIntent().getIntExtra("position", R.id.nav_my_order);

            callFragment(navigationView, id);

        }
//
//        Menu menu = navigationView.getMenu();
//
//        // find MenuItem you want to change
////        MenuItem nav_camara = menu.findItem(R.id.nav_anouncement);
//        Rect rect=new Rect(40,40,140,140);
////        nav_camara.getIcon().setBounds(rect);
//
//        Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_bull_horn_announcer,null);
//        drawable.setBounds(rect);
//
//                drawable.setBounds(0,0,10,10);
////
//        navigationView.getMenu().findItem(R.id.nav_anouncement).setIcon(drawable);


    }


    private void callFragment(NavigationView navigationView, int id) {
        navigationView.setCheckedItem(id);
        addFragment(id);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.dealer_dashboard, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @AfterPermissionGranted(RC_LOCATION)
    private void locationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing

            if (dataSource.getRoleType().toLowerCase().equalsIgnoreCase("customer")) {

                startActivity(new Intent(DealerDashboardActivity.this, AddOrderForCustomerActivity.class).putExtra("isfinish", false));
            } else {
                startActivity(new Intent(DealerDashboardActivity.this, AddOrderForDealerActivity.class).putExtra("isfinish", false));
            }
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    RC_LOCATION, perms);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_order) {
            // Handle the camera action
            locationPermission();
//            if (dataSource.getRoleType().toLowerCase().equalsIgnoreCase("customer")) {
//
//                startActivity(new Intent(DealerDashboardActivity.this, AddOrderForCustomerActivity.class).putExtra("isfinish", false));
//            } else {
//                startActivity(new Intent(DealerDashboardActivity.this, AddOrderForDealerActivity.class).putExtra("isfinish", false));
//            }

//            if (dataSource.getRoleType().toLowerCase().equalsIgnoreCase("customer")) {
//
//                startActivity(new Intent(DealerDashboardActivity.this, AddOrderForCustomerActivity.class).putExtra("isfinish",false));
//            }else {
//                startActivity(new Intent(DealerDashboardActivity.this, AddOrderForDealerActivity.class).putExtra("isfinish",false));
//            }

        } else {
            addFragment(item.getItemId());
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (fragment instanceof MyOrderFragment) {

            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void addFragment(int id) {


        switch (id) {

            case R.id.nav_my_order:
                fragment = new MyOrderFragment();
                setTitle(getString(R.string.my_order));
                break;

            case R.id.nav_check_stock:

                fragment = new CheckStockFragment();

                setTitle(getString(R.string.check_stock));

                break;

            case R.id.nav_anouncement:

                fragment = new AnouncementFragment();

                setTitle(getString(R.string.anouncement));

                break;

            case R.id.nav_services:

                fragment = new MyServicesFragment();

                setTitle(getString(R.string.services));

                break;

            case R.id.nav_downlods:

                fragment = new DownloadsFragment();

                setTitle(getString(R.string.downloads));

                break;

            case R.id.nav_logout:

                logout();
//                dataSource.clear();
//
//                Intent intent = new Intent(DealerDashboardActivity.this, LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                return;

            default:
                fragment = new MyOrderFragment();
                setTitle("My Order");

        }

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
