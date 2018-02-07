package com.example.sergiogeek7.appiris.appiris;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 7/02/18.
 */

public class BodySectorML {

    String key;
    List<BodyPart> parts;

    public BodySectorML(Context context, String key){
        this.key = key;
        this.parts = new ArrayList<>();
        this.parts.add(new BodyPart(context.getString(R.string.pies), ""));
        this.parts.add(new BodyPart(context.getString(R.string.piernas), ""));
        this.parts.add(new BodyPart(context.getString(R.string.trompas_de_falopio), ""));
        this.parts.add(new BodyPart(context.getString(R.string.rinones), ""));
        this.parts.add(new BodyPart(context.getString(R.string.glándulas_suprarrenales), ""));
        this.parts.add(new BodyPart(context.getString(R.string.intestino_delgado), ""));
        this.parts.add(new BodyPart(context.getString(R.string.sigmoidea), ""));
        this.parts.add(new BodyPart(context.getString(R.string.ciego), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.bajo_abdomen), ""));
        this.parts.add(new BodyPart(context.getString(R.string.fingle), ""));
        this.parts.add(new BodyPart(context.getString(R.string.piernas), ""));
        this.parts.add(new BodyPart(context.getString(R.string.trompa), ""));
        this.parts.add(new BodyPart(context.getString(R.string.intestinos), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.colon_sigmoideo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.apendice), ""));
        this.parts.add(new BodyPart(context.getString(R.string.ciego), ""));
        this.parts.add(new BodyPart(context.getString(R.string.ileon), ""));
    }
}
