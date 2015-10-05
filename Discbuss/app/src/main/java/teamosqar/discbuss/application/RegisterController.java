package teamosqar.discbuss.application;

import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import teamosqar.discbuss.model.Model;

/**
 * Created by Jakob on 02/10/15.
 */
public class RegisterController {
    private Firebase mref;
    private String fName;

    public RegisterController() {
        mref = Model.getInstance().getMRef().child("users");
    }
    public void registerUser(String name, String email, String password){
        fName = name;
        mref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                 mref.child((String)result.get("uid")).child("name").setValue(fName);
                mref.child((String)result.get("uid")).child("karma").setValue(0);
            }

            @Override
            public void onError(FirebaseError firebaseError) {

            }
        });
    }
}
