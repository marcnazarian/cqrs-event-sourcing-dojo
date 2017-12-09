package main.messageevent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessageEventStream {

    private final List<MessageEvent> messageEvents = new ArrayList<>();

    public void add(MessageEvent messageEvent) {
        messageEvents.add(messageEvent);
    }

    public Collection<MessageEvent> getMessageEvents() {
        return messageEvents;
    }

}
