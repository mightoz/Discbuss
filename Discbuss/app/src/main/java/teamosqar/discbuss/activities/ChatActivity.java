package teamosqar.discbuss.activities;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;


import com.firebase.client.Firebase;

import java.util.Timer;
import java.util.TimerTask;

import teamosqar.discbuss.application.ChatAdapter;

/**
 * Created by joakim on 2015-09-29.
 */
public class ChatActivity extends ListActivity {

    private ChatAdapter chatAdapter;
    private EditText msgToSend;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_chat);
        chatAdapter = new ChatAdapter(this.getLayoutInflater());
        msgToSend = (EditText) findViewById(R.id.msgToSend);

    }

    @Override
    protected void onStart(){
        super.onStart();
        final ListView listView = getListView();
        listView.setAdapter(chatAdapter);
    }

    public void onStop(){
        super.onStop();
    }

    /**
     * Sends entered message to database. Also delays minimization of keyboard.
     * @param view
     */
    public void sendMessage(View view){

        chatAdapter.sendMessage(msgToSend.getText().toString());
        msgToSend.setText("");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(msgToSend.getWindowToken(), 0);
            }
        }, 400);

    }

    public void upVote(View view){
        chatAdapter.upVote(view);
    }

    public void downVote(View view){
        chatAdapter.downVote(view);
    }
}
