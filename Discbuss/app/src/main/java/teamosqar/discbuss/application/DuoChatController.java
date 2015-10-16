package teamosqar.discbuss.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Firebase;

import teamosqar.discbuss.activities.R;
import teamosqar.discbuss.util.Message;

/**
 * Created by joakim on 2015-10-16.
 */
public class DuoChatController extends ChatController{

    private Context context;

    public DuoChatController(Context context, String chatRoom) {
        super(context, chatRoom);
        this.context = context;
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
    protected void populateView(View view, Message message) {
        String author = message.getAuthor();
        String msg = message.getMessage();

        TextView authorView = (TextView) view.findViewById(R.id.author);
        TextView msgView = (TextView) view.findViewById(R.id.message);

        authorView.setText(author);
        msgView.setText(msg);

    }
}
