package com.tsc.robocontroller;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class ParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            StringBuilder sb = new StringBuilder("These permissions were denied:\n");
                            for(PermissionDeniedResponse pr: multiplePermissionsReport.getDeniedPermissionResponses())
                                sb.append(pr.getPermissionName().replace("android.permission.","")).append("\n");
                            sb.append("And hence, you can not proceed.");

                            AlertDialog.Builder builder = new AlertDialog.Builder(ParentActivity.this)
                                    .setIcon(ContextCompat.getDrawable(ParentActivity.this, R.drawable.info))
                                    .setTitle("Permissions denied!")
                                    .setMessage(sb.toString())
                                    .setPositiveButton("Grant Permissions", (dialog, which) -> {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .setNegativeButton("Quit", (dialog, which) -> finish());
                            builder.show();
                        } else {
                            //todo: connect to nodemcu & then start activity
                            startActivity(new Intent(ParentActivity.this, MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
}