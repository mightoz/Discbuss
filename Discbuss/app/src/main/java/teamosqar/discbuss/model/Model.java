package teamosqar.discbuss.model;

import com.firebase.client.Firebase;

/**
 * Created by Oscar on 2015-09-30.
 */
public class Model{
    private static Model model = new Model();
    private Firebase mref;
    private String username;
    private String email;

    private Model(){
        mref = new Firebase("https://boiling-heat-3778.firebaseio.com");
        username = "jag";
    }

    public static Model getInstance(){
        return model;
    }

    public Firebase getMref(){
        return mref;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getUsername(){
        return username;
    }
}