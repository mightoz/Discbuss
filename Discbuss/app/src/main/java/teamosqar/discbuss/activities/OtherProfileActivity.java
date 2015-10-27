package teamosqar.discbuss.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.application.DuoChatController;
import teamosqar.discbuss.application.ProfileController;

/**
 * Created by oskar on 16/10/2015.
 */
public class OtherProfileActivity extends ProfileActivity implements Observer {

    private TextView userInfo, karma;
    private String chatUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        chatUID = uid;
        profileController = new ProfileController(this, uid);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        profileController.addObserver(this);
        userInfo = (TextView) findViewById(R.id.textViewDisplayName);
        karma = (TextView) findViewById(R.id.textViewUserKarma);


        /*=============================================================== */
        TextView actionBarText;
        ActionBar actionBar;
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.activity_action_bar,
                null);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        actionBarText = (TextView) findViewById(R.id.actionBarTextView);
        actionBarText.setText("Discbuss"); // <-- as always this is how its done. easy to do.

        /*=============================================================== */

    }

    @Override
    public void onStart(){
        super.onStart();
        profileController.addAsObserver();
        profileController.updateNextBusStop();
    }


    public void update(Observable observable, Object data){
        userInfo.setText(profileController.getName() + ", " + profileController.getGender() + "(" + profileController.getAge()+")");
        karma.setText(profileController.getKarma());
        displayTopComments();
    }

    public void launchDuoChat(View view){
        DuoChatController.launchDuoChat(this, chatUID);
    }
}

