package teamosqar.discbuss.application;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.activities.R;

/**
 * Created by joakim on 2015-10-29.
 */
public class ActionBarController implements Observer {

    private Context context;

    public ActionBarController(Context context){
        this.context = context;
    }

    public void resetModel(){
        Model.getInstance().resetModel();
    }

    public void setCurrentActionBar(ActionBar supportActionBar) {
        Model.getInstance().setCurrentActionBar(supportActionBar);
    }

    @Override
    public void update(Observable observable, Object obj) { updateNextBusStop();
    }

    public void updateNextBusStop(){
        if (Model.getInstance().getNextBusStop()!=null&& !Model.getInstance().getNextBusStop().isEmpty()) {
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textView = (TextView) ((Activity) context).findViewById(R.id.actionBarTextView);
                    textView.setText("Nästa hållplats: " + Model.getInstance().getNextBusStop());
                }
            });
        }
    }

    public void addAsObserver(){
        Model.getInstance().addObserver(this);
    }

    public void removeAsObserver(){
        Model.getInstance().deleteObserver(this);
    }
}
