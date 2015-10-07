package teamosqar.discbuss.application;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;

import java.util.ArrayList;
import java.util.List;

import teamosqar.discbuss.activities.R;
import teamosqar.discbuss.model.Model;
import teamosqar.discbuss.util.Message;


/**
 * Created by joakim on 2015-09-29.
 */
public class ChatAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Firebase chatFireBaseRef;
    private ChildEventListener chatListener;
    private List<Message> messageModels;
    private List<String> messageKeys;


    public ChatAdapter(LayoutInflater inflater, String chatRoom){
        this.inflater = inflater;
        chatFireBaseRef = Model.getInstance().getMRef().child("chat");
        //chatFireBaseRef = Model.getInstance().getMRef().child(chatRoom);
        messageModels = new ArrayList<Message>();
        messageKeys = new ArrayList<String>();

        chatListener = chatFireBaseRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {

                Message message = dataSnapshot.getValue(Message.class);
                String key = dataSnapshot.getKey();

                //Insert into correct location, based on previous child
                if(previousChildKey == null){
                    messageModels.add(0, message);
                    messageKeys.add(0,key);
                }else{
                    int previousIndex = messageKeys.indexOf(previousChildKey);
                    int nextIndex = previousIndex + 1;
                    if(nextIndex == messageModels.size()){
                        messageModels.add(message);
                        messageKeys.add(key);
                    }else{
                        messageModels.add(nextIndex, message);
                        messageKeys.add(key);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                Message message = dataSnapshot.getValue(Message.class);
                int index = messageKeys.indexOf(key);

                messageModels.set(index, message);
                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = messageKeys.indexOf(key);

                messageKeys.remove(index);
                messageModels.remove(index);
                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildKey) {
                String key = dataSnapshot.getKey();
                Message message = dataSnapshot.getValue(Message.class);

                int index = messageKeys.indexOf(key);
                messageModels.remove(index);
                messageKeys.remove(index);

                if(previousChildKey == null){
                    messageModels.add(0, message);
                    messageKeys.add(0, key);
                }else{
                    int previousIndex = messageKeys.indexOf(previousChildKey);
                    int nextIndex = previousIndex + 1;
                    if(nextIndex == messageModels.size()){
                        messageModels.add(message);
                        messageKeys.add(key);
                    }else{
                        messageModels.add(nextIndex, message);
                        messageKeys.add(nextIndex, key);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur");
            }
        });
    }

    public void upVote(int i){
        performKarmaChange(i, 1);
    }

    public void downVote(int i){
        performKarmaChange(i, -1);
    }

    private void performKarmaChange(final int message, final int change){
        Firebase voteRef = chatFireBaseRef.child(messageKeys.get(message)).child("karma");

        voteRef.runTransaction(new Transaction.Handler(){

            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if(mutableData.getValue() == null){
                    mutableData.setValue(change);
                }else{
                    mutableData.setValue((Long)mutableData.getValue() + change);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                //datasnapshot is the karma child? will this work?
                messageModels.get(message).setKarma((Integer) dataSnapshot.getValue());
            }
        });


        //Sets userRef to the karma child of the users child
        Firebase userRef = Model.getInstance().getMRef().child("users").child(messageModels.get(messageKeys.indexOf(message)).getUid()).child("karma");

        userRef.runTransaction(new Transaction.Handler() {

            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    mutableData.setValue(change);
                } else {
                    mutableData.setValue((Long) mutableData.getValue() + change);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                //If we add karma to model, this is where we know it has been updated in firebase
            }
        });
    }

    public void sendMessage(String msg){
        if(!msg.equals("")) {
            Message message = new Message(Model.getInstance().getUid(), msg, Model.getInstance().getUsername());
            chatFireBaseRef.push().setValue(message);
        }
    }

    @Override
    public int getCount() {
        return messageModels.size();
    }

    @Override
    public Object getItem(int position) {
        return messageModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.message_chat, parent, false);
        }
        Message msg = messageModels.get(position);
        populateView(convertView, msg);

        return convertView;
    }

    private void populateView(View view, Message message){
        String author = message.getAuthor();
        String msg = message.getMessage();

        TextView authorView = (TextView) view.findViewById(R.id.author);

        TextView msgView = (TextView) view.findViewById(R.id.message);

        authorView.setText(author + ": ");
        msgView.setText(msg);

    }
}
