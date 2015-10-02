package teamosqar.discbuss.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;


import teamosqar.discbuss.application.ChatAdapter;
import teamosqar.discbuss.application.ChatController;

/**
 * Created by joakim on 2015-09-29.
 */
public class ChatActivity extends ListActivity {

    private ChatController chatController;
    private EditText msgToSend;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatController = new ChatController();
        msgToSend = (EditText) findViewById(R.id.msgToSend);
        adapter = new ChatAdapter();

        findViewById(R.id.sendMsgButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        final ListView listView = getListView();
        listView.setAdapter(adapter);


    //    chatController.getPartialChatHistory(); -NOT IMPLEMENTED YET
    }

    public void onStop(){
        super.onStop();
    }

    public void sendMessage(){
        chatController.sendMessage(msgToSend.getText().toString());
    }
}
