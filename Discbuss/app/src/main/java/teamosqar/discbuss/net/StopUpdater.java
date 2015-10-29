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

    /**
     * Starts the stopUpdater if it's not already running, checking for updates every 10 seconds
     */
    public void start(){
        if(!isRunning){
            timer.schedule(new Updater(),0,10000);
            isRunning = true;
        }
    }

    /**
     * Stops the stopUpdater
     */
    public void stop(){
        isRunning = false;
        timer.cancel();
    }

    private class Updater extends TimerTask{
        /**
         * Checks for new busStop and notifies observers if the upcoming bus stop isn't the same as the currently displayed
         */
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

    /**
     * @return the upcoming busStop as a string
     */
    public String getNextBusStop(){
        return nextBusStop;
    }
}
