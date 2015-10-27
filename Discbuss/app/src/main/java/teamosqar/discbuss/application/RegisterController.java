package teamosqar.discbuss.application;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * Created by Jakob on 02/10/15.
 */
public class RegisterController {
    private Firebase mref;
    private String fName, fGender, fYear, fMonth, fDay;
    public RegisterController() {
        mref = Model.getInstance().getMRef().child("users");
    }

    /**
     * Registers the user in FireBase
     * @param name the users registered username
     * @param email the users registered email
     * @param password the users registered password
     * @param gender the users registered gender
     * @param year the users registered birth year
     * @param month the users registered birth month
     * @param day the users registered birth day
     */
    public void registerUser(String name, String email, String password, String gender, String year, String month, String day){
        fName = name;
        fGender = gender;
        fYear = year;
        fMonth = month;
        fDay = day;
        mref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                mref.child((String)result.get("uid")).child("name").setValue(fName);
                mref.child((String)result.get("uid")).child("karma").setValue(0);
                mref.child((String)result.get("uid")).child("gender").setValue(fGender);
                mref.child((String)result.get("uid")).child("year").setValue(fYear);
                mref.child((String)result.get("uid")).child("month").setValue(fMonth);
                mref.child((String)result.get("uid")).child("day").setValue(fDay);
            }

            @Override
            public void onError(FirebaseError firebaseError) {

            }
        });
    }
}
