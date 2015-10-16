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
        connectedToBusWifi = false;
        this.context = context;
        model = Model.getInstance();

    }

    public void checkWifiState(){
        try {
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

        }
    }

    public boolean isConnectedToBus() {
        return isConnectedToBus();
    }

    public int getIndexOfId(){
        return idIndex;
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
