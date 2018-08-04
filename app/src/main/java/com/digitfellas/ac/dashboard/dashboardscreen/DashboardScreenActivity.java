package com.digitfellas.ac.dashboard.dashboardscreen;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseActivity;
import com.digitfellas.ac.dashboard.addorder.AddOrderForCustomerActivity;
import com.digitfellas.ac.dashboard.addorder.AddOrderForDealerActivity;
import com.digitfellas.ac.dashboard.announcementdetails.AnnouncementDeatailsActivity;
import com.digitfellas.ac.dashboard.dealer.DealerDashboardActivity;
import com.digitfellas.ac.data.model.response.Comments;
import com.digitfellas.ac.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class DashboardScreenActivity extends BaseActivity {

    private static final String TAG = "DashboardScreenActivity";
    private static final int RC_LOCATION = 12;

    @BindView(R.id.layout_dashboard)
    LinearLayout mDashBoard;

    @BindView(R.id.dash_services)
    CardView mDashServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_dashboard_screen);
        setButterKnife();


        mDashServices.setVisibility(View.GONE);

        if (dataSource.getRoleType().toLowerCase().equalsIgnoreCase("customer")) {

            mDashServices.setVisibility(View.VISIBLE);
        }


        phoneStatePermission();


        Intent intent = getIntent();

        if (intent != null) {

            if (intent.hasExtra("type")) {

//                showToast(intent.getStringExtra("id"));

                String type = intent.getStringExtra("type");
                int id = Integer.parseInt(intent.getStringExtra("id"));

                if (type.toUpperCase().equalsIgnoreCase("download")) {


                    moveToNextActivity(R.id.nav_downlods);


                } else {


                    moveToNextActivity(R.id.nav_anouncement);

                    Intent intent1 = new Intent(getApplicationContext(), AnnouncementDeatailsActivity.class);

                    intent1.putExtra("id", id);

                    startActivity(intent1);


                }


            }

        }


    }

    @AfterPermissionGranted(RC_LOCATION)
    private void locationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing

            if (dataSource.getRoleType().toLowerCase().equalsIgnoreCase("customer")) {

                startActivity(new Intent(DashboardScreenActivity.this, AddOrderForCustomerActivity.class));
            } else {
                startActivity(new Intent(DashboardScreenActivity.this, AddOrderForDealerActivity.class));
            }

            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    RC_LOCATION, perms);
        }
    }


    @OnClick({R.id.dash_add_order, R.id.dash_my_order, R.id.dash_check_stock, R.id.dash_announcement, R.id.dash_download, R.id.dash_services, R.id.logout})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.dash_add_order:

                locationPermission();

                break;

            case R.id.dash_my_order:

                moveToNextActivity(R.id.nav_my_order);

                break;

            case R.id.dash_check_stock:

                moveToNextActivity(R.id.nav_check_stock);

                break;

            case R.id.dash_announcement:
                moveToNextActivity(R.id.nav_anouncement);
                break;

            case R.id.dash_download:
                moveToNextActivity(R.id.nav_downlods);
                break;

            case R.id.dash_services:

                moveToNextActivity(R.id.nav_services);

                break;

            case R.id.logout:


                logout();

//                dataSource.clear();
//                Intent intent = new Intent(DashboardScreenActivity.this, LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);

                break;

        }


    }


    private void moveToNextActivity(int position) {

        Intent intent = new Intent(DashboardScreenActivity.this, DealerDashboardActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);

    }
}
