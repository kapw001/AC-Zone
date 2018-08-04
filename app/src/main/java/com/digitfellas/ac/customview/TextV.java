package com.digitfellas.ac.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class TextV extends android.support.v7.widget.AppCompatTextView {

    public TextV(Context context) {
        super(context);
        init();
    }

    public TextV(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public TextV(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){

//        https://android-arsenal.com/details/1/4603



        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        float w = getResources().getDisplayMetrics().densityDpi;

        setTextSize(16*w/160);



    }

}
