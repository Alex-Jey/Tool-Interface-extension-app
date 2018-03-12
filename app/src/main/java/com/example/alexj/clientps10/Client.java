package com.example.alexj.clientps10;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.Formatter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.xml.transform.SourceLocator;

import static android.content.Context.WIFI_SERVICE;
import static com.example.alexj.clientps10.ConstantsKey.ANSWER_KEY;
import static com.example.alexj.clientps10.ConstantsKey.IP_KEY;
import static com.example.alexj.clientps10.ConstantsKey.PORT_KEY;

/**
 * Created by alexj on 04.03.2018.
 */

public class Client extends AsyncTask<Void, String, Void> {

    private boolean isRun = false;
    private String answer;
    private BufferedReader reader;
    LocalBroadcastManager broadcastManager;
    BroadcastReceiver broadcastReceiver;

    private char[] Data;
    private Socket socket;
    private Context context;
    SocketAddress sockaddr;
    DataOutputStream dataOutputStream;

    Receive receiveThread;


    int port;
    String IP;


    Client(Context context, String IP, int port) {
        this.context = context;
        socket = null;
        this.IP = IP;
        this.port = port;
        sockaddr = new InetSocketAddress(IP, port);
        receiveThread = new Receive();

        broadcastManager = LocalBroadcastManager.getInstance(context);



        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                RestartReceive();

            }
        };

        IntentFilter filter = new IntentFilter(ConstantsKey.ACTION_BROADCAST);
        broadcastManager.registerReceiver(broadcastReceiver, filter);

    }



    private void RestartReceive() {
        if (receiveThread.getStatus() == Status.RUNNING || receiveThread != null || receiveThread.isCancelled()) {
            receiveThread.cancel(true);
            receiveThread = null;
            receiveThread = new Receive();
        }
        receiveThread.execute();
    }

    public boolean Connect() {

        if (receiveThread.getStatus() == Status.RUNNING || receiveThread != null || receiveThread.isCancelled()) {
            receiveThread.cancel(true);
            receiveThread = null;
            receiveThread = new Receive();
        }

        try {
            socket = new Connect().execute(sockaddr).get();
            if(socket!= null) {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (socket != null && reader != null && dataOutputStream != null){
            receiveThread.execute();
            return true;
        }

        return false;
    }

    class Connect extends AsyncTask<SocketAddress, Void, Socket> {

        @Override
        protected Socket doInBackground(SocketAddress... voids) {
            Socket socket = new Socket();
            try {
                socket.connect(voids[0], 1000);
                return socket;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public void SendCommand(final String command) throws IOException, ExecutionException, InterruptedException {
        if (socket != null) {
            isRun = false;
            dataOutputStream.write(command.getBytes("UTF8"));
            socket.close();
            Connect();
            isRun = true;
        }
    }

    class Receive extends AsyncTask<Void, String, String> {

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(context, values[0], Toast.LENGTH_LONG).show();

            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... voids) {
            Data = new char[1024];
            try {
                if (reader.read(Data) > 0) {
                    String result = String.valueOf(Data);
                    publishProgress(result);
                    broadcastManager.sendBroadcast(new Intent(ConstantsKey.ACTION_BROADCAST));
                    return result;
                    //String data = String.valueOf(Data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    @Override
    protected Void doInBackground(Void... params) {

        while (true) {


        }
    }

}
