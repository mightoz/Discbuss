package teamosqar.discbuss.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Oscar on 14/10/15.
 */
public class ElecApi {

    private String bssid;
    private String baseUrl;
    private String key;

    public ElecApi(String bssid){
        this.bssid = bssid;
        baseUrl = "https://ece01.ericsson.net:4443/ecity";
        key = "Z3JwNTg6c3VHLXBVWC1iNA==";

    }

    public String getNextBusStop()throws IOException {

        long t2 = System.currentTimeMillis();
        long t1 = t2 - (1000 * 10);
        String dgwVin = BusFactory.getDgwVin(bssid);

        StringBuffer response = new StringBuffer();

        URL requestURL = new URL(baseUrl+dgwVin+"&sensorSpec=Ericsson$Next_Stop&t1="
                + t1 + "&t2=" + t2);
        HttpsURLConnection con = (HttpsURLConnection) requestURL
                .openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Basic "+key);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + baseUrl);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
        //TODO: Format response.toString() to get next dest.
        return "s";
    }
}
