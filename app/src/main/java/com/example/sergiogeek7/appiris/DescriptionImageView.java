package com.example.sergiogeek7.appiris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import com.example.sergiogeek7.appiris.opencv.Shape;
import com.example.sergiogeek7.appiris.opencv.ShapeContext;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;
import org.opencv.core.Point;

public class DescriptionImageView
        extends View
        implements ShapeContext
{
    private final int RADIUS = 70;
    private Bitmap bitmap;
    private Paint mPaint;
    private Shape shape;

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
        this.mPaint = new Paint();
        this.mPaint.setARGB(100, 255, 255, 255);
    }

    public int getColumn()
    {
        return getWidth();
    }

    public int getRow()
    {
        return getHeight();
    }

    protected void onDraw(Canvas paramCanvas)
    {
        super.onDraw(paramCanvas);
        if (this.bitmap == null) {
            return;
        }
        Point localPoint = this.shape.getCoordinates(this);
        paramCanvas.scale(2.0F, 2.0F, 70.0F + (float)localPoint.x,
                70.0F + (float)localPoint.y);
        paramCanvas.drawBitmap(BitmapUtils.getResizedBitmap(this.bitmap, getWidth(),
                getHeight()), 0.0F, 0.0F, null);
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
        Point localPoint = this.shape.getCoordinates(shapeContext);
        canvas.scale(2.0F, 2.0F, 10.0F + (float)localPoint.x,
                40.0F + (float)localPoint.y);
        canvas.drawBitmap(this.bitmap, 0.0F, 0.0F, null);
        BitmapUtils.saveBitmap(getContext(), localBitmap, paramUri);
        localBitmap.recycle();
    }

    public void updateView(Shape paramShape, Bitmap paramBitmap)
    {
        if (this.bitmap != null) {
            this.bitmap.recycle();
        }
        this.shape = paramShape;
        this.bitmap = paramBitmap;
        invalidate();
    }
}
