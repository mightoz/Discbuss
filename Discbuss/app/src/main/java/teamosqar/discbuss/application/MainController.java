package teamosqar.discbuss.application;


import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

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

    public void resetModel(){
        model.resetModel();
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
        model.addObserver(this);
    }

    public void removeAsObserver(){
        model.deleteObserver(this);
    }

    @Override
    public void update(Observable observable, final Object nextBusStop) {
       updateNextBusStop();
    }

    public void updateNextBusStop() {
        if (model.getNextBusStop()!=null&& !model.getNextBusStop().isEmpty()) {
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textView = (TextView) ((Activity) context).findViewById(R.id.actionBarTextView);
                    textView.setText("Nästa hållplats: " + model.getNextBusStop());
                }
            });
        }
    }
}
