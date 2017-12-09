package main.messageevent;

import java.util.ArrayList;
import java.util.List;

public class MessageEventStream {

    private final List<MessageEvent> messageEvents = new ArrayList<>();

    public void add(MessageEvent messageEvent) {
        messageEvents.add(messageEvent);
    }

    public List<MessageEvent> getMessageEvents() {
        return messageEvents;
    }
    
    public boolean contains(String eventName) {
        return getNumberOfEventsNamed(eventName) >= 1;
    }

    public boolean containsOnlyOnce(String eventName) {
        return getNumberOfEventsNamed(eventName) == 1;
    }

    private long getNumberOfEventsNamed(String eventName) {
        return messageEvents.stream().filter(e->e.getName().equals(eventName)).count();
    }
    
}