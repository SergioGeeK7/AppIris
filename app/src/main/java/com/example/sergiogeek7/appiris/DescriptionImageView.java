package com.example.sergiogeek7.appiris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

import com.example.sergiogeek7.appiris.appiris.BodyPart;
import com.example.sergiogeek7.appiris.appiris.BodySector;
import com.example.sergiogeek7.appiris.opencv.Shape;
import com.example.sergiogeek7.appiris.opencv.ShapeContext;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.List;

public class DescriptionImageView
        extends View
        implements ShapeContext {

    private Bitmap bitmap;
    double [] scale;
    private int drawableId;

    public DescriptionImageView(Context paramContext)
    {
        super(paramContext);
        init(paramContext);
    }

    public DescriptionImageView(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        init(paramContext);
    }

    public DescriptionImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
        init(paramContext);
    }

    private void init(Context paramContext)
    {

    }

    public int getColumn()
    {
        return getWidth();
    }

    public int getRow()
    {
        return getHeight();
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if (this.bitmap == null) {
            return;
        }
        Log.e("scale level",  "" + scale[0] + " " + scale[1] + " " + scale[2] + " "
                + scale[3]);
        // x- width , y -> height
        Bitmap newBitmap = Bitmap.createBitmap(this.bitmap,
                (int)(scale[0] * this.bitmap.getWidth()),
                (int)(scale[1] * this.bitmap.getHeight()),
                (int)(scale[2] * this.bitmap.getWidth()),
                (int)(scale[3] * this.bitmap.getHeight()));
        canvas.drawBitmap(newBitmap, 0, 0, null);
    }

//    if(this.drawableBodyPart != 0){
//    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
//    Log.e("idBitmap", this.drawableBodyPart + "");
//    Drawable dr = getResources().getDrawable(drawableBodyPart);
//    Bitmap bodyBitmap = drawableToBitmap(dr);
//    if(bodyBitmap == null){
//        Log.e("idBitmap", this.drawableBodyPart + " is null");
//    }
//    paramCanvas.drawBitmap(
//            bodyBitmap,0,0,paint);
//}

    public Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    protected void onMeasure(int paramInt1, int paramInt2)
    {
        int i = View.MeasureSpec.getSize(paramInt2);
        setMeasuredDimension(View.MeasureSpec.getSize(paramInt1), i);
    }

    public void saveView(Uri paramUri)
    {
        Bitmap localBitmap = Bitmap.createBitmap(this.bitmap.getWidth(),
                this.bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(localBitmap);
        ShapeContext shapeContext = new ShapeContext()
        {
            public int getColumn()
            {
                return canvas.getWidth();
            }

            public int getRow()
            {
                return canvas.getHeight();
            }
        };
        //Point localPoint = this.shape.getCoordinates(shapeContext);
//        canvas.scale(2.0F, 2.0F, 10.0F + (float)localPoint.x,
//                40.0F + (float)localPoint.y);
        canvas.drawBitmap(this.bitmap, 0.0F, 0.0F, null);
        BitmapUtils.saveBitmap(getContext(), localBitmap, paramUri);
        localBitmap.recycle();
    }

    public void updateView(Bitmap bitmap, int drawableId, double[] scale)
    {
        if (this.bitmap != null) {
            this.bitmap.recycle();
        }
        this.scale = scale;
        this.drawableId = drawableId;
        this.bitmap = bitmap;
        invalidate();
    }
}
