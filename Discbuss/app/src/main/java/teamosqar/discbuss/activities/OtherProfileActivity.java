package teamosqar.discbuss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.application.Model;
import teamosqar.discbuss.application.ProfileController;
import teamosqar.discbuss.fragments.ChangePasswordFragment;
import teamosqar.discbuss.fragments.EditDisplayname;

/**
 * Created by oskar on 16/10/2015.
 */
public class OtherProfileActivity extends ProfileActivity implements Observer {

    private TextView userInfo, karma;
    private ProfileController profileController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        profileController = new ProfileController(uid);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        profileController.addObserver(this);
        userInfo = (TextView) findViewById(R.id.textViewDisplayName);
        karma = (TextView) findViewById(R.id.textViewUserKarma);
    }

    public void update(Observable observable, Object data){
        userInfo.setText(profileController.getName() + " ," + profileController.getGender() + " " + profileController.getAge());
        karma.setText("Karma: " + profileController.getKarma());
    }
}

