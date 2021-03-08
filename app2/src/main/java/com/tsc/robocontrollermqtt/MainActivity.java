package com.tsc.robocontrollermqtt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

public class MainActivity extends AppCompatActivity {

    private Button upBtn;
    private Button rightBtn;
    private Button downBtn;
    private Button leftBtn;
    private Button powerBtn;
    private Slider slider;
    private TextView mainDisplay;

   // private final NodeMCU mcu = NodeMCU.getInstance();
    private SharedPreferences preferences;

    int rotAngle = 0;

    private void initWidgets() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Manual Mode");

        upBtn = findViewById(R.id.upBtn);
        rightBtn = findViewById(R.id.rightBtn);
        downBtn = findViewById(R.id.downBtn);
        leftBtn = findViewById(R.id.leftBtn);
        powerBtn = findViewById(R.id.ctrlBtn);
//        powerBtn.setOnClickListener(v -> {
//            Task t = new Task("", preferences);
//            if (powerBtn.getText().equals("start")) {
//                enableViews(true);
//                powerBtn.setText("stop");
//                t.begin();
//            } else {
//                enableViews(false);
//                powerBtn.setText("start");
//                t.stop();
//            }
//       });

        slider = findViewById(R.id.slider);

        mainDisplay = findViewById(R.id.mainDisplay);

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    private void enableViews(boolean enable) {
        upBtn.setEnabled(enable);
        rightBtn.setEnabled(enable);
        leftBtn.setEnabled(enable);
        downBtn.setEnabled(enable);
        slider.setEnabled(enable);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getApplicationContext().getSharedPreferences("settings", MODE_PRIVATE);

        initWidgets();

//        rotAngle = preferences.getInt(Prefs.KEY_ROTATIONS, 0);
//
//        upBtn.setOnTouchListener(new Task(Commands.MOVE_UP, preferences));
//        rightBtn.setOnTouchListener(new Task(Commands.MOVE_RIGHT, preferences));
//        downBtn.setOnTouchListener(new Task(Commands.MOVE_DOWN, preferences));
//        leftBtn.setOnTouchListener(new Task(Commands.MOVE_LEFT, preferences));
//
//        slider.addOnSliderTouchListener(onSliderTouchListener);
//        mcu.setOnCommandSentListener(onCommandSentListener);
    }

    private final Slider.OnSliderTouchListener onSliderTouchListener = new Slider.OnSliderTouchListener() {
        @Override
        public void onStartTrackingTouch(@NonNull Slider slider) {

        }

        @Override
        public void onStopTrackingTouch(@NonNull Slider slider) {
           // mcu.sendCommand(Commands.SET_MSPEED, (int) slider.getValue());
        }
    };

//    private final StringBuilder sb = new StringBuilder();
//    private final NodeMCU.OnCommandSentListener onCommandSentListener = commandOrError -> {
//        sb.append(commandOrError).append("\n");
//        runOnUiThread(() -> {
//            mainDisplay.setText(sb.toString());
//        });
//    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.autoMode:
               // startActivity(new Intent(this, AutoActivity.class));
                finish();
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.clearLogs:
                mainDisplay.setText("");
              //  sb.delete(0, sb.length());
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Do you really want to exit ?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}