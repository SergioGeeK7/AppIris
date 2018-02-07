package com.example.sergiogeek7.appiris.components;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.sergiogeek7.appiris.R;

/**
 * Created by sergiogeek7 on 6/02/18.
 */

public class ButtonTab extends android.support.v7.widget.AppCompatButton {

    private boolean enable;

    public ButtonTab(Context context, String text) {
        super(context);
        this.setText(text);
        setUpUI();
    }

    void setUpUI(){
        this.setPadding(8,0,8,4);
        this.setAllCaps(false);
        setEnabled(false);
        this.setOnClickListener((v) -> setEnabled(!isEnabled()));
    }

    public boolean isEnabled(){
        return enable;
    }

    @Override
    public void setEnabled(boolean enable){
        if(enable){
            this.setTextColor(ContextCompat.getColor(getContext(), R.color.tw__solid_white));
            this.setBackground(ContextCompat.getDrawable(getContext(),
                    R.drawable.blue_button_organ_selected));
        }else{
            this.setTextColor(ContextCompat.getColor(getContext(), R.color.disable_organ_button));
            this.setBackground(ContextCompat.getDrawable(getContext(),
                    R.drawable.button_organ));
        }
        this.enable = enable;
    }

    public ButtonTab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
