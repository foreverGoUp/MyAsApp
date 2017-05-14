package com.kc.activity;

import android.database.Observable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.kc.custom.CstTextView;
import com.kc.myasapp.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final CstTextView textView = new CstTextView(this);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setTextSize(60);
        textView.setLayoutParams(lp);
        textView.setText("Scroller");
        textView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        setContentView(textView);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                textView.handleY(200);
//            }
//        },2000);


    }
}
