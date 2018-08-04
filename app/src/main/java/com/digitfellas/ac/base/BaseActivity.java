package com.digitfellas.ac.base;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.digitfellas.ac.R;
import com.digitfellas.ac.dashboard.addorder.AddOrderForCustomerActivity;
import com.digitfellas.ac.dashboard.addorder.AddOrderForDealerActivity;
import com.digitfellas.ac.dashboard.dashboardscreen.DashboardScreenActivity;
import com.digitfellas.ac.data.DataRepository;
import com.digitfellas.ac.data.DataSource;
import com.digitfellas.ac.data.helper.L;
import com.digitfellas.ac.data.listener.DataListener;
import com.digitfellas.ac.data.model.request.Deviceinfo;
import com.digitfellas.ac.data.model.response.APIError;
import com.digitfellas.ac.data.model.response.Logout;
import com.digitfellas.ac.data.pref.Pref;
import com.digitfellas.ac.data.pref.PreferencesHelper;
import com.digitfellas.ac.data.remote.ApiService;
import com.digitfellas.ac.data.remote.RemoteDataSourceHelper;
import com.digitfellas.ac.data.retrofitclient.ApiEndPoint;
import com.digitfellas.ac.data.retrofitclient.RetrofitClient;
import com.digitfellas.ac.splash_screen_login_signup.LoginSignUpActivity;
import com.digitfellas.ac.utility.APIErrorUtil;
import com.digitfellas.ac.utility.DialogUtil;
import com.digitfellas.ac.utility.ProgressUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.digitfellas.ac.data.pref.PreferencesHelper.KEY_USER_ID;

public class BaseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks, DataListener {

    private static final String TAG = "BaseActivity";

    private static final int RC_PHONE_STATS = 12;

    public Unbinder unbinder;

    private Pref pref;
    private RetrofitClient retrofitClient;
    private ApiService apiService;
    private RemoteDataSourceHelper remoteDataSource;
    public DataSource dataSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        pref = PreferencesHelper.getPreferencesInstance(getApplicationContext());

        retrofitClient = RetrofitClient.getRetrofitClientInstance(ApiEndPoint.BASE_URL);

        apiService = retrofitClient.getRetrofit().create(ApiService.class);

        remoteDataSource = new RemoteDataSourceHelper(apiService);

        dataSource = new DataRepository(getApplicationContext(), remoteDataSource, pref);

    }

    public void setButterKnife() {
        unbinder = ButterKnife.bind(this);
    }


    protected void getFcmTokenAndUpdate() {

        String android_id = "";
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.d("Android", "Android ID : " + android_id);

        Map<String, String> tokenAndDeviceID = dataSource.getTokenAndDeviceID();

        final String token = tokenAndDeviceID.get(PreferencesHelper.KEY_FCM_TOKEN);

        if (!(token != null && token.length() > 0)) {
//            a9cf10a5798da358


            final String finalAndroid_id = android_id;
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(final InstanceIdResult instanceIdResult) {

                    Deviceinfo deviceinfo = new Deviceinfo();

                    deviceinfo.setDevice_id(finalAndroid_id);
                    deviceinfo.setPush_token_id(instanceIdResult.getToken());
                    deviceinfo.setDevice_model("Android");
                    String id = dataSource.getUserDetails().get(KEY_USER_ID).toString();
                    deviceinfo.setUser_id(id);


                    loge(new Gson().toJson(deviceinfo));

                    dataSource.postDeviceInfo(dataSource.getAuthendicate(), deviceinfo, new DataListener() {
                        @Override
                        public void onSuccess(Object object) {

                            dataSource.saveTokenAndDeviceID(instanceIdResult.getToken(), finalAndroid_id);

                            Log.e(TAG, "onSuccess: fcm success registered " + object.toString());
                        }

                        @Override
                        public void onFail(Throwable throwable) {

                            Log.e(TAG, "onFail: " + throwable.getMessage());

                            Log.e(TAG, "onFail: fcm fail to regidtered");
                        }

                        @Override
                        public void onNetworkFailure() {

                        }
                    });


//                    Log.e(TAG, "onSuccess: token " + instanceIdResult.getToken());
//                    Log.e(TAG, "onSuccess: id " + instanceIdResult.getId());


                }
            });
        } else {
            loge("Already registered");
        }


    }


    @AfterPermissionGranted(RC_PHONE_STATS)
    protected void phoneStatePermission() {
//        String[] perms = {Manifest.permission.READ_PHONE_STATE};
//        if (EasyPermissions.hasPermissions(this, perms)) {
//            // Already have permission, do the thing

        getFcmTokenAndUpdate();
//            // ...
//        } else {
//            getFcmTokenAndUpdate();
//            // Do not have permissions, request them now
//            EasyPermissions.requestPermissions(this, getString(R.string.phone_state_rationale),
//                    RC_PHONE_STATS, perms);
//        }
    }


    public void logout() {


        showLoading();
        dataSource.logout(dataSource.getAuthendicate(), new DataListener() {
            @Override
            public void onSuccess(Object object) {

                BaseActivity.this.onSuccess(object);

                Logout lout = (Logout) object;

                if (lout.isSuccess()) {


                    dataSource.clear();
                    showToast(lout.getMessage());

                    Intent intent = new Intent(getApplicationContext(), LoginSignUpActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);

                } else {

                    showToast(lout.getMessage());

                }

            }

            @Override
            public void onFail(Throwable throwable) {

                BaseActivity.this.onFail(throwable);

                showToast(throwable.getMessage());

            }

            @Override
            public void onNetworkFailure() {

                BaseActivity.this.onNetworkFailure();

                showToast("There is no internet connection");
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.logi("onDestory");
        if (unbinder != null) unbinder.unbind();

    }

    @Override
    protected void onStart() {
        super.onStart();

        L.logi("onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();

        L.logi("onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();

        L.logi("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        L.logi("onStop");
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setTitle(String title) {

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle(title);
        }

    }

    public void setBackButtonEnabledAndTitle(String title) {

        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    public void showSnackBar(View view, String msg) {

        Snackbar snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }


    @Override
    public void onSuccess(Object object) {
        hideLoading();
    }

    public void onFail(Throwable throwable) {
        hideLoading();

        if (throwable instanceof HttpException) {

            APIError apiError = APIErrorUtil.parseError(((HttpException) throwable).response());

            Log.e(TAG, "onFail: APIError   " + apiError.toString());

            if (apiError.getData() instanceof String) {


                showDialog("Error", apiError.getData().toString());

            } else {

                showToast("Something went wrong ");

            }
        }


//        Toast.makeText(this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();

//        progressStateCall(R.drawable.somethingwentwrong, "error");
    }


    public void onNetworkFailure() {
        hideLoading();
        showDialogCallBack("Error", "There is no internet connection");
//        Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
//        progressStateCall(R.drawable.nointernet, "nointernet");


    }


    public void showLoading() {

        ProgressUtils.showProgress(this, "Loading");

//        progressStateCall(R.drawable.nointernet, "loading");

    }


    public void hideLoading() {

        ProgressUtils.hideProgress();
//        progressStateCall(R.drawable.nointernet, "content");

    }


    public void showToast(String msg) {

        Toast.makeText(this, "" + msg
                , Toast.LENGTH_SHORT).show();

    }


    public void showToast(@StringRes int msgID) {

        Toast.makeText(this, "" + msgID, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
////            String yes = getString(R.string.yes);
////            String no = getString(R.string.no);
////
////            // Do something after user returned from app settings screen, like showing a Toast.
////            Toast.makeText(
////                    this,
////                    getString(R.string.returned_from_app_settings_to_activity,
////                            hasCameraPermission() ? yes : no,
////                            hasLocationAndContactsPermissions() ? yes : no,
////                            hasSmsPermission() ? yes : no),
////                    Toast.LENGTH_LONG)
////                    .show();
//        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        Log.d(TAG, "onRationaleAccepted:" + requestCode);
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        Log.d(TAG, "onRationaleDenied:" + requestCode);
    }


    public void showDialog(String title, String content) {

        DialogUtil.showDialog(BaseActivity.this, title, content);

    }

    public void showDialogCallBack(String title, String content) {

        DialogUtil.showDialogWithCallBack(BaseActivity.this, title, content, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.setClassName("com.android.phone","com.android.phone.NetworkSetting");
//                startActivity(intent);
                Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(i);

            }
        });

    }


    public void loge(String message) {

        Log.e(TAG, "loge: " + message);

    }

    public void logi(String message) {

        Log.i(TAG, "logi: " + message);

    }

    public void logv(String message) {

        Log.v(TAG, "logv: " + message);

    }

}
