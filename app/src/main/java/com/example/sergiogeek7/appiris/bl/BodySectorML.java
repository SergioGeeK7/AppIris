package com.example.sergiogeek7.appiris.bl;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;

/**
 * Sector ML
 */

public class BodySectorML extends BodySector{

    public BodySectorML(Context context, int[] rightKey, int[] leftKey,
                        double[] scaleRight,
                        double[] scaleLeft,
                        Gender gender){
        super(rightKey, leftKey, R.drawable.ic_ml_l);
        this.scaleRight = scaleRight;
        this.scaleLeft = scaleLeft;
        int id = -1;
        this.parts.add(new BodyPart(context.getString(R.string.estomago),
                context.getString(R.string.diagnosis_estomago), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.intestinos),
                context.getString(R.string.diagnosis_intestinos), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.rinones),
                context.getString(R.string.diagnosis_rinones_vejiga) , ++id));
        this.parts.add(new BodyPart(context.getString(R.string.piernas),
                context.getString(R.string.diagnosis_piernas), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.pies),
                context.getString(R.string.diagnosis_piernas), ++id));
    }
}