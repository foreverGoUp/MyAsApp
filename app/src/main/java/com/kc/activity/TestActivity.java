package com.kc.activity;

import android.os.Bundle;

import com.daShen.custom.timeAxis.TimeAlgorithm;
import com.daShen.custom.timeAxis.TimeAxis;
import com.kc.base.BaseActivity;
import com.kc.myasapp.R;

public class TestActivity extends BaseActivity {

    TimeAxis timeAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        timeAxis = getViewById(R.id.timeAxis);
        timeAxis.setOnValueChangeListener(new TimeAxis.OnValueChangeListener() {
            @Override
            public void onValueChange(TimeAlgorithm _value) {

            }

            @Override
            public void onStartValueChange(TimeAlgorithm _value) {

            }

            @Override
            public void onStopValueChange(TimeAlgorithm _value) {

            }
        });

//        final CstTextView textView = new CstTextView(this);
//        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        textView.setTextSize(60);
//        textView.setLayoutParams(lp);
//        textView.setText("Scroller");
//        textView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
//        setContentView(textView);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                textView.handleY(200);
//            }
//        },2000);


    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void init() {

    }

    @Override
    protected void initData() {

    }
}
