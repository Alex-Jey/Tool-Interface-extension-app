package com.example.alexj.clientps10;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import static com.example.alexj.clientps10.ConstantsKey.IP_KEY;
import static com.example.alexj.clientps10.ConstantsKey.PORT_KEY;

public class ServiceReceiver extends Service {

    InetSocketAddress endPoint;
    Client client;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String ip = intent.getStringExtra(IP_KEY);
        InetAddress IP = null;
        try {
            IP = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        int port = intent.getIntExtra(PORT_KEY, 0);
        endPoint = new InetSocketAddress(IP, port);

      //  client = new Client(getApplicationContext());
        //client.execute(endPoint);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        client.cancel(true);
        client = null;
    }






    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




}
