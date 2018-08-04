package com.digitfellas.ac.dashboard.customerservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.digitfellas.ac.R;
import com.digitfellas.ac.base.BaseActivity;

public class PostServiceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_service);

        setButterKnife();
        setBackButtonEnabledAndTitle("Post your service");
    }
}
