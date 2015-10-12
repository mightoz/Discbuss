package teamosqar.discbuss.util;

/**
 * Created by joakim on 2015-10-12.
 */
public class MessageInbox {

    private boolean seenByMe;
    private boolean seenByOther;
    private String latestActivity; //Should contain date/time for latest activity in chat, should this be a string?

    public MessageInbox(String latestActivity, boolean seenByMe, boolean seenByOther){
        this.latestActivity = latestActivity;
        this.seenByMe = seenByMe;
        this.seenByOther = seenByOther;
    }

    public boolean isBefore(String otherDate){
        //TODO: test if latestActivity is before otherDate, do we need to store this differently???
        return false;
    }

    public boolean isSeenByMe(){
        return seenByMe;
    }

    public boolean isSeenByOther(){
        return seenByOther;
    }

    public String getLatestActivity(){
        return latestActivity;
    }
}
