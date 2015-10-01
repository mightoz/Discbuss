package teamosqar.discbuss.util;

/**
 * Created by joakim on 2015-09-29.
 */
public class Message {

    private String message;
    private String author;
    private int karma;

    //required default constructor for firebase object mapping
    private Message(){
    }

    public Message(String message, String author){
        this.message = message;
        this.author = author;
        karma = 0;
    }

    public Message(String message, String author, int karma){
        this.message = message;
        this.author = author;
        this.karma = karma;
    }

    public String getMessage(){
        return message;
    }

    public String getAuthor(){
        return author;
    }

    public int getKarma(){
        return karma;
    }


    //sync with firebase??
    public void upVote(){
        karma++;
    }

    public void downVote(){
        karma--;
    }
}
