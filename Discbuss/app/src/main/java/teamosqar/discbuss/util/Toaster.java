package teamosqar.discbuss.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by joakim on 2015-10-02.
 */
public class Toaster {

    private Toaster(){
    }

    public static void displayToast(String text, Context context, int toastDuration){
        Toast toast = Toast.makeText(context, text, toastDuration);
        toast.show();
    }
}
