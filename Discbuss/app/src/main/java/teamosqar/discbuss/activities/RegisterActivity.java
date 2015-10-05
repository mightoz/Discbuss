package teamosqar.discbuss.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import teamosqar.discbuss.application.RegisterController;
import teamosqar.discbuss.util.Toaster;

/**
 * @author Holmus
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText editName, editMail, editPass, editConfPass;
    private String name, mail, password, confPassword;
    private RegisterController rc;

    @Override
    protected void onStart(){
        Firebase.setAndroidContext(this);
        rc = new RegisterController();
        super.onStart();
        editName = (EditText) findViewById(R.id.editTextName);
        editMail = (EditText) findViewById(R.id.editTextEmail);
        editPass = (EditText) findViewById(R.id.editTextPassword);
        editConfPass = (EditText) findViewById(R.id.editTextConfPass);
    }

    public void registerPressed(View view){
        name = editName.getText().toString();
        mail = editMail.getText().toString();
        password = editPass.getText().toString();
        confPassword = editConfPass.getText().toString();
        if(checkData()){
            rc.registerUser(name, mail, password);
            goToLogin();
        }
    }
    private void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        Toaster.displayToast("Registration successful!", this.getApplicationContext(), Toast.LENGTH_SHORT);
        finish();
    }

    private boolean checkData(){
        if(name.isEmpty()){
            Toaster.displayToast("Enter a name", this.getApplicationContext(), Toast.LENGTH_SHORT);
            return false;
        } else if(mail.isEmpty()|| !validateEmail()){
            Toaster.displayToast("Enter an email", this.getApplicationContext(), Toast.LENGTH_SHORT);
            return false;
        } else if(password.isEmpty()){
            Toaster.displayToast("Enter a password", this.getApplicationContext(), Toast.LENGTH_SHORT);
            return false;
        } else if(confPassword.isEmpty()){
            Toaster.displayToast("Confirm your password", this.getApplicationContext(), Toast.LENGTH_SHORT);
            return false;
        } else if(!password.equals(confPassword)){
            Toaster.displayToast("Passwords don't match", this.getApplicationContext(), Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }
    private boolean validateEmail(){
        //TODO: Better validation of that string would be neat
        for(int i = 0; i<mail.length(); i++){
            if(mail.charAt(i) == '@') {
                return true;
            }
        }
        return false;
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
