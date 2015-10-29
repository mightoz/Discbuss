package teamosqar.discbuss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import teamosqar.discbuss.application.MessageController;

public class MessageActivity extends BusBarActivity {

    private MessageController messageController;
    private ListView listView;
    private TextView actionBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageController = new MessageController(this);
        setContentView(R.layout.activity_message);

        listView = (ListView)findViewById(R.id.messageList);
        listView.setAdapter(messageController);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DuoChatActivity.class);
                intent.putExtra("EXTRA_ROOM", messageController.getChatRefKey(position));
                startActivity(intent);
                /*
                We need to send info on which room we are accessing!! chat ref key can be retrieved by:
                messageController.getChatRefKey(position);
                Intent intent = new Intent(this, DuoChatActivity.class);
                startActivity(intent);
                */
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        messageController.addAsObserver();
        messageController.updateNextBusStop();
    }

    public void leaveDuoChat(View view){
        int pos = listView.getPositionForView((View)view.getParent());
        messageController.leaveChat(pos);
    }
}
