package teamosqar.discbuss.application;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Oscar on 16/10/15.
 */
public class MainController {

    private Context context;
    private Model model;
    private WifiInfo wifiInfo;

    public MainController(Context context){
        this.context = context;
        model = Model.getInstance();

    }

    public boolean checkWifiState(){
        try {
            WifiManager mWifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            wifiInfo=mWifiManager.getConnectionInfo();
            if (!mWifiManager.isWifiEnabled() || wifiInfo.getSSID() == null || wifiInfo.getBSSID() == null) {
                return false;
            }
            return true;
        }
        catch (  Exception e) {
            return false;
        }
    }
}
