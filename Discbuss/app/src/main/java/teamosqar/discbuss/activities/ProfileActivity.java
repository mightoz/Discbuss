package teamosqar.discbuss.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import teamosqar.discbuss.activities.R;
import teamosqar.discbuss.application.ProfileController;

public class ProfileActivity extends AppCompatActivity {

    private ProfileController profileController;
    private TextView name, email, pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileController = new ProfileController();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = (TextView)findViewById(R.id.displayname);
        email = (TextView)findViewById(R.id.useremail);
        pw = (TextView)findViewById(R.id.userpassword);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
