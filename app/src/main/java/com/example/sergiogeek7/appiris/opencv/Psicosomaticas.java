package com.example.sergiogeek7.appiris.opencv;

import android.content.Context;
import com.example.sergiogeek7.appiris.appiris.BodyPart;
import com.example.sergiogeek7.appiris.appiris.BodySector;
import com.example.sergiogeek7.appiris.appiris.BodySectorCX;
import com.example.sergiogeek7.appiris.appiris.BodySectorGD;
import com.example.sergiogeek7.appiris.appiris.BodySectorKH;
import com.example.sergiogeek7.appiris.appiris.BodySectorML;
import com.example.sergiogeek7.appiris.appiris.BodySectorPN;
import com.example.sergiogeek7.appiris.appiris.BodySectorQ;
import com.example.sergiogeek7.appiris.appiris.BodySectorVR;
import com.example.sergiogeek7.appiris.utils.Gender;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 27/12/17.
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

    public Psicosomaticas(Context context, Gender gender){
        bodySectors = new ArrayList<>();
        bodySectors.add(new BodySectorGD(context, KEY_GD_R, KEY_GD_L, gender));
        bodySectors.add(new BodySectorCX(context, KEY_CX_R, KEY_CX_L, gender));
        bodySectors.add(new BodySectorVR(context, KEY_VR_R, KEY_VR_L, gender));
        bodySectors.add(new BodySectorQ(context, KEY_Q_R, KEY_Q_L, gender));
        bodySectors.add(new BodySectorPN(context, KEY_PN_R, KEY_PN_L, gender));
        bodySectors.add(new BodySectorML(context, KEY_ML_R, KEY_ML_L, gender));
        bodySectors.add(new BodySectorKH(context, KEY_KH_R, KEY_KH_L, gender));
    }

    public List<BodyPart> getBodyPart(Shape shape, int keySide){
        int SLICE = 15;
        int index = (int) Math.floor(shape.getAngle() / SLICE);
        for(BodySector bodySector : bodySectors){
            if (bodySector.isInRange(index, keySide)) {
                return bodySector.getParts();
            }
        }
        return null;
    }
}
