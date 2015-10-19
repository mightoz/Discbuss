package teamosqar.discbuss.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import teamosqar.discbuss.application.MainController;
import teamosqar.discbuss.fragments.SuggestFragment;
import teamosqar.discbuss.util.Toaster;

public class MainActivity extends AppCompatActivity {

    private MainController mainController;
    private Firebase mref;
    private boolean doubleBackAgain = false;
    //TODO: Remove, model should not be saved in here. Use controller instead.
//    private Model model = Model.getInstance();
    private TextView suggestView;
    //BELOW ONLY FOR TESTING...
    private final String bssidMightos = "bc:ee:7b:55:47:16";
    //ABOVE ONLY FOR TESTING...

    private EditText fragmentData;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private SuggestFragment fragment;
    TextView actionBarText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main);
        mainController = new MainController(this);
        mainController.checkWifiState();
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.activity_action_bar,
                null);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        actionBarText = (TextView)findViewById(R.id.actionBarTextView);

        actionBarText.setText("CustomMessage"); // <-- as always this is how its done. easy to do.

        actionBarText.setTextColor(Color.GRAY);

    }

    @Override
    protected void onStart(){
        super.onStart();
        Firebase.setAndroidContext(this);
        suggestView = (TextView) findViewById(R.id.textViewStatement);
        fragment = new SuggestFragment();
        mainController.addAsObserver();
    }

    @Override
    public void onBackPressed(){
        if(doubleBackAgain){
            Log.d("quitting", "quitting");
            super.onBackPressed();
            return;
        } else {
            Log.d("not quitting", "not quitting");
            doubleBackAgain = true;
            Toaster.displayToast("Please click BACK again to exit", this, Toast.LENGTH_SHORT);

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
            chatRoom = chatRoom + roomNbr;
            Intent intent = new Intent(this, BusChatActivity.class);
            intent.putExtra("EXTRA_ROOM", chatRoom);
            startActivity(intent);
        }else {
            Toaster.displayToast("Connect to buss WiFi", getApplicationContext(), Toast.LENGTH_SHORT);
        }

    }

    public void goToProfile(View view){
        Intent intent = new Intent(this, MyProfileActivity.class);
        startActivity(intent);
    }

    public void goToMessages(View view){
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }

    public void suggestStatement(View view){
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.fragmentPlaceholder, fragment);
        ft.commit();
    }
    public void submitStatement(View view){
        fragmentData = (EditText) findViewById(R.id.editTextStatement);
        if(!fragmentData.getText().toString().isEmpty()) {
            mainController.submitStatement(fragmentData.getText().toString());
            //TODO: Add toast saying it was successful! Remove Fragment.
            FragmentTransaction newFt = getFragmentManager().beginTransaction();
            newFt.remove(fragment);
            newFt.commit();
            Toaster.displayToast("Statement submitted!", getApplicationContext(), Toast.LENGTH_SHORT);
        } else {
            //TODO: Update with new toaster
            Toaster.displayToast("Write a statement", getApplicationContext(), Toast.LENGTH_SHORT);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    //TODO: Refactor this method. -> Move to controller and call from here.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.logout){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("logout", "logout");
            startActivity(intent);
            mainController.resetModel();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
