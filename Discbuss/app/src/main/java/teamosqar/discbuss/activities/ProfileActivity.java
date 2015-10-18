package teamosqar.discbuss.activities;


import android.content.Intent;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.application.ProfileController;
import teamosqar.discbuss.fragments.ChangePasswordFragment;
import teamosqar.discbuss.fragments.EditDisplayname;
import teamosqar.discbuss.util.Toaster;

public class ProfileActivity extends AppCompatActivity implements Observer {

    private ProfileController profileController;
    //TODO: This should not be saved here. -> Move to controller.
//    private Model model = Model.getInstance();
    private TextView name, email, karma, nameTag, emailTag, karmaTag, actionBarText;
    private Button pwButton, statementButton, displayNameButton;
    private EditDisplayname displaynameFragment;
    private ChangePasswordFragment pwFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileController = new ProfileController(this);
        profileController.addObserver(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = (TextView)findViewById(R.id.textViewDisplayName);
        email = (TextView)findViewById(R.id.textViewUserEmail);
        karma = (TextView) findViewById(R.id.textViewUserKarma);
        karmaTag = (TextView)findViewById(R.id.textViewKarmaTag);
        emailTag = (TextView)findViewById(R.id.textViewEmailTag);
        nameTag = (TextView)findViewById(R.id.textViewNameTag);
        pwButton = (Button) findViewById(R.id.changePwButton);
        statementButton = (Button) findViewById(R.id.topStatementButton);
        displayNameButton = (Button) findViewById(R.id.displaynameButton);
        displaynameFragment = new EditDisplayname();
        pwFragment = new ChangePasswordFragment();

        /*=============================================================== */
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.activity_action_bar,
                null);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        actionBarText = (TextView)findViewById(R.id.actionBarTextView);
        actionBarText.setText("Nästa: "); // <-- as always this is how its done. easy to do.

        actionBarText.setTextColor(Color.GRAY);
        /*=============================================================== */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }
    //TODO: Needs refactoring, model is held in controller.
   /* @Override
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
        else if(id == R.id.logout){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("logout", "logout");
            startActivity(intent);
            model.resetModel();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }*/

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
        pwButton.setVisibility(View.GONE);
        statementButton.setVisibility(View.GONE);
        displayNameButton.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        karma.setVisibility(View.GONE);
        karmaTag.setVisibility(View.GONE);
        emailTag.setVisibility(View.GONE);
        nameTag.setVisibility(View.GONE);

        fm = getFragmentManager();
        FragmentTransaction newFt = fm.beginTransaction();
        newFt.add(R.id.changePasswordPlaceholder, pwFragment);
        newFt.commit();

    }

    public void setNewPassword(View view) {

        EditText newPassword = (EditText)findViewById(R.id.editTextNewPassword);
        EditText confirmPassword = (EditText)findViewById(R.id.editTextConfirmPassword);
        EditText oldPassword = (EditText)findViewById(R.id.editTextOldPassword);

        if(newPassword.getText().toString().equals(confirmPassword.getText().toString())){

            profileController.changePassword(oldPassword.getText().toString(), newPassword.getText().toString(), this);

            FragmentTransaction newFt = fm.beginTransaction();
            newFt.remove(pwFragment);
            newFt.commit();


            pwButton.setVisibility(View.VISIBLE);
            statementButton.setVisibility(View.VISIBLE);
            displayNameButton.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            karma.setVisibility(View.VISIBLE);
            karmaTag.setVisibility(View.VISIBLE);
            emailTag.setVisibility(View.VISIBLE);
            nameTag.setVisibility(View.VISIBLE);

        }else{
            Toaster.displayToast("Wrong confirmation password", this, Toast.LENGTH_LONG);
        }

    }

}
