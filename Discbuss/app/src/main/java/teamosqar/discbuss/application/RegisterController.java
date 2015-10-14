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
    private String fGender;
    private String fAge;

    public RegisterController() {
        mref = Model.getInstance().getMRef().child("users");
    }
    public void registerUser(String name, String email, String password, String gender, String age){
        fName = name;
        fGender = gender;
        fAge = age;
        mref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                mref.child((String)result.get("uid")).child("name").setValue(fName);
                mref.child((String)result.get("uid")).child("karma").setValue(0);
                mref.child((String)result.get("uid")).child("gender").setValue(fGender);
                mref.child((String)result.get("uid")).child("age").setValue(fAge);
            }

            @Override
            public void onError(FirebaseError firebaseError) {

            }
        });
    }
}
