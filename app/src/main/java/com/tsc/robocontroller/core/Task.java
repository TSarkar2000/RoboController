package com.tsc.robocontroller.core;

import android.content.SharedPreferences;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.tsc.robocontroller.utils.Prefs;

public class Task implements View.OnTouchListener, Runnable, SharedPreferences.OnSharedPreferenceChangeListener {

    private Handler handler;
    private String taskType;
    private String ip;
    int rotAngle;


    private final NodeMCU mcu = NodeMCU.getInstance();

    public Task(String taskType, SharedPreferences preferences) {
        this.taskType = taskType;
        rotAngle = preferences.getInt(Prefs.KEY_ROTATIONS, Prefs.DEF_ROTATIONS);
        ip = preferences.getString(Prefs.KEY_IP, Prefs.DEF_IP);
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void begin() {
        mcu.begin(ip, Commands.MODE_MANUAL);
    }

    public void stop() {
        mcu.stop();
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
                    mcu.sendCommand(Commands.HALT, -1);
                    break;
        }
        return false;
    }

    @Override
    public void run() {
        switch (taskType) {
            case Commands.MOVE_UP:
                mcu.sendCommand(Commands.MOVE_UP, -1);
                break;
            case Commands.MOVE_RIGHT:
                mcu.sendCommand(Commands.MOVE_RIGHT, rotAngle);
                break;
            case Commands.MOVE_DOWN:
                mcu.sendCommand(Commands.MOVE_DOWN, -1);
                break;
            case Commands.MOVE_LEFT:
                mcu.sendCommand(Commands.MOVE_LEFT, rotAngle);
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
