package teamosqar.discbuss.application;

import com.firebase.client.Firebase;

import java.util.ArrayList;
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

    private Model(){
        mref = new Firebase("https://boiling-heat-3778.firebaseio.com");
        username="";
        busBSSIDs = new ArrayList<>();
        loadBusIds();
    }

    /**
     * Inserts all busses BSSIDs into a list
     */
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

    /**
     * @return true if the user is currently connected to one of the busses, matched by BSSID
     */
    protected boolean connectedToBusWifi(){
        if(busBSSIDs.contains(currentBSSID))
                return true;
        return false;
    }

    /**
     * Sets the current BSSID according to the param
     * @param bssid
     */
    protected void setCurrentBSSID(String bssid){
        this.currentBSSID = bssid;
    }

    /**
     * Matches the bus BSSID with the bus index and returns the bus-index of the bus connected to
     * @return the bus index in the list
     */
    protected int getIndexOfBSSID(){
        return busBSSIDs.indexOf(currentBSSID);
    }

    /**
     * @return a reference to the model
     */

    protected static Model getInstance(){
        return model;
    }

    /**
     *
     * @return a reference to our firebase
     */
    protected Firebase getMRef(){
        return mref;
    }

    /**
     * Sets the active user's UID according to param
     * @param uid
     */
    protected void setUid(String uid){
        this.uid = uid;
    }

    /**
     * @return the active user's UID
     */
    protected String getUid(){
        return uid;
    }

    /**
     * Adds the active user to the chat provided as param
     * @param activeChat
     */
    protected void addUserToChat(String activeChat){
        mref.child("activeUsers").child(activeChat).child(uid).setValue(uid);
    }

    /**
     * Removes the active user from the chat provided as param
     * @param activeChat
     */
    protected void removeUserFromChat(String activeChat){
        mref.child("activeUsers").child(activeChat).child(uid).removeValue();
    }

    /**
     * Set the username of the active user based on the string provided as param
     * @param username
     */
    protected void setUsername(String username){
        this.username = username;
    }

    /**
     * Sets the email of the active user based on the string provided as param
     * @param email
     */
    protected void setEmail(String email){
        this.email = email;
    }

    /**
     * @return the email of the active user
     */
    protected String getEmail(){
        return email;
    }

    /**
     * @return the username of the active user
     */
    protected String getUsername(){
        return username;
    }

    /**
     * resets the values of username, userID and email
     */
    protected void resetModel(){
        username = "";
        uid = "";
        email = "";
    }

    /**
     * Starts timer that retrieves bus data for currently connected bus.
     */
    protected void startRetrievingStopInfo(){
        stopUpdater = new StopUpdater(currentBSSID);
        stopUpdater.addObserver(this);
        stopUpdater.start();
    }


    /**
     * Retrieves the next bus stop info and formats it into a presentable string. Then updates
     * listeners.
     * @param observable not used
     * @param data not used
     */
    @Override
    public void update(Observable observable, Object data) {
        String busStopTmp = stopUpdater.getNextBusStop();
        if(busStopTmp != null && busStopTmp.length()>1){
            busStopTmp = busStopTmp.substring(0,busStopTmp.length()-1);
        }

        switch (busStopTmp){
            case "G�taplatse":
                nextBusStop = "Götaplatsen";
                break;
            case "Kungsportsplats":
                nextBusStop = "Kungsportsplatsen";
                break;
            case "NisseTerminale":
                nextBusStop = "Nils Ericson Terminalen";
                break;
            case "Frihamne":
                nextBusStop = "Frihamnen";
                break;
            case "Kungsportspl":
                nextBusStop = "Kungsportsplatsen";
                break;
            default:
                nextBusStop = busStopTmp;
                break;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * @return upcoming bus stop
     */
    protected String getNextBusStop(){
        return nextBusStop;
    }
}