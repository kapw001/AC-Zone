package com.digitfellas.ac.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.digitfellas.ac.data.DataRepository;
import com.digitfellas.ac.data.DataSource;
import com.digitfellas.ac.data.helper.L;
import com.digitfellas.ac.data.pref.Pref;
import com.digitfellas.ac.data.pref.PreferencesHelper;
import com.digitfellas.ac.data.remote.ApiService;
import com.digitfellas.ac.data.remote.RemoteDataSourceHelper;
import com.digitfellas.ac.data.retrofitclient.ApiEndPoint;
import com.digitfellas.ac.data.retrofitclient.RetrofitClient;
import com.digitfellas.ac.utility.ProgressUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by yasar on 26/3/18.
 */

public class BaseFragment extends Fragment implements EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {

    private static final String TAG = "BaseFragment";

    private Pref pref;
    private RetrofitClient retrofitClient;
    private ApiService apiService;
    private RemoteDataSourceHelper remoteDataSource;
    public DataSource dataSource;
    private Unbinder unbinder;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        L.loge("onAttach Fragment");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        pref = PreferencesHelper.getPreferencesInstance(getContext());

        retrofitClient = RetrofitClient.getRetrofitClientInstance(ApiEndPoint.BASE_URL);

        apiService = retrofitClient.getRetrofit().create(ApiService.class);

        remoteDataSource = new RemoteDataSourceHelper(apiService);

        dataSource = new DataRepository(getContext(), remoteDataSource, pref);


        L.loge("onCreate Fragment");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        L.loge("onCreateView Fragment");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        L.loge("onViewCreated Fragment");
    }

    public void setButterKnife(Fragment fragment,View view){
        unbinder= ButterKnife.bind(fragment,view);
    }

    @Override
    public void onStart() {
        super.onStart();

        L.loge("onStart Fragment");
    }

    @Override
    public void onPause() {
        super.onPause();

        L.loge("onPause Fragment");
    }

    @Override
    public void onResume() {
        super.onResume();

        L.loge("onResume Fragment");
    }

    @Override
    public void onStop() {
        super.onStop();

        L.loge("onStop Fragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (unbinder!=null){
            unbinder.unbind();
        }
        L.loge("onDestroyView Fragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        L.loge("onDestroy Fragment");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        L.loge("onDetach Fragment");
    }


    public int getColor(@ColorRes int colorId) {
        return ContextCompat.getColor(getActivity(), colorId);
    }


    public void showSnackBar(View view, String msg) {

        Snackbar snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    public void onSuccess(Object object) {
        hideLoading();
    }

    public void onFail(Throwable throwable) {
        hideLoading();
//        Toast.makeText(getContext(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();

//        progressStateCall(R.drawable.somethingwentwrong, "error");
    }


    public void onNetworkFailure() {
        hideLoading();
        Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//        progressStateCall(R.drawable.nointernet, "nointernet");

    }


    public void showLoading() {

        ProgressUtils.showProgress(getContext(), "Loading");

//        progressStateCall(R.drawable.nointernet, "loading");

    }


    public void hideLoading() {

        ProgressUtils.hideProgress();
//        progressStateCall(R.drawable.nointernet, "content");

    }


    public void showToast(String msg) {

        Toast.makeText(getContext(), "" + msg
                , Toast.LENGTH_SHORT).show();

    }


    public void showToast(@StringRes int msgID) {

        Toast.makeText(getContext(), "" + msgID, Toast.LENGTH_SHORT).show();

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    public View.OnClickListener errorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callNetwork();
        }
    };

    public void callNetwork() {

    }



    public void loge(String message){

        Log.e(TAG, "loge: "+message );

    }

    public void logi(String message){

        Log.i(TAG, "logi: "+message );

    }

    public void logv(String message){

        Log.v(TAG, "logv: "+message );

    }


    public String getText(TextInputLayout inputLayout){

        return inputLayout.getEditText().getText().toString();
    }



}
