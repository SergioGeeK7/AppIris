package com.example.sergiogeek7.appiris.appiris;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;
import com.example.sergiogeek7.appiris.utils.Gender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 7/02/18.
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
        this.parts.add(new BodyPart(context.getString(R.string.pelvis),
                context.getString(R.string.diagnosis_pelvis), ++id));
        if(Gender.WOMAN == gender){
            this.parts.add(new BodyPart(context.getString(R.string.ovarios),
                    context.getString(R.string.diagnosis_testiculos), ++id));
        }
        this.parts.add(new BodyPart(context.getString(R.string.intestinos),
                context.getString(R.string.diagnosis_intestinos), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.estomago),
                context.getString(R.string.diagnosis_estomago), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.colon_sigmoideo),
                context.getString(R.string.diagnosis_colon), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.apendice),
                context.getString(R.string.diagnosis_abdomen), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.ciego),
                context.getString(R.string.diagnosis_pelvis), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.ileon),
                context.getString(R.string.diagnosis_pelvis), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.pancreas),
                context.getString(R.string.diagnosis_pancreas) , ++id));
        this.parts.add(new BodyPart(context.getString(R.string.colon),
                context.getString(R.string.diagnosis_colon), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.higado),
                context.getString(R.string.diagnosis_higado), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.vesicula),
                context.getString(R.string.diagnosis_vesicula_biliar), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.conducto_cistico),
                context.getString(R.string.diagnosis_vesicula_biliar), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.manos),
                context.getString(R.string.diagnosis_brazos), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.brazos),
                context.getString(R.string.diagnosis_brazos), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.bazo),
                context.getString(R.string.diagnosis_abdomen) , ++id));
        this.parts.add(new BodyPart(context.getString(R.string.caderas_ilion),
                context.getString(R.string.diagnosis_pelvis), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.caja_toraxica),
                context.getString(R.string.diagnosis_caja_toracica), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.costillas_inferiores),
                context.getString(R.string.diagnosis_pulmones), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.esternon),
                context.getString(R.string.diagnosis_pulmones), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.nervio_vago),
                context.getString(R.string.diagnosis_cuello), ++id));
        this.parts.add(new BodyPart(context.getString(R.string.plexo_solar),
                context.getString(R.string.diagnosis_abdomen), ++id));
    }
}
