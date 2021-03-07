package com.tsc.robocontroller;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tsc.robocontroller.utils.Prefs;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ParentActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        preferences = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            StringBuilder sb = new StringBuilder("These permissions were denied:\n");
                            for (PermissionDeniedResponse pr : multiplePermissionsReport.getDeniedPermissionResponses())
                                sb.append(pr.getPermissionName().replace("android.permission.", "")).append("\n");
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
                            new Worker().execute(preferences.getString(Prefs.KEY_IP, Prefs.DEF_IP));
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    boolean flag = false;

    @Override
    protected void onPause() {
        super.onPause();
        flag = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(flag)
           new Worker().execute(preferences.getString(Prefs.KEY_IP, Prefs.DEF_IP));
        flag = false;
    }

    class Worker extends AsyncTask<String, Void, Void> {
        
        private ProgressBar progressBar;
        private String str, title = "", ip;
        private boolean connectionEstablished = false;

        @Override
        protected void onPreExecute() {
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.INVISIBLE);
            if (!connectionEstablished) {
                if (str.contains("ENETUNREACH")) {
                    title = "Network unreachable";
                    str = str + "\n Please enable mobile hotspot & retry";
                } else if (str.equals("Host unreachable")) {
                    title = str;
                    str = "Please check whether:" +
                            "\n 1) NodeMCU is powered on & listening on port 9999;" +
                            "\n 2) IP addr: " + ip + " is correct.";
                }
                new AlertDialog.Builder(ParentActivity.this)
                        .setTitle(title)
                        .setMessage(str)
                        .setPositiveButton("Retry", (dialog, which) -> {
                            new Worker().execute(ip);
                        })
                        .setNegativeButton("Change IP addr", (dialog, which) -> {
                            dialog.dismiss();
                            startActivity(new Intent(ParentActivity.this, SettingsActivity.class));
                        }).setCancelable(false)
                        .create()
                        .show();
            } else {
                Toast.makeText(ParentActivity.this, str, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ParentActivity.this, MainActivity.class));
                finish();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                ip = strings[0];
                Socket s = new Socket(ip, 9999);
                str = "Connected!";
                connectionEstablished = s.isConnected();

            } catch (IOException e) {
                str = e.getLocalizedMessage();
            }

            return null;
        }
    }
}