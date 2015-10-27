package teamosqar.discbuss.activities;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import teamosqar.discbuss.application.ProfileController;
import teamosqar.discbuss.util.Message;

/**
 * abstract class which contains the code used in both profile views
 */
public abstract class ProfileActivity extends AppCompatActivity {

    private List<TextView> topCommentValues, topCommentKarmas;
    protected ProfileController profileController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        topCommentValues = new ArrayList<>();
        topCommentKarmas = new ArrayList<>();
    }

    @Override
    public void onStop(){
        super.onStop();
        profileController.removeAsObserver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("logout", "logout");
            startActivity(intent);
            profileController.resetModel();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method which takes the users top comments and displays them in the profile.
     */
    public void displayTopComments() {
        TextView topComment1, topComment2, topComment3, topKarma1, topKarma2, topKarma3;
        ArrayList<String> topComments = profileController.getTopMessages();


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
        for (int i = 0; i < topComments.size(); i++) {

            topCommentValues.get(i).setText(profileController.getTopMessages().get(i));
            topCommentKarmas.get(i).setText(profileController.getTopKarma().get(i));

        }
    }
}
