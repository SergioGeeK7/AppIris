package com.example.sergiogeek7.appiris.appiris;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 7/02/18.
 */

public class BodySectorPN {

    String key;
    List<BodyPart> parts;


    public BodySectorPN(Context context, String key){
        this.key = key;
        this.parts = new ArrayList<>();
        this.parts.add(new BodyPart(context.getString(R.string.pene), ""));
        this.parts.add(new BodyPart(context.getString(R.string.uretra), ""));
        this.parts.add(new BodyPart(context.getString(R.string.vejiga), ""));
        this.parts.add(new BodyPart(context.getString(R.string.ureter), ""));
        this.parts.add(new BodyPart(context.getString(R.string.coxis), ""));
        this.parts.add(new BodyPart(context.getString(R.string.columna_vertebral), ""));
        this.parts.add(new BodyPart(context.getString(R.string.intestino_delgado), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.vagina), ""));
        this.parts.add(new BodyPart(context.getString(R.string.uretra), ""));
        this.parts.add(new BodyPart(context.getString(R.string.utero), ""));
        this.parts.add(new BodyPart(context.getString(R.string.vejiga), ""));
        this.parts.add(new BodyPart(context.getString(R.string.uréter), ""));
        this.parts.add(new BodyPart(context.getString(R.string.coxis), ""));
        this.parts.add(new BodyPart(context.getString(R.string.columna_vertebral), ""));
        this.parts.add(new BodyPart(context.getString(R.string.intestino_delgado), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.vagina), ""));
        this.parts.add(new BodyPart(context.getString(R.string.uretra), ""));
        this.parts.add(new BodyPart(context.getString(R.string.utero), ""));
        this.parts.add(new BodyPart(context.getString(R.string.trompa_falopio), ""));
        this.parts.add(new BodyPart(context.getString(R.string.rinones), ""));
        this.parts.add(new BodyPart(context.getString(R.string.capas_suprarrenales), ""));
        this.parts.add(new BodyPart(context.getString(R.string.intestino), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.ano), ""));
        this.parts.add(new BodyPart(context.getString(R.string.recto), ""));
        this.parts.add(new BodyPart(context.getString(R.string.intestino_delgado), ""));
    }
}
