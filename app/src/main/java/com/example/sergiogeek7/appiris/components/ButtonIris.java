package com.example.sergiogeek7.appiris.components;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.example.sergiogeek7.appiris.R;

/**
 * Created by sergiogeek7 on 1/02/18.
 */

public class ButtonIris extends android.support.v7.widget.AppCompatButton {
    public ButtonIris(Context context) {
        super(context);
    }

    public ButtonIris(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonIris(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(!enabled){
            this.setBackground(
                    ContextCompat.getDrawable(getContext(),R.drawable.button_disable));
        }else {
            this.setBackground(
                    ContextCompat.getDrawable(getContext(),R.drawable.gradient_button));
        }
    }
}
