package com.example.xi.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;
//import com.google.android.gms;

 import java.util.List;
 import android.os.Handler;

public class getInfoActivity extends AppCompatActivity {

    private int IMAGE_REQUEST_CODE=300;

    private Button submitBtn;
    private EditText peopleNumberInput;
    private EditText phoneNumberInput;
    private EditText addressInput;

    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private ImageView mImageView;


    int peopleNumber, phoneNumber;
    String address;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_info);
        Button submit=(Button) findViewById(R.id.submitBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getInfoActivity.this, "Submitted!", Toast.LENGTH_LONG).show();
            }
        });

        ImageButton uploadImage=(ImageButton) findViewById(R.id.uploadPhotoBtn);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage(view);
            }
        });

    }

    public void StoreInputString(){


    }

    public  void captureImage(View view){
        Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File video_file=getFilepath();
        Uri video_uri=Uri.fromFile(video_file);

        if (camera_intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(camera_intent, IMAGE_REQUEST_CODE);
        }

    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==IMAGE_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Toast.makeText(getApplicationContext(), "Image Successfully Captured", Toast.LENGTH_LONG).show();
                //uploadFile(new File(data.getData().getPath()));
            }else{
                Toast.makeText(getApplicationContext(), "Image Capture Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    public File getFilepath(){
        File folder=new File("sdcard/video_app");
        if(folder.exists()){
            folder.mkdir();
        }
        File video_file=new File(folder, "sample_video.mp4");

        return video_file;
    }
}
