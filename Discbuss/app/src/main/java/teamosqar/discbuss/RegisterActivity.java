package teamosqar.discbuss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * NOTE: This is really ugly code meant to test the firebase registration and storing of data.
 */
public class RegisterActivity extends AppCompatActivity {

    private Firebase mref;
    private EditText editName, editMail, editPass, editConfPass;
    private String name, mail, password, confPassword;
    @Override
    protected void onStart(){
        super.onStart();
        Firebase.setAndroidContext(this);
        mref = new Firebase("https://boiling-heat-3778.firebaseio.com/users");
        editName = (EditText) findViewById(R.id.editTextName);
        editMail = (EditText) findViewById(R.id.editTextEmail);
        editPass = (EditText) findViewById(R.id.editTextPassword);
        editConfPass = (EditText) findViewById(R.id.editTextConfPass);
    }

    public void registerPressed(View view){
        //TODO: Null-check, update Toast accordingly
        name = editName.getText().toString();
        mail = editMail.getText().toString();
        password = editPass.getText().toString();
        confPassword = editConfPass.getText().toString();
        if(password.equals(confPassword)) {
            mref.createUser(mail, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                    System.out.println("Successfully created user account with uid: " + result.get("uid"));
                    mref.child((String)result.get("uid")).child("name").setValue(name);
                    mref.child((String)result.get("uid")).child("karma").setValue(0);
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    // there was an error
                }
            });
        } else {
            //TODO: Password entries didn't match, deal with it.
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
