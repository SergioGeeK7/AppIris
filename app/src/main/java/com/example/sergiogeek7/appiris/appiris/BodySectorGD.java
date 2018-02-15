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

    public BodySectorGD(Context context, int[] rightKey, int[] leftKey, Gender gender){
        super(rightKey, leftKey, R.drawable.ic_gd_l);
        int id = -1;
        this.parts.add(new BodyPart(context.getString(R.string.pleura),
                context.getString(R.string.diagnosis_pulmones), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.corazon),
                context.getString(R.string.diagnosis_corazon), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.glandulas_mamarias),
                context.getString(R.string.diagnosis_senos), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.costillas_superiores),
                context.getString(R.string.diagnosis_pulmones), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),
                context.getString(R.string.diagnosis_estomago), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.pulmones),
                context.getString(R.string.diagnosis_pulmones), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.nervio_vago),
                context.getString(R.string.diagnosis_cuello), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.timo),
                context.getString(R.string.diagnosis_pulmones), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.colon),
                context.getString(R.string.diagnosis_colon), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.E_axilas),
                context.getString(R.string.diagnosis_brazos), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.traquea),
                context.getString(R.string.diagnosis_cuello), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.bronquios),
                context.getString(R.string.diagnosis_pulmones), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.aorta),
                context.getString(R.string.diagnosis_pulmones), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.aurículas_corazon),
                context.getString(R.string.diagnosis_corazon), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.D_cuello),
                context.getString(R.string.diagnosis_cuello), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.glandula_axiliar),
                context.getString(R.string.diagnosis_brazos), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.clavicula),
                context.getString(R.string.diagnosis_cuello), ++id));
    }
}
