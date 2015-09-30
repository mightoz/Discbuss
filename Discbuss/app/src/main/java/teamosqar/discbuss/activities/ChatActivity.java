package teamosqar.discbuss.activities;

import android.app.ListActivity;
import android.os.Bundle;

import teamosqar.application.ChatController;

/**
 * Created by joakim on 2015-09-29.
 */
public class ChatActivity extends ListActivity {

    private ChatController chatController;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        chatController = new ChatController();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    public void onStop(){
        super.onStop();
    }
}
