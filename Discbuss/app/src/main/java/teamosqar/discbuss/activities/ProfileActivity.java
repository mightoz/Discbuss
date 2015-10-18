package teamosqar.discbuss.activities;


import android.content.Intent;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

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
import teamosqar.discbuss.application.ProfileController;
import teamosqar.discbuss.fragments.ChangePasswordFragment;
import teamosqar.discbuss.fragments.EditDisplayname;
import teamosqar.discbuss.util.Toaster;

public abstract class ProfileActivity extends AppCompatActivity {

    protected ProfileController profileController;


    private TextView name, email, karma, nameTag, emailTag, karmaTag;
    private Button pwButton, statementButton, displayNameButton;
    private EditDisplayname displaynameFragment;
    private ChangePasswordFragment pwFragment;
    private FragmentManager fm;
    private FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        profileController = new ProfileController();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    public void presentTopStatements(View view) {
        //TODO what will happen here?

    }

}
