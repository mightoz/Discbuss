package teamosqar.discbuss.util;

/**
 * Created by joakim on 2015-09-29.
 */
public class Message {

    private String uid;
    private String message;
    private String author;
    private int karma;

    //required default constructor for firebase object mapping
    private Message(){
    }

    public Message(String uid, String message, String author){
        this.uid = uid;
        this.message = message;
        this.author = author;
        karma = 0;
    }

    public Message(String uid, String message, String author, int karma){
        this.uid = uid;
        this.message = message;
        this.author = author;
        this.karma = karma;
    }

    public String getUid(){ return uid; }

    public String getMessage(){
        return message;
    }

    public String getAuthor(){
        return author;
    }

    public int getKarma(){
        return karma;
    }

    public void setKarma(int karma){
        this.karma = karma;
    }
}
