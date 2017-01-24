package com.example.presly.Values;

import android.app.Activity;
        import android.content.res.Configuration;



public class Utilities {
   //check device for orientation (landscape or portrait)

    public static boolean isPortrait(Activity activity) {
        return (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }
    public static boolean isLandscape(Activity activity) {
        return !isPortrait(activity);
    }


}
