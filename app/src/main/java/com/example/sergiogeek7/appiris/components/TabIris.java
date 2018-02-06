package com.example.sergiogeek7.appiris.components;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.sergiogeek7.appiris.R;

/**
 * Created by sergiogeek7 on 1/02/18.
 */

public class TabIris extends android.support.v7.widget.AppCompatTextView {
    public TabIris(Context context) {
        super(context);
    }

    public TabIris(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabIris(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setEnabled (boolean enable){
        if(enable){
            this.setTextColor(ContextCompat.getColor(getContext(), R.color.blue_sea));
            return;
        }
        this.setTextColor(ContextCompat.getColor(getContext(), R.color.tab_text));
    }

}
