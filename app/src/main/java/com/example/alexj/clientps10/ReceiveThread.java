package com.example.alexj.clientps10;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * Created by alexj on 04.03.2018.
 */

public class ReceiveThread extends AsyncTask<InetSocketAddress, String, Void> {

    private boolean isRun = false;
    private String answer;
    private BufferedReader reader;
    private LocalBroadcastManager broadcastManager;
    private char[] Data;
    private Socket socket;
    private Context context;

    ReceiveThread(Context context) {
        this.context = context;
    }

    @Override
    protected void onProgressUpdate(String... values) {

        Toast.makeText(context, values[0], Toast.LENGTH_LONG).show();
        super.onProgressUpdate(values);
    }

    public void setRun(boolean isRun) {
        this.isRun = isRun;
    }

    public void CloseSocket() throws IOException {
        socket.close();
        socket = null;
        //  socket.
    }

    @Override
    protected Void doInBackground(InetSocketAddress... params) {

        isRun = true;

        int port = params[0].getPort();
        String IP = params[0].getHostName();

        try {
            socket = new Socket(IP, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (isRun) {
                Data = new char[1024];
                if (reader.read(Data) > 0) {
                    String data = String.valueOf(Data);
                    publishProgress(data);
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
