package com.example.seekm.studemts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seekm.studemts.MobileV;
import com.example.seekm.studemts.R;
import com.example.seekm.studemts.SliderAdapter;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.List;

public class OnBoarding_1 extends AppCompatActivity {
    private FloatingActionButton mNext;

    private ViewPager mViewPager;
    private LinearLayout mLinearLayout;

    private SliderAdapter sliderAdapter;

    private ViewPager.OnPageChangeListener pageChangedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_1);



        mViewPager = (ViewPager)findViewById(R.id.slideViewPager);

        sliderAdapter = new SliderAdapter(this);
        mViewPager.setAdapter(sliderAdapter);

        DotsIndicator dotsIndicator = (DotsIndicator) findViewById(R.id.dots_indicator);
        dotsIndicator.setViewPager(mViewPager);

        mNext = (FloatingActionButton)findViewById(R.id.floatingActionButton2);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(OnBoarding_1.this,MobileV.class);
            }
        });


    }
    void startNewActivity(Activity one, Class two){
        Intent intent = new Intent(one,two);
        startActivity(intent);
    }

}
