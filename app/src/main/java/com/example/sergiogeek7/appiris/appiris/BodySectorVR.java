package com.example.sergiogeek7.appiris.appiris;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;
import com.example.sergiogeek7.appiris.utils.Gender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 7/02/18.
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
        this.parts.add(new BodyPart(context.getString(R.string.orificios_nasales),
                context.getString(R.string.diagnosis_nariz), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.ojos),
                context.getString(R.string.diagnosis_ojos), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.nariz),
                context.getString(R.string.diagnosis_nariz), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.lengua),
                context.getString(R.string.diagnosis_cara), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.amigdalas),
                context.getString(R.string.diagnosis_cara), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.laringe),
                context.getString(R.string.diagnosis_cuello), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.garganta),
                context.getString(R.string.diagnosis_cuello), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.colon_transversal),
                context.getString(R.string.diagnosis_colon), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),
                context.getString(R.string.diagnosis_estomago), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.intestino_delgado),
                context.getString(R.string.intestino_delgado), ++id));
    }

}
