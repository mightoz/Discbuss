package teamosqar.discbuss.application;

import android.content.Context;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Observable;

import teamosqar.discbuss.util.Toaster;

/**
 * Created by rutanjr on 2015-10-05.
 *
 * A controller class for the ProfileActivity class. Uses a firebase login to find the data it needs
 * and that is fetched from the model.
 */
public class ProfileController extends Observable {

    private Firebase fireRef;
    private Firebase userRef; //firebase reference to the user that is currently logged in.
    private DataSnapshot snapshot; //reference to the data contained in this user.
    private Model model = Model.getInstance();

    public ProfileController(){
        fireRef = Model.getInstance().getMRef();
        userRef = Model.getInstance().getMRef().child("users").child(Model.getInstance().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                snapshot = dataSnapshot;
                setChanged();
                notifyObservers();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

     /**
     * Gets the name from the snapshot data and returns it as a string.
     * @return the displayname for the user that is currently logged in.
     */
    public String getName() {
        String name = "";
        if(snapshot != null) {
            name = snapshot.child("name").getValue(String.class);
        }
        return name;
    }

    public String getGender(){
        String gender = "";
        if(snapshot != null) {
            gender = snapshot.child("gender").getValue(String.class);
        }
        return gender;
    }

    public String getAge(){
        String age = "";
        if(snapshot != null) {
            age = snapshot.child("age").getValue(String.class);
        }
        return age;
    }

    /**
     * gets the emailadress from the authentication data and returns it as a string.
     * @return the authentication emailadress for the account currently logged in.
     */
    public String getEmail() {
        String email;
        AuthData authData = userRef.getAuth();
        email = (String)authData.getProviderData().get("email");
        return email;
    }

    /**
     * Gets the karma from the snapshot data and returns it as a string.
     * @return the karma for the user that is currently logged in.
     */
    public String getKarma() {
        String karma = "";
        int karmaVal;
        if(snapshot != null) {
            karmaVal = snapshot.child("karma").getValue(Integer.class);
            karma = Integer.toString(karmaVal);
        }
        return karma;
    }

    /**
     * Changes the "display" name for the user.
     * @param newName, the new name the user wants to use as display name
     */
    public void setNewDisplayName(String newName){
        userRef.child("name").setValue(newName);
        Model.getInstance().setUsername(newName);
        System.out.println(fireRef.getAuth().getProviderData().get("email").toString());
    }


    public void changePassword(String oldPw, String newPw, final Context context) {
        fireRef.changePassword(fireRef.getAuth().getProviderData().get("email").toString(), oldPw, newPw, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                Toaster.displayToast("Password successfully changed", context, Toast.LENGTH_LONG);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Toaster.displayToast("Password change failed", context, Toast.LENGTH_LONG);
            }
        });

    }
    public void resetModel(){
        Model.getInstance().resetModel();
    }
}
