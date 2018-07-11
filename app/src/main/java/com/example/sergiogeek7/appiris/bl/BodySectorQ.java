package com.example.sergiogeek7.appiris.bl;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;

/**
 * Sector Q
 */

public class BodySectorQ extends BodySector{

    public BodySectorQ (Context context, int[] rightKey, int[] leftKey,
                        double[] scaleRight,
                        double[] scaleLeft,
                        Gender gender){
        super(rightKey, leftKey,R.drawable.ic_q_l);
        this.scaleRight = scaleRight;
        this.scaleLeft = scaleLeft;
        int id = -1;

        this.parts.add(new BodyPart(context.getString(R.string.estomago),
                context.getString(R.string.diagnosis_estomago), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.intestino_delgado),
                context.getString(R.string.diagnosis_intestinos), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.columna_vertebral),
                context.getString(R.string.diagnosis_columna_vertebral), ++id));

    }
}
