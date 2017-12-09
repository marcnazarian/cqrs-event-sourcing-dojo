package main;

import main.messageevent.*;

import java.util.ArrayList;
import java.util.List;

class DecisionProjection {

    private boolean isDeleted;
    private String author;
    private String content;
    private List<String> reQuackedBy = new ArrayList<>();
    private int numberOfReQuacks = 0;

    DecisionProjection(MessageEventStream history) {
        
        for(MessageEvent event: history.getMessageEvents()) {
            
            apply(event);
        }
    }

    void apply(MessageEvent event) {
        if (event.getClass().equals(MessageDeletedEvent.class))
        {
            apply((MessageDeletedEvent)event);
        }

        if (event.getClass().equals(MessageQuackedEvent.class))
        {
            apply((MessageQuackedEvent)event);
        }

        if (event.getClass().equals(MessageReQuackedEvent.class))
        {
            apply((MessageReQuackedEvent)event);
        }
    }

    private void apply(MessageDeletedEvent messageDeletedEvent) {
        isDeleted = true;
    }

    private void apply(MessageQuackedEvent messageQuackedEvent) {
        author = messageQuackedEvent.getAuthor();
        content = messageQuackedEvent.getContent();
    }

    private void apply(MessageReQuackedEvent messageReQuackedEvent) {
        numberOfReQuacks++;
        reQuackedBy.add(messageReQuackedEvent.getReQuackedBy());
    }

    boolean isDeleted() {
        return isDeleted;
    }

    String getAuthor() {
        return author;
    }

    String getContent() {
        return content;
    }

    int getNumberOfReQuacks() {
        return numberOfReQuacks;
    }

    Iterable<String> getRequackedBy() {
        return reQuackedBy;
    }
}
