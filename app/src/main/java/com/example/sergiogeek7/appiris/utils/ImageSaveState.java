package com.example.sergiogeek7.appiris.utils;

import com.zomato.photofilters.imageprocessors.Filter;

/**
 * ImageSaveState
 *
 * Estado de filtros en una imagen
 */

public class ImageSaveState {

    public final int DEFAULT_BRIGHTNESS = 0;
    public final float DEFAULT_SATURATION = 1.0f;
    public final float DEFAULT_CONTRAST = 1.0f;
    public int brightness = DEFAULT_BRIGHTNESS;
    public float saturation = DEFAULT_SATURATION;
    public float contrast = DEFAULT_CONTRAST;
    public Filter filter;

    /**
     * Detecta si una imagen ah sido modificada por algun filtro
     * @return
     */
    public boolean isDirty(){
        return brightness != DEFAULT_BRIGHTNESS || saturation != DEFAULT_SATURATION
                || contrast != DEFAULT_CONTRAST;
    }

    /**
     * Reestablese los filtros a su estado original
     */
    public void reset() {
        brightness = DEFAULT_BRIGHTNESS;
        saturation = DEFAULT_SATURATION;
        contrast = DEFAULT_CONTRAST;
    }
}
