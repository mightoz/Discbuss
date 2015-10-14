package teamosqar.discbuss.application;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import teamosqar.discbuss.activities.ChatActivity;
import teamosqar.discbuss.activities.R;
import teamosqar.discbuss.model.Model;
import teamosqar.discbuss.util.Message;


/**
 * Created by joakim on 2015-09-29.
 */
public class ChatAdapter extends BaseAdapter{

    private final Context context;
    private LayoutInflater inflater;
    private Firebase chatFireBaseRef, activeUserRef;
    private ChildEventListener chatListener;
    private ValueEventListener activeUserListener;
    private List<Message> messageModels;
    private List<String> messageKeys;
    private int clickedMessage;
    private View clickedView;


    public ChatAdapter(Context context, LayoutInflater inflater, String chatRoom){
        this.context = context;
        this.inflater = inflater;
        clickedMessage = -1;
        clickedView = null;

        //chatFireBaseRef = Model.getInstance().getMRef().child("chatRooms").child(chatRoom); //TODO: Use to bind chatrooms to buses
        //activeUserRef = Model.getInstance().getMRef().child("activeUsers").child(chatRoom); //TODO: Use to bind chatrooms to buses
        chatFireBaseRef = Model.getInstance().getMRef().child("chat");                        //TODO: Remove when done testing
        activeUserRef = Model.getInstance().getMRef().child("activeUsers").child("chat");     //TODO: Remove when done testing
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

        /**
         * Listener for number of participants in chat rooms.
         */
        activeUserListener = activeUserRef.addValueEventListener(new ValueEventListener() {
            int connectedUsers;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                connectedUsers = (int)(dataSnapshot.getChildrenCount());
                updateUserCount(connectedUsers);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void updateUserCount(int users){
        TextView numUsers = (TextView) ((Activity) context).findViewById(R.id.textViewActiveUsers);
        numUsers.setText(Integer.toString(users));
    }
    //TODO: Only let a user have one upvote/downvote active for each comment.
    public void upVote(int i){
        performKarmaChange(i, 1);
    }
    //TODO: Only let a user have one upvote/downvote active for each comment.
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
                messageModels.get(message).setKarma(((Long)dataSnapshot.getValue()).intValue());
            }
        });



        //TODO: Not sure if this is needed yet. Add later if needed, else remove.
        //Adds the karma change to model. Not finished.
        //Sets userRef to the karma child of the users child
        /*Firebase userRef = Model.getInstance().getMRef().child("users").child(messageModels.get(messageKeys.indexOf(message)).getUid()).child("karma");

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
        });*/
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
        if(clickedMessage == position) {
            if(clickedView != null){
                convertView = clickedView;
            }else {
                convertView = inflater.inflate(R.layout.message_chat, parent, false);
                View viewExtension = inflater.inflate(R.layout.message_chat_extension, null);
                ((ViewGroup) convertView).addView(viewExtension, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                clickedView = convertView;
            }
        }else if (convertView == null || clickedView == convertView) {
            convertView = inflater.inflate(R.layout.message_chat, parent, false);
        }


        Message msg = messageModels.get(position);
        populateView(convertView, msg);


        return convertView;
    }

    private void populateView(View view, Message message){
        String author = message.getAuthor();
        String msg = message.getMessage();
        int karma = message.getKarma();

        TextView authorView = (TextView) view.findViewById(R.id.author);
        TextView msgView = (TextView) view.findViewById(R.id.message);
        TextView commentKarma = (TextView) view.findViewById(R.id.commentKarma);


        //TODO: Change colour of comments to indicate which is yours. Make it work uniquely for each user.
        //Sets color of your usrname to green and others' to gray. Not finished.

      /*  if(message.getUid()!= null && message.getUid().equals(Model.getInstance().getUid())){
            authorView.setTextColor(Color.GREEN);
        }else{
            authorView.setTextColor(Color.DKGRAY);
        }*/

        authorView.setText(author);
        msgView.setText(msg);
        commentKarma.setText(Integer.toString(karma));

        //TODO: Make good looking up/down-vote buttons. Downvote should glow red if clicked, upvote should glow green.

    }

    public void updateParticipants(){
        //
    }

    public void messageClicked(int position) {
        if(clickedMessage != position) {
            clickedMessage = position;
        }else{
            clickedMessage = -1;
        }
        notifyDataSetChanged();
    }
}
