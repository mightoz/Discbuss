package teamosqar.discbuss.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import teamosqar.discbuss.application.BusChatController;
import teamosqar.discbuss.application.ChatController;

/**
 * Created by joakim on 2015-10-16.
 */
public class BusChatActivity extends ChatActivity {

    private BusChatController chatController;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        String roomName = getIntent().getExtras().getString("EXTRA_ROOM");
        chatController = new BusChatController(this, roomName);
        super.onCreate(savedInstanceState);

        ViewGroup layout = (ViewGroup) findViewById(R.id.actionBarPlaceholder);
        View actionBarLayout = getLayoutInflater().inflate(
                R.layout.statement_bar_layout,
                null);
        layout.addView(actionBarLayout, -1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        findViewById(R.id.statementText).setSelected(true);
    }

    /**
     * Method to return the correct instance of the activity's ChatController
     * @return the chatActivitys specific chatController.
     */
    
    protected ChatController getChatController() {
        return chatController;
    }

    /**
     * Method to upvote a message, called by the clicked button and providing a view to ensure the correct comment is upvoted.
     */
    public void upVote(View view){
        ListView lv = (ListView)findViewById(R.id.myList);
        int pos = lv.getPositionForView((View)view.getParent().getParent());
        view.setBackgroundResource(R.drawable.arrows_05);
        chatController.upVote(pos);
    }
    /**
     * Method to downvote a message, called by the clicked button and providing a view to ensure the correct message is downvoted.
     */

    public void downVote(View view){
        ListView lv = (ListView)findViewById(R.id.myList);
        int pos = lv.getPositionForView((View)view.getParent().getParent());
        view.setBackgroundResource(R.drawable.arrows_06);
        chatController.downVote(pos);
    }

    /**
     * Method to start a private chat with the author of the message, provides a view to ensure the correct message author.
     */
    public void sendPersonalMessageClicked(View view){
        chatController.personalMessageClicked(((ListView)findViewById(R.id.myList)).getPositionForView((View) view.getParent().getParent().getParent()));
    }
}
