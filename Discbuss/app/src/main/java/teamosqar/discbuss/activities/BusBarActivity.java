package teamosqar.discbuss.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import teamosqar.discbuss.application.ActionBarController;

/**
 * Created by joakim on 2015-10-29.
 */
public class BusBarActivity extends AppCompatActivity {

    private ActionBarController controller;
    private TextView actionBarText;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        controller = new ActionBarController();

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.activity_action_bar,
                null);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        actionBarText = (TextView)findViewById(R.id.actionBarTextView);

        actionBarText.setText("Discbuss");

        controller.onActivityCreated(getApplicationContext());
    }

    @Override
    protected void onStart(){
        super.onStart();

        controller.onActivityStarted(getApplicationContext());

    }

}
