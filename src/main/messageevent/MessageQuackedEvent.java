package main.messageevent;

public class MessageQuackedEvent extends MessageEvent {

    public static final String EVENT_MESSAGE_QUACKED = "MessageQuacked";

    private final String author;
    private final String content;
    
    public MessageQuackedEvent(String author, String content) {
        super(EVENT_MESSAGE_QUACKED);
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
