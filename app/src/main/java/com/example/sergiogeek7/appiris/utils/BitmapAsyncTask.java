package com.example.sergiogeek7.appiris.utils;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.squareup.picasso.Picasso;

/**
 * Leer bitmap asincronamente
 */

public class BitmapAsyncTask extends AsyncTask<Uri, Void, Bitmap> {


    private Context context;
    private BitmapAsync ba;

    public BitmapAsyncTask(Context context, BitmapAsync ba){
        this.context = context;
        this.ba = ba;
    }

    @Override
    protected Bitmap doInBackground(Uri... uris) {
        try{
            return Picasso.with(context)
                    .load(uris[0])
                    .get();
        }catch (Exception ex){
            Log.e("e", ex.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ba.onBitmapLoaded(bitmap);
    }

    public interface BitmapAsync {
        void onBitmapLoaded(Bitmap bitmap);
    }
}
