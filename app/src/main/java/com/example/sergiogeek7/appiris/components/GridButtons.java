package com.example.sergiogeek7.appiris.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 6/02/18.
 */

public class GridButtons extends FlowLayout {

    private List<ButtonTab> buttons = new ArrayList<>();

    public GridButtons(Context context, String[] items) {
        super(context);
        this.setPadding(32,32,32,32);
        buildButtons(items);
    }


    void buildButtons(String[] items){
        for(String name: items){
            buttons.add(this.addButton(name));
        }
    }

    public List<String> getSeletedItems(){
        List<String> selected = new ArrayList<>();
        for (ButtonTab btn : buttons){
            if(btn.isEnabled()){
                selected.add(btn.getText().toString());
            }
        }
        return selected;
    }

    ButtonTab addButton(String name){
        ButtonTab txt = new ButtonTab(getContext(), name);

        int pxHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                20, getResources().getDisplayMetrics());


        FlowLayout.LayoutParams lParams = new FlowLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                pxHeight);
        int pxMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                8, getResources().getDisplayMetrics());
        lParams.setMargins(0, pxMargin, pxMargin, 0);
        this.addView(txt,0,lParams);
        return txt;
    }

    public GridButtons(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridButtons(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
