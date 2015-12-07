package com.olfu.virtualhomeinteriordesigning;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by mykelneds on 6/20/15.
 */
public class UtilsApp extends Application {

    private static Context mContext;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    // Error message
    public static String E_EMAIL = "Please enter your email.";              // EMAIL FIELD IS NULL
    public static String E_PASSWORD = "Please enter your password";         // PASSWORD FIELD IS NULL
    public static String E_PASSWORD2 = "Please confirm your password";      // CONFIRMED YOUR PASSWORD
    public static String E_PASSLENGTH = "Your password must be alteast 8 characters";
    public static String E_NPASSWORD = "Password does not match";      // PASSWORD NOT MATCH;

    public static String E_LOGIN2 = "Your email and password does not match!";
    public static String E_NETWORK = "Please check your internet connection and try again.";

    // Message for deliveries
    public static String M_DELIVERY = "";
    public static String M_INTRANSIT = "";
    public static String M_ALREADYPICKUP = "";

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }


    public static Typeface opensansNormal() {

        return Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans_Light.ttf");
    }

    public static Typeface opensansBold() {

        return Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans_Bold.ttf");
    }


    // SharedPreferences (start)
    public static String getString(String title) {
        String str = preferences.getString(title, null);
        return str;
    }


    public static int getInt(String title) {
        int i = preferences.getInt(title, 0);
        return i;
    }

    public static void putString(String title, String value) {
        editor.putString(title, value);
        editor.apply();
    }

    public static void putInt(String title, int value) {
        editor.putInt(title, value);
        editor.apply();
    }

    public static void flushPrefences() {
        editor.clear();
        editor.commit();
    }
    // SharedPreferences (end)


    public static void toast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastInt(int i) {
        Toast.makeText(mContext, "Value: " + String.valueOf(i), Toast.LENGTH_SHORT).show();
    }


    public static void hideSoftKeyboard(final Activity activity, View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                hideSoftKeyboard(activity, innerView);
            }
        }
    }

    public static float getPXValue(float dp){
        //return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
        // return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
        return ((dp * mContext.getResources().getDisplayMetrics().density + 0.5f));
    }

    public static final float getDPValue(float px){

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, mContext.getResources().getDisplayMetrics());

    }
//
//    public static void deleteTables() {
//
//        DatabaseHelper dbHelper = DatabaseHelper.getInstance(mContext);
//        SQLiteDatabase db;
//
//        db = dbHelper.getWritableDatabase();
//        db.delete("tbl_seller", null, null);
//        db.delete("tbl_buyer", null, null);
//        db.delete("tbl_package", null, null);
//        db.delete("tbl_active_package", null, null);
//    }
}
