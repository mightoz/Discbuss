package teamosqar.discbuss.net;

import org.json.JSONException;

import java.io.IOException;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Oscar on 14/10/15.
 */
public class StopUpdater extends Observable{

    private Timer timer;
    private boolean isRunning;
    private BusConnector connector;
    private String nextBusStop;

    public StopUpdater(String bssid) {
        connector = new BusConnector(bssid);
        timer = new Timer();
        nextBusStop = "";

    }

    public void start(){
        if(!isRunning){
            timer.schedule(new Updater(),0,10000);
            isRunning = true;
        }
    }

    public void stop(){
        isRunning = false;
        timer.cancel();
    }

    private class Updater extends TimerTask{

        public void run(){
            try {
                String next;
                next = nextBusStop;
                nextBusStop = connector.getNextStop();
                if (!nextBusStop.equals(next)){
                    setChanged();
                    notifyObservers();
                }

                
            } catch (IOException e) {
                e.printStackTrace();
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    public String getNextBusStop(){
        return nextBusStop;
    }
}
