package main.messageevent;

public class MessageReQuackedEvent extends MessageEvent {

    public static final String EVENT_MESSAGE_REQUACKED = "MessageReQuacked";

    private final String reQuackedBy;

    public MessageReQuackedEvent(String reQuackedBy) {
        super(EVENT_MESSAGE_REQUACKED);
        this.reQuackedBy = reQuackedBy;
    }

    public String getReQuackedBy() {
        return reQuackedBy;
    }
}
