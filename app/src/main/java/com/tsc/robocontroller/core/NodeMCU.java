package com.tsc.robocontroller.core;

import android.content.Context;

public class NodeMCU {

    private NodeMCU() {
    }

    private static NodeMCU nodeMCU;

    public static NodeMCU getInstance() {
        if (nodeMCU == null)
            nodeMCU = new NodeMCU();
        return nodeMCU;
    }

    public boolean connect(Context ctx, String ip) {
        return false;
    }

    public void initialize(int mode) {

    }

    public void reset() {

    }

    // set value = -1 if not required
    public void sendCommand(int comm, int value) {
        switch (comm) {
            case Commands.MOVE_UP:
                break;
            case Commands.MOVE_DOWN:
                break;
            case Commands.MOVE_LEFT:
                break;
            case Commands.MOVE_RIGHT:
                break;
            case Commands.HALT:
                break;
            case Commands.SET_MSPEED:
                break;
        }
    }
}
