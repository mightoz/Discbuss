package teamosqar.discbuss.net;


/**
 * Created by Oscar on 14/10/15.
 */
public class BusFactory {

    private BusFactory(){

    }

    /**
     * @param bssid BSSID for the network
     * @return Returns the dgw and VIN as a coherent string, formatted to be insertable into an url.
     */
    public static String getDgwVin(String bssid){
        String dgwVin ="";
        switch(bssid){
            case "04:f0:21:10:0a:07":
                dgwVin = "?dgw=Ericsson$100020";
                break;
            case "04:f0:21:10:09:df":
                dgwVin = "?dgw=Ericsson$100021";
                break;
            case "04:f0:21:10:09:e8":
                dgwVin = "?dgw=Ericsson$100022";
                break;
            case "04:f0:21:10:09:b8":
                dgwVin = "?dgw=Ericsson$171164";
                break;
            case "04:f0:21:10:09:e7":
                dgwVin = "?dgw=Ericsson$171234";
                break;
            case "04:f0:21:10:09:5b":
                dgwVin = "?dgw=Ericsson$171235";
                break;
            case "04:f0:21:10:09:53":
                dgwVin = "?dgw=Ericsson$171327";
                break;
            case "04:f0:21:10:09:b9":
                dgwVin = "?dgw=Ericsson$171328";
                break;
            case "9":
                dgwVin = "?dgw=Ericsson$171329";
                break;
            case "04:f0:21:10:09:b7":
                dgwVin = "?dgw=Ericsson$171330";
                break;
            case "testBus":
                dgwVin = "?dgw=Ericsson$Vin_Num_001";
        }
        if(dgwVin.equals(""))throw new IllegalArgumentException("Invalid BSSID");
        return dgwVin;
    }
}
