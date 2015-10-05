package teamosqar.discbuss.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.application.LoginController;
import teamosqar.discbuss.util.Toaster;

public class LoginActivity extends AppCompatActivity implements Observer {

    //Used for retrieving from savedInstanceState
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String AUTO_LOGIN = "autoLogin";

    private LoginController loginController;
    private EditText editEmail, editPassword;
    private CheckBox autoLoginCheckbox;
    private SharedPreferences sharedPref;

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_login);
        editEmail = (EditText)findViewById(R.id.editTextLoginEmail);
        editPassword = (EditText)findViewById(R.id.editTextLoginPassword);
        autoLoginCheckbox = (CheckBox)findViewById(R.id.autoLoginCheckBox);
        loginController = new LoginController();
        loginController.addObserver(this);



        sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean autoLogin = sharedPref.getBoolean(AUTO_LOGIN, false);
        if(autoLogin){
            String email = sharedPref.getString(EMAIL, "email");
            String password = sharedPref.getString(PASSWORD, "pass");
            editEmail.setText(email);
            editPassword.setText(password);
            autoLoginCheckbox.setChecked(autoLogin);
            loginController.tryLogin(email, password);
        }

    }

    public void loginPressed(View view){
        loginController.tryLogin(editEmail.getText().toString(), editPassword.getText().toString());
    }

    public void notRegisteredPressed(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    @Override
    public void update(Observable observable, Object data) {
        Log.d("notifications", "recieved notification");
        if(loginController.getLoginStatus()){
            SharedPreferences.Editor editor = sharedPref.edit();
            if(autoLoginCheckbox.isChecked()) {
                editor.putString(EMAIL, editEmail.getText().toString());
                editor.putString(PASSWORD, editPassword.getText().toString());
                editor.putBoolean(AUTO_LOGIN, autoLoginCheckbox.isChecked());
                editor.commit();
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toaster.displayToast("Login failed", getApplicationContext(), Toast.LENGTH_SHORT);
        }
    }
}
