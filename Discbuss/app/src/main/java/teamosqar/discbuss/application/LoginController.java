package teamosqar.discbuss.application;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Observable;

import teamosqar.discbuss.model.Model;

/**
 * Created by joakim on 2015-10-02.
 */
public class LoginController extends Observable{

    private Firebase userRef;
    private boolean loginStatus;
    public LoginController(){
        userRef = Model.getInstance().getMref().child("users");
    }

    public void tryLogin(final String email, String password){

        userRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                loginStatus = true;
                Model.getInstance().setUsername(authData.getUid());
                Model.getInstance().setEmail(email);
                notifyObservers();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                loginStatus = false;
                notifyObservers();
            }

        });
    }

    public boolean getLoginStatus(){
        return loginStatus;
    }
}
