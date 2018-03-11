package com.example.alexj.clientps10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {


    Client client;
    String ip = "192.168.1.105";
    int port = 10001;

    BroadcastReceiver receiver;
    String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                answer = intent.getStringExtra(Client.ANSWER_KEY);
                Toast.makeText(getApplicationContext(), answer, Toast.LENGTH_LONG).show();
            }
        };

        IntentFilter updateIntentFilter = new IntentFilter(Client.ACTION_ANSWER);
        registerReceiver(receiver, updateIntentFilter);


        try {

            client = new Client(ip, port, getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }


        ((Button) findViewById(R.id.button_send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                String hexColor = "#FFFFFFFF";
                Command command = new Command(ConstantsKey.Color, hexColor);

                try {
                    client.SendCommand(command);
                    Toast.makeText(getApplicationContext(), command.toString(), Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.button_connect).

                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //client.StartReceive();
                        try {
                            client.Connect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}


