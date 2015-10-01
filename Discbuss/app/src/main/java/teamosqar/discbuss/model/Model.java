package teamosqar.discbuss.model;

import com.firebase.client.Firebase;

/**
 * Created by Oscar on 2015-09-30.
 */
public class Model{
    Firebase mref;

    public Model(){
        mref = new Firebase("https://boiling-heat-3778.firebaseio.com/users");
    }

    public Firebase getMRef(){
        return mref;
    }
}