package teamosqar.discbuss.application;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
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
import java.util.Observable;
import java.util.Observer;

import teamosqar.discbuss.activities.R;
import teamosqar.discbuss.util.Message;
import teamosqar.discbuss.util.MessageInbox;

/**
 * Created by joakim on 2015-10-11.
 */
public class MessageController extends BaseAdapter implements Observer {

    private Firebase messagesFirebaseRef;
    private List<MessageInbox> messageInboxes;
    private List<Message> mostRecentMsg;
    private List<String> keys; //Remember the lists are ordered by their date/time in the messageInbox model, keys does not control the ordering in this case
    private Map<String, ChildEventListener> childEventListenerMap;
    private LayoutInflater inflater;
    private Context context;

    public MessageController(Context context) {
        messagesFirebaseRef = Model.getInstance().getMRef().child("duoChats");

        inflater = LayoutInflater.from(context);
        this.context = context;

        messageInboxes = new ArrayList<>();
        mostRecentMsg = new ArrayList<>();
        keys = new ArrayList<>();
        childEventListenerMap = new HashMap<>();

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

    /**
     * Adds listeners for each chat the user participates in
     *
     * @param key
     */
    private void startListeningAt(final String key) {
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
            }
        });

        childEventListenerMap.put(key, messageListener);
    }

    /**
     * Removes listeners for the chat the user no longer participates in
     *
     * @param key
     */
    private void stopListeningAt(String key) {
        ChildEventListener listener = childEventListenerMap.remove(key);
        if (listener != null) {
            messagesFirebaseRef.child(key).removeEventListener(listener);
        }

        int index = keys.indexOf(key);
        if (index != -1) {
            messageInboxes.remove(index);
            mostRecentMsg.remove(index);
            keys.remove(index);
        }
    }

    /**
     * @param iterator which iterates through the chat to find the last message
     * @return the last message found when iterating
     */
    private Message getLastIteratorMessage(Iterator iterator) {
        Message lastElement = null;
        while (iterator.hasNext()) {
            lastElement = ((DataSnapshot) iterator.next()).child("message").getValue(Message.class);
        }
        return lastElement;
    }

    /**
     * Creates a new MessageInbox according to the new information provided in the dataSnapshot
     *
     * @param dataSnapshot
     * @return
     */
    private MessageInbox createMessageInbox(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();
        String latestActivity = "";
        boolean seenByMe = false;
        boolean seenByOther = false;
        String otherPersonUid = "";
        while (iterator.hasNext()) {
            DataSnapshot dataSnapshotChild = (DataSnapshot) iterator.next();
            if (dataSnapshotChild.getKey().contains(Model.getInstance().getUid())) {
                seenByMe = (boolean) dataSnapshotChild.getValue();
            } else if (dataSnapshotChild.getKey().contains("latest")) {
                latestActivity = dataSnapshotChild.getValue(String.class);
            } else {
                seenByOther = (boolean) dataSnapshotChild.getValue();
                otherPersonUid = dataSnapshotChild.getKey();
            }
        }
        final MessageInbox messageInbox = new MessageInbox(latestActivity, seenByMe, seenByOther);
        if (otherPersonUid != "") {
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

    /**
     * Adds the new message to the proper inbox and updates it with the status of the message
     *
     * @param position
     * @param inbox
     * @param mostRecentMessage
     * @param key
     */
    private void insertIntoLists(int position, MessageInbox inbox, Message mostRecentMessage, String key) {
        if (position == messageInboxes.size()) {
            messageInboxes.add(inbox);
            mostRecentMsg.add(mostRecentMessage);
            keys.add(key);
        } else {
            messageInboxes.add(position, inbox);
            mostRecentMsg.add(position, mostRecentMessage);
            keys.add(position, key);
        }
    }

    /**
     * @param inbox
     * @return an int representing the MessageInbox's position in the list
     */
    private int getCorrectListSpot(MessageInbox inbox) {

        for (int i = 0; i < messageInboxes.size(); i++) {
            if (messageInboxes.get(i).isBefore(inbox.getLatestActivity())) {
                return i;
            }
        }
        return messageInboxes.size();
    }

    /**
     * @return the number of MessageInboxes
     */
    @Override
    public int getCount() {
        return messageInboxes.size();
    }

    /**
     * @param position
     * @return the inbox on the position provided as param
     */
    @Override
    public Object getItem(int position) {
        return messageInboxes.get(position);
    }

    /**
     * @param position
     * @return the key for the chat on the position provided as param
     */
    public String getChatRefKey(int position) {
        return keys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return the view item on the position provided as param
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.message_inbox, parent, false);
        }

        populateView(convertView, position);
        return convertView;
    }

    /**
     * Updates the view sent in as param with potential new data, such as wether the most recent message has been read
     *
     * @param view
     * @param position
     */
    private void populateView(View view, int position) {
        Message msg = mostRecentMsg.get(position);
        String chattingWith = messageInboxes.get(position).getOtherParticipant();

        TextView authorView = (TextView) view.findViewById(R.id.messageInboxNick);
        TextView messageView = (TextView) view.findViewById(R.id.messageInboxMessage);
        TextView messageReadView = (TextView) view.findViewById(R.id.messageInboxRead);

        authorView.setText(chattingWith + ": ");
        if (msg != null) {
            messageView.setText(msg.getMessage());
        }
        if (!messageInboxes.get(position).isSeenByMe()) {
            messageView.setTypeface(null, Typeface.BOLD);
            authorView.setTypeface(null, Typeface.BOLD);
        } else {
            messageView.setTypeface(null, Typeface.NORMAL);
            authorView.setTypeface(null, Typeface.NORMAL);
        }
        if (messageInboxes.get(position).isSeenByOther()) {
            messageReadView.setText("Read");
        } else {
            messageReadView.setText("");
        }
    }

    /**
     * Updates the action bar with the upcoming bus stop
     */
    public void updateNextBusStop() {
        if (Model.getInstance().getNextBusStop() != null && !Model.getInstance().getNextBusStop().isEmpty()) {
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textView = (TextView) ((Activity) context).findViewById(R.id.actionBarTextView);
                    textView.setText("Nästa hållplats: " + Model.getInstance().getNextBusStop());
                }
            });
        }
    }

    /**
     * Adds self as an observer in the model
     */
    public void addAsObserver() {
        Model.getInstance().addObserver(this);
    }

    /**
     * Called by the observed object when an update has been made
     *
     * @param observable
     * @param nextBusStop
     */
    @Override
    public void update(Observable observable, final Object nextBusStop) {
        updateNextBusStop();
    }

    /**
     * Closes the chat at the position provided as param
     *
     * @param position
     */

    public void leaveChat(int position) {
        final String key = keys.get(position);
        stopListeningAt(key);

        String ids[] = key.split("!");

        for (int i = 0; i < ids.length; i++) {
            final String uId = ids[i];
            Model.getInstance().getMRef().child("users").child(uId).child("activeChats").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot tempDs = iterator.next();
                        if (tempDs.getValue(String.class).equals(key)) {
                            Model.getInstance().getMRef().child("users").child(uId).child("activeChats").child(tempDs.getKey()).removeValue();
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        messagesFirebaseRef.child(key).removeValue();

        notifyDataSetChanged();
    }
}
