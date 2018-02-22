package com.example.sergiogeek7.appiris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.sergiogeek7.appiris.opencv.Psicosomaticas;
import com.example.sergiogeek7.appiris.opencv.Shape;
import com.example.sergiogeek7.appiris.opencv.ShapeContext;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;
import org.opencv.core.Point;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by sergiogeek7 on 21/12/17.
 */

public class InteractiveEyeVIew extends View implements ShapeContext {

    private Paint mPaint;
    private EyeViewBindings bindings;
    private Bitmap bitmap;
    private Bitmap bitmapResized;
    private List<Shape> shapes;
    private final int RADIUS = 25;

    public void saveView(Uri uri){
        Bitmap bitmap = this.bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        ShapeContext shapeContext = new ShapeContext() {
            @Override
            public int getColumn() {
                return canvas.getWidth();
            }
            @Override
            public int getRow() {
                return canvas.getHeight();
            }
        };
        for (Shape shape : this.shapes) {
            Point point = shape.getCoordinates(shapeContext);
            canvas.drawCircle((float) point.x, (float)  point.y, RADIUS, mPaint);
        }
        BitmapUtils.saveBitmap(getContext(), bitmap, uri);
        bitmap.recycle();
    }

    @Override
    public int getColumn() {
        return this.getWidth();
    }

    @Override
    public int getRow() {
        return this.getHeight();
    }

    interface EyeViewBindings {
        void onShapeClick(Shape shape);
    }

    public void updateView (List<Shape> shapes, Bitmap bitmap){
        if(this.bitmapResized != null){
            this.bitmapResized.recycle();
        }
        this.shapes = shapes;
        this.bitmap = bitmap;
        this.bitmapResized = BitmapUtils.getResizedBitmap(bitmap, this.getWidth(), this.getHeight());
        invalidate();
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setARGB(120,230,0,0);
        mPaint.setStyle(Paint.Style.STROKE);
        setDrawingCacheEnabled(true);
        try {
            bindings = (EyeViewBindings) context;
        }catch (Exception ex){
            Log.e("e", "Must implement " + EyeViewBindings.class.getName());
        }
    }

    public InteractiveEyeVIew (Context context){
        super(context);
        init(context);
    }

    public InteractiveEyeVIew (Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public InteractiveEyeVIew (Context context, AttributeSet attrs, int defaultStyle){
        super(context, attrs, defaultStyle);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(this.shapes == null)
            return;
        canvas.drawBitmap(this.bitmapResized, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN: {
                int x = (int)event.getX();
                int y = (int)event.getY();
                bindings.onShapeClick(new Shape(x, y, this));
               //invalidate(); re pain
            }
        }
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int myHeight  = hSpecSize;
        int myWidth = wSpecSize;
        setMeasuredDimension(myWidth, myHeight);
    }

}
