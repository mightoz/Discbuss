package teamosqar.discbuss.net;


/**
 * Created by Oscar on 14/10/15.
 */
public class BusFactory {

    private BusFactory(){

    }

    /**
     *
     * @param bssid BSSID for the network
     * @return Returns the dgw and VIN as a coherent string, formatted to be insertable into an url.
     */
    //TODO: Replace numbers 1-10 with all BSSIDs for all buses matching the dgws and vins. See http://platform.goteborgelectricity.se/api/bussar for reference
    public static String getDgwVin(String bssid){
        String dgwVin ="";
        switch(bssid){
            case "04:f0:21:10:0a:07":
                dgwVin = "?dgw=Ericsson$100020";
                //$Vin_Num_YV3U0V222FA100020
                break;
            case "04:f0:21:10:09:df":
                dgwVin = "?dgw=Ericsson$100021";
                //$Vin_Num_YV3U0V222FA100021
                break;
            case "04:f0:21:10:09:e8":
                dgwVin = "?dgw=Ericsson$100022";
                //$Vin_Num_YV3U0V222FA100022
                break;
            case "04:f0:21:10:09:b8":
                dgwVin = "?dgw=Ericsson$171164";
                //$Vin_Num_YV3T1U22XF1171164
                break;
            case "04:f0:21:10:09:e7":
                dgwVin = "?dgw=Ericsson$171234";
                //$Vin_Num_YV3T1U225F1171234
                break;
            case "04:f0:21:10:09:5b":
                dgwVin = "?dgw=Ericsson$171235";
                //$Vin_Num_YV3T1U227F1171235
                break;
            case "04:f0:21:10:09:53":
                dgwVin = "?dgw=Ericsson$171327";
                //$Vin_Num_YV3T1U221F1171327
                break;
            case "04:f0:21:10:09:b9":
                dgwVin = "?dgw=Ericsson$171328";
                //$Vin_Num_YV3T1U223F1171328
                break;
            case "9":
                dgwVin = "?dgw=Ericsson$171329";
                //$Vin_Num_YV3T1U225F1171329
                break;
            case "04:f0:21:10:09:b7":
                dgwVin = "?dgw=Ericsson$171330";
                //$Vin_Num_YV3T1U223F1171330
                break;
            case "testBus":
                dgwVin = "?dgw=Ericsson$Vin_Num_001";
        }
        if(dgwVin.equals(""))throw new IllegalArgumentException("Invalid BSSID");
        return dgwVin;
    }
}
