package teamosqar.discbuss.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.activities.R;
import teamosqar.discbuss.application.ProfileController;
import teamosqar.discbuss.util.Toaster;

public class ProfileActivity extends AppCompatActivity implements Observer {

    private ProfileController profileController;
    private TextView name, email, pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileController = new ProfileController();
        profileController.addObserver(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = (EditText)findViewById(R.id.displayname);
        email = (TextView)findViewById(R.id.useremail);
        pw = (TextView)findViewById(R.id.userpassword);
        name.setClickable(false);
        name.setFocusable(false);
        name.setBackgroundColor(Color.TRANSPARENT);
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
        if (id == R.id.action_displayname) {
            name.setClickable(true);
            name.setFocusable(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void update(Observable observable, Object data){
        name.setText(profileController.getName());
        email.setText(profileController.getEmail());
    }
}
