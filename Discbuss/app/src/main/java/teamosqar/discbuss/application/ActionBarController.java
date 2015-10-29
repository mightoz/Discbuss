package teamosqar.discbuss.application;

import android.content.Context;

/**
 * Created by joakim on 2015-10-29.
 */
public class ActionBarController {

    public ActionBarController(){

    }

    public void onActivityCreated(Context context){
        Model.getInstance().onActivityCreated(context);
    }

    public void onActivityStarted(Context context){
        Model.getInstance().onActivityStarted(context);
    }

    public void resetModel(){
        Model.getInstance().resetModel();
    }
}
