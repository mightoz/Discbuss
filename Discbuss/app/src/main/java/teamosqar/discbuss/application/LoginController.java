package teamosqar.discbuss.application;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Observable;

/**
 * Created by joakim on 2015-10-02.
 * <p/>
 * Controller class for the login view
 */
public class LoginController extends Observable {

    private Firebase userRef;
    private boolean loginStatus;

    public LoginController() {
        userRef = Model.getInstance().getMRef().child("users");
        loginStatus = false;
    }

    /**
     * checks if the user input checks out with any registered user in FireBase
     *
     * @param email    the email input
     * @param password the password input
     */
    public void tryLogin(final String email, String password) {
        Log.d(email, password);
        userRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.d("email", "success");
                loginStatus = true;
                Model.getInstance().setUid(authData.getUid());
                Model.getInstance().setEmail(email);
                userRef.child(authData.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Model.getInstance().setUsername(dataSnapshot.child("name").getValue().toString());
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
                setChanged();
                notifyObservers();

            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                loginStatus = false;
                setChanged();
                notifyObservers();
            }

        });
    }

    /**
     * Gets the loginStatus of the user
     *
     * @return the users loginStatus as a boolean
     */
    public boolean getLoginStatus() {
        return loginStatus;
    }
}
