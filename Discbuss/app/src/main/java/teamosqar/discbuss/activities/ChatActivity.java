package teamosqar.discbuss.activities;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;



import com.firebase.client.Firebase;

import java.util.Timer;
import java.util.TimerTask;

import teamosqar.discbuss.application.ChatController;


/**
 * Created by joakim on 2015-09-29.
 */
public abstract class ChatActivity extends ListActivity {

    private EditText msgToSend;
    private ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_chat);

        msgToSend = (EditText) findViewById(R.id.msgToSend);

        setAdapter();

    }

    private void setAdapter(){
        //Chatadapter needs to be set before calling oncreate from subclasses extending this class
        listView = getListView();
        listView.setAdapter(getChatController());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getChatController().messageClicked(position);
            }
        });
    }

    protected abstract ChatController getChatController();

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }


    /**
     * Sends entered message to database. Also delays minimization of keyboard.
     * @param view
     */
    public void sendMessage(View view){

        getChatController().sendMessage(msgToSend.getText().toString());
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

    public void viewPersonalProfileClicked(View view){
        getChatController().personalProfileClicked(listView.getPositionForView((View) view.getParent().getParent().getParent()));
    }

    @Override
    protected void onStart(){
        super.onStart();
        getChatController().onEnteredChat();
    }

    @Override
    public void onStop(){
        getChatController().onLeftChat();
        super.onStop();
    }
}