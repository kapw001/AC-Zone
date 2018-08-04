package com.digitfellas.ac.signup;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseActivity;
import com.digitfellas.ac.data.model.request.Register;
import com.digitfellas.ac.data.model.response.APIError;
import com.digitfellas.ac.data.model.response.RegisterResponse;
import com.digitfellas.ac.login.LoginActivity;
import com.digitfellas.ac.utility.KeyboardUtils;
import com.digitfellas.ac.utility.TextInputUtil;
import com.digitfellas.ac.utility.ValidationUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity implements SignUpPresenter.ResponseCallBack {


    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.terms)
    TextView mTerms;
    @BindView(R.id.sign_input_txt_name)
    TextInputLayout mSignUpName;
    @BindView(R.id.sign_input_txt_mobile)
    TextInputLayout mSignUpMobile;
    @BindView(R.id.sign_input_txt_email)
    TextInputLayout mSignUpEmail;
    @BindView(R.id.sign_input_txt_password)
    TextInputLayout mSignUpPassword;
    @BindView(R.id.sign_input_txt_con_password)
    TextInputLayout mSignUpConfirmPassword;
    @BindView(R.id.sign_input_btn_signup)
    Button mSignUp;
    @BindView(R.id.sign_txt_login_now)
    TextView mSignUpLoginNow;

    private SignUpPresenter signUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setButterKnife();

        mTerms.setText(Html.fromHtml(getString(R.string.terms)));
        mTerms.setMovementMethod(LinkMovementMethod.getInstance());

//        mSignUpName.setFocusable(true);
//        mSignUpName.setFocusableInTouchMode(true);
//        mSignUpName.getEditText().setFocusable(true);
        mSignUpName.getEditText().setFocusableInTouchMode(true);



//        TextInputUtil.setErrorFocusable(mSignUpMobile, "This number will be verified via OTP ",R.style.before_error_appearance);

        KeyboardUtils.hideKeyboard(this);

        signUpPresenter=new SignUpPresenter(dataSource,this);

    }


    @OnClick({R.id.sign_txt_login_now, R.id.sign_input_btn_signup, R.id.back})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.back:
                SignUpActivity.super.onBackPressed();
                break;

            case R.id.sign_txt_login_now:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                break;

            case R.id.sign_input_btn_signup:
                if (validateFormInputFieldsAll()) {
                    KeyboardUtils.hideKeyboard(this);
//                    showToast("Valid");

                    Map<String,Object> register=new HashMap<>();

                    register.put("name",TextInputUtil.getText(mSignUpName));
                    register.put("mobile_no",Long.parseLong(TextInputUtil.getText(mSignUpMobile)));
                    register.put("password",TextInputUtil.getText(mSignUpPassword));
                    register.put("c_password",TextInputUtil.getText(mSignUpPassword));

                    signUpPresenter.callRegister(new JSONObject(register).toString(),this);

                } else {

                    showToast(getString(R.string.allfieldsrequired));

                }

                break;


        }

    }


    private boolean validateFormInputFieldsAll() {

        boolean isValid = true;

        if (TextUtils.isEmpty(mSignUpName.getEditText().getText())) {
            TextInputUtil.setError(mSignUpName, getString(R.string.fullnameempty));
            isValid = false;
        } else {
            TextInputUtil.setDisableError(mSignUpName);
        }


        if (TextUtils.isEmpty(mSignUpMobile.getEditText().getText())) {
            TextInputUtil.setError(mSignUpMobile, getString(R.string.mobileempty));
            isValid = false;
        }
        else if (ValidationUtil.isValidMobile(mSignUpMobile.getEditText().getText())) {
            TextInputUtil.setError(mSignUpMobile, getString(R.string.mobile_must_be));
            isValid = false;
        }
        else {
            TextInputUtil.setDisableError(mSignUpMobile);
        }


//        if (TextUtils.isEmpty(mSignUpEmail.getEditText().getText())) {
//            TextInputUtil.setError(mSignUpEmail, getString(R.string.emailempty));
//            isValid = false;
//        } else if (!ValidationUtil.isEmail(mSignUpEmail.getEditText().getText())) {
//            TextInputUtil.setError(mSignUpEmail, getString(R.string.emailnotvalid));
//            isValid = false;
//        } else {
//            TextInputUtil.setDisableError(mSignUpEmail);
//        }

        if (TextUtils.isEmpty(mSignUpPassword.getEditText().getText())) {
            TextInputUtil.setError(mSignUpPassword, getString(R.string.passwordempty));
            isValid = false;
        }else if (!(mSignUpPassword.getEditText().getText().toString().length()>=6)){
            TextInputUtil.setError(mSignUpPassword, getString(R.string.passwordmustbe));
            isValid = false;
        }else {
            TextInputUtil.setDisableError(mSignUpPassword);
        }

        if (TextUtils.isEmpty(mSignUpConfirmPassword.getEditText().getText())) {
            TextInputUtil.setError(mSignUpConfirmPassword, getString(R.string.confirm_passwordempty));
            isValid = false;
        } else {
            TextInputUtil.setDisableError(mSignUpConfirmPassword);
        }

        if (!mSignUpPassword.getEditText().getText().toString().equalsIgnoreCase(mSignUpConfirmPassword.getEditText().getText().toString())){

            TextInputUtil.setError(mSignUpConfirmPassword, getString(R.string.notmatch));
            showToast(getString(R.string.notmatch));
            isValid = false;

        }

        return isValid;

    }

    @Override
    public void onValidationError(APIError apiError) {

        if (apiError.getData()!=null){

            APIError.Data data = (APIError.Data) apiError.getData();

            if (data.getName()!=null){
                TextInputUtil.setError(mSignUpName,data.getName().get(0));
            }

            if (data.getMobileNo()!=null){
                TextInputUtil.setError(mSignUpMobile,data.getMobileNo().get(0));
            }

            if (data.getPassword()!=null){
                TextInputUtil.setError(mSignUpPassword,data.getPassword().get(0));
            }


        }


    }

    @Override
    public void onError(Throwable throwable) {

        showToast(throwable.getMessage());

    }

    @Override
    public void onSuccess(RegisterResponse registerResponse) {

        showToast(registerResponse.getMessage());
        finish();

    }

    @Override
    public void noNetwork() {

        super.onNetworkFailure();
        showToast(getString(R.string.nointernet));

    }
}
