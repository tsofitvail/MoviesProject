package com.movieProject.movie.Movie;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.movieProject.movie.Models.MovieModel;
import com.movieProject.movie.R;
import com.movieProject.movie.Services.DownloadService;

public class DownloadActivity extends AppCompatActivity {

    public static final String PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int PERMISSIONS_REQUEST_CODE = 42;
    public static final String URL_IMAGE="urlImage";
    public static final String ARG_FILE_PATH = "Image-File-Path";
    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        if (isPermissionGranted()) {
            startDownloadService();
        } else {
            showExplainingRationaleDialog();
        }
        broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(), "Download Completed", Toast.LENGTH_SHORT).show();
                String filePath = intent.getStringExtra(ARG_FILE_PATH);
                if (!TextUtils.isEmpty(filePath)) {
                    showImage(filePath);
                }

            }
        };
    }

    private void showImage(String filePath) {
        ImageView imageView=findViewById(R.id.imageMovie);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        imageView.setImageBitmap(bitmap);
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(DownloadService.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    private void startDownloadService() {
        String movieUrl=getIntent().getStringExtra(URL_IMAGE);
        DownloadService.startService(this,movieUrl);
    }

    public static void startActivity(Context context, String backImage){
        Intent intent = new Intent(context, DownloadActivity.class);
        intent.putExtra(URL_IMAGE, backImage);
        context.startActivity(intent);
    }

    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, PERMISSION)== PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
      //  if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION)) {
        //    showExplainingRationaleDialog();
     //   } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{PERMISSION}, PERMISSIONS_REQUEST_CODE);
            startDownloadService();
     //   }
    }

    private void showExplainingRationaleDialog(){
        new AlertDialog.Builder(this)
                .setMessage(
                        "We need access to your storage so you can download images")
                .setTitle("Dear User")
                .setPositiveButton(
                        "Got It",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermission();
                            }
                        })
                .setNegativeButton(
                        "No,Thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    finishAffinity();
                                } else {
                                    finish();
                                }
                            }
                        }
                )
                .setCancelable(false)
                .create()
                .show();

    }


}
