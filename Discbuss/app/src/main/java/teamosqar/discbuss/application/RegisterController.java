package teamosqar.discbuss.application;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import teamosqar.discbuss.model.Model;

/**
 * Created by Jakob on 02/10/15.
 */
public class RegisterController {
    private Firebase mref;
    private boolean regStatus;
    private String fName;

    public RegisterController() {
        mref = Model.getInstance().getMref();
        mref = mref.child("users");
    }

    public void registerUser(String name, String email, String password, String confPassword){
        fName = name;
        mref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                mref.child((String)result.get("uid")).child("name").setValue(fName);
                mref.child((String)result.get("uid")).child("karma").setValue(0);
                regStatus = true;
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }
}
