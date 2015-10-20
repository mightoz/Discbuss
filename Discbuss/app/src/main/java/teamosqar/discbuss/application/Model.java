package teamosqar.discbuss.application;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.net.StopUpdater;

/**
 * Created by Oscar on 2015-09-30.
 */
public class Model extends Observable implements Observer{
    private static Model model = new Model();
    private Firebase mref;
    private String username;
    private String email;
    private String uid;
    private StopUpdater stopUpdater;
    private String currentBSSID;
    private ArrayList<String> busBSSIDs;
    private String nextBusStop;

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
    private final String testBus = "testBus";


    //KapellplatsenE
    //GötaplatsenA
    //ValandC
    //KungsportsplC
    //BrunnsparkenB
    //Lilla BommenB
    //FrihamnsportenB
    //Pumpgatan
    //RegnbågsgatanD
    //LindholmenD
    //TeknikgatanA
    //Lindholmsplatsen

    //LindholmsplatsenA
    //RegnbågsgatanB
    //PumpgatanA
    //FrihamnsportenA
    //Lilla Bommen
    //BrunnsparkenA
    //KungsportsplD
    //Valand
    //Götaplatsen
    //ÅlandsgatanB
    //Chalmers tvärgata
    //Sven Hultins plats
    //Chalmersplatsen
    //Kapellplatsen

    private Model(){
        mref = new Firebase("https://boiling-heat-3778.firebaseio.com");
        username="";
        busBSSIDs = new ArrayList<>();
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
        busBSSIDs.add(testBus);

    }

    protected List<String> getBusBSSIDs(){
        return busBSSIDs;
    }

    protected void setCurrentBSSID(String bssid){
        this.currentBSSID = bssid;
    }

    protected int getIndexOfBSSID(){
        return busBSSIDs.indexOf(currentBSSID);
    }

    protected static Model getInstance(){
        return model;
    }

    protected Firebase getMRef(){
        return mref;
    }

    protected void setUid(String uid){
        this.uid = uid;
    }

    protected String getUid(){
        return uid;
    }

    protected void addUserToChat(String activeChat){
        //mref.child("chatRooms").child(activeChat).setValue(uid); //TODO: Should this really be used?
        mref.child("activeUsers").child(activeChat).child(uid).setValue(uid);
    }

    protected void removeUserFromChat(String activeChat){
        //mref.child("chatRooms").child(activeChat).child(uid).removeValue();//TODO: Should this really be used?
        mref.child("activeUsers").child(activeChat).child(uid).removeValue();
    }

    protected void setUsername(String username){
        this.username = username;
    }

    protected void setEmail(String email){
        this.email = email;
    }

    protected String getEmail(){
        return email;
    }

    protected String getUsername(){
        return username;
    }

    protected void resetModel(){
        username = "";
        uid = "";
        email = "";
    }

    protected void startRetrievingStopInfo(){
        stopUpdater = new StopUpdater(currentBSSID);
        stopUpdater.addObserver(this);
        stopUpdater.start();
    }

    @Override
    public void update(Observable observable, Object data) {
        nextBusStop = stopUpdater.getNextBusStop();
        setChanged();
        notifyObservers();
    }

    protected String getNextBusStop(){
        return nextBusStop;
    }
}