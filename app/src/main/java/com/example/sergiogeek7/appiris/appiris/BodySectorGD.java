package com.example.sergiogeek7.appiris.appiris;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 7/02/18.
 */

public class BodySectorGD {
    String key;
    List<BodyPart> parts;

    public BodySectorGD(Context context, String key){
        this.key = key;
        this.parts = new ArrayList<>();
        this.parts.add(new BodyPart(context.getString(R.string.pleura), ""));
        this.parts.add(new BodyPart(context.getString(R.string.nervio_vago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.timo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.corazón), ""));
        this.parts.add(new BodyPart(context.getString(R.string.glándulas_mamarias), ""));
        this.parts.add(new BodyPart(context.getString(R.string.costillas_superiores), ""));
        this.parts.add(new BodyPart(context.getString(R.string.colon), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.pulmones), ""));
        this.parts.add(new BodyPart(context.getString(R.string.nervio_vago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.bronquios), ""));
        this.parts.add(new BodyPart(context.getString(R.string.timo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.corazón), ""));
        this.parts.add(new BodyPart(context.getString(R.string.colon), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.E_axilas), ""));
        this.parts.add(new BodyPart(context.getString(R.string.pulmones), ""));
        this.parts.add(new BodyPart(context.getString(R.string.traquea), ""));
        this.parts.add(new BodyPart(context.getString(R.string.nervio_vago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.bronquios), ""));
        this.parts.add(new BodyPart(context.getString(R.string.aorta), ""));
        this.parts.add(new BodyPart(context.getString(R.string.aurículas_corazon), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.D_cuello), ""));
        this.parts.add(new BodyPart(context.getString(R.string.nervio_vago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.glandula_axiliar), ""));
        this.parts.add(new BodyPart(context.getString(R.string.clavicula), ""));
        this.parts.add(new BodyPart(context.getString(R.string.colon), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
    }
}
