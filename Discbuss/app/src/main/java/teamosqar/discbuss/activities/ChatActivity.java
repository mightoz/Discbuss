package teamosqar.discbuss.activities;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.firebase.client.Firebase;

import java.util.Timer;
import java.util.TimerTask;

import teamosqar.discbuss.application.ChatAdapter;
import teamosqar.discbuss.model.Model;

/**
 * Created by joakim on 2015-09-29.
 */
public class ChatActivity extends ListActivity {

    private ChatAdapter chatAdapter;
    private EditText msgToSend;
    private TextView activeUsers;
    private String roomName;
    private Model model;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_chat);
        roomName = getIntent().getExtras().getString("EXTRA_ROOM");
        chatAdapter = new ChatAdapter(this.getLayoutInflater(), roomName);
        msgToSend = (EditText) findViewById(R.id.msgToSend);
        activeUsers = (TextView) findViewById(R.id.textViewActiveUsers);
        model = Model.getInstance();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart(){
        super.onStart();
        final ListView listView = getListView();
        listView.setAdapter(chatAdapter);
        model.addUserToChat(roomName);
    }

    public void onStop(){
        model.removeUserFromChat(roomName);
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
        ListView lv = getListView();
        int pos = lv.getPositionForView((View)view.getParent().getParent());
        chatAdapter.upVote(pos);
    }

    public void downVote(View view){
        ListView lv = getListView();
        int pos = lv.getPositionForView((View)view.getParent().getParent());
        chatAdapter.downVote(pos);
    }
}
