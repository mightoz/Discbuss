package teamosqar.discbuss.util;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import teamosqar.discbuss.activities.BusBarActivity;
import teamosqar.discbuss.activities.LoginActivity;

/**
 * Created by oskar on 30/10/2015.
 */
public class HideKeyboard {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }



}
