package com.tsc.robocontroller.core;

public class Commands {

    /*
     * 0 - up
     * 1 - right
     * 2 - down
     * 3 - left
     * 4 - halt
     * 5 - set motor speed (150 - 255)
     * "71"+ any integer = rotate left by specified integer (in degrees) (eg, 7190)
     * "72" + any integer = rotate right by specified integer (in degrees) (eg, 72180)
     */

    public static final int MOVE_UP = 0;
    public static final int MOVE_RIGHT = 1;
    public static final int MOVE_DOWN = 2;
    public static final int MOVE_LEFT = 3;
    public static final int HALT = 4;

    public static final int SET_MSPEED = 5;
    public static final int MODE_MANUAL = 8;
    public static final int MODE_AUTO = 9;

    public static int getLeft(int degrees) {
        return Integer.parseInt("71"+degrees);
    }

    public static int getRight(int degrees) {
        return Integer.parseInt("72"+degrees);
    }

}
