package com.tsc.robocontroller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button upBtn, rightBtn, downBtn, leftBtn;

    private void initWidgets() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Manual Mode");

        upBtn = findViewById(R.id.upBtn);
        rightBtn = findViewById(R.id.rightBtn);
        downBtn = findViewById(R.id.downBtn);
        leftBtn = findViewById(R.id.leftBtn);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();

        upBtn.setOnTouchListener(new Task(Task.MOVE_UP, 500));
        rightBtn.setOnTouchListener(new Task(Task.MOVE_RIGHT, 500));
        downBtn.setOnTouchListener(new Task(Task.MOVE_DOWN, 500));
        leftBtn.setOnTouchListener(new Task(Task.MOVE_LEFT, 500));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

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
}