package com.example.sergiogeek7.appiris.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.example.sergiogeek7.appiris.appiris.BodyPart;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 6/02/18.
 */

public class GridButtons extends GridLayout {

    private List<ButtonTab> buttons = new ArrayList<>();

    public GridButtons(Context context, List<BodyPart> items, ButtonTab.ButtonTabListener listener) {
        super(context);
        this.setPadding(32,32,32,32);
        this.setColumnCount(6);
        buildButtons(items, listener);
    }

    void buildButtons(List<BodyPart> pars, ButtonTab.ButtonTabListener listener){
        for(BodyPart part: pars){
            buttons.add(this.addButton(part, listener));
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

    public void setSelectedItems(List<BodyPart> parts){
        for (BodyPart part : parts){
            for (ButtonTab btn : buttons){
                if(btn.part.id == part.id){
                    btn.setEnabled(true);
                }
            }
        }
    }

    ButtonTab addButton(BodyPart part, ButtonTab.ButtonTabListener listener){
        ButtonTab txt = new ButtonTab(getContext(), part, listener);

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
