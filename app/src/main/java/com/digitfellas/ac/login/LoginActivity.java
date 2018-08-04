package com.digitfellas.ac.login;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseActivity;
import com.digitfellas.ac.dashboard.dashboardscreen.DashboardScreenActivity;
import com.digitfellas.ac.dashboard.dealer.DealerDashboardActivity;
import com.digitfellas.ac.dashboard.salesman.SalesManActivity;
import com.digitfellas.ac.dashboard.serviceman.ServiceManActivity;
import com.digitfellas.ac.dashboard.serviceman.ServiceManServiceDetailsActivity;
import com.digitfellas.ac.data.listener.DataListener;
import com.digitfellas.ac.data.model.request.Login;
import com.digitfellas.ac.data.model.response.APIError;
import com.digitfellas.ac.data.model.response.LoginResponse;
import com.digitfellas.ac.data.model.response.UserDetailsResponse;
import com.digitfellas.ac.forgotpassword.ForgotPasswordActivity;
import com.digitfellas.ac.signup.SignUpActivity;
import com.digitfellas.ac.utility.APIErrorUtil;
import com.digitfellas.ac.utility.DialogUtil;
import com.digitfellas.ac.utility.KeyboardUtils;
import com.digitfellas.ac.utility.TextInputUtil;
import com.digitfellas.ac.utility.Utils;
import com.digitfellas.ac.utility.ValidationUtil;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;

public class LoginActivity extends BaseActivity {


    private static final String TAG = "LoginActivity";

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.mobile)
    TextInputLayout mMobile;
    @BindView(R.id.password)
    TextInputLayout mPassword;
    @BindView(R.id.login)
    Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setButterKnife();


    }


    @OnClick({R.id.back, R.id.forgot_password, R.id.sign_up_now, R.id.login})
    public void OnClickView(View view) {

        switch (view.getId()) {

            case R.id.back:
                LoginActivity.this.onBackPressed();
                break;

            case R.id.forgot_password:

                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));

                break;

            case R.id.sign_up_now:

                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

                break;

            case R.id.login:

//                startActivity(new Intent(LoginActivity.this, DashboardScreenActivity.class));
                if (validateFormInputFieldsAll()) {
                    KeyboardUtils.hideKeyboard(this);
//                    showToast("Valid");

//                    startActivity(new Intent(LoginActivity.this, DealerDashboardActivity.class));
                    callLoginApi();
                } else {

                    showToast(getString(R.string.allfieldsrequired));
                }

                break;


        }


    }


    private void callLoginApi() {

        final Map<String, Object> login = new HashMap<>();

        login.put("mobile_no", Long.parseLong(mMobile.getEditText().getText().toString()));
        login.put("password", mPassword.getEditText().getText().toString());

//        showLoading();

//        dataSource.login(new JSONObject(login).toString(), this);


        showLoading();
        dataSource.login(new JSONObject(login).toString(), new DataListener() {
            @Override
            public void onSuccess(Object object) {

                LoginActivity.super.onSuccess(object);


                final LoginResponse response = (LoginResponse) object;

                if (response.getSuccess()) {

                    Map<String, String> headers = new HashMap<>();

                    String auth = response.getData().getTokenType() + " " + response.getData().getAccessToken();

                    headers.put("Authorization", auth);

                    headers.put("Accept", "application/json");
                    showLoading();
                    dataSource.getUserProfile(headers, new DataListener() {
                        @Override
                        public void onSuccess(Object object) {
                            LoginActivity.super.onSuccess(object);

                            UserDetailsResponse detailsResponse = (UserDetailsResponse) object;

                            if (detailsResponse.getSuccess()) {

                                LoginResponse.Data data = response.getData();

                                UserDetailsResponse.Data userData = detailsResponse.getData();

                                dataSource.saveSession(data.getAccessToken(), data.getTokenType(), data.getRole(), data.getRoleId(), data.getUserId(), userData.getName(), userData.getEmail(), userData.getMobileNo());


                                if (dataSource.isLoggedIn()) {


                                    showToast(response.getMessage());

                                    if (dataSource.getRoleType().toLowerCase().equalsIgnoreCase("ServiceRep".toLowerCase())) {

                                        startActivity(new Intent(LoginActivity.this, ServiceManActivity.class));

                                        finish();

                                    } else if (dataSource.getRoleType().toLowerCase().equalsIgnoreCase("SalesRep".toLowerCase())) {

                                        startActivity(new Intent(LoginActivity.this, SalesManActivity.class));

                                        finish();

                                    } else {

                                        startActivity(new Intent(LoginActivity.this, DashboardScreenActivity.class));

                                        finish();
                                    }

                                } else {
                                    showToast("Something went wrong");
                                }


                            } else {

                                showToast("Something went wrong, Please try again");

                            }


                        }

                        @Override
                        public void onFail(Throwable throwable) {
                            LoginActivity.this.onFail(throwable);
                        }

                        @Override
                        public void onNetworkFailure() {
                            LoginActivity.super.onNetworkFailure();
                        }
                    });


                } else {

                    showToast("Something went wrong, Please try again");
                }


            }

            @Override
            public void onFail(Throwable throwable) {

                LoginActivity.this.onFail(throwable);

            }

            @Override
            public void onNetworkFailure() {
                LoginActivity.super.onNetworkFailure();
            }
        });


    }


//    @Override
//    public void onSuccess(Object object) {
//        super.onSuccess(object);
//
//        if (object instanceof LoginResponse) {
//
//            LoginResponse response = (LoginResponse) object;
//
//            if (response.getSuccess()) {
//
//
//                LoginResponse.Data data = response.getData();
//
////                dataSource.saveSession(data.getAccessToken(), data.getTokenType(), data.getRole(), data.getRoleId(), data.getUserId());
//
//                if (dataSource.isLoggedIn()) {
//                    showToast(response.getMessage());
//
//                    startActivity(new Intent(LoginActivity.this, DashboardScreenActivity.class));
//
//                    finish();
//
//                } else {
//                    showToast("Something went wrong");
//                }
//
//
//            } else {
//                showToast("Something went wrong");
//            }
//
//
//        } else {
//
//            showToast("Something went wrong");
//
//        }
//
//    }

    @Override
    public void onFail(Throwable throwable) {
        super.onFail(throwable);

        if (!(throwable instanceof HttpException)) {

            showToast(throwable.getMessage());

        }

    }

    private boolean validateFormInputFieldsAll() {

        boolean isValid = true;

        if (TextUtils.isEmpty(mMobile.getEditText().getText())) {
            TextInputUtil.setError(mMobile, getString(R.string.mobileempty));
            isValid = false;
        } else if (!(mMobile.getEditText().getText().toString().length() >= 10)) {
            TextInputUtil.setError(mMobile, getString(R.string.mobile_must_be));
            isValid = false;
        } else {
            TextInputUtil.setDisableError(mMobile);
        }

        if (TextUtils.isEmpty(mPassword.getEditText().getText())) {
            TextInputUtil.setError(mPassword, getString(R.string.passwordempty));
            isValid = false;
        } else if (!(mPassword.getEditText().getText().toString().length() >= 6)) {
            TextInputUtil.setError(mPassword, getString(R.string.passwordmustbe));
            isValid = false;
        } else {
            TextInputUtil.setDisableError(mPassword);
        }

        return isValid;

    }
}
