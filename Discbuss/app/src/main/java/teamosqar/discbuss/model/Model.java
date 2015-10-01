package teamosqar.discbuss.model;

import com.firebase.client.Firebase;

/**
 * Created by Oscar on 2015-09-30.
 */
public class Model{
    private static Model model = new Model();
    private Firebase mref;
    private String username;

    private Model(){
        mref = new Firebase("https://boiling-heat-3778.firebaseio.com/users");
        username = "jag";
    }

    public static Model getInstance(){
        return model;
    }

    public Firebase getMref(){
        return mref;
    }

    public String getUsername(){
        return username;
    }
}