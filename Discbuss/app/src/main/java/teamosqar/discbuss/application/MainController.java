package teamosqar.discbuss.application;


import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import teamosqar.discbuss.activities.ChatActivity;
import teamosqar.discbuss.util.Toaster;

/**
 * Created by Oscar on 16/10/15.
 */
public class MainController {

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

        //Testcode, to be removed when finished
        model.setCurrentBSSID("testId");
        idIndex = model.getIndexOfBSSID();
        connectedToBusWifi = true;

        //This code should be used when not testing, i.e. real connection to buses.
        /*try {
            WifiManager mWifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            wifiInfo=mWifiManager.getConnectionInfo();
            if (!mWifiManager.isWifiEnabled() || wifiInfo.getSSID() == null || wifiInfo.getBSSID() == null) {
                model.setCurrentBSSID(wifiInfo.getBSSID());
                idIndex = model.getIndexOfBSSID();
                if(idIndex != -1){
                    connectedToBusWifi = true;
                }else{
                    connectedToBusWifi = false;
                }
            }else{
                connectedToBusWifi = false;
            }
        }
        catch (  Exception e) {

        }*/
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
   /* public void tryEnterChat(Class<ChatActivity> chatActivityClass){
        if(connectedToBusWifi){
            String chatRoom = "chatRooms/";
            chatRoom = chatRoom + idIndex;
            Intent intent = new Intent(context, chatActivityClass);
            intent.putExtra("EXTRA_ROOM", chatRoom);
            context.startActivity(intent);
        }else{
            Toaster.displayToast("Connect to buss WiFi", , Toast.LENGTH_SHORT);
        }
    */

}
