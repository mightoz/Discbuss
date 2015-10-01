package teamosqar.discbuss.application;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import teamosqar.util.Message;

/**
 * Created by joakim on 2015-09-29.
 */
public class ChatController extends Observable {

    private String myUsername;
    private Firebase chatFireBaseRef;
    private ChildEventListener chatListener;
    private List<Message> messageModels;
    private List<String> messageKeys;


    public ChatController(){

        chatFireBaseRef = ApplicationConstants.getInstance().getFirebaseRef().child("chat");
        messageModels = new ArrayList<Message>();
        messageKeys = new ArrayList<String>();

        chatListener = chatFireBaseRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                Message message = dataSnapshot.getValue(Message.class);
                String key = dataSnapshot.getKey();

                //Insert into correct location, based on previous child
                if(previousChildName == null){
                    messageModels.add(0, message);
                    messageKeys.add(0,key);
                }else{
                    int previousIndex = messageKeys.indexOf(previousChildName);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public void sendMessage(String msg){
        Message message = new Message(msg,"jag");
        chatFireBaseRef.push().setValue(message);
    }
}
