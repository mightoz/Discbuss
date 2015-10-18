package teamosqar.discbuss.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.fragments.ChangePasswordFragment;
import teamosqar.discbuss.fragments.EditDisplayname;

/**
 * Created by oskar on 16/10/2015.
 */
public class OtherProfileActivity extends ProfileActivity implements Observer {

    private TextView userInfo, karma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        profileController.addObserver(this);
        userInfo = (TextView) findViewById(R.id.textViewDisplayName);
        karma = (TextView) findViewById(R.id.textViewUserKarma);
    }

    public void update(Observable observable, Object data){
        userInfo.setText(profileController.getName() + " ," + profileController.getGender() + " " + profileController.getAge());
        karma.setText(profileController.getKarma());
    }
}

