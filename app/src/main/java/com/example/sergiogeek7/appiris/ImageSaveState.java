package com.example.sergiogeek7.appiris;

import com.zomato.photofilters.imageprocessors.Filter;

/**
 * Created by sergiogeek7 on 26/12/17.
 */

public class ImageSaveState {

    public final int DEFAULT_BRIGHTNESS = 0;
    public final float DEFAULT_SATURATION = 1.0f;
    public final float DEFAULT_CONTRAST = 1.0f;
    public int brightness = DEFAULT_BRIGHTNESS;
    public float saturation = DEFAULT_SATURATION;
    public float contrast = DEFAULT_CONTRAST;
    public Filter filter;

    public boolean isDirty(){
        return brightness != DEFAULT_BRIGHTNESS || saturation != DEFAULT_SATURATION
                || contrast != DEFAULT_CONTRAST;
    }

    public void reset() {
        brightness = DEFAULT_BRIGHTNESS;
        saturation = DEFAULT_SATURATION;
        contrast = DEFAULT_CONTRAST;
    }
}
