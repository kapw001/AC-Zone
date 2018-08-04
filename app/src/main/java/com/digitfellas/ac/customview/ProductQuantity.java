package com.digitfellas.ac.customview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.digitfellas.ac.R;

public class ProductQuantity extends LinearLayout implements View.OnClickListener, TextWatcher {

    private EditText mCount;
    private Button mAdd, mSub;

    private int quantity = 1;
    private int maxQuantity = 0;

    private QuantityCallBack mQuantityCallBack;

    public ProductQuantity(Context context) {
        super(context);
        initViews();
    }

    public ProductQuantity(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public ProductQuantity(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProductQuantity(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }


    private void initViews() {

        inflate(getContext(), R.layout.custom_layout_product_count, this);

        mAdd = (Button) findViewById(R.id.add_count);
        mSub = (Button) findViewById(R.id.sub_count);
        mCount = (EditText) findViewById(R.id.count);

        mAdd.setOnClickListener(this);
        mSub.setOnClickListener(this);

//        mCount.addTextChangedListener(this);

        mCount.setEnabled(false);
    }

    public void setmQuantityCallBack(QuantityCallBack mQuantityCallBack) {
        this.mQuantityCallBack = mQuantityCallBack;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;

        mCount.setText(this.quantity + "");

        if (mQuantityCallBack != null) {
            mQuantityCallBack.quantityCount(quantity);
        }
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public int getMaxQuantity(){return maxQuantity; }

    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.add_count:

                if (maxQuantity > quantity) quantity++;


                setQuantity(quantity);
                break;

            case R.id.sub_count:

                if (quantity > 1) quantity--;

                setQuantity(quantity);
                break;

        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {



        if (s.length() > 0) {


            int value=Integer.parseInt(s.toString());

            if (value > 0 && value <= maxQuantity) {

                this.quantity = Integer.parseInt(s.toString());

            } else {

                Toast.makeText(getContext(), "Please enter another value", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface QuantityCallBack {

        void quantityCount(int q);
    }
}
