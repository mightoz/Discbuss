package teamosqar.discbuss.application;

import android.graphics.Typeface;
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
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import teamosqar.discbuss.activities.R;
import teamosqar.discbuss.util.Message;
import teamosqar.discbuss.util.MessageInbox;

/**
 * Created by joakim on 2015-10-11.
 */
public class MessageController extends BaseAdapter {

    private Firebase messagesFirebaseRef;
    private List<MessageInbox> messageInboxes;
    private List<Message> mostRecentMsg;
    private List<String> keys; //Remember the lists are ordered by their date/time in the messageInbox model, keys does not controll the ordering in this case
    private Map<String, ChildEventListener> childEventListenerMap;
    private LayoutInflater inflater;

    public MessageController(LayoutInflater inflater){
        messagesFirebaseRef = Model.getInstance().getMRef().child("duoChats");

        this.inflater = inflater;

        messageInboxes = new ArrayList<MessageInbox>();
        mostRecentMsg = new ArrayList<Message>();
        keys = new ArrayList<String>();
        childEventListenerMap = new HashMap<String, ChildEventListener>();

        Firebase activeChatsRef = Model.getInstance().getMRef().child("users").child(Model.getInstance().getUid()).child("activeChats");

        ChildEventListener participatingChat = activeChatsRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getValue(String.class);

                startListeningAt(key);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getValue(String.class);

                stopListeningAt(key);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private void startListeningAt(final String key){
        ChildEventListener messageListener = messagesFirebaseRef.child(key).addChildEventListener(new ChildEventListener() {
            @Override
            //Remember that seenByY and seenByX will translate directly into messageInbox seenByMe and seenByOther
            //Will need to be switched in case they are incorrect, or controlled in some other way!
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {

                MessageInbox inbox = createMessageInbox(dataSnapshot.child("inboxInfo"));
                Iterator iterator = dataSnapshot.child("chat").getChildren().iterator();
                Message mostRecentMessage = getLastIteratorMessage(iterator);


                int index = getCorrectListSpot(inbox);

                insertIntoLists(index, inbox, mostRecentMessage, key);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                int currentIndex = keys.indexOf(key);
                MessageInbox inbox = createMessageInbox(dataSnapshot.child("inboxInfo"));
                Iterator iterator = dataSnapshot.child("chat").getChildren().iterator();

                Message mostRecentMessage = getLastIteratorMessage(iterator);


                messageInboxes.remove(currentIndex);
                mostRecentMsg.remove(currentIndex);
                keys.remove(currentIndex);

                int newIndex = getCorrectListSpot(inbox);

                insertIntoLists(newIndex, inbox, mostRecentMessage, key);

                notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                int index = keys.indexOf(key);

                keys.remove(index);
                messageInboxes.remove(index);
                mostRecentMsg.remove(index);

                notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildKey) {

                MessageInbox inbox = createMessageInbox(dataSnapshot.child("inboxInfo"));

                Iterator iterator = dataSnapshot.child("chat").getChildren().iterator();

                Message mostRecentMessage = getLastIteratorMessage(iterator);

                int currentIndex = keys.indexOf(key);
                messageInboxes.remove(currentIndex);
                mostRecentMsg.remove(currentIndex);
                keys.remove(currentIndex);

                insertIntoLists(getCorrectListSpot(inbox), inbox, mostRecentMessage, key);

                notifyDataSetChanged();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur");
            }
        });

        childEventListenerMap.put(key, messageListener);
    }

    private void stopListeningAt(String key){
        messagesFirebaseRef.child(key).removeEventListener(childEventListenerMap.get(key));
        childEventListenerMap.remove(key);

        int index = keys.indexOf(key);
        messageInboxes.remove(index);
        mostRecentMsg.remove(index);
        keys.remove(index);
    }

    private Message getLastIteratorMessage(Iterator iterator){
        Message lastElement = null;
        while(iterator.hasNext()){
            lastElement = ((DataSnapshot)iterator.next()).child("message").getValue(Message.class);
        }
        return lastElement;
    }

    private MessageInbox createMessageInbox(DataSnapshot dataSnapshot){
        Iterator iterator = dataSnapshot.getChildren().iterator();
        String latestActivity = "";
        boolean seenByMe = false;
        boolean seenByOther = false;
        String otherPersonUid = "";
        while(iterator.hasNext()){
            DataSnapshot dataSnapshotChild = (DataSnapshot)iterator.next();
            if(dataSnapshotChild.getKey().contains(Model.getInstance().getUid())){
                seenByMe = (boolean)dataSnapshotChild.getValue();
            }else if(dataSnapshotChild.getKey().contains("latest")){
                latestActivity = dataSnapshotChild.getValue(String.class);
            }else{
                seenByOther = (boolean)dataSnapshotChild.getValue();
                otherPersonUid = dataSnapshotChild.getKey();
            }
        }
        final MessageInbox messageInbox = new MessageInbox(latestActivity, seenByMe, seenByOther);
        if(otherPersonUid != ""){
            Model.getInstance().getMRef().child("users").child(otherPersonUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    messageInbox.setOtherParticipant(name);
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
        return messageInbox;
    }


    private void insertIntoLists(int position, MessageInbox inbox, Message mostRecentMessage, String key){
        if(position == messageInboxes.size()){
            messageInboxes.add(inbox);
            mostRecentMsg.add(mostRecentMessage);
            keys.add(key);
        }else{
            messageInboxes.add(position, inbox);
            mostRecentMsg.add(position, mostRecentMessage);
            keys.add(position, key);
        }
    }

    private int getCorrectListSpot(MessageInbox inbox){

        for(int i = 0; i < messageInboxes.size(); i++){
            if(messageInboxes.get(i).isBefore(inbox.getLatestActivity())){
                return i;
            }
        }
        return messageInboxes.size();
    }


    @Override
    public int getCount() {
        return messageInboxes.size();
    }

    @Override
    public Object getItem(int position) {
        return messageInboxes.get(position);
    }

    public String getChatRefKey(int position){
        return keys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.message_inbox, parent, false);
        }

        populateView(convertView, position);
        return convertView;
    }

    private void populateView(View view, int position){
        Message msg = mostRecentMsg.get(position);
        String chattingWith = messageInboxes.get(position).getOtherParticipant();

        TextView authorView = (TextView) view.findViewById(R.id.messageInboxNick);
        TextView messageView = (TextView) view.findViewById(R.id.messageInboxMessage);
        TextView messageReadView = (TextView) view.findViewById(R.id.messageInboxRead);

        authorView.setText(chattingWith+ ": ");
        if(msg != null) {
            messageView.setText(msg.getMessage());
        }
        if(!messageInboxes.get(position).isSeenByMe()){
            messageView.setTypeface(null, Typeface.BOLD);
            authorView.setTypeface(null, Typeface.BOLD);
        }else{
            messageView.setTypeface(null, Typeface.NORMAL);
            authorView.setTypeface(null, Typeface.NORMAL);
        }
        if(messageInboxes.get(position).isSeenByOther()) {
            messageReadView.setText("Read");
        }else{
            messageReadView.setText("");
        }
    }
}
