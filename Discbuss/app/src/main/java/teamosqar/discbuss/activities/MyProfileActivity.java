package teamosqar.discbuss.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.application.ProfileController;
import teamosqar.discbuss.fragments.ChangePasswordFragment;
import teamosqar.discbuss.fragments.EditDisplayname;
import teamosqar.discbuss.util.Toaster;

public class MyProfileActivity extends ProfileActivity implements Observer {

    private TextView email, name, karma, actionBarText, topComment1, topComment2, topComment3, topKarma1, topKarma2, topKarma3;
    private Button pwButton, displayNameButton;
    private EditDisplayname displaynameFragment;
    private boolean changePass, changeName;
    private ChangePasswordFragment pwFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FrameLayout fragmentPlaceholder;
    private ProfileController profileController;
    private ActionBar actionBar;
    private List<TextView> topCommentValues, topCommentKarmas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileController = new ProfileController(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        profileController.addObserver(this);
        name = (TextView) findViewById(R.id.textViewDisplayName);
        karma = (TextView) findViewById(R.id.textViewUserKarma);
        email = (TextView) findViewById(R.id.textViewUserEmail);
        pwButton = (Button) findViewById(R.id.changePwButton);
        displayNameButton = (Button) findViewById(R.id.displaynameButton);
        displaynameFragment = new EditDisplayname();
        pwFragment = new ChangePasswordFragment();
        topCommentValues = new ArrayList<>();
        topCommentKarmas = new ArrayList<>();
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
    }

    public void onBackPressed(){
        if(changePass){
            FragmentTransaction newFt = getFragmentManager().beginTransaction();
            newFt.remove(pwFragment);
            newFt.commit();
            pwButton.setVisibility(View.VISIBLE);
            displayNameButton.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            karma.setVisibility(View.VISIBLE);
        } else if(changeName){
            FragmentTransaction newFt = getFragmentManager().beginTransaction();
            newFt.remove(displaynameFragment);
            fragmentPlaceholder.setVisibility(View.INVISIBLE);
            newFt.commit();
            pwButton.setVisibility(View.VISIBLE);
            displayNameButton.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    public void update(Observable observable, Object data) {
        name.setText(profileController.getName() + ", " + profileController.getGender() + " " + profileController.getAge() + " år");
            email.setText("Email: " + profileController.getEmail());
            karma.setText(profileController.getKarma());
            displayTopComments();
    }

    @Override
    public void onStart(){
        super.onStart();
        changePass = false;
        changeName = false;
        Firebase.setAndroidContext(this);
        profileController.addAsObserver();
        profileController.updateNextBusStop();
        fragmentPlaceholder = (FrameLayout)findViewById(R.id.fragmentPlaceholder);
    }
    @Override
    public void onStop(){
        super.onStop();
        profileController.removeAsObserver();
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


    public void changeUserName(View view) {
        pwButton.setVisibility(View.GONE);
        displayNameButton.setVisibility(View.GONE);
        fragmentPlaceholder.setVisibility(View.VISIBLE);
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.fragmentPlaceholder, displaynameFragment);
        ft.commit();
        changeName = true;
    }

    public void changeDisplayname(View view) {
        EditText newName = (EditText) findViewById(R.id.editTextNewName);

        if (!newName.getText().toString().isEmpty()) {
            profileController.setNewDisplayName(newName.getText().toString());
            FragmentTransaction newFt = getFragmentManager().beginTransaction();
            newFt.remove(displaynameFragment);
            newFt.commit();
            pwButton.setVisibility(View.VISIBLE);
            displayNameButton.setVisibility(View.VISIBLE);
            changeName = false;
        } else {
            Toaster.displayToast("Enter a name", this, Toast.LENGTH_SHORT);
        }
    }

    public void changePassword(View view) {
        //TODO create this, what will happen when this button is pressed?
        pwButton.setVisibility(View.GONE);
        displayNameButton.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        karma.setVisibility(View.GONE);

        fm = getFragmentManager();
        FragmentTransaction newFt = fm.beginTransaction();
        newFt.add(R.id.changePasswordPlaceholder, pwFragment);
        newFt.commit();
        changePass = true;

    }

    public void setNewPassword(View view) {

        EditText newPassword = (EditText) findViewById(R.id.editTextNewPassword);
        EditText confirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        EditText oldPassword = (EditText) findViewById(R.id.editTextOldPassword);

        if (newPassword.getText().toString().equals(confirmPassword.getText().toString()) && !newPassword.getText().toString().isEmpty()) {

            profileController.changePassword(oldPassword.getText().toString(), newPassword.getText().toString(), this);

            FragmentTransaction newFt = fm.beginTransaction();
            newFt.remove(pwFragment);
            newFt.commit();
            pwButton.setVisibility(View.VISIBLE);
            displayNameButton.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            karma.setVisibility(View.VISIBLE);
            changePass = false;

        } else {
            Toaster.displayToast("Fel konfirmationslösen", this, Toast.LENGTH_LONG);
        }

    }

    public void displayTopComments() {
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
        for (int i = 0; i < topComments.size(); i++) {

            topCommentValues.get(i).setText(profileController.getTopMessages().get(i));
            topCommentKarmas.get(i).setText(profileController.getTopKarma().get(i));

        }
    }
}
