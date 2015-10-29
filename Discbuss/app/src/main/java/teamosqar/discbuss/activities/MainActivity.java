package teamosqar.discbuss.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

//import teamosqar.discbuss.application.ActionBarHandler;
import teamosqar.discbuss.application.BusActionBar;
import teamosqar.discbuss.application.MainController;
import teamosqar.discbuss.fragments.SuggestFragment;
import teamosqar.discbuss.util.Toaster;

public class MainActivity extends AppCompatActivity {

    private MainController mainController;
    private Firebase mref;
    private boolean doubleBackAgain = false;
    private boolean fragmentOpen = false;
    //BELOW ONLY FOR TESTING...
    private final String bssidMightos = "bc:ee:7b:55:47:16";
    //ABOVE ONLY FOR TESTING...

    private EditText fragmentData;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private SuggestFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main);
        mainController = new MainController(this);
        mainController.updateContext(this);
        mainController.checkWifiState();
    }

    @Override
    protected void onStart(){
        super.onStart();
        Firebase.setAndroidContext(this);
        fragment = new SuggestFragment();
        mainController.updateContext(this);
        //mainController.updateNextBusStop();
    }

    public void onStop(){
        super.onStop();
    }

    @Override
    public void onBackPressed(){
        if(doubleBackAgain && !fragmentOpen){
            super.onBackPressed();
        } else if(fragmentOpen) {
            FragmentTransaction newFt = getFragmentManager().beginTransaction();
            newFt.remove(fragment);
            newFt.commit();
            findViewById(R.id.textViewStatement).setVisibility(View.VISIBLE);
            fragmentOpen = false;
        } else {
            doubleBackAgain = true;

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackAgain=false;
                }
            }, 2000);
        }
    }

    /**
     * If connected to a bus wifi, enter it's chat room. Otherwise displays error toast.
     * @param view
     */
    public void enterDiscussion(View view) {

        String chatRoom = "chatRooms/";

        if(mainController.isConnectedToBus()){
            String roomNbr = Integer.toString(mainController.getIndexOfId()+1);
            if(!roomNbr.equals("0"))
            chatRoom = chatRoom + roomNbr;
            Intent intent = new Intent(this, BusChatActivity.class);
            intent.putExtra("EXTRA_ROOM", chatRoom);
            startActivity(intent);
        }else {
            Toaster.displayToast("Anslut till ett bussWiFi", getApplicationContext(), Toast.LENGTH_SHORT);
        }

    }

    /**
     * Views the currently logged in user's personal profile.
     * @param view
     */
    public void goToProfile(View view){
        Intent intent = new Intent(this, MyProfileActivity.class);
        startActivity(intent);
    }

    /**
     * Views the currently logged in user's active private chats
     * @param view
     */
    public void goToMessages(View view){
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the suggest statement fragment
     * @param view
     */
    public void suggestStatement(View view){
        findViewById(R.id.textViewStatement).setVisibility(View.INVISIBLE);
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.fragmentPlaceholder, fragment);
        ft.commit();
        fragmentOpen = true;
    }

    /**
     * Calls the submit statement method in controller and reverts the view to normal
     * @param view
     */
    public void submitStatement(View view){
        fragmentData = (EditText) findViewById(R.id.editTextStatement);
        if(!fragmentData.getText().toString().isEmpty()) {
            mainController.submitStatement(fragmentData.getText().toString());
            FragmentTransaction newFt = getFragmentManager().beginTransaction();
            newFt.remove(fragment);
            newFt.commit();
            findViewById(R.id.textViewStatement).setVisibility(View.VISIBLE);
            Toaster.displayToast("Topic skickad!", getApplicationContext(), Toast.LENGTH_SHORT);
            fragmentOpen = false;
        } else {
            //TODO: Update with new toaster
            Toaster.displayToast("Skriv ner en topic", getApplicationContext(), Toast.LENGTH_SHORT);
        }
    }

}
