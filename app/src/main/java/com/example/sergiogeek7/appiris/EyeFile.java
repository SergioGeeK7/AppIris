package com.example.sergiogeek7.appiris;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sergiogeek7 on 23/12/17.
 */

public class EyeFile implements Parcelable{

    private Uri uri;
    private String absolutePath;

    public ImageSaveState getSaveState() {
        return saveState;
    }

    public void setSaveState(ImageSaveState saveState) {
        this.saveState = saveState;
    }

    private ImageSaveState saveState = new ImageSaveState();

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Bitmap bitmap;

    protected EyeFile(Parcel in) {
        //uri = in.readParcelable(Uri.class.getClassLoader());
        this.uri = Uri.parse(in.readString());
        this.absolutePath = in.readString();
    }

    public EyeFile (){

    }


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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uri.toString());
        parcel.writeString(absolutePath);
    }

    public static final Creator<EyeFile> CREATOR = new Creator<EyeFile>() {
        @Override
        public EyeFile createFromParcel(Parcel in) {
            return new EyeFile(in);
        }

        @Override
        public EyeFile[] newArray(int size) {
            return new EyeFile[size];
        }
    };
}
