package com.example.alexj.clientps10;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.IOException;

import java.util.concurrent.ExecutionException;

import static com.example.alexj.clientps10.ConstantsKey.ACTION_ANSWER;
import static com.example.alexj.clientps10.ConstantsKey.ANSWER_KEY;

public class MainActivity extends AppCompatActivity  {


    String ip = "192.168.1.105";
    int port = 10001;

    BroadcastReceiver commandReceiver;
    Client thread;
    FragmentTransaction transaction;

    private FrameLayout dynamicContainer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        commandReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                intent.getBundleExtra("");
                switch (intent.getStringExtra(ConstantsKey.COMMAND_TYPE))
                {
                    case (ConstantsKey.Restart_KEY):
                        ip = intent.getStringExtra(ConstantsKey.IP_KEY);
                        port = intent.getIntExtra(ConstantsKey.PORT_KEY, 10001);
                        thread = new Client(getApplicationContext(), ip, port);
                        if  (thread.Connect()){
                            Toast.makeText(getApplicationContext(), "Подключение установлено", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Подключение не установлено", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case (ConstantsKey.Send_KEY):
                        String command = intent.getStringExtra(ConstantsKey.Command);
                        try {
                            thread.SendCommand(command);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };

    }


    @Override
    protected void onStop() {
        unregisterReceiver(commandReceiver);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Передает комманды от фрагментов потоку сокета
        IntentFilter sendIntentFilter = new IntentFilter(ConstantsKey.ACTION_SEND);
        registerReceiver(commandReceiver, sendIntentFilter);

        //Фрагмент с настройками порта и ip
        findViewById(R.id.button_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.dynamics_tool_frame, new FragmentSettings());
                transaction.commit();
            }
        });

        findViewById(R.id.button_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    thread.SendCommand("Color#FFFFFFFF");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //Фрагмет Color Piker
        findViewById(R.id.button_color).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /*
                        String serverColor = "#F02F6DA3";
                        int color = android.graphics.Color.parseColor(serverColor);
                        SimpleColorWheelDialog.build()
                                .color(color)
                                .alpha(true)
                                .show(activity, SimpleColorWheelDialog.TAG);
                                */

                      transaction = getFragmentManager().beginTransaction();
                      transaction.replace(R.id.dynamics_tool_frame, new FragmentColorWheel());
                      transaction.commit();
                    }
                });

    }



}


