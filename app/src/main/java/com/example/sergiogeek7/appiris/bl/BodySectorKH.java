package com.example.sergiogeek7.appiris.bl;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;

/**
 * Sector KH
 */

public class BodySectorKH extends BodySector{

    public BodySectorKH(Context context, int[] rightKey, int[] leftKey,
                        double[] scaleRight,
                        double[] scaleLeft,
                        Gender gender){
        super(rightKey, leftKey, R.drawable.ic_kh_l);
        this.scaleRight = scaleRight;
        this.scaleLeft = scaleLeft;
        int id = -1;
        this.parts.add(new BodyPart(context.getString(R.string.bazo),
                context.getString(R.string.diagnosis_abdomen) , ++id));
        this.parts.add(new BodyPart(context.getString(R.string.pancreas),
                context.getString(R.string.diagnosis_pancreas) , ++id));
        this.parts.add(new BodyPart(context.getString(R.string.higado),
                context.getString(R.string.diagnosis_higado), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.vesicula),
                context.getString(R.string.diagnosis_vesicula_biliar), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),
                context.getString(R.string.diagnosis_estomago), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.intestinos),
                context.getString(R.string.diagnosis_intestinos), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.colon),
                context.getString(R.string.diagnosis_colon), ++id));
        if(Gender.WOMAN == gender){
            this.parts.add(new BodyPart(context.getString(R.string.ovarios),"", ++id));
        }
        this.parts.add(new BodyPart(context.getString(R.string.brazos),"", ++id));
        this.parts.add(new BodyPart(context.getString(R.string.manos),"", ++id));
    }
}
