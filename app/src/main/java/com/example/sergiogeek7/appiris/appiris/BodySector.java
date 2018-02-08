package com.example.sergiogeek7.appiris.appiris;

import com.example.sergiogeek7.appiris.ImageFilters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiogeek7 on 7/02/18.
 */

public class BodySector {

    private int[] rightKey;
    private int[] leftKey;
    List<BodyPart> parts;

    public BodySector(int[] rightKey, int[] leftKey){
        this.rightKey = rightKey;
        this.leftKey = leftKey;
        this.parts = new ArrayList<>();
    }

    public boolean isInRange(int index, int keySide){
        int [] keys = keySide == ImageFilters.LEFT_EYE ? leftKey : rightKey;
        for (int key: keys){
            if(key == index){
                return true;
            }
        }
        return false;
    }

    public List<BodyPart> getParts(){
        return this.parts;
    }
}
