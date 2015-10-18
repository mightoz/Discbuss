package teamosqar.discbuss.application;


import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.activities.MainActivity;
import teamosqar.discbuss.activities.R;


/**
 * Created by Oscar on 16/10/15.
 */
public class MainController implements Observer {

    private Context context;
    private Model model;
    private WifiInfo wifiInfo;
    private boolean connectedToBusWifi;
    private int idIndex;

    public MainController(Context context){
        //connectedToBusWifi = false;
        this.context = context;
        model = Model.getInstance();

    }

    /**
     * Checks if unit is connected to a bus wifi. Sets idIndex to -1 if not, otherwise the index of
     * the bus.
     */
    public void checkWifiState(){


        //TODO: If not connected to a bus, connects to a simulated bus. Replace code with comments to only enable app when connected to a real bus.
        try {
            WifiManager mWifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            wifiInfo=mWifiManager.getConnectionInfo();
            if (!mWifiManager.isWifiEnabled() || wifiInfo.getSSID() == null || wifiInfo.getBSSID() == null) {
                if(model.getBusBSSIDs().contains(wifiInfo.getBSSID())){
                    model.setCurrentBSSID(wifiInfo.getBSSID());
                    idIndex = model.getIndexOfBSSID();
                    connectedToBusWifi = true;
                    model.startRetrievingStopInfo();
                }else{
                    model.setCurrentBSSID("testBus");
                    idIndex = model.getIndexOfBSSID();
                    connectedToBusWifi = true;
                    model.startRetrievingStopInfo();
                }
                /*model.setCurrentBSSID(wifiInfo.getBSSID());
                idIndex = model.getIndexOfBSSID();
                if(idIndex != -1){
                    connectedToBusWifi = true;
                    model.startRetrievingStopInfo();
                }else{
                    connectedToBusWifi = false;
                }*/
            }else{
                model.setCurrentBSSID("testBus");
                idIndex = model.getIndexOfBSSID();
                connectedToBusWifi = true;
                model.startRetrievingStopInfo();
                //connectedToBusWifi = false;
            }
            addAsObserver();
        }
        catch (  Exception e) {

        }
    }

    /**
     *
     * @return True if connected to a bus.
     */
    public boolean isConnectedToBus() {
        return connectedToBusWifi;
    }

    /**
     *
     * @return Which bus currently connected to, -1 if none.
     */
    public int getIndexOfId(){
        return idIndex;
    }

    /**
     * Submits statement to DB.
     * @param statement to be submitted.
     */
    public void submitStatement(String statement){
        model.getMRef().child("statements").push().setValue(statement);
    }

    public void addAsObserver(){
        model.addObserverToList(this);
    }

    @Override
    public void update(Observable observable, Object nextBusStop) {
        //TODO: Draw next bus stop here.
        View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        TextView v = (TextView)rootView.findViewById(R.id.actionBarTextView);
        try{
            v.setText("Setting new text");
            System.out.println("MainController: " + nextBusStop);
        } catch (NullPointerException e) {
            System.out.println("MainController: " + e.getCause() + " " + e.getMessage());
        }
    }
}
