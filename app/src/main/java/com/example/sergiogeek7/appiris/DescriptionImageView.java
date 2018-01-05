package com.example.sergiogeek7.appiris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.example.sergiogeek7.appiris.opencv.Shape;
import com.example.sergiogeek7.appiris.opencv.ShapeContext;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;
import org.opencv.core.Point;

/**
 * Created by sergiogeek7 on 2/01/18.
 */

public class DescriptionImageView extends View implements ShapeContext {

    private Paint mPaint;
    private int x;
    private int y;
    private Bitmap bitmap;
    private Shape shape;
    private final int RADIUS = 70;


    @Override
    public int getColumn() {
        return this.getWidth();
    }

    @Override
    public int getRow() {
        return this.getHeight();
    }

    public void updateView (Shape shape, Bitmap bitmap){
        if (this.bitmap != null){
            this.bitmap.recycle();
        }
        this.shape = shape;
        this.bitmap = bitmap;
        invalidate();
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setARGB(100,255,255,255);
    }

    public DescriptionImageView (Context context){
        super(context);
        init(context);
    }

    public DescriptionImageView (Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public DescriptionImageView (Context context, AttributeSet attrs, int defaultStyle){
        super(context, attrs, defaultStyle);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(this.bitmap == null)
            return;

        canvas.save();
        Point point = shape.getCoordinates(this);
        //canvas.translate((float) point.x - RADIUS, (float)  point.y - RADIUS);
        //canvas.drawCircle(0, 0, RADIUS, mPaint);
        canvas.scale(2, 2, (float) point.x + RADIUS, (float)  point.y + RADIUS);
        Bitmap bitmap = BitmapUtils.getResizedBitmap(this.bitmap, this.getWidth(), this.getHeight());
        this.bitmap.recycle();
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        //int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        // hSpecMode == MeasureSpec.EXACTLY set the minimum with scroll
        // hSpecMode == MeasureSpec.AT_MOST Wrap Content
        int hSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int wSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int myHeight = x = hSpecSize;
        int myWidth = y = wSpecSize;
        setMeasuredDimension(myWidth, myHeight);
    }
}
