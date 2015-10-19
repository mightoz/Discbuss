package teamosqar.discbuss.application;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.activities.R;
import teamosqar.discbuss.util.Message;
import teamosqar.discbuss.util.Toaster;

/**
 * Created by rutanjr on 2015-10-05.
 *
 * A controller class for the ProfileActivity class. Uses a firebase login to find the data it needs
 * and that is fetched from the model.
 */
public class ProfileController extends Observable implements Observer {

    private Firebase fireRef;
    private ArrayList<Message> topMessages;
    private Firebase userRef; //firebase reference to the user that is currently logged in.
    private DataSnapshot snapshot; //reference to the data contained in this user.
    private String nextStop;
    Context context;
    private TextView actionBarText, nameText, emailText, pwText;
    private Model model = Model.getInstance();

    public ProfileController(Context context) {
        this.context = context;
        model = Model.getInstance();
        model.addObserverToList(this);
        fireRef = model.getMRef();
        userRef = model.getMRef().child("users").child(Model.getInstance().getUid());

    }

    public ProfileController(){
        fireRef = Model.getInstance().getMRef();
        userRef = Model.getInstance().getMRef().child("users").child(Model.getInstance().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                snapshot = dataSnapshot;
                topMessages = new ArrayList<>();
                Message tempMsg;
                Iterator iterator = snapshot.child("topStatements").getChildren().iterator();
                while(iterator.hasNext()){
                    snapshot = (DataSnapshot)iterator.next();
                    tempMsg = snapshot.getValue(Message.class);
                    topMessages.add(tempMsg);
                }
                setChanged();
                notifyObservers();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        nextStop = "";
    }

    public ProfileController(String uid){
        fireRef = Model.getInstance().getMRef();
        userRef = fireRef.child("users").child(uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                snapshot = dataSnapshot;
                topMessages = new ArrayList<>();
                Message tempMsg;
                Iterator iterator = snapshot.child("topStatements").getChildren().iterator();
                while(iterator.hasNext()){
                    snapshot = (DataSnapshot)iterator.next();
                    tempMsg = snapshot.getValue(Message.class);
                    topMessages.add(tempMsg);
                }
                setChanged();
                notifyObservers();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public String getNextStop() {
        return nextStop;
    }

    public ArrayList<Message> getTopMessages(){
        return topMessages;
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


    @Override
    public void update(Observable observable, Object nextBusStop) {
        nextStop = (String) nextBusStop;
        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView) ((Activity) context).findViewById(R.id.actionBarTextView);
                textView.setText("Nästa: " + nextStop);
            }
        });
    }

    public void resetModel(){
        Model.getInstance().resetModel();

    }
}
