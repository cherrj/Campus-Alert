package com.example.xi.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;

public class newMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);

        //Go to Report Incident page
        ImageButton IncidentreportImgBtn=(ImageButton)findViewById(R.id.ReportIncidentImgBtn);
        IncidentreportImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent startChooseIncident=new Intent(getApplicationContext(), chooseIncidentActivity.class);

                startActivity(startChooseIncident);
            }
        });

        //go to San Jose Police Department website
        ImageButton SJPDImgBtn=(ImageButton)findViewById(R.id.SJSUpoliceImgBtn);
        SJPDImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String SJPD="http://www.sjpd.org/";
                Uri SJPDsite=Uri.parse(SJPD);

                Intent gotoSJPDsite=new Intent(Intent.ACTION_VIEW, SJPDsite);
                if(gotoSJPDsite.resolveActivity(getPackageManager())!=null){
                    startActivity(gotoSJPDsite);
                }
            }
        });

        //go to San Jose local news website
        ImageButton SJNewsImgBtn=(ImageButton)findViewById((R.id.cityNewsImgBtn));
        SJNewsImgBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String SJNEWS="https://www.mercurynews.com/location/san-jose/";
                Uri SJmercury=Uri.parse(SJNEWS);

                Intent gotoMercury=new Intent(Intent.ACTION_VIEW, SJmercury);
                if(gotoMercury.resolveActivity(getPackageManager())!=null){
                    startActivity(gotoMercury);
                }
            }
        }));

        //go to San Jose State University Campus Alert website
        ImageButton SJSUalert=(ImageButton)findViewById(R.id.SJSUNewsImgBtn);
        SJSUalert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String SJSUalert="http://www.sjsu.edu/police/crime_reporting/safety_alerts/";
                Uri SJSU=Uri.parse(SJSUalert);

                Intent gotoSJSUAlert=new Intent(Intent.ACTION_VIEW, SJSU);
                if(gotoSJSUAlert.resolveActivity(getPackageManager())!=null){
                    startActivity(gotoSJSUAlert);
                }
            }
        });


        SwipeButton swipeButton=(SwipeButton)findViewById(R.id.swipe_btn);
        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                //Toast.makeText(newMainActivity.this, "Active: "+active, Toast.LENGTH_SHORT ).show();
                String phone = "911";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);

            }
        });
    }
}
