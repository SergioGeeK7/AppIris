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

    public BodySectorPN(Context context, int[] rightKey, int[] leftKey,
                        double[] scaleRight,
                        double[] scaleLeft,
                        Gender gender){
        super(rightKey, leftKey,R.drawable.ic_pn_l_);
        this.scaleRight = scaleRight;
        this.scaleLeft = scaleLeft;
        int id = -1;
        this.parts.add(new BodyPart(context.getString(R.string.columna_vertebral),
                context.getString(R.string.diagnosis_columna_vertebral), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),
                context.getString(R.string.diagnosis_estomago), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.intestino_delgado),
                context.getString(R.string.diagnosis_intestinos), ++id));
        if (gender == Gender.MAN){
            this.parts.add(new BodyPart(context.getString(R.string.pene),
                    context.getString(R.string.diagnosis_organos_reproductores), ++id));
        }else{
            this.parts.add(new BodyPart(context.getString(R.string.vagina),
                    context.getString(R.string.diagnosis_organos_reproductores), ++id));
        }
        this.parts.add(new BodyPart(context.getString(R.string.ano),"", ++id));
        this.parts.add(new BodyPart(context.getString(R.string.recto),"", ++id));
    }
}
