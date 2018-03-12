package com.example.alexj.clientps10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import static android.content.Context.WIFI_SERVICE;


public class FragmentSettings extends android.app.Fragment {


    private Context context;

    public FragmentSettings()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //Вернет свой адрес, может пригодится
    @RequiresApi(api = Build.VERSION_CODES.M)
    public String getLocalIpAddress() {
        @SuppressLint("WifiManagerPotentialLeak") WifiManager wm = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wm != null;
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();

        final View view = inflater.inflate(R.layout.fragment_settings, container, false);

        view.findViewById(R.id.button_settings_apply).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int port = Integer.parseInt (((EditText)view.findViewById(R.id.settings_port)).getText().toString());
                String ip = ((EditText)view.findViewById(R.id.settings_ip)).getText().toString();

                Intent commandIntent = new Intent(ConstantsKey.ACTION_SEND);
                commandIntent.putExtra(ConstantsKey.COMMAND_TYPE, ConstantsKey.Restart_KEY);
                commandIntent.putExtra(ConstantsKey.IP_KEY, ip);
                commandIntent.putExtra(ConstantsKey.PORT_KEY, port);
                getActivity().sendBroadcast(commandIntent);
            }
        });

        return view;
    }



}
