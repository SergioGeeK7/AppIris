package com.example.sergiogeek7.appiris.appiris;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;
import com.example.sergiogeek7.appiris.utils.Gender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 7/02/18.
 */

public class BodySectorQ extends BodySector{

    public BodySectorQ (Context context, int[] rightKey, int[] leftKey, Gender gender){
        super(rightKey, leftKey,R.drawable.ic_q_l);
        int id = -1;
        this.parts.add(new BodyPart(context.getString(R.string.zona_umbilical),
                context.getString(R.string.diagnosis_top_espalda), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.columna_vertebral),
                context.getString(R.string.diagnosis_top_espalda), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.omoplato),
                context.getString(R.string.diagnosis_middle_espalda), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.intestino_delgado),
                context.getString(R.string.diagnosis_intestinos), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),
                context.getString(R.string.diagnosis_estomago), ++id));
    }
}
