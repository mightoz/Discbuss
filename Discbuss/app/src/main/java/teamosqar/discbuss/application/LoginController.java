package teamosqar.discbuss.application;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import teamosqar.discbuss.model.Model;

/**
 * Created by joakim on 2015-10-02.
 */
public class LoginController {

    private Firebase userRef;

    public LoginController(){
        userRef = Model.getInstance().getMref().child("users");
    }

    public boolean tryLogin(String email, String password){


        userRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {

            }

        });


    }
}
