package com.example.sergiogeek7.appiris.bl;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;

/**
 * Sector CX
 */

public class BodySectorCX extends BodySector{

    public BodySectorCX(Context context,
                        int[] rightKey,
                        int[] leftKey,
                        double[] scaleRight,
                        double[] scaleLeft,
                        Gender gender) {
        super(rightKey, leftKey, R.drawable.ic_cx_l);
        this.scaleRight = scaleRight;
        this.scaleLeft = scaleLeft;
        int id = -1;
        this.parts.add(new BodyPart(context.getString(R.string.cerebro),"", ++id));
        this.parts.add(new BodyPart(context.getString(R.string.cerebelo),"", ++id));
        this.parts.add(new BodyPart(context.getString(R.string.glandula_pineal),"", ++id));
        this.parts.add(new BodyPart(context.getString(R.string.frente), "", ++id));
        this.parts.add(new BodyPart(context.getString(R.string.oidos),
                context.getString(R.string.diagnosis_oidos), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),
                context.getString(R.string.diagnosis_estomago), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.colon_transversal),
                context.getString(R.string.diagnosis_colon), ++id));

    }

}
