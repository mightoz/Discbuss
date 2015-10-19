package teamosqar.discbuss.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import teamosqar.discbuss.application.BusChatController;
import teamosqar.discbuss.application.ChatController;

/**
 * Created by joakim on 2015-10-16.
 */
public class BusChatActivity extends ChatActivity {

    private String roomName;
    private BusChatController chatController;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        roomName = getIntent().getExtras().getString("EXTRA_ROOM");
        chatController = new BusChatController(this, roomName);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart(){
        super.onStart();
        chatController.addAsObserver();
    }

    @Override
    protected ChatController getChatController() {
        return chatController;
    }

    public void upVote(View view){
        ListView lv = getListView();
        int pos = lv.getPositionForView((View)view.getParent().getParent());
        view.setBackgroundResource(R.drawable.arrows_05);
        chatController.upVote(pos);
    }

    public void downVote(View view){
        ListView lv = getListView();
        int pos = lv.getPositionForView((View)view.getParent().getParent());
        Log.d("parent", view.getParent().toString());
        view.setBackgroundResource(R.drawable.arrows_06);
        chatController.downVote(pos);
    }

    public void sendPersonalMessageClicked(View view){
        chatController.personalMessageClicked(getListView().getPositionForView((View) view.getParent().getParent().getParent()));
    }
}
