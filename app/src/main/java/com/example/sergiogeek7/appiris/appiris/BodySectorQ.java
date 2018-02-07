package com.example.sergiogeek7.appiris.appiris;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 7/02/18.
 */

public class BodySectorQ {
    List<BodyPart> parts;
    String key;

    public BodySectorQ (Context context, String key){
        this.key = key;
        this.parts = new ArrayList<>();
        this.parts.add(new BodyPart(context.getString(R.string.zona_umbilical), ""));
        this.parts.add(new BodyPart(context.getString(R.string.columna_vertebral), ""));
        this.parts.add(new BodyPart(context.getString(R.string.omoplato), ""));
        this.parts.add(new BodyPart(context.getString(R.string.intestino_delgado), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
    }
}
