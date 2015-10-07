package teamosqar.discbuss.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.activities.R;
import teamosqar.discbuss.application.ProfileController;
import teamosqar.discbuss.fragments.EditDisplayname;
import teamosqar.discbuss.util.Toaster;

public class ProfileActivity extends AppCompatActivity implements Observer {

    private ProfileController profileController;
    private TextView name, email, karma;
    private Button pwButton, statementButton, displayNameButton;
    private EditDisplayname displaynameFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileController = new ProfileController();
        profileController.addObserver(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = (EditText)findViewById(R.id.displayname);
        email = (TextView)findViewById(R.id.useremail);
        name.setClickable(false);
        name.setFocusable(false);
        name.setBackgroundColor(Color.TRANSPARENT);
        karma = (TextView) findViewById(R.id.userkarma);
        pwButton = (Button) findViewById(R.id.changePwButton);
        statementButton = (Button) findViewById(R.id.topstatementButton);
        displayNameButton = (Button) findViewById(R.id.displaynameButton);
        displaynameFragment = new EditDisplayname();
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
        karma.setText(profileController.getKarma());
    }

    public void changeUserName(View view) {
        pwButton.setVisibility(View.GONE);
        statementButton.setVisibility(View.GONE);
        displayNameButton.setVisibility(View.GONE);

        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.fragmentPlaceholder, displaynameFragment);
        ft.commit();
    }

    public void changeDisplayname(View view) {
        EditText newName = (EditText)findViewById(R.id.editTextNewName);

        if(!newName.getText().toString().isEmpty()) {
            profileController.setNewDisplayName(newName.getText().toString());
            FragmentTransaction newFt = getFragmentManager().beginTransaction();
            newFt.remove(displaynameFragment);
            newFt.commit();
            pwButton.setVisibility(View.VISIBLE);
            statementButton.setVisibility(View.VISIBLE);
            displayNameButton.setVisibility(View.VISIBLE);
        }
    }

    public void presentTopStatements(View view) {
        //TODO what will happen here?

    }

    public void changePassword(View view) {
        //TODO create this, what will happen when this button is pressed?

    }
}
