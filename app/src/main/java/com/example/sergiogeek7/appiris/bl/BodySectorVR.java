package com.example.sergiogeek7.appiris.bl;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;

/**
 * Sector VR
 */

public class BodySectorVR extends BodySector{

    public BodySectorVR (Context context, int[] rightKey, int[] leftKey,
                         double[] scaleRight,
                         double[] scaleLeft,
                         Gender gender){
        super(rightKey, leftKey,R.drawable.ic_vr_l);
        this.scaleRight = scaleRight;
        this.scaleLeft = scaleLeft;
        int id = -1;
        this.parts.add(new BodyPart(context.getString(R.string.ojos),
                context.getString(R.string.diagnosis_ojos), ++id));

        this.parts.add(new BodyPart(context.getString(R.string.nariz),
                context.getString(R.string.diagnosis_nariz), ++id));

        this.parts.add(new BodyPart(context.getString(R.string.orificios_nasales),
                context.getString(R.string.diagnosis_nariz), ++id));

        this.parts.add(new BodyPart(context.getString(R.string.lengua),"", ++id));
        this.parts.add(new BodyPart(context.getString(R.string.laringe),"", ++id));
        this.parts.add(new BodyPart(context.getString(R.string.garganta),"", ++id));

        this.parts.add(new BodyPart(context.getString(R.string.amigdalas),"", ++id));

        this.parts.add(new BodyPart(context.getString(R.string.glandula_tiroide_paratiroides),
                "", ++id));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),
                context.getString(R.string.diagnosis_estomago), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.intestino_delgado),
                context.getString(R.string.diagnosis_intestinos), ++id));
    }

}
