package com.example.alexj.clientps10;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.annotation.Nullable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;


public class ServiceSend extends IntentService {


    public ServiceSend() {
        super("ServiceSend");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        String ip = intent.getStringExtra(Client.IP_KEY);
        int port = intent.getIntExtra(Client.PORT_KEY, 0);
        String message = intent.getStringExtra(Client.ANSWER_KEY);


        InetSocketAddress endPoint = new InetSocketAddress(ip, port);

        try {
            if(new SenderThread(endPoint).execute(message).get())
             this.stopSelf();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {


    }


}
