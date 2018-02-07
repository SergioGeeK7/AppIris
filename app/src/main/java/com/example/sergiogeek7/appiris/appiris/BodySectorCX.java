package com.example.sergiogeek7.appiris.appiris;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 7/02/18.
 */

public class BodySectorCX {
    List<BodyPart> parts;
    String key;

    public BodySectorCX(Context context, String key){
        this.parts = new ArrayList<>();
        this.key = key;
        this.parts.add(new BodyPart(context.getString(R.string.cuero_cabelludo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.craneo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.cerebro), ""));
        this.parts.add(new BodyPart(context.getString(R.string.cerebelo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.oídos__timpano), ""));
        this.parts.add(new BodyPart(context.getString(R.string.glandula_parotida), ""));
        this.parts.add(new BodyPart(context.getString(R.string.nervio_vago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.colon_transversal), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.B_cuero_cabelludo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.craneo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.cerebro), ""));
        this.parts.add(new BodyPart(context.getString(R.string.cerebelo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.nervio_vago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.colon_tranversal), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.A_cuero_cabelludo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.craneo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.cerebro), ""));
        this.parts.add(new BodyPart(context.getString(R.string.septumlucidum), ""));
        this.parts.add(new BodyPart(context.getString(R.string.cuerpo_calloso), ""));
        this.parts.add(new BodyPart(context.getString(R.string.glandula_pineal), ""));
        this.parts.add(new BodyPart(context.getString(R.string.colon_transversal), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.cuero_cabelludo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.cráneo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.cerebro), ""));
        this.parts.add(new BodyPart(context.getString(R.string.septum_lucidum), ""));
        this.parts.add(new BodyPart(context.getString(R.string.cuerpo_calloso), ""));
        this.parts.add(new BodyPart(context.getString(R.string.glándula_pineal), ""));
        this.parts.add(new BodyPart(context.getString(R.string.colon_transversal), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.frente), ""));
        this.parts.add(new BodyPart(context.getString(R.string.cuero_cabelludo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.cráneo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.cerebro), ""));
        this.parts.add(new BodyPart(context.getString(R.string.septum_lucidum), ""));
        this.parts.add(new BodyPart(context.getString(R.string.cuerpo_calloso), ""));
        this.parts.add(new BodyPart(context.getString(R.string.glándula_pineal), ""));
        this.parts.add(new BodyPart(context.getString(R.string.colon_transversal), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
    }

}
