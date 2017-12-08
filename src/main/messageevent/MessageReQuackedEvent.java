package main.messageevent;

public class MessageReQuackedEvent extends MessageEvent {
    
    public static final String EVENT_MESSAGE_REQUACKED = "MessageReQuacked";

    private final String author;
    private final String content;

    public MessageReQuackedEvent(String author, String content) {
        super(EVENT_MESSAGE_REQUACKED);
        this.author = author;
        this.content = content;
    }
}
