package teamosqar.discbuss.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.activities.LoginActivity;
import teamosqar.discbuss.activities.R;

/**
 * Created by joakim on 2015-10-27.
 */
public class BusActionBar extends Toolbar{

    public BusActionBar(Context context) {
        super(context);
    }



    public void initializeActionBar(final Context context){
        final ViewGroup actionBarLayout = (ViewGroup) ((Activity)context).getLayoutInflater().inflate(
                R.layout.activity_action_bar,
                null);
        ActionBar actionBar = ((AppCompatActivity)context).getSupportActionBar();

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        TextView actionBarText = (TextView)actionBarLayout.findViewById(R.id.actionBarTextView);
        actionBarText.setText("Discbuss");

        Model.getInstance().addObserver(new Observer(){

            public void updateNextBusStop() {
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

            @Override
            public void update(Observable observable, Object data) {
                updateNextBusStop();
            }
        });
    }
}
