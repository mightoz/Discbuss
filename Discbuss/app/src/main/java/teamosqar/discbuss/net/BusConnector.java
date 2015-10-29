package teamosqar.discbuss.net;

import org.json.JSONException;

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

    /**
     *
     * @return the upcoming bus stop as a string
     * @throws IOException
     * @throws JSONException
     */
    public String getNextStop() throws IOException, JSONException {
        return eAPI.getNextBusStop();
    }
}
