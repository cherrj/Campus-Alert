package com.example.xi.myapplication;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppCompatActivity{

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;

    private TextView[] mDots;

    private  Button mNextBtn;
    private Button mBackBtn;
    private int mCurrentPage;


    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro1);

        mSlideViewPager=(ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout=(LinearLayout) findViewById(R.id.dotslayout);

        sliderAdapter=new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListner);

        mBackBtn=(Button) findViewById(R.id.previousBnt);
        mNextBtn=(Button) findViewById(R.id.nextBnt);


        //Onclick Listener

            mNextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent gotoMainActivity=new Intent(getApplicationContext(), newMainActivity.class);
                    String incidentCategory="crime";

                    startActivity(gotoMainActivity);
                }
            });
    }

    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        mDotLayout.removeAllViews();

        for(int i=0; i<mDots.length; i++){
            mDots[i]=new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;" ));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            mDotLayout.addView(mDots[i]);
        }
        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListner=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }


        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mCurrentPage=i;

            if(i==0){
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(false);
                mBackBtn.setVisibility(View.INVISIBLE);
                mNextBtn.setText("Next");
                mBackBtn.setText("");
            }else if(i==mDots.length-1){
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setText("Finish");
                mBackBtn.setText("Back");
            }else{
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);
                mNextBtn.setText("Next");
                mBackBtn.setText("Back");

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}