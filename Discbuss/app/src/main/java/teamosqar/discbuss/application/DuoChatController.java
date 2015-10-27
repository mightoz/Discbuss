package teamosqar.discbuss.application;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


import java.util.Calendar;
import java.util.Iterator;

import teamosqar.discbuss.activities.DuoChatActivity;

import teamosqar.discbuss.activities.R;
import teamosqar.discbuss.util.Message;

/**
 * Created by joakim on 2015-10-16.
 */
public class DuoChatController extends ChatController{

    private Context context;
    private Firebase chatRoomRef;
    private Firebase seenByMeRef, seenByOtherRef;
    private Model model;

    public DuoChatController(Context context, String chatRoom) {
        super(context, chatRoom);
        this.context = context;
        this.chatRoomRef = Model.getInstance().getMRef().child("duoChats").child(chatRoom).child("content");
        model = Model.getInstance();


        String chatterUIds[] = chatRoom.split("!");

        for(int i = 0; i < chatterUIds.length; i++) {
            if(chatterUIds[i].equals(Model.getInstance().getUid())) {
                seenByMeRef = this.chatRoomRef.child("inboxInfo").child(chatterUIds[i]);
            }else{
                seenByOtherRef = this.chatRoomRef.child("inboxInfo").child(chatterUIds[i]);
            }
        }
    }

    @Override
    protected void onMessageRecieved() {
        setSeenLatestMessage();
    }


    @Override
    protected Firebase getFirebaseChatRef(String chatRoom) {
        return Model.getInstance().getMRef().child("duoChats").child(chatRoom).child("content").child("chat");
    }

    @Override
    protected View getMessageView(ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.duomessage_chat, parent, false);
    }

    @Override
    protected View getMessageViewExtension() {
        return LayoutInflater.from(context).inflate(R.layout.message_duochat_extension, null);
    }

    @Override
    protected void populateView(View view, Message message, int position) {
        String author = message.getAuthor();
        String msg = message.getMessage();

        TextView authorView = (TextView) view.findViewById(R.id.author);
        TextView msgView = (TextView) view.findViewById(R.id.message);

        authorView.setText(author);
        msgView.setText(msg);

    }

    @Override
    protected void populateViewOnExtension(View view, Message message){}

    @Override
    public void onEnteredChat() {
        setSeenLatestMessage();
        startListener();
    }

    @Override
    public void onLeftChat() {
        shutDownListener();
    }

    public void addAsObserver(){
        model.addObserver(this);
    }

    public void removeAsObserver(){
        model.deleteObserver(this);
    }

    @Override
    public void notifyDataSetChanged(){
        super.notifyDataSetChanged();
    }

    private void setSeenLatestMessage(){
        seenByMeRef.setValue(true);
    }

    private void setNewMessageToSee(){
        seenByOtherRef.setValue(false);
    }

    @Override
    protected void onSentMessage() {
        setNewMessageToSee();
    }

    public static void launchDuoChat(final Context context, final String otherUid){
        if(!otherUid.equals(Model.getInstance().getUid())){
            Model.getInstance().getMRef().child("users").child(Model.getInstance().getUid()).child("activeChats").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterator children = dataSnapshot.getChildren().iterator();
                    boolean foundChat = false;
                    String finalChatRef = "";
                    while (children.hasNext()) {
                        DataSnapshot snap = (DataSnapshot) children.next();
                        String currentChatRef = snap.getValue(String.class);
                        if (currentChatRef.contains(otherUid)) {
                            foundChat = true;
                            finalChatRef = currentChatRef;
                        }
                    }
                    if (!foundChat) {
                        Firebase userRef = Model.getInstance().getMRef().child("users");

                        finalChatRef = otherUid + "!" + Model.getInstance().getUid();
                        userRef.child(Model.getInstance().getUid()).child("activeChats").push().setValue(finalChatRef);
                        userRef.child(otherUid).child("activeChats").push().setValue(finalChatRef);

                        Firebase chatRef = Model.getInstance().getMRef().child("duoChats").child(finalChatRef).child("content");
                        chatRef.child("inboxInfo").child(otherUid).setValue(true);
                        chatRef.child("inboxInfo").child(Model.getInstance().getUid()).setValue(false);
                        Calendar calendar = Calendar.getInstance();
                        chatRef.child("inboxInfo").child("latestActivity").setValue(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.DAY_OF_YEAR) + "-" + calendar.get(Calendar.HOUR_OF_DAY) + "-" + calendar.get(Calendar.MINUTE));

                    }
                    if (!finalChatRef.equals("")) {
                        Intent intent = new Intent(context, DuoChatActivity.class);
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
}
