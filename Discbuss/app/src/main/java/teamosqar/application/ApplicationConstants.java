package teamosqar.application;

import com.firebase.client.Firebase;

/**
 * Created by joakim on 2015-09-30.
 */
public class ApplicationConstants {
    private static ApplicationConstants ourInstance = new ApplicationConstants();

    private Firebase mainRef;

    protected static ApplicationConstants getInstance() {
        return ourInstance;
    }

    private ApplicationConstants() {
        mainRef = new Firebase("https://boiling-heat-3778.firebaseio.com/");
    }

    protected Firebase getFirebaseRef(){
        return mainRef;
    }
}
