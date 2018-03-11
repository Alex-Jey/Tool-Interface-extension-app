package com.example.alexj.clientps10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by alexj on 21.02.2018.
 */

public class SenderThread extends AsyncTask<String, Void, Boolean> {

    InetSocketAddress endPoint;

    public SenderThread(InetSocketAddress address) {
        endPoint = address;
    }

    @Override
    protected Boolean doInBackground(String... values) {
        try {
            Socket socket = new Socket(endPoint.getHostName(), endPoint.getPort());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            byte[] outMsg = values[0].getBytes("UTF8");
            dataOutputStream.write(outMsg);
            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();

            return true;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
