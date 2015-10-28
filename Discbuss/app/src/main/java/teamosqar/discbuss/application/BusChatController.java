package teamosqar.discbuss.application;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;

import teamosqar.discbuss.activities.R;
import teamosqar.discbuss.util.Message;

/**
 * Created by joakim on 2015-10-16.
 */
public class BusChatController extends ChatController implements Observer{

    private Firebase chatFirebaseRef; //TODO: This is also kept in superclass.. Should we consider other solution? variable needed for performKarmaChange
    private Context context;
    private String chatRoom;
    private Model model;
    private List<Integer> messageValues;

    public BusChatController(Context context, String chatRoom) {
        super(context, chatRoom);
        this.context = context;
        this.chatRoom = chatRoom;
        model = Model.getInstance();
        Firebase activeUserRef = Model.getInstance().getMRef().child("activeUsers").child(chatRoom);
        chatFirebaseRef = getFirebaseChatRef(chatRoom);
        messageValues = new ArrayList<>();


        ValueEventListener activeUserListener = activeUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int connectedUsers = (int) (dataSnapshot.getChildrenCount());
                updateUserCount(connectedUsers);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ValueEventListener UserKarmaListener = Model.getInstance().getMRef().child("users").child(Model.getInstance().getUid()).child("karma").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int currentKarma = ((Long) dataSnapshot.getValue()).intValue();
                updateCurrentKarma(currentKarma);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        chatFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    int tmp;
                    DataSnapshot ds = (DataSnapshot) iterator.next();
                    if (ds.child("usersVoted").hasChild(model.getUid())) {
                        tmp = ((Long) ds.child("usersVoted").child(model.getUid()).getValue()).intValue();
                    } else {
                        tmp = 0;
                    }
                    messageValues.add(getMessageKeys().indexOf(ds.getKey()), tmp);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ValueEventListener statementListener = Model.getInstance().getMRef().child("chatRooms").child("currentStatement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateStatement(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    protected void onMessageReceived() {}

    @Override
    protected Firebase getFirebaseChatRef(String chatRoom) {
        return Model.getInstance().getMRef().child(chatRoom);
    }

    public void onEnteredChat(){
        model.addUserToChat(chatRoom);
    }

    public void onLeftChat(){
        model.removeUserFromChat(chatRoom);
    }

    public void addAsObserver(){
        model.addObserver(this);
    }

    public void removeAsObserver(){
        model.deleteObserver(this);
    }

    private void updateUserCount(int users){
        TextView numUsers = (TextView) ((Activity) context).findViewById(R.id.textViewActiveUsers);
        numUsers.setText(Integer.toString(users));
    }

    
    private void updateCurrentKarma(int karma){
        TextView karmaText = (TextView)((Activity)context).findViewById(R.id.statementKarmaText);
        karmaText.setText(Integer.toString(karma));
    }

    private void updateStatement(String statement){
        TextView statementView = (TextView)((Activity)context).findViewById(R.id.statementText);
        statementView.setText(statement);
    }

    public void upVote(int i){
        performKarmaChange(i, 1);
    }


    public void downVote(int i){
        performKarmaChange(i, -1);
    }

    private void performKarmaChange(final int message, final int change){
        final Firebase messageRef = chatFirebaseRef.child(getMessageKey(message));

        messageRef.child("usersVoted").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean doTransaction = false;
                boolean changeShouldDouble = false;
                if (!getMessageModel(message).getUid().equals(Model.getInstance().getUid())) {
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

                    if (changeShouldDouble) {
                        correctedChange = change * 2;
                    } else {
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
                            getMessageModel(message).setKarma(((Long) dataSnapshot.getValue()).intValue());
                            updateTopStatement(message);
                        }
                    });


                    Firebase userRef = Model.getInstance().getMRef().child("users").child(getMessageModel(message).getUid()).child("karma");

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

    private void updateTopStatement(int message){
        final Message messageModel = getMessageModel(message);
        final String messageKey = getMessageKey(message);
        final Firebase topStatementsRef = Model.getInstance().getMRef().child("users").child(messageModel.getUid()).child("topStatements");

        topStatementsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(messageKey) || dataSnapshot.getChildrenCount() < 3) {
                    setTopStatement(topStatementsRef, messageModel, messageKey);
                } else {
                    Iterator iterator = dataSnapshot.getChildren().iterator();

                    DataSnapshot lowestKarmaSnapshot = (DataSnapshot) iterator.next();
                    while (iterator.hasNext()) {
                        DataSnapshot currentSnapshot = (DataSnapshot) iterator.next();
                        if ((Long) currentSnapshot.child("karma").getValue() < (Long) lowestKarmaSnapshot.child("karma").getValue()) {
                            lowestKarmaSnapshot = currentSnapshot;
                        }
                    }

                    if ((Long) lowestKarmaSnapshot.child("karma").getValue() < messageModel.getKarma()) {
                        topStatementsRef.child(lowestKarmaSnapshot.getKey()).removeValue();
                        setTopStatement(topStatementsRef, messageModel, messageKey);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void setTopStatement(Firebase topStatementsRef, final Message messageModel, String messageKey){
        topStatementsRef.child(messageKey).runTransaction(new Transaction.Handler() {

            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.setValue(messageModel);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    @Override
    protected void populateView(View view, Message message, int position){
        String author = message.getAuthor();
        String msg = message.getMessage();
        int karma = message.getKarma();

        TextView authorView = (TextView) view.findViewById(R.id.author);
        TextView msgView = (TextView) view.findViewById(R.id.message);
        TextView commentKarma = (TextView) view.findViewById(R.id.commentKarma);

        if(message.getUid().equals(Model.getInstance().getUid())){
            view.findViewById(R.id.upVoteBtn).setVisibility(View.GONE);
            view.findViewById(R.id.downVoteBtn).setVisibility(View.GONE);
        }else{
            view.findViewById(R.id.upVoteBtn).setVisibility(View.VISIBLE);
            view.findViewById(R.id.downVoteBtn).setVisibility(View.VISIBLE);
        }
        if(messageValues.get(position) == 0){
            view.findViewById(R.id.upVoteBtn).setBackgroundResource(R.drawable.arrows_02);
            view.findViewById(R.id.downVoteBtn).setBackgroundResource(R.drawable.arrows_03);
        }
        else if(messageValues.get(position) == 1){
            view.findViewById(R.id.upVoteBtn).setBackgroundResource(R.drawable.arrows_05);
            view.findViewById(R.id.downVoteBtn).setBackgroundResource(R.drawable.arrows_03);
        }
        else if(messageValues.get(position) == -1){
            view.findViewById(R.id.upVoteBtn).setBackgroundResource(R.drawable.arrows_02);
            view.findViewById(R.id.downVoteBtn).setBackgroundResource(R.drawable.arrows_06);
        }

        authorView.setText(author);
        msgView.setText(msg);
        commentKarma.setText(Integer.toString(karma));

        //TODO: Make good looking up/down-vote buttons. Downvote should glow red if clicked, upvote should glow green.

    }

    @Override
    protected void populateViewOnExtension(View view, Message message){
        if(message.getUid().equals(Model.getInstance().getUid())){
            view.findViewById(R.id.sendPersonalMessageButton).setVisibility(View.GONE);
        }else{
            view.findViewById(R.id.sendPersonalMessageButton).setVisibility(View.VISIBLE);
        }
    }

    public void personalMessageClicked(int position){
        final String otherUid = getMessageModel(position).getUid();
        DuoChatController.launchDuoChat(context, otherUid);

    }

    @Override
    protected View getMessageView(ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.busmessage_chat, parent, false);
    }

    @Override
    protected View getMessageViewExtension(){
        return LayoutInflater.from(context).inflate(R.layout.message_buschat_extension, null);
    }

    @Override
    protected void onSentMessage() {}
}
