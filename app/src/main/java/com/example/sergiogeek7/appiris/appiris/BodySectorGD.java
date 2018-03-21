package com.example.sergiogeek7.appiris.appiris;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;
import com.example.sergiogeek7.appiris.utils.Gender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 7/02/18.
 */

public class BodySectorGD extends BodySector{

    public BodySectorGD(Context context, int[] rightKey, int[] leftKey,
                        double[] scaleRight,
                        double[] scaleLeft,
                        Gender gender){
        super(rightKey, leftKey, R.drawable.ic_gd_l);
        this.scaleRight = scaleRight;
        this.scaleLeft = scaleLeft;
        int id = -1;
        this.parts.add(new BodyPart(context.getString(R.string.corazon),
                context.getString(R.string.diagnosis_corazon), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.pulmones),
                context.getString(R.string.diagnosis_pulmones), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.glandulas_mamarias),
                context.getString(R.string.diagnosis_senos), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.colon),
                context.getString(R.string.diagnosis_colon), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),
                context.getString(R.string.diagnosis_estomago), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.costillas_superiores),
                context.getString(R.string.diagnosis_pulmones), ++id));
    }
}
