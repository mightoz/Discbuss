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
            case "1":
                dgwVin = "?dgw=Ericsson$100020$Vin_Num_YV3U0V222FA100020";
                break;
            case "2":
                dgwVin = "?dgw=Ericsson$100021$Vin_Num_YV3U0V222FA100021";
                break;
            case "3":
                dgwVin = "?dgw=Ericsson$100022$Vin_Num_YV3U0V222FA100022";
                break;
            case "4":
                dgwVin = "?dgw=Ericsson$171164$Vin_Num_YV3T1U22XF1171164";
                break;
            case "5":
                dgwVin = "?dgw=Ericsson$171234$Vin_Num_YV3T1U225F1171234";
                break;
            case "6":
                dgwVin = "?dgw=Ericsson$171235$Vin_Num_YV3T1U227F1171235";
                break;
            case "7":
                dgwVin = "?dgw=Ericsson$171327$Vin_Num_YV3T1U221F1171327";
                break;
            case "8":
                dgwVin = "?dgw=Ericsson$171328$Vin_Num_YV3T1U223F1171328";
                break;
            case "9":
                dgwVin = "?dgw=Ericsson$171329$Vin_Num_YV3T1U225F1171329";
                break;
            case "10":
                dgwVin = "?dgw=Ericsson$171330$Vin_Num_YV3T1U223F1171330";
                break;
        }
        if(dgwVin.equals(""))throw new IllegalArgumentException("Invalid BSSID");
        return dgwVin;
    }
}
