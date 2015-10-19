package teamosqar.discbuss.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.Observable;

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
    }

    @Override
    public void onLeftChat() {}

    public void addAsObserver(){
        model.addObserver(this);
    }

    public void removeAsObserver(){
        model.deleteObserver(this);
    }

    @Override
    public void notifyDataSetChanged(){
        setSeenLatestMessage();
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

}
