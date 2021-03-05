package com.tsc.robocontroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.slider.Slider;
import com.tsc.robocontroller.core.Commands;
import com.tsc.robocontroller.core.NodeMCU;
import com.tsc.robocontroller.core.Task;

public class MainActivity extends AppCompatActivity {

    private Button upBtn, rightBtn, downBtn, leftBtn;
    private Slider slider;

    private void initWidgets() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Manual Mode");

        upBtn = findViewById(R.id.upBtn);
        rightBtn = findViewById(R.id.rightBtn);
        downBtn = findViewById(R.id.downBtn);
        leftBtn = findViewById(R.id.leftBtn);

        slider = findViewById(R.id.slider);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();

        upBtn.setOnTouchListener(new Task(Commands.MOVE_UP));
        rightBtn.setOnTouchListener(new Task(Commands.MOVE_RIGHT));
        downBtn.setOnTouchListener(new Task(Commands.MOVE_DOWN));
        leftBtn.setOnTouchListener(new Task(Commands.MOVE_LEFT));

        slider.addOnSliderTouchListener(onSliderTouchListener);
    }

    private final Slider.OnSliderTouchListener onSliderTouchListener = new Slider.OnSliderTouchListener() {
        @Override
        public void onStartTrackingTouch(@NonNull Slider slider) {

        }

        @Override
        public void onStopTrackingTouch(@NonNull Slider slider) {
            NodeMCU.getInstance().sendCommand(Commands.SET_MSPEED, (int) slider.getValue());
        }
    };

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
                startActivity(new Intent(this, AutoActivity.class));
                finish();
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
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