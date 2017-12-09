package main.messageevent;

public class MessageReQuackedEvent extends MessageEvent {

    public static final String EVENT_MESSAGE_REQUACKED = "MessageReQuacked";

    public MessageReQuackedEvent(String reQuacker, String author, String content) {
        super(EVENT_MESSAGE_REQUACKED);
    }
}
