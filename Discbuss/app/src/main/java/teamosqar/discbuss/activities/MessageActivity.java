package teamosqar.discbuss.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import teamosqar.discbuss.application.MessageAdapter;

public class MessageActivity extends ListActivity {

    private MessageAdapter messageAdapter;
    private ListView listView;
    private TextView noContentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageAdapter = new MessageAdapter(getLayoutInflater());
        setContentView(R.layout.activity_message);
        noContentText = (TextView) findViewById(R.id.noContentMessage);


        listView = getListView();
        listView.setAdapter(messageAdapter);

        if(messageAdapter.getCount() == 0){
            noContentText.setText("You do not participate in any duo chats");
        } else {
            noContentText.setText("");
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("EXTRA_ROOM", messageAdapter.getChatRefKey(position));
                startActivity(intent);
                /*
                We need to send info on which room we are accessing!! chat ref key can be retrieved by:
                messageAdapter.getChatRefKey(position);
                Intent intent = new Intent(this, DuoChatActivity.class);
                startActivity(intent);
                */
            }
        });
    }

    @Override
    public void onContentChanged(){
        if(noContentText != null){
            if(messageAdapter.getCount() == 0){
                noContentText.setText("You do not participate in any duo chats");
            } else {
                noContentText.setText("");
            }
        }
        super.onContentChanged();
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
