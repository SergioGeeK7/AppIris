package com.example.sergiogeek7.appiris.appiris;

import android.content.Context;

import com.example.sergiogeek7.appiris.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 7/02/18.
 */

public class BodySectorKH {
    String key;
    List<BodyPart> parts;

    public BodySectorKH(Context context, String key){
        this.key = key;
        this.parts = new ArrayList<>();
        this.parts.add(new BodyPart(context.getString(R.string.pelvis), ""));
        this.parts.add(new BodyPart(context.getString(R.string.ovarios), ""));
        this.parts.add(new BodyPart(context.getString(R.string.intestinos), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.colon_sigmoideo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.apendice), ""));
        this.parts.add(new BodyPart(context.getString(R.string.ciego), ""));
        this.parts.add(new BodyPart(context.getString(R.string.ileon), ""));
        this.parts.add(new BodyPart(context.getString(R.string.ovarios_testículos), ""));
        this.parts.add(new BodyPart(context.getString(R.string.páncreas), ""));
        this.parts.add(new BodyPart(context.getString(R.string.intestinos), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.colon), ""));
        this.parts.add(new BodyPart(context.getString(R.string.higado), ""));
        this.parts.add(new BodyPart(context.getString(R.string.vesicula), ""));
        this.parts.add(new BodyPart(context.getString(R.string.conducto_cistico), ""));
        this.parts.add(new BodyPart(context.getString(R.string.manos), ""));
        this.parts.add(new BodyPart(context.getString(R.string.brazos), ""));
        this.parts.add(new BodyPart(context.getString(R.string.bazo), ""));
        this.parts.add(new BodyPart(context.getString(R.string.higado), ""));
        this.parts.add(new BodyPart(context.getString(R.string.vesicula), ""));
        this.parts.add(new BodyPart(context.getString(R.string.colon), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.caderas_ilion), ""));
        this.parts.add(new BodyPart(context.getString(R.string.caja_toraxica), ""));
        this.parts.add(new BodyPart(context.getString(R.string.costillas_inferiores), ""));
        this.parts.add(new BodyPart(context.getString(R.string.esternon), ""));
        this.parts.add(new BodyPart(context.getString(R.string.nervio_vago), ""));
        this.parts.add(new BodyPart(context.getString(R.string.plexo_solar), ""));
        this.parts.add(new BodyPart(context.getString(R.string.manos), ""));
        this.parts.add(new BodyPart(context.getString(R.string.brazos), ""));
        this.parts.add(new BodyPart(context.getString(R.string.colon), ""));
        this.parts.add(new BodyPart(context.getString(R.string.estomago), ""));
    }
}
