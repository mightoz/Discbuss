package teamosqar.discbuss;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.Random;

/**
 * NOTE: This is really ugly code and is just for learning purposes. To learn about using wifi-connectivity.
 */

public class NetworkTestActivity extends AppCompatActivity {
    private String MAC;
    private Button chatButton;
    private TextView textDisplay;
    private String Hubben = "0:1f:9d:b6:e0:0";
    private String EPO131 = "0013951349f5";
    private String lobbyNr = "Unknown net";
    private Random generator = new Random();
    private WifiManager wfManager;
    private WifiInfo wifiinfo;
    private RelativeLayout bgElement;
    @Override
    protected void onStart(){
        super.onStart();
        textDisplay = (TextView) findViewById(R.id.textViewConnection);
        chatButton = (Button) findViewById(R.id.buttonChat);

        wfManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
    }
    public void testPressed(View view){
        if(wfManager.isWifiEnabled()) {

            wifiinfo = wfManager.getConnectionInfo();
            MAC = wifiinfo.getBSSID();
            if (MAC.isEmpty()) {
                textDisplay.setText("Unknown net");
            }
            Log.d("Holmus", MAC);
            switch (MAC) {
                case "04:f0:21:10:09:df":
                    lobbyNr = "EPO136";
                    chatButton.setVisibility(View.VISIBLE);
                    break;
                case "f4:1f:c2:09:5a:6e":
                    lobbyNr = "Hubben!";
                    chatButton.setVisibility(View.VISIBLE);
                    break;
                case "44:ad:d9:f1:4b:7e":
                    lobbyNr = "Hubben!";
                    chatButton.setVisibility(View.VISIBLE);
                    break;
            }
            textDisplay.setText(lobbyNr);
        } else {
            textDisplay.setText("Connect to Wifi!");
        }
    }
    public void youCanChatBro(View view){
        if(wfManager.isWifiEnabled()) {
            wifiinfo = wfManager.getConnectionInfo();
            if (MAC.equals(wifiinfo.getBSSID())) {
                bgElement = (RelativeLayout) findViewById(R.id.container);
                int c = generator.nextInt(5);
                switch (c) {
                    case 0:
                        bgElement.setBackgroundColor(Color.YELLOW);
                        break;
                    case 1:
                        bgElement.setBackgroundColor(Color.CYAN);
                        break;
                    case 2:
                        bgElement.setBackgroundColor(Color.RED);
                        break;
                    case 3:
                        bgElement.setBackgroundColor(Color.GREEN);
                        break;
                    case 4:
                        bgElement.setBackgroundColor(Color.LTGRAY);
                        break;
                }
            }
        } else {
            chatButton.setVisibility(View.INVISIBLE);
                bgElement.setBackgroundColor(Color.WHITE);
            if(!wfManager.isWifiEnabled()){
                textDisplay.setText("Connect to Wifi!");
            }
            }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_network_test, menu);
        return true;
    }

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

        return super.onOptionsItemSelected(item);
    }
}
