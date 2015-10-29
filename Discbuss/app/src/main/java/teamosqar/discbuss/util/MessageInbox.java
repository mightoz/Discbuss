package teamosqar.discbuss.util;


/**
 * Created by joakim on 2015-10-12.
 */
public class MessageInbox {

    private boolean seenByMe;
    private boolean seenByOther;
    private String latestActivity; //Should contain date/time for latest activity in chat, should this be a string?
    private String otherParticipant;


    private MessageInbox(){}

    public MessageInbox(String latestActivity, boolean seenByMe, boolean seenByOther){
        this.latestActivity = latestActivity;
        this.seenByMe = seenByMe;
        this.seenByOther = seenByOther;
        otherParticipant = "";
    }

    /**
     * Compares two dates
     * @param otherDate
     * @return true if the date of the latest message in the inbox calling the method is before the date of the latest message in the inbox provided as param
     */
    public boolean isBefore(String otherDate){
        String latestActivity_split[] = latestActivity.split("-");
        String otherDate_split[] = otherDate.split("-");

        for(int i = 0; i < latestActivity_split.length; i++){
            if(Integer.parseInt(latestActivity_split[i]) < Integer.parseInt(otherDate_split[i])){
                return true;
            }
        }
        return false;
    }

    /**
     * @return true if the latest message recieved was seen by me
     */
    public boolean isSeenByMe(){
        return seenByMe;
    }

    /**
     * @return true if the latest message sent was seen by the other person in the private chat
     */
    public boolean isSeenByOther(){
        return seenByOther;
    }

    /**
     * @return a string containing date/time for latest activity in chat
     */
    public String getLatestActivity(){
        return latestActivity;
    }

    /**
     * Adds the other user, provided as param, to the private chat
     * @param otherParticipant
     */
    public void setOtherParticipant(String otherParticipant){
        this.otherParticipant = otherParticipant;
    }

    /**
     * @return the other user in the private chat's nickname
     */
    public String getOtherParticipant(){
        return otherParticipant;
    }
}
