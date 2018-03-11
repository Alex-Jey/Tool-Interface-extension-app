package com.example.alexj.clientps10;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ServerService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new ReceiveThread(getApplicationContext()).execute();
        return super.onStartCommand(intent, flags, startId);
    }

    public ServerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
