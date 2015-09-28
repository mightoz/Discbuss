package teamosqar.discbuss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;

public class LoginActivity extends AppCompatActivity {
    private Firebase mref;
    private EditText editMail, editPass;
    private String email, password;
    protected void onStart(){
        super.onStart();
        Firebase.setAndroidContext(this);
        mref = new Firebase("https://boiling-heat-3778.firebaseio.com/users");
        editMail = (EditText) findViewById(R.id.editEmail);
        editPass = (EditText) findViewById(R.id.editPass);
    }
    public void loginPressed(View view){
        email = editMail.getText().toString();
        password = editPass.getText().toString();
        mref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                //mref.child(authData.getUid()).child("karma").setValue(1);
                incrementKarma(authData.getUid());
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
            }
        });

    }
    private void incrementKarma(String uid){
        mref = new Firebase("https://boiling-heat-3778.firebaseio.com/users" + "/" + uid + "/karma");
        mref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                if (currentData.getValue() == null) {
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                }
                return Transaction.success(currentData);
            }

            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                if (firebaseError != null) {
                    System.out.println("Firebase counter increment failed.");
                } else {
                    System.out.println("Firebase counter increment succeeded.");
                }
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
