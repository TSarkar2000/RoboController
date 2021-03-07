package com.tsc.robocontroller.core;


import android.os.AsyncTask;

import com.tsc.robocontroller.utils.Prefs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class NodeMCU {

    private NodeMCU() {
    }

    private static NodeMCU nodeMCU;
    private OnCommandSentListener onCommandSentListener;
    private Queue<String> commandQueue = new LinkedList<>();

    private String ip = Prefs.DEF_IP;
    private boolean run = false;

    public static NodeMCU getInstance() {
        if (nodeMCU == null)
            nodeMCU = new NodeMCU();
        return nodeMCU;
    }

    public void setOnCommandSentListener(OnCommandSentListener listener) {
        onCommandSentListener = listener;
    }

    public void begin(String ip, String mode) {
        this.ip = ip;
        run = true;
        new ESPController().execute();
        onCommandSentListener.onCommandSent("Running..Waiting for commands.");
    }

    public void stop() {
        run = false;
        commandQueue.clear();
        onCommandSentListener.onCommandSent("Stopped.");
    }

    // set value = 0 if not required
    public void sendCommand(String comm, int value) {
        String data = "";
        switch (comm) {
            case Commands.MOVE_UP:
            case Commands.HALT:
            case Commands.MOVE_DOWN:
                data = comm;
                break;
            case Commands.MOVE_LEFT:
                data = "L" + value;
                break;
            case Commands.MOVE_RIGHT:
                data = "R" + value;
                break;
            case Commands.SET_MSPEED:
                data = "M" + value;
                break;
        }
        commandQueue.add(data);
    }

    class ESPController extends AsyncTask<Void, Void, Void> {

        private String cmd = Commands.HALT;

        @Override
        protected Void doInBackground(Void... voids) {
            try (
                    Socket s = new Socket(ip, 9999);
                    PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            ) {

                while (run) {
                    if (br.readLine().equals("GET")) {
                        if (!commandQueue.isEmpty())
                            cmd = commandQueue.poll();
                        out.println(cmd);

                        onCommandSentListener.onCommandSent(evaluateMsg(cmd));
                    } else
                        Thread.sleep(500);
                }

                onCommandSentListener.onCommandSent("Connection closed.");

            } catch (IOException | InterruptedException e) {
                onCommandSentListener.onCommandSent(e.getMessage());
            }
            return null;
        }

        private String evaluateMsg(String msg) {
            if (msg.startsWith("L"))
                return "Turning left by " + msg.replace("L", "") + " deg";
            else if (msg.startsWith("R"))
                return "Turning right by " + msg.replace("R", "") + " deg";
            else if (msg.startsWith("M"))
                return "Motor speed = " + msg.replace("M", "");
            else if (msg.equals(Commands.MOVE_UP))
                return "Command: UP";
            else if (msg.equals(Commands.MOVE_DOWN))
                return "Command: DOWN";
            else if (msg.equals(Commands.HALT))
                return "Command: HALT";
            return "Error!\n" + msg;
        }
    }

    public interface OnCommandSentListener {
        void onCommandSent(String commandOrError);
    }
}
