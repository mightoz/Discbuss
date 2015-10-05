package teamosqar.discbuss.activities;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import com.firebase.client.Firebase;

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
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_chat);
        chatController = new ChatController();
        msgToSend = (EditText) findViewById(R.id.msgToSend);
        adapter = new ChatAdapter();

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

    public void sendMessage(View view){
        chatController.sendMessage(msgToSend.getText().toString());
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(msgToSend.getWindowToken(), 0);
        msgToSend.setText("");
    }
}
