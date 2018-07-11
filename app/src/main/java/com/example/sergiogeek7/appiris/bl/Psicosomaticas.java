package com.example.sergiogeek7.appiris.bl;

import android.content.Context;

import com.example.sergiogeek7.appiris.opencv.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Psicosomaticas
 *
 * Map sectores en coordenadas
 */

public class Psicosomaticas {

    List<BodySector> bodySectors;

    // LEFT KEYS
    int[] KEY_GD_L = {23, 0, 1, 2};
    int[] KEY_CX_L = {3, 4, 5, 6, 7};
    int[] KEY_VR_L = {8, 9, 10, 11, 12};
    int[] KEY_Q_L =  {13};
    int[] KEY_PN_L = {14, 15, 16};
    int[] KEY_ML_L = {17, 18};
    int[] KEY_KH_L = {19, 20, 21, 22};

    // RIGHT KEYS
    int[] KEY_VR_R = {23, 0, 1, 2, 3};
    int[] KEY_CX_R = {4, 5, 6, 7, 8};
    int[] KEY_GD_R = {9, 10, 11, 12};
    int[] KEY_KH_R = {13, 14, 15, 16};
    int[] KEY_ML_R = {17, 18};
    int[] KEY_PN_R = {19, 20, 21};
    int[] KEY_Q_R =  {22};

    // SCALE LEFT
    double[] SCALE_VR_L = {0.01, 0.06, 0.42, 0.57};
    double[] SCALE_Q_L = {0.01, 0.52, 0.45, 0.24};
    double[] SCALE_PN_L = {0.04, 0.57, 0.42, 0.42};
    double[] SCALE_ML_L = {0.38, 0.6, 0.25, 0.4};
    double[] SCALE_HK_L = {0.53, 0.51, 0.47, 0.49};
    double[] SCALE_GD_L = {0.58, 0.13, 0.42, 0.46};
    double[] SCALE_CX_L = {0.23, 0, 0.61, 0.4};

    // SCALE RIGHT
    double[] SCALE_VR_R = {0.58, 0.05, 0.42, 0.56};
    double[] SCALE_Q_R = {0.6, 0.5, 0.4, 0.25};
    double[] SCALE_PN_R = {0.55, 0.55, 0.4, 0.45};
    double[] SCALE_ML_R = {0.38, 0.6, 0.25, 0.4};
    double[] SCALE_HK_R = {0.03, 0.57, 0.48, 0.42};
    double[] SCALE_GD_R = {0.02, 0.14, 0.38, 0.5};
    double[] SCALE_CX_R = {0.15, 0, 0.6, 0.4};

    public Psicosomaticas(Context context, Gender gender){
        bodySectors = new ArrayList<>();
        bodySectors.add(new BodySectorGD(context, KEY_GD_R, KEY_GD_L, SCALE_GD_R, SCALE_GD_L, gender));
        bodySectors.add(new BodySectorCX(context, KEY_CX_R, KEY_CX_L, SCALE_CX_R, SCALE_CX_L, gender));
        bodySectors.add(new BodySectorVR(context, KEY_VR_R, KEY_VR_L, SCALE_VR_R, SCALE_VR_L, gender));
        bodySectors.add(new BodySectorQ(context, KEY_Q_R, KEY_Q_L, SCALE_Q_R, SCALE_Q_L, gender));
        bodySectors.add(new BodySectorPN(context, KEY_PN_R, KEY_PN_L, SCALE_PN_R, SCALE_PN_L, gender));
        bodySectors.add(new BodySectorML(context, KEY_ML_R, KEY_ML_L, SCALE_ML_R, SCALE_ML_L, gender));
        bodySectors.add(new BodySectorKH(context, KEY_KH_R, KEY_KH_L, SCALE_HK_R, SCALE_HK_L, gender));
    }

    public BodySector getBodySector(Shape shape, int keySide){
        int SLICE = 15;
        int index = (int) Math.floor(shape.getAngle() / SLICE);
        for(BodySector bodySector : bodySectors){
            if (bodySector.isInRange(index, keySide)) {
                return bodySector;
            }
        }
        return null;
    }

    public List<BodySector> getAll(){
        return this.bodySectors;
    }
}
