package teamosqar.discbuss.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import teamosqar.discbuss.application.LoginController;

public class LoginActivity extends AppCompatActivity {

    private LoginController loginController;
    private EditText editEmail, editPassword;
    private String name, password;
    private Button buttonLogin;
    private Button buttonNotRegistered;

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (EditText)findViewById(R.id.editTextLoginEmail);
        editPassword = (EditText)findViewById(R.id.editTextLoginPassword);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonNotRegistered = (Button)findViewById(R.id.buttonNotRegistered);
        loginController = new LoginController();
    }

    public void loginPressed(View view){
        if(loginController.tryLogin(editEmail.getText().toString(), editPassword.getText().toString())){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            //TODO: handle failed login
        }
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
}
