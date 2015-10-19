package teamosqar.discbuss.net;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
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
    private List<Observer> observerList;

    public StopUpdater(String bssid) {
        connector = new BusConnector(bssid);
        timer = new Timer();
        nextBusStop = "";
        observerList = new ArrayList<>();

    }

    public void start(){
        if(!isRunning){
            timer.schedule(new Updater(),0,15000);
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
                if (!nextBusStop.equals(next))
                    notifyObservers();
                
            } catch (IOException e) {
                e.printStackTrace();
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    public void addObserver(Observer o){
        if(!observerList.contains(o))
            observerList.add(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer: observerList){
            observer.update(this, nextBusStop);
        }
    }
}
