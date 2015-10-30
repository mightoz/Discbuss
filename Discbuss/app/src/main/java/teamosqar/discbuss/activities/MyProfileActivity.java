package teamosqar.discbuss.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.application.ProfileController;
import teamosqar.discbuss.fragments.ChangePasswordFragment;
import teamosqar.discbuss.fragments.EditUserName;
import teamosqar.discbuss.util.Toaster;

public class MyProfileActivity extends ProfileActivity implements Observer {

    private TextView email, name, karma;
    private Button pwButton, displayNameButton;
    private EditUserName userNameFragment;
    private boolean changePass, changeName;
    private ChangePasswordFragment pwFragment;
    private FragmentManager fm;
    private FrameLayout fragmentPlaceholder;


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
        userNameFragment = new EditUserName();
        pwFragment = new ChangePasswordFragment();

    }

    /**
     * Makes sure the back button works as intended when fragments are active
     */
    public void onBackPressed() {
        if (changePass) {
            FragmentTransaction newFt = getFragmentManager().beginTransaction();
            newFt.remove(pwFragment);
            newFt.commit();
            pwButton.setVisibility(View.VISIBLE);
            displayNameButton.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            karma.setVisibility(View.VISIBLE);
            changePass = false;
        } else if (changeName) {
            FragmentTransaction newFt = getFragmentManager().beginTransaction();
            newFt.remove(userNameFragment);
            fragmentPlaceholder.setVisibility(View.INVISIBLE);
            newFt.commit();
            pwButton.setVisibility(View.VISIBLE);
            displayNameButton.setVisibility(View.VISIBLE);

            changeName = false;
        } else {
            System.out.println("Runs supermethod.");
            super.onBackPressed();
            return;
        }
    }

    /**
     * Updates the users data based on new input, called as an Observer when the Observed object identifies an update is required
     *
     * @param observable
     * @param data
     */
    public void update(Observable observable, Object data) {
        name.setText(profileController.getName() + ", " + profileController.getGender() + " " + profileController.getAge() + " år");
        email.setText("Email: " + profileController.getEmail());
        karma.setText(profileController.getKarma());
        displayTopComments();
    }

    @Override
    public void onStart() {
        super.onStart();
        changePass = false;
        changeName = false;
        Firebase.setAndroidContext(this);
        fragmentPlaceholder = (FrameLayout)findViewById(R.id.fragmentPlaceholder);
    }

    /**
     * Called when the change user name button is pressed.
     * Hides specific profile elements and displays the change user name fragment.
     *
     * @param view
     */
    public void changeUserName(View view) {
        pwButton.setVisibility(View.GONE);
        displayNameButton.setVisibility(View.GONE);
        fragmentPlaceholder.setVisibility(View.VISIBLE);
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragmentPlaceholder, userNameFragment);
        ft.commit();
        changeName = true;
    }

    /**
     * Called when the save name button is pressed when changing user name.
     * Calls the change user name method in controller and then reverts the profile new to normal.
     *
     * @param view
     */
    public void confirmUserName(View view) {
        EditText newName = (EditText) findViewById(R.id.editTextNewName);

        if (!newName.getText().toString().isEmpty()) {
            profileController.setNewDisplayName(newName.getText().toString());
            FragmentTransaction newFt = getFragmentManager().beginTransaction();
            newFt.remove(userNameFragment);
            newFt.commit();
            pwButton.setVisibility(View.VISIBLE);
            displayNameButton.setVisibility(View.VISIBLE);
            changeName = false;
        } else {
            Toaster.displayToast("Enter a name", this, Toast.LENGTH_SHORT);
        }
    }

    /**
     * Called when the change password button is pressed.
     * Hides specific profile elements and displays the change password fragment
     *
     * @param view
     */
    public void changePassword(View view) {
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

    /**
     * Called when the save password button is pressed
     * Calls the change password method in controller and reverts profile view to normal
     *
     * @param view
     */
    public void confirmNewPassword(View view) {

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
}
