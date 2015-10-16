package teamosqar.discbuss.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import teamosqar.discbuss.application.BusChatController;
import teamosqar.discbuss.application.ChatController;

/**
 * Created by joakim on 2015-10-16.
 */
public class BusChatActivity extends ChatActivity {

    private String roomName;
    private TextView activeUsers;
    private BusChatController chatController;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        roomName = getIntent().getExtras().getString("EXTRA_ROOM");
        chatController = new BusChatController(this, roomName);
        super.onCreate(savedInstanceState);

        activeUsers = (TextView) findViewById(R.id.textViewActiveUsers);//TODO: What do we store this for? it is not used?
    }

    @Override
    protected ChatController getChatController() {
        return chatController;
    }

    public void upVote(View view){
        ListView lv = getListView();
        int pos = lv.getPositionForView((View)view.getParent().getParent());
        chatController.upVote(pos);
    }

    public void downVote(View view){
        ListView lv = getListView();
        int pos = lv.getPositionForView((View)view.getParent().getParent());
        chatController.downVote(pos);
    }

    public void sendPersonalMessageClicked(View view){
        chatController.personalMessageClicked(getListView().getPositionForView((View) view.getParent().getParent().getParent()));
    }
}
