package com.example.sergiogeek7.appiris.appiris;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 7/02/18.
 */

public class BodySectorVR {
    List<BodyPart> parts;
    String key;

    public BodySectorVR (Context context, String key){
        this.key = key;
        this.parts = new ArrayList<>();
        this.parts.add(new BodyPart(context.getString(R.string.orificios_nasales),""));
        this.parts.add(new BodyPart(context.getString(R.string.ojos),""));
        this.parts.add(new BodyPart(context.getString(R.string.colon_transversal),""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),""));
        this.parts.add(new BodyPart(context.getString(R.string.maxilar_superior),""));
        this.parts.add(new BodyPart(context.getString(R.string.lengua),""));
        this.parts.add(new BodyPart(context.getString(R.string.amígdalas),""));
        this.parts.add(new BodyPart(context.getString(R.string.nariz),""));
        this.parts.add(new BodyPart(context.getString(R.string.intestino_delgado),""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),""));
        this.parts.add(new BodyPart(context.getString(R.string.torioides),""));
        this.parts.add(new BodyPart(context.getString(R.string.laringe),""));
        this.parts.add(new BodyPart(context.getString(R.string.glandulas_linguales),""));
        this.parts.add(new BodyPart(context.getString(R.string.lengua),""));
        this.parts.add(new BodyPart(context.getString(R.string.cuerdas_bocales),""));
        this.parts.add(new BodyPart(context.getString(R.string.maxilar_inferior),""));
        this.parts.add(new BodyPart(context.getString(R.string.intestino_delgado),""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),""));
        this.parts.add(new BodyPart(context.getString(R.string.garganta),""));
        this.parts.add(new BodyPart(context.getString(R.string.traquea),""));
        this.parts.add(new BodyPart(context.getString(R.string.glandula_tiroide_paratiroides),""));
        this.parts.add(new BodyPart(context.getString(R.string.paratiroides),""));
        this.parts.add(new BodyPart(context.getString(R.string.intestino_delgado),""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),""));
        this.parts.add(new BodyPart(context.getString(R.string.esfínteres_cardias_piloro),""));
        this.parts.add(new BodyPart(context.getString(R.string.piloro),""));
        this.parts.add(new BodyPart(context.getString(R.string.esófago),""));
        this.parts.add(new BodyPart(context.getString(R.string.omoplato),""));
        this.parts.add(new BodyPart(context.getString(R.string.traquea),""));
        this.parts.add(new BodyPart(context.getString(R.string.intestino_delgado),""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),""));
    }

}
