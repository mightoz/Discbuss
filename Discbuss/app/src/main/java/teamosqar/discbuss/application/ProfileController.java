package teamosqar.discbuss.application;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Observable;

import teamosqar.discbuss.model.Model;

/**
 * Created by rutanjr on 2015-10-05.
 */
public class ProfileController extends Observable {

    private Firebase userRef;
    private DataSnapshot snapshot;

    public ProfileController(){
        userRef = Model.getInstance().getMRef().child("users").child(Model.getInstance().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

    public String getName() {
        String name = "";
        if(snapshot != null) {
            name = snapshot.child("name").getValue(String.class);
        }
        return name;
    }

}
