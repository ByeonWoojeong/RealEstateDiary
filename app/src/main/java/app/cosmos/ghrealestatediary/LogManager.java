package app.cosmos.ghrealestatediary;

import android.util.Log;


public class LogManager {
    private static final String TAG = "MainActivity";
    private static final boolean DEBUG = true;
    static void print(String msg){
        Log.v(TAG,msg);
    }
}
