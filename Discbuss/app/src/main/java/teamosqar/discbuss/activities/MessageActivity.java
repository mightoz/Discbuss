package teamosqar.discbuss.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import teamosqar.discbuss.activities.R;
import teamosqar.discbuss.application.MessageController;

public class MessageActivity extends ListActivity {

    private MessageController messageController;
    private ListView listView;
    private TextView noContentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        messageController = new MessageController(getLayoutInflater());
        noContentText = (TextView) findViewById(R.id.noContentMessage);
        listView = getListView();

        listView.setAdapter(messageController);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //We need to send info on which room we are accessing!
                //Intent intent = new Intent(this, DuoChatActivity.class);
                //startActivity(intent);
            }
        });
    }

    @Override
    public void onContentChanged(){
        if(messageController.getCount() == 0){
            noContentText.setText("You do not participate in any duo chats");
        }else{
            noContentText.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
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
