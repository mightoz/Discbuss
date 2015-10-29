package teamosqar.discbuss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import teamosqar.discbuss.application.ActionBarController;

/**
 * Created by joakim on 2015-10-29.
 */
public abstract class BusBarActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //initialize();
    }

    @Override
    protected void onStart(){
        super.onStart();
        //initialize();
    }

    //protected abstract void initialize();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.logout){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("logout", "logout");
            startActivity(intent);
            //controller.resetModel();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
