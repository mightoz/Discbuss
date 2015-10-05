package teamosqar.discbuss.application;

import com.firebase.client.Firebase;

import java.util.Observable;

import teamosqar.discbuss.model.Model;

/**
 * Created by rutanjr on 2015-10-05.
 */
public class ProfileController extends Observable {

    private Firebase userRef;

    public ProfileController(){
        userRef = Model.getInstance().getMRef().child("users");
    }


}
