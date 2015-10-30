package teamosqar.discbuss.activities;

import android.os.Bundle;

import teamosqar.discbuss.application.ChatController;
import teamosqar.discbuss.application.DuoChatController;

/**
 * Created by joakim on 2015-10-16.
 */
public class DuoChatActivity extends ChatActivity{

    private DuoChatController chatController;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        String roomName = getIntent().getExtras().getString("EXTRA_ROOM");
        chatController = new DuoChatController(this, roomName);
        chatController.updateContext(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart(){
        super.onStart();
        chatController.updateContext(this);

    }

    @Override
    protected ChatController getChatController() {
        return chatController;
    }
}
