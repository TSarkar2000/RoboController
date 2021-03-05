package com.tsc.robocontroller.core;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class Task implements View.OnTouchListener, Runnable {

    private Handler handler;
    private final int taskType;

    private NodeMCU mcu = NodeMCU.getInstance();

    public Task(int taskType) {
        this.taskType = taskType;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.performClick();
                if(handler != null) return true;
                handler = new Handler();
                handler.postDelayed(this, 500);
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
                break;
            case Commands.MOVE_RIGHT:
                break;
            case Commands.MOVE_DOWN:
                break;
            case Commands.MOVE_LEFT:
                break;
        }
        handler.postDelayed(this, 500);
    }
}
