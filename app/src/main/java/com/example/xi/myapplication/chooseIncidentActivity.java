package com.example.xi.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Callback;

import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;

import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.*;
import org.apache.http.Header;

public class chooseIncidentActivity extends AppCompatActivity {
    //capture image define variables
    private final int VIDEO_REQUEST_CODE =200;
    OkHttpClient client = new OkHttpClient();


    //upload image define variables
    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = chooseIncidentActivity.class.getSimpleName();
    private String selectedFilePath;
    private String SERVER_URL = "http://192.168.0.111:8080/";
    ImageView ivAttachment;
    Button bUpload;
    TextView tvFileName;


    //upload file
    final String uploadFilePath = "/mnt/sdcard/";
    final String uploadFileName = "service_lifecycle.png";
    TextView messageText;
    Button uploadButton;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    String upLoadServerUri = null;

    //location service
    private Button button;
    private LocationManager locationManager;
    private LocationListener listener;
    private int clickCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_incident);

        ImageButton crimeImgBtn=(ImageButton)findViewById(R.id.crimeimgBtn);
        crimeImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotorefortInfo=new Intent(getApplicationContext(), getInfoActivity.class);
                String incidentCategory="crime";

                startActivity(gotorefortInfo);
            }
        });

        ImageButton medicalImgBtn=(ImageButton)findViewById(R.id.medicalimgBtn);
        medicalImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotorefortInfo=new Intent(getApplicationContext(), getInfoActivity.class);
                String incidentCategory="medical";

                startActivity(gotorefortInfo);
            }
        });

        ImageButton fireImgBtn=(ImageButton)findViewById(R.id.fireImgBtn);
        fireImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotorefortInfo=new Intent(getApplicationContext(), getInfoActivity.class);
                String incidentCategory="fire";

                startActivity(gotorefortInfo);
            }
        });



        //image capture
        ImageButton SOSImgBtn=(ImageButton)findViewById(R.id.SOSImgBtn);
        SOSImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureVideo(view);
            }
        });

        //location service
        //textView = (TextView) findViewById(R.id.showlocationTextView);
        button = (Button) findViewById(R.id.sendlocationBtn);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                /*textView.append("Your current longitude is " + location.getLongitude() + "\n " +
                        "Your current Latitude is "+ location.getLatitude());*/

                Toast.makeText(chooseIncidentActivity.this, "Longitude: " + location.getLongitude() + "\n " +
                        "Latitude: "+ location.getLatitude(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        configure_button();

    }


    //*********capture video*************
    public  void captureVideo(View view){
        Intent camera_intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File video_file=getFilepath();
        Uri video_uri=Uri.fromFile(video_file);
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, video_uri);
        camera_intent.putExtra("android.intent.extra.durationLimit", 10000);//record 10 seconds
        camera_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        if (camera_intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(camera_intent, VIDEO_REQUEST_CODE);
        }

    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==VIDEO_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Toast.makeText(getApplicationContext(), "Video Successfully Recorded", Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), data.getData().getPath(), Toast.LENGTH_LONG).show();
                uploadFile(new File(getRealPathFromURI(data.getData())));

            }else{
                Toast.makeText(getApplicationContext(), "Video Recording Failed", Toast.LENGTH_LONG).show();
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



    //************get location**************
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        // this code won'textView execute IF permissions are not allowed, because in the line above there is return statement.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if(clickCount==0) {
                    if (ActivityCompat.checkSelfPermission(chooseIncidentActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(chooseIncidentActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                        //获取当前可用的位置控制器
                        List<String> list = locationManager.getProviders(true);

                        if (list.contains(LocationManager.GPS_PROVIDER)) {
                            //是否为GPS位置控制器
                            String provider = LocationManager.GPS_PROVIDER;
                        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
                            //是否为网络位置控制器
                            String provider = LocationManager.NETWORK_PROVIDER;

                        } else {
                            Toast.makeText(chooseIncidentActivity.this, "Please check if GPS is allowed",
                                    Toast.LENGTH_LONG).show();
                        }
                        return;
                    }
                    clickCount++;
                    button.setText("PRESS TO DISABLE LOCATION SERVICE");
                } else if(clickCount==1){
                    button.setText("PRESS TO ENABLE LOCATION SERVICE");
                    locationManager.removeUpdates(listener);
                    clickCount--;
                }

                if(clickCount==1) {
                    locationManager.requestLocationUpdates("gps", 5000, 0, listener);
                }else{

                }
            }
        });
    }


    //***************upload file**************
    public void uploadFile(File file) {
        try {
            if (file.exists() && file.length() > 0) {
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("tempfile", file);
                client.post(SERVER_URL, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                        Toast.makeText(getApplicationContext(), "Upload complete!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_LONG).show();
                    }
                });
            } else {

                Toast.makeText(getApplicationContext(), "invalid", Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage()+"aloha", Toast.LENGTH_LONG).show();
        }
        /*
        try {

            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("video/mp4"), file))
                    .build();
            Request request = new Request.Builder().url(SERVER_URL).post(formBody).build();
            Response response = this.client.newCall(request).execute();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call request, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if (!response.isSuccessful()) {
                        // Handle the error
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    }
                    // Upload successful
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();

                }
            });
            Toast.makeText(getApplicationContext(), response.message()+"hello", Toast.LENGTH_LONG).show();
            //System.out.println(response.isSuccessful());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage()+"aloha", Toast.LENGTH_LONG).show();
            //System.out.println(e);
        }*/

    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    /*
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }*/

}
