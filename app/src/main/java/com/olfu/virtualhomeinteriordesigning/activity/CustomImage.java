package com.olfu.virtualhomeinteriordesigning.activity;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by mykelneds on 10/5/15.
 */
public class CustomImage extends ImageView implements View.OnTouchListener {

    private static final String TAG = "Custom Image";

    PointF prevPoint = new PointF();
    PointF newPoint = new PointF();


    ScaleGestureDetector sgd;

    public CustomImage(Context context) {
        super(context);

        sgd = new ScaleGestureDetector(context, new scaleGestureDetector());
//        setScaleType(ScaleType.MATRIX);


//        setBackgroundColor(Color.parseColor("#000000"));
        setAdjustViewBounds(true);
//        setOnTouchListener(this);

    }

    public CustomImage(Context context, AttributeSet attrs) {
        super(context, attrs);

//        Matrix matrix = getImageMatrix();

        //matrix.getValues();
        //matrix.setScale;
        //matrix.postSca

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE:

                newPoint = new PointF(event.getX(), event.getY());
                setX(getX() + (newPoint.x - prevPoint.x));
                setY(getY() + (newPoint.y - prevPoint.y));
//                prevPoint = newPoint;

                break;

            case MotionEvent.ACTION_DOWN:

                prevPoint = new PointF(event.getX(), event.getY());
                break;

        }
        if (sgd.onTouchEvent(event)) return false;
        return super.onTouchEvent(event);

    }

    public class scaleGestureDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float scaleFactor;
            scaleFactor = detector.getScaleFactor();

            resizeImage(scaleFactor);
            Log.d("Custom Image", "Scale factor: " + scaleFactor);

            return true;
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void resizeImage(float scale) {

        ViewGroup.LayoutParams params = getLayoutParams();
        int width = getWidth();

        float fWidth = (float) width * scale;
        Log.d("Custom Image", "Width: " + width + "Scale: " + fWidth);
        params.width = (int) fWidth;

        params.width *= scale;
        setLayoutParams(params);

    }

}
