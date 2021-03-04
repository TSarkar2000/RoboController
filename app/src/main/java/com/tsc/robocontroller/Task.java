package com.tsc.robocontroller;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class Task implements View.OnTouchListener, Runnable {

    public static final int MOVE_UP = 0;
    public static final int MOVE_RIGHT = 1;
    public static final int MOVE_DOWN = 2;
    public static final int MOVE_LEFT = 3;

    private Handler handler;
    private final int delay, taskType;

    public Task(int taskType,int delay) {
        this.delay = delay;
        this.taskType = taskType;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.performClick();
                if(handler != null) return true;
                handler = new Handler();
                handler.postDelayed(this, delay);
                break;
                case MotionEvent.ACTION_UP:
                    if(handler == null) return true;
                    handler.removeCallbacks(this);
                    handler = null;
                    // call halt
                    break;
        }
        return false;
    }


    @Override
    public void run() {
        switch (taskType) {
            case MOVE_UP:
                break;
            case MOVE_RIGHT:
                break;
            case MOVE_DOWN:
                break;
            case MOVE_LEFT:
                break;
        }
        handler.postDelayed(this, delay);
    }
}
