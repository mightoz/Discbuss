package teamosqar.discbuss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.application.DuoChatController;
import teamosqar.discbuss.application.Model;
import teamosqar.discbuss.application.ProfileController;
import teamosqar.discbuss.fragments.ChangePasswordFragment;
import teamosqar.discbuss.fragments.EditDisplayname;
import teamosqar.discbuss.util.Message;

/**
 * Created by oskar on 16/10/2015.
 */
public class OtherProfileActivity extends ProfileActivity implements Observer {

    private TextView userInfo, karma, topComment1, topComment2, topComment3, topKarma1, topKarma2, topKarma3;
    private ProfileController profileController;
    private List<TextView> topCommentValues, topCommentKarmas;
    private String chatUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        chatUID = uid;
        profileController = new ProfileController(uid);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        profileController.addObserver(this);
        userInfo = (TextView) findViewById(R.id.textViewDisplayName);
        karma = (TextView) findViewById(R.id.textViewUserKarma);
        topCommentValues = new ArrayList<>();
        topCommentKarmas = new ArrayList<>();

    }

    public void update(Observable observable, Object data){
        userInfo.setText(profileController.getName() + " ," + profileController.getGender() + " " + profileController.getAge());
        karma.setText("Karma: " + profileController.getKarma());
        displayTopComments();
    }

    public void displayTopComments(){
        ArrayList<String> topComments;
        topComments = profileController.getTopMessages();

        topComment1 = (TextView) findViewById(R.id.topComment1Value);
        topComment2 = (TextView) findViewById(R.id.topComment2Value);
        topComment3 = (TextView) findViewById(R.id.topComment3Value);
        topKarma1 = (TextView) findViewById(R.id.topKarma1Value);
        topKarma2 = (TextView) findViewById(R.id.topKarma2Value);
        topKarma3 = (TextView) findViewById(R.id.topKarma3Value);
        topCommentValues.add(topComment1);
        topCommentValues.add(topComment2);
        topCommentValues.add(topComment3);
        topCommentKarmas.add(topKarma1);
        topCommentKarmas.add(topKarma2);
        topCommentKarmas.add(topKarma3);
        for(int i = 0; i < topComments.size(); i++){

            topCommentValues.get(i).setText(profileController.getTopMessages().get(i));
            topCommentKarmas.get(i).setText(profileController.getTopKarma().get(i));

        }
    }

    public void launchDuoChat(View view){
        DuoChatController.launchDuoChat(this, chatUID);
    }
}

