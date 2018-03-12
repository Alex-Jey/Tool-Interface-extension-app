package com.example.alexj.clientps10;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by alexj on 12.03.2018.
 */

public class FragmentImagePreview extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

         View view = inflater.inflate(R.layout.fragment_settings, container, false);

        return view;
    }
}
