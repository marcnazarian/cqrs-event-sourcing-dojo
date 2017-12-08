package main.messageevent;

public class MessageDeletedEvent extends MessageEvent {

    public static final String EVENT_MESSAGE_DELETED = "MessageDeleted";

    private final String deletedBy;

    public MessageDeletedEvent(String deletedBy) {
        super(EVENT_MESSAGE_DELETED);
        this.deletedBy = deletedBy;
    }
}
