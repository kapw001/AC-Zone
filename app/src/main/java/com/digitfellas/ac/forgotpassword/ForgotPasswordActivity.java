package com.digitfellas.ac.forgotpassword;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseActivity;
import com.digitfellas.ac.utility.TextInputUtil;
import com.digitfellas.ac.utility.ValidationUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgotPasswordActivity extends BaseActivity {


    @BindView(R.id.email)
    TextInputLayout mEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setButterKnife();


    }


    @OnClick({R.id.back, R.id.forgot_btn_send, R.id.forgot_login_now})
    public void onCLick(View v) {

        switch (v.getId()) {

            case R.id.back:

                onBackPressed();

                break;

            case R.id.forgot_btn_send:

                if (validateFormInputFieldsAll()){

                    showToast("Valid");

                }else {
                    showToast(getString(R.string.allfieldsrequired));
                }

                break;

            case R.id.forgot_login_now:
                onBackPressed();
                break;

        }

    }


    private boolean validateFormInputFieldsAll() {

        boolean isValid = true;

        if (TextUtils.isEmpty(mEmail.getEditText().getText())) {
            TextInputUtil.setError(mEmail, getString(R.string.emailempty));
            isValid = false;
        } else if (!ValidationUtil.isEmail(mEmail.getEditText().getText())) {
            TextInputUtil.setError(mEmail, getString(R.string.emailnotvalid));
            isValid = false;
        } else {
            TextInputUtil.setDisableError(mEmail);
        }

        return isValid;

    }

}
