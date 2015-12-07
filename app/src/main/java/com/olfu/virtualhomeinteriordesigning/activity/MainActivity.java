package com.olfu.virtualhomeinteriordesigning.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.olfu.virtualhomeinteriordesigning.R;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private static final String TAG = "Main Activity";

    LinearLayout leftContainer;
    ImageView leftStrip;

    //-- insert something her... later basta zxczczx
    float leftX, leftXnew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        leftContainer = (LinearLayout) findViewById(R.id.leftContainer);
        leftStrip = (ImageView) findViewById(R.id.leftStrip);
        leftStrip.setOnTouchListener(this);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float stripX = v.getX();

        if (v == leftStrip) {
            Log.d(TAG, "leftStrip touch");

            switch (event.getAction()) {

                case MotionEvent.ACTION_MOVE:
                    float distance = event.getX() - leftX;
//                    float xPos = UtilsApp.getPXValue(stripX);
                    Log.d(TAG, "StripX: " + stripX);
//                    if (xPos >= 0 && xPos <= 300) {

                    if (stripX < 0) {
                        stripX = 0;
                        v.setX(0);
                    } else if (stripX > leftContainer.getWidth()) {
                        stripX = leftContainer.getWidth();
                    }

                    if (stripX >= 0 && stripX <= leftContainer.getWidth()) {
                        leftContainer.setTranslationX(stripX + distance);
                        v.setX(stripX + distance);
                    }

                    break;

                case MotionEvent.ACTION_DOWN:
                    leftX = event.getX();
                    break;

                case MotionEvent.ACTION_BUTTON_RELEASE:
                    Log.d(TAG, "Release");
                    break;

            }

        }

        return true;
    }


}
