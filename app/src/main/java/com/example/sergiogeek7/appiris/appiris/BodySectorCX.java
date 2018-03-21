package com.example.sergiogeek7.appiris.appiris;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;
import com.example.sergiogeek7.appiris.utils.Gender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 7/02/18.
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
        this.parts.add(new BodyPart(context.getString(R.string.cerebro),
                context.getString(R.string.diagnosis_cabeza), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.cerebelo),
                context.getString(R.string.diagnosis_cabeza), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.oidos),
                context.getString(R.string.diagnosis_oidos), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.glandula_pineal),
                context.getString(R.string.diagnosis_cabeza), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.frente),
                context.getString(R.string.diagnosis_cara), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.colon_transversal),
                context.getString(R.string.diagnosis_colon), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),
                context.getString(R.string.diagnosis_estomago), ++id));
    }

}
