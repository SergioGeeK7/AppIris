package com.example.sergiogeek7.appiris;

import android.net.Uri;

/**
 * Created by sergiogeek7 on 23/12/17.
 */

public class Eye {

    private EyeFile original = new EyeFile();
    private EyeFile croped = new EyeFile();

    public Eye(EyeFile original, EyeFile croped){
        this.original = original;
        this.croped = croped;
    }

    public EyeFile getOriginal() {
        return original;
    }

    public void setOriginal(EyeFile original) {
        this.original = original;
    }

    public EyeFile getCroped() {
        return croped;
    }

    public void setCroped(EyeFile croped) {
        this.croped = croped;
    }
}
