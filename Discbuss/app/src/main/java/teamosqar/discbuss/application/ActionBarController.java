package teamosqar.discbuss.application;

import android.content.Context;

/**
 * Created by joakim on 2015-10-29.
 */
public class ActionBarController{


    public ActionBarController(){

    }

    public void resetModel(){
        Model.getInstance().resetModel();
    }

    public void updateCurrentContext(Context context) {
        Model.getInstance().updateCurrentContext(context);
    }
}
