package com.tsc.robocontrollermqtt.core;

import android.content.SharedPreferences;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.tsc.robocontrollermqtt.utils.Commands;
import com.tsc.robocontrollermqtt.utils.Prefs;

public class Task implements View.OnTouchListener, Runnable, SharedPreferences.OnSharedPreferenceChangeListener {

    private Handler handler;
    private String taskType;
    private String ip;
    int rotAngle;

    public Task(String taskType, SharedPreferences preferences) {
        this.taskType = taskType;
        rotAngle = preferences.getInt(Prefs.KEY_ROTATIONS, Prefs.DEF_ROTATIONS);
        ip = preferences.getString(Prefs.KEY_IP, Prefs.DEF_IP);
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void begin() {

    }

    public void stop() {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.performClick();
                if(handler != null) return true;
                handler = new Handler();
                handler.postDelayed(this, 300);
                break;
            case MotionEvent.ACTION_UP:
                if(handler == null) return true;
                handler.removeCallbacks(this);
                handler = null;
                // send halt
                break;
        }
        return false;
    }

    @Override
    public void run() {
        switch (taskType) {
            case Commands.MOVE_UP:
                break;
            case Commands.MOVE_RIGHT:
                break;
            case Commands.MOVE_DOWN:
                break;
            case Commands.MOVE_LEFT:
                break;
        }

        // handler.postDelayed(this, 300); // uncomment for continuous input
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(Prefs.KEY_ROTATIONS))
            rotAngle = sharedPreferences.getInt(key, Prefs.DEF_ROTATIONS);
        else if(key.equals(Prefs.KEY_IP))
            ip = sharedPreferences.getString(key, Prefs.DEF_IP);
    }
}
