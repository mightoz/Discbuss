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

/**
 * @author Holmus
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText editName, editMail, editPass, editConfPass;
    private String name, mail, password, confPassword;
    private RegisterController rc;
    private int toastDuration = Toast.LENGTH_SHORT;
    private Context context;
    private Toast toast;
    @Override
    protected void onStart(){
        rc = new RegisterController();
        super.onStart();
        editName = (EditText) findViewById(R.id.editTextName);
        editMail = (EditText) findViewById(R.id.editTextEmail);
        editPass = (EditText) findViewById(R.id.editTextPassword);
        editConfPass = (EditText) findViewById(R.id.editTextConfPass);
        context = getApplicationContext();
    }

    public void registerPressed(View view){
        name = editName.getText().toString();
        mail = editMail.getText().toString();
        password = editPass.getText().toString();
        confPassword = editConfPass.getText().toString();
        if(checkData()){
            rc.registerUser(name, mail, password, confPassword);
            goToLogin();
        }
    }
    private void goToLogin(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        displayToast("Registration successful!");
        finish();
    }

    private boolean checkData(){
        if(name.isEmpty()){
            displayToast("Enter a name");
            return false;
        } else if(mail.isEmpty()|| !validateEmail()){
            displayToast("Enter an email");
            return false;
        } else if(password.isEmpty()){
            displayToast("Enter a password");
            return false;
        } else if(confPassword.isEmpty()){
            displayToast("Confirm your password");
            return false;
        } else if(!password.equals(confPassword)){
            displayToast("Passwords don't match");
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
    private void displayToast(String string){
        toast = toast.makeText(context, string, toastDuration);
        toast.show();
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
