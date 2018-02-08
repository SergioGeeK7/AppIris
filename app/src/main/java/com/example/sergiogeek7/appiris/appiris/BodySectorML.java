package com.example.sergiogeek7.appiris.appiris;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;
import com.example.sergiogeek7.appiris.utils.Gender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 7/02/18.
 */

public class BodySectorML extends BodySector{

    public BodySectorML(Context context, int[] rightKey, int[] leftKey, Gender gender){
        super(rightKey, leftKey);
        int id = -1;
        this.parts.add(new BodyPart(context.getString(R.string.pies),
                context.getString(R.string.diagnosis_piernas), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.piernas),
                context.getString(R.string.diagnosis_piernas), ++id));
        if(Gender.WOMAN == gender){
            this.parts.add(new BodyPart(context.getString(R.string.trompas_de_falopio),
                    context.getString(R.string.diagnosis_pelvis), ++id));
        }
        this.parts.add(new BodyPart(context.getString(R.string.rinones),
                context.getString(R.string.diagnosis_pelvis) , ++id));
        this.parts.add(new BodyPart(context.getString(R.string.glándulas_suprarrenales),
                context.getString(R.string.diagnosis_pelvis), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.intestino_delgado),
                context.getString(R.string.diagnosis_pelvis), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.sigmoidea),
                context.getString(R.string.diagnosis_pelvis), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.ciego),
                context.getString(R.string.diagnosis_pelvis), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),
                context.getString(R.string.diagnosis_estomago), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.bajo_abdomen),
                context.getString(R.string.diagnosis_abdomen), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.fingle),
                context.getString(R.string.diagnosis_pelvis), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.intestinos),
                context.getString(R.string.diagnosis_pelvis), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.colon_sigmoideo),
                context.getString(R.string.diagnosis_colon), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.apendice),
                context.getString(R.string.diagnosis_pelvis), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.ileon),
                context.getString(R.string.diagnosis_pelvis), ++id));
    }
}
