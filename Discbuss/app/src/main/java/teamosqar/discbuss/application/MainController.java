package teamosqar.discbuss.application;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.activities.R;


/**
 * Created by Oscar on 16/10/15.
 */
public class MainController implements ActionBarController{

    private Context context;
    private Model model;
    private WifiInfo wifiInfo;
    private boolean connectedToBusWifi;
    private int idIndex;

    public MainController(Context context){
        this.context = context;
        model = Model.getInstance();
        connectedToBusWifi = false;

    }

    /**
     * Checks if unit is connected to a bus wifi. Sets idIndex to -1 if not, otherwise the index of
     * the bus.
     */
    public void checkWifiState(){

        boolean testing = true; //TODO: Set to true if you want to test the chat room. Will then connect to a chat room for a simulated bus trip.

        if(testing){
            model.setCurrentBSSID("testBus");
            idIndex = model.getIndexOfBSSID();
            connectedToBusWifi = true;
            model.startRetrievingStopInfo();
        }else{
            try {
                WifiManager mWifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                wifiInfo=mWifiManager.getConnectionInfo();
                if (mWifiManager.isWifiEnabled() || wifiInfo.getSSID() != null || wifiInfo.getBSSID() != null) {
                    model.setCurrentBSSID(wifiInfo.getBSSID());
                    idIndex = model.getIndexOfBSSID();
                    connectedToBusWifi = model.connectedToBusWifi();
                    if(connectedToBusWifi)
                        model.startRetrievingStopInfo();
                }else{
                    idIndex = -1;
                    connectedToBusWifi = false;
                }
            }
            catch (  Exception e) {

            }

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

    public void updateNextBusStop() {
       /* if (model.getNextBusStop()!=null&& !model.getNextBusStop().isEmpty()) {
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textView = (TextView) ((Activity) context).findViewById(R.id.actionBarTextView);
                    textView.setText("Nästa hållplats: " + model.getNextBusStop());
                }
            });
        }*/
    }


    @Override
    public void updateContext(Context context) {
        Model.getInstance().updateContext(context);
    }
}
