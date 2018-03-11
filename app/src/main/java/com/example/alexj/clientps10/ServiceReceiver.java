package com.example.alexj.clientps10;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class ServiceReceiver extends Service {

    InetSocketAddress endPoint;
    ReceiveThread receiveThread;

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            receiveThread.setRun(false);
            receiveThread.CloseSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
        receiveThread.cancel(true);
        receiveThread = null;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String ip = intent.getStringExtra(Client.IP_KEY);
        InetAddress IP = null;
        try {
            IP = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        int port = intent.getIntExtra(Client.PORT_KEY, 0);
        endPoint = new InetSocketAddress(IP, port);

        receiveThread = new ReceiveThread(getApplicationContext());
        receiveThread.execute(endPoint);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
