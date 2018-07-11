package com.example.sergiogeek7.appiris.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.example.sergiogeek7.appiris.bl.BodyPart;

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
        this.setColumnCount(3);
        buildButtons(items, listener);
    }

    void buildButtons(List<BodyPart> pars, ButtonTab.ButtonTabListener listener){
        for(int i = pars.size() - 1; i > -1; i--){
            BodyPart part = pars.get(i);
            buttons.add(this.addButton(part, listener));
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
