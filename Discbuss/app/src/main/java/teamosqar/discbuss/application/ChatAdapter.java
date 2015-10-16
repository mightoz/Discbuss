package teamosqar.discbuss.application;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
    boolean isPrivateChat;

    public ChatAdapter(Context context, LayoutInflater inflater, String chatRoom){
        this.context = context;
        this.inflater = inflater;
        clickedMessage = -1;
        clickedView = null;

        //chatFireBaseRef = Model.getInstance().getMRef().child("chatRooms").child(chatRoom); //TODO: Use to bind chatrooms to buses
        //activeUserRef = Model.getInstance().getMRef().child("activeUsers").child(chatRoom); //TODO: Use to bind chatrooms to buses
        //chatFireBaseRef = Model.getInstance().getMRef().child("chat");                        //TODO: Remove when done testing
        //activeUserRef = Model.getInstance().getMRef().child("activeUsers").child("chat");     //TODO: Remove when done testing

        if(chatRoom.contains(Model.getInstance().getUid())){
            chatFireBaseRef = Model.getInstance().getMRef().child("duoChats").child(chatRoom).child("content").child("chat");
            isPrivateChat = true;
        }else{
            chatFireBaseRef = Model.getInstance().getMRef().child("chat");
            activeUserRef = Model.getInstance().getMRef().child("activeUsers").child("chat");
            isPrivateChat = false;
        }
        messageModels = new ArrayList<Message>();
        messageKeys = new ArrayList<String>();
        chatListener = chatFireBaseRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {

                Message message = dataSnapshot.child("message").getValue(Message.class);
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
                Message message = dataSnapshot.child("message").getValue(Message.class);
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
                Message message = dataSnapshot.child("message").getValue(Message.class);

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
        if(!isPrivateChat) {
            activeUserListener = activeUserRef.addValueEventListener(new ValueEventListener() {
                int connectedUsers;

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    connectedUsers = (int) (dataSnapshot.getChildrenCount());
                    updateUserCount(connectedUsers);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
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
        final Firebase messageRef = chatFireBaseRef.child(messageKeys.get(message));

        messageRef.child("usersVoted").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean doTransaction = false;
                boolean changeShouldDouble = false;
                if(!messageModels.get(message).getUid().equals(Model.getInstance().getUid())) {
                    if (!dataSnapshot.hasChild(Model.getInstance().getUid())) {
                        doTransaction = true;
                    } else if ((dataSnapshot.child(Model.getInstance().getUid()).getValue(Integer.class) != change)) {
                        doTransaction = true;
                        changeShouldDouble = true;
                    }
                }

                if (doTransaction) {

                    messageRef.child("usersVoted").child(Model.getInstance().getUid()).setValue(change);
                    final int correctedChange;

                    if(changeShouldDouble){
                       correctedChange = change*2;
                    }else{
                        correctedChange = change;
                    }

                    messageRef.child("message").child("karma").runTransaction(new Transaction.Handler() {

                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            if (mutableData.getValue() == null) {
                                mutableData.setValue(correctedChange);
                            } else {
                                mutableData.setValue((Long) mutableData.getValue() + correctedChange);
                            }

                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                            //datasnapshot is the karma child? will this work?
                            messageModels.get(message).setKarma(((Long) dataSnapshot.getValue()).intValue());
                        }
                    });


                    Firebase userRef = Model.getInstance().getMRef().child("users").child(messageModels.get(message).getUid()).child("karma");

                    userRef.runTransaction(new Transaction.Handler() {

                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            if (mutableData.getValue() == null) {
                                mutableData.setValue(correctedChange);
                            } else {
                                mutableData.setValue((Long) mutableData.getValue() + correctedChange);
                            }

                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                            //Karma has been added to the specified user
                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    public void sendMessage(String msg){
        if(!msg.equals("")) {
            Message message = new Message(Model.getInstance().getUid(), msg, Model.getInstance().getUsername());
            chatFireBaseRef.push().child("message").setValue(message);
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

    public void personalMessageClicked(int position){
        final String otherUid = messageModels.get(position).getUid();
        if(!otherUid.equals(Model.getInstance().getUid())){
            Model.getInstance().getMRef().child("users").child(Model.getInstance().getUid()).child("activeChats").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterator children = dataSnapshot.getChildren().iterator();
                    boolean foundChat = false;
                    String finalChatRef = "";
                    while(children.hasNext()){
                        DataSnapshot snap = (DataSnapshot)children.next();
                        String currentChatRef = snap.getValue(String.class);
                        if(currentChatRef.contains(otherUid)){
                            foundChat = true;
                            finalChatRef = currentChatRef;
                        }
                    }
                    if(!foundChat){
                        Firebase userRef = Model.getInstance().getMRef().child("users");

                        finalChatRef = otherUid + "!" + Model.getInstance().getUid();
                        userRef.child(Model.getInstance().getUid()).child("activeChats").push().setValue(finalChatRef);
                        userRef.child(otherUid).child("activeChats").push().setValue(finalChatRef);

                        Firebase chatRef = Model.getInstance().getMRef().child("duoChats").child(finalChatRef).child("content");
                        chatRef.child("inboxInfo").child(otherUid).setValue(true);
                        chatRef.child("inboxInfo").child(Model.getInstance().getUid()).setValue(false);
                        Calendar calendar = Calendar.getInstance();
                        chatRef.child("inboxInfo").child("latestActivity").setValue(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.DAY_OF_YEAR)+ "-" + calendar.get(Calendar.HOUR_OF_DAY) + "-" + calendar.get(Calendar.MINUTE));

                    }
                    if(!finalChatRef.equals("")) {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("EXTRA_ROOM", finalChatRef);
                        context.startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }

    public void personalProfileClicked(int position){
        final String otherUid = messageModels.get(position).getUid();
        if(!otherUid.equals(Model.getInstance().getUid())){
            //TODO: Launch profile using otherUid
        }
    }
}
