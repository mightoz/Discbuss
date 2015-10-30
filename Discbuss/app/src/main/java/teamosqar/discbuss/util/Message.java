package teamosqar.discbuss.util;

/**
 * Created by joakim on 2015-09-29.
 */
public class Message implements Comparable<Message> {

    private String uid;
    private String message;
    private String author;
    private int karma;

    /**
     * Compares the message calling the method to the method provided as param based on karma
     *
     * @param other
     * @return 1 if the message calling the method is superior to the message provided as param in terms of current karma, 0 if equal, -1 if less.
     */
    public int compareTo(Message other) {
        int result = 0;
        result = Integer.compare(karma, other.getKarma());
        return result;
    }

    private Message() {
        //required default constructor for firebase object mapping
    }

    public Message(String uid, String message, String author) {
        this.uid = uid;
        this.message = message;
        this.author = author;
        karma = 0;
    }

    public Message(String uid, String message, String author, int karma) {
        this.uid = uid;
        this.message = message;
        this.author = author;
        this.karma = karma;
    }

    /**
     * @return the UID of the author of the message
     */
    public String getUid() {
        return uid;
    }

    /**
     * @return the message itself
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the author of the message
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @return the karma of the message
     */
    public int getKarma() {
        return karma;
    }

    /**
     * Sets the karma of the message based on the input param
     *
     * @param karma
     */
    public void setKarma(int karma) {
        this.karma = karma;
    }
}
