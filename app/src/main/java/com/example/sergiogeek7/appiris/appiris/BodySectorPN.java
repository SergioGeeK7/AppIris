package com.example.sergiogeek7.appiris.appiris;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;
import com.example.sergiogeek7.appiris.utils.Gender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 7/02/18.
 */

public class BodySectorPN extends BodySector{

    public BodySectorPN(Context context, int[] rightKey, int[] leftKey, Gender gender){
        super(rightKey, leftKey);
        int id = -1;
        if (gender == Gender.MAN){
            this.parts.add(new BodyPart(context.getString(R.string.pene),
                    context.getString(R.string.diagnosis_organos_reproductores), ++id));
        }else{
            this.parts.add(new BodyPart(context.getString(R.string.uretra),
                    context.getString(R.string.diagnosis_pelvis), ++id));
            this.parts.add(new BodyPart(context.getString(R.string.vagina),
                    context.getString(R.string.diagnosis_organos_reproductores), ++id));
            this.parts.add(new BodyPart(context.getString(R.string.utero),
                    context.getString(R.string.diagnosis_organos_reproductores), ++id));
            this.parts.add(new BodyPart(context.getString(R.string.trompa_falopio),
                    context.getString(R.string.diagnosis_organos_reproductores), ++id));
        }

        this.parts.add(new BodyPart(context.getString(R.string.vejiga),
                context.getString(R.string.diagnosis_pelvis), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.ureter),
                context.getString(R.string.diagnosis_pelvis), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.coxis),
                context.getString(R.string.diagnosis_pelvis), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.columna_vertebral),
                context.getString(R.string.diagnosis_columna_vertebral), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.intestino_delgado),
                context.getString(R.string.diagnosis_intestinos), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),
                context.getString(R.string.diagnosis_estomago), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.rinones),
                context.getString(R.string.diagnosis_rinones_vejiga) , ++id));
        this.parts.add(new BodyPart(context.getString(R.string.capas_suprarrenales),
                context.getString(R.string.diagnosis_rinones_vejiga), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.intestino),
                context.getString(R.string.diagnosis_intestinos), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.ano),
                context.getString(R.string.diagnosis_pelvis), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.recto),
                context.getString(R.string.diagnosis_pelvis), ++id));
    }
}
