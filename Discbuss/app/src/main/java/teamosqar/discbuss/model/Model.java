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
    private String uid;

    private Model(){
        mref = new Firebase("https://boiling-heat-3778.firebaseio.com");
        username = "jag";
    }

    public static Model getInstance(){
        return model;
    }

    public Firebase getMRef(){
        return mref;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

    public String getUid(){
        return uid;
    }
    public void addUserToChat(String activeChat){
        //mref.child("chatRooms").child(activeChat).setValue(uid);
        mref.child("testChat").child("activeUsers").child(uid).setValue(uid);
    }
    public void removeUserFromChat(String activeChat){
        //mref.child("chatRooms").child(activeChat).child(uid).removeValue();
        mref.child("testChat").child("activeUsers").child(uid).removeValue();
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public String getUsername(){
        return username;
    }
}