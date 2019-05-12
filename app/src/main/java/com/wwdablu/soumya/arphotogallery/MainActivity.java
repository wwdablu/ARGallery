package com.wwdablu.soumya.arphotogallery;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;

    private ARGalleryFragment arGalleryFragment;
    private boolean rotateEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arGalleryFragment = (ARGalleryFragment) getSupportFragmentManager()
            .findFragmentById(R.id.arfrag_gallery);

        findViewById(R.id.btn_rotate).setOnClickListener(rotateButtonClickListener);

        //Check for permission to access gallery
        if(!GalleryUtil.isPermitted(this)) {
            ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            handleGalleryPermissionObtained();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Need permission to gallery.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        handleGalleryPermissionObtained();
    }

    private void handleGalleryPermissionObtained() {
        //
    }

    private View.OnClickListener rotateButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            rotateEnable = !rotateEnable;
            arGalleryFragment.rotate(rotateEnable);
        }
    };
}
