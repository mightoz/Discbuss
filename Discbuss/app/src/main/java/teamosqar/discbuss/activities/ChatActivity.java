package teamosqar.discbuss.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import teamosqar.discbuss.application.ChatController;

/**
 * Created by joakim on 2015-09-29.
 */
public class ChatActivity extends ListActivity {

    private ChatController chatController;
    private Button sendMsgButton;
    private EditText msgToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        chatController = new ChatController();
        sendMsgButton = (Button) findViewById(R.id.sendMsgButton);
        msgToSend = (EditText) findViewById(R.id.msgToSend);

    }

    @Override
    protected void onStart(){
        super.onStart();

    //    chatController.getPartialChatHistory(); -NOT IMPLEMENTED YET
    }

    public void onStop(){
        super.onStop();
    }
}
