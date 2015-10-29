package teamosqar.discbuss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;

import teamosqar.discbuss.application.RegisterController;
import teamosqar.discbuss.util.Toaster;

/**
 * @author Holmus
 *
 * Activity class for the register view
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText editName, editMail, editPass, editConfPass;
    private String name, mail, password, confPassword, genderSelection,
            birthYear, birthMonth, birthDay;
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

    /**
     * If all registration data is valid, calls the registration method in controller
     * @param view
     */
    public void registerPressed(View view){
        name = editName.getText().toString();
        mail = editMail.getText().toString();
        password = editPass.getText().toString();
        confPassword = editConfPass.getText().toString();
        if(checkData()){
            rc.registerUser(name, mail, password, genderSelection, birthYear, birthMonth, birthDay);
            goToLogin();
        }
    }

    /**
     * takes the user to the login view
     */
    private void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        Toaster.displayToast("Registrering lyckades!", this.getApplicationContext(), Toast.LENGTH_SHORT);
        finish();
    }

    /**
     * Checks if all the input data from the user in the registration process is valid
     * @return whether all input in the registration process is valid
     */
    private boolean checkData(){
        if(name.isEmpty()){
            Toaster.displayToast("Ange visningsnamn", this.getApplicationContext(), Toast.LENGTH_SHORT);
            return false;
        } else if(mail.isEmpty()|| !validateEmail()){
            Toaster.displayToast("Ange email", this.getApplicationContext(), Toast.LENGTH_SHORT);
            return false;
        } else if(password.isEmpty()){
            Toaster.displayToast("Ange lösenord", this.getApplicationContext(), Toast.LENGTH_SHORT);
            return false;
        } else if(confPassword.isEmpty()){
            Toaster.displayToast("Bekräfta lösenord", this.getApplicationContext(), Toast.LENGTH_SHORT);
            return false;
        } else if(!password.equals(confPassword)){
            Toaster.displayToast("Lösenord matchar ej", this.getApplicationContext(), Toast.LENGTH_SHORT);
            return false;
        } else if(genderSelection.isEmpty()){
            Toaster.displayToast("Inget kön valt", this.getApplicationContext(), Toast.LENGTH_SHORT);
            return false;
        }
        if(birthMonth.equals("4")||birthMonth.equals("6")||birthMonth.equals("9")||birthMonth.equals("11")) {
            if (Integer.parseInt(birthDay) > 30) {
                Toaster.displayToast("Datumet existerar ej", this.getApplicationContext(), Toast.LENGTH_SHORT);
                return false;
            }
        }
        //Taking leap years into consideration.
        if((Integer.parseInt(birthYear) % 4 == 0) &&
                ((Integer.parseInt(birthYear) % 100 != 0) ||
                (Integer.parseInt(birthYear) % 400 == 0))) {
            if (birthMonth.equals("2") && Integer.parseInt(birthDay) > 29) {
                Toaster.displayToast("Datumet existerar ej", this.getApplicationContext(), Toast.LENGTH_SHORT);
                return false;
            }

        } else if(birthMonth.equals("2")&&Integer.parseInt(birthDay)>28){
            Toaster.displayToast("Datumet existerar ej", this.getApplicationContext(),Toast.LENGTH_SHORT);
            return false;
        }

        return true;
    }

    /**
     * checks if the entered email address is valid
     * @return whether the email is valid or not
     */
    private boolean validateEmail(){
        //TODO: Better validation of that string would be neat
        for(int i = 0; i<mail.length(); i++){
            if(mail.charAt(i) == '@') {
                return true;
            }
        }
        return false;
    }

    /**
     * initiates the spinners for choosing gender and date of birth
     * and saves the users choices
     */
    private void initiateAllSpinners(){
        Spinner genderSpinner;
        ArrayAdapter<CharSequence> genderAdapter;

        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        genderAdapter = ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    genderSelection = (String) parent.getItemAtPosition(position);

                } else {
                    Toaster.displayToast("Var god gör ett val", getApplicationContext(), Toast.LENGTH_SHORT);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toaster.displayToast("Var god gör ett val", getApplicationContext(), Toast.LENGTH_SHORT);
            }
        });

        Spinner yearSpinner;
        ArrayAdapter<CharSequence> yearAdapter;

        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        yearAdapter = ArrayAdapter.createFromResource(this, R.array.yearOfBirth, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                birthYear = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toaster.displayToast("Var god gör ett val", getApplicationContext(), Toast.LENGTH_SHORT);
            }
        });

        Spinner monthSpinner;
        ArrayAdapter<CharSequence> monthAdapter;

        monthSpinner = (Spinner) findViewById(R.id.monthSpinner);
        monthAdapter = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                birthMonth = Integer.toString(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toaster.displayToast("Var god gör ett val", getApplicationContext(), Toast.LENGTH_SHORT);
            }
        });

        Spinner dateSpinner;
        ArrayAdapter<CharSequence> dateAdapter;

        dateSpinner = (Spinner) findViewById(R.id.dateSpinner);
        dateAdapter = ArrayAdapter.createFromResource(this, R.array.date, android.R.layout.simple_spinner_item);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateAdapter);
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                birthDay = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toaster.displayToast("Var god gör ett val", getApplicationContext(), Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initiateAllSpinners();

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
