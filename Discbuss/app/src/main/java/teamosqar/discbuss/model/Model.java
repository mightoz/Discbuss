package teamosqar.discbuss.model;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oscar on 2015-09-30.
 */
public class Model{
    private static Model model = new Model();
    private Firebase mref;
    private String username;
    private String email;
    private String uid;
    private List<String> busBSSIDs;

    private final String buss1 = "04:f0:21:10:09:df";
    private final String buss2 = "04:f0:21:10:09:b9";
    private final String buss3 = "04:f0:21:10:09:e8";
    private final String buss4 = "04:f0:21:10:09:b7";
    private final String buss5 = "04:f0:21:10:09:53";
    private final String buss6 = "04:f0:21:10:09:5b";
    private final String buss7 = "04:f0:21:10:09:b8";
    private final String buss8 = "04:f0:21:10:09:b9";
    private final String buss9 = "n/a";
    private final String buss10 = "04:f0:21:10:09:b7";

    private Model(){
        mref = new Firebase("https://boiling-heat-3778.firebaseio.com");
        username="";
        busBSSIDs = new ArrayList<>(10);
        loadBusIds();
    }

    private void loadBusIds(){
        busBSSIDs.add(buss1);
        busBSSIDs.add(buss2);
        busBSSIDs.add(buss3);
        busBSSIDs.add(buss4);
        busBSSIDs.add(buss5);
        busBSSIDs.add(buss6);
        busBSSIDs.add(buss7);
        busBSSIDs.add(buss8);
        busBSSIDs.add(buss9);
        busBSSIDs.add(buss10);

    }

    public List<String> getBusBSSIDs(){
        List<String> buses = busBSSIDs;
        return buses;
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
        //mref.child("chatRooms").child(activeChat).setValue(uid); //TODO: Use when finished
        mref.child("activeUsers").child("chat").child(uid).setValue(uid);//TODO: Remove when finished
    }
    public void removeUserFromChat(String activeChat){
        //mref.child("chatRooms").child(activeChat).child(uid).removeValue();//TODO: Use when finished
        mref.child("activeUsers").child("chat").child(uid).removeValue();//TODO: Remove when finished
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