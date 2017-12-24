package com.example.sergiogeek7.appiris;

import android.net.Uri;

/**
 * Created by sergiogeek7 on 23/12/17.
 */

public class EyeFile {

    private Uri uri;
    private String absolutePath;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getAbsoletePath() {
        return absolutePath;
    }

    public void setAbsoletePath(String absoletePath) {
        this.absolutePath = absoletePath;
    }


}
