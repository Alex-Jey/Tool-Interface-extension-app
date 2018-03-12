package com.example.alexj.clientps10;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

public class FragmentColorWheel extends Fragment {






    private ColorWheelView mColorWheelView;
    private EditText mHexInput;
    private ImageView mNew;
    private ImageView mOld;
    private SeekBar mAlphaSlider;
    private View mTransparency;

    private boolean AlphaOn = true;
    private boolean HexOn = true;


    private final TextWatcher hexEditWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            try {
                int color = ((255 - mAlphaSlider.getProgress()) << 24)
                        + (int) Long.parseLong(s.toString(), 16);
                mColorWheelView.setColor(color, false);
                mNew.setImageDrawable(new ColorDrawable(color));
            } catch (NumberFormatException ignored){}
        }
    };



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.simpledialogfragment_color_wheel, container, false);
        mColorWheelView = (ColorWheelView) view.findViewById(R.id.colorWheel);
        mTransparency = view.findViewById(R.id.transparencyBox);
        mAlphaSlider = (SeekBar) view.findViewById(R.id.alpha);
        mHexInput = (EditText) view.findViewById(R.id.hexEditText);
        mNew = (ImageView) view.findViewById(R.id.colorNew);
        mOld = (ImageView) view.findViewById(R.id.colorOld);
        View hexLayout = view.findViewById(R.id.hexLayout);

        String serverColor = "#F02F6DA3";

        int color = android.graphics.Color.parseColor(serverColor);
        //getArguments().getInt(COLOR, ColorWheelView.DEFAULT_COLOR);
        int oldColor = android.graphics.Color.parseColor(serverColor);
        //getArguments().getInt(COLOR);
        if (AlphaOn
            //!getArguments().getBoolean(ALPHA)
                ){
            color = color | 0xFF000000;
            oldColor = oldColor | 0xFF000000;
        }

        mColorWheelView.setColor(color);
        mNew.setImageDrawable(new ColorDrawable(color));
        mAlphaSlider.setMax(255);
        mAlphaSlider.setProgress(255 - Color.alpha(color));
        mHexInput.setText(String.format("%06X", color & 0xFFFFFF));
        // hexLayout.setVisibility(HexOn ? View.GONE : View.VISIBLE);
        mOld.setVisibility(
                //getArguments().containsKey(COLOR)
                HexOn ? View.VISIBLE : View.GONE);
        mOld.setImageDrawable(new ColorDrawable(oldColor));
        final int finalOldColor = oldColor;
        mOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColorWheelView.setColor(finalOldColor);
                mAlphaSlider.setProgress(255 - Color.alpha(finalOldColor));
            }
        });



        mHexInput.addTextChangedListener(hexEditWatcher);
        mColorWheelView.setOnColorChangeListener(new ColorWheelView.OnColorChangeListener() {
            @Override
            public void onColorChange(int color) {
                mHexInput.removeTextChangedListener(hexEditWatcher);
                mHexInput.setText(String.format("%06X", color & 0xFFFFFF));
                mHexInput.addTextChangedListener(hexEditWatcher);
                mNew.setImageDrawable(new ColorDrawable(color));
            }
        });

        mTransparency.setVisibility(
                //getArguments().getBoolean(ALPHA)
                AlphaOn ? View.VISIBLE : View.GONE);

        mAlphaSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mColorWheelView.updateAlpha(255 - progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        view.findViewById(R.id.button_ok_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commandIntent = new Intent(ConstantsKey.ACTION_SEND);

                String hex = Integer.toHexString(mColorWheelView.getColor());
                if(hex.length()<7)
                    hex="00"+hex;
                hex = "Color #"+ hex;
                commandIntent.putExtra(ConstantsKey.COMMAND_TYPE, ConstantsKey.Send_KEY);
                commandIntent.putExtra(ConstantsKey.Command, hex);


                getActivity().sendBroadcast(commandIntent);
            }
        });

        return  view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
