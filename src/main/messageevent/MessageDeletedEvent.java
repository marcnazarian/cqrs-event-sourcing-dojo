package main.messageevent;

public class MessageDeletedEvent extends MessageEvent {

    public static final String EVENT_MESSAGE_DELETED = "MessageDeleted";

    public MessageDeletedEvent() {
        super(EVENT_MESSAGE_DELETED);
    }
}
