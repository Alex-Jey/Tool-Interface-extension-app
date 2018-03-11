package com.example.alexj.clientps10;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by alexj on 26.02.2018.
 */

public class Client {


    public static String PORT_KEY = "Port";
    public static String IP_KEY = "IPAdress";
    public static String ANSWER_KEY = "ANSWER";
    public static String ACTION_ANSWER = "ActionAnswer";

    InetAddress IP;
    int port;
    Socket socket;
    Context context;
    InetSocketAddress endPoint;
    String answer;
    DataOutputStream dataOutputStream;
    private BufferedReader reader;
    boolean isRun = false;

    Intent receiveIntent;
    private OnMessageReceived mMessageListener = null;

    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

    Client(String ip, int port, Context context) throws IOException {
        ip = "192.168.1.100";
        port = 10001;
        this.IP = InetAddress.getByName(ip);
        this.port = port;
        this.context = context;
        endPoint = new InetSocketAddress(IP, port);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void Connect() throws IOException {

        receiveIntent = new Intent(context, ServiceReceiver.class);
        receiveIntent.putExtra(PORT_KEY, port);
        receiveIntent.putExtra(IP_KEY, endPoint.getHostString());
        context.startService(receiveIntent);

        //new ReceiveThread(context).execute(endPoint);
    }

    //Вернет свой адрес, может пригодится
    public String getLocalIpAddress() {
        @SuppressLint("WifiManagerPotentialLeak") WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
        assert wm != null;
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }


    //Отправка команд серверу
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void SendCommand(final Command command) throws IOException, ExecutionException, InterruptedException {

        if (receiveIntent !=null)
        context.stopService(receiveIntent);



        Intent sendIntent = new Intent(context, ServiceSend.class);
        sendIntent.putExtra(IP_KEY, endPoint.getHostString());
        sendIntent.putExtra(PORT_KEY, port);
        sendIntent.putExtra(ANSWER_KEY, command.toString());
        context.startService(sendIntent);

        if (receiveIntent !=null)
         Connect();


    }

}
