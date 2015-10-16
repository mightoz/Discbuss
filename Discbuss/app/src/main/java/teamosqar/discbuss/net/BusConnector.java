package teamosqar.discbuss.net;

import java.io.IOException;

/**
 * Created by Oscar on 14/10/15.
 */
public class BusConnector {
    private ElecApi eAPI;
    private VastApi vAPI;

    public BusConnector(String bssid){
        eAPI = new ElecApi(bssid);
        vAPI = new VastApi();
    }
    public String getNextStop() throws IOException {
        return eAPI.getNextBusStop();
    }
}