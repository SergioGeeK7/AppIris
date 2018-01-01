package com.example.sergiogeek7.appiris;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.squareup.picasso.Picasso;

/**
 * Created by sergiogeek7 on 31/12/17.
 */

public class BitmapAsyncTask<T> extends AsyncTaskLoader<Bitmap>{

    Uri uri;

    public BitmapAsyncTask(Context context){
        super(context);
    }

    public BitmapAsyncTask(Context context, Uri uri){
        super(context);
        this.uri = uri;
    }

    @Override
    public Bitmap loadInBackground() {
        try{
            return Picasso.with(getContext())
                    .load(this.uri)
                    .resize(400, 400)
                    .get();
        }catch (Exception ex){
            Log.e("e", ex.getMessage());
            return null;
        }
    }
}
