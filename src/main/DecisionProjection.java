package main;

import main.messageevent.MessageDeletedEvent;
import main.messageevent.MessageEvent;
import main.messageevent.MessageEventStream;
import main.messageevent.MessageQuackedEvent;

class DecisionProjection {

    private boolean isDeleted;
    private String author;
    private String content;

    DecisionProjection(MessageEventStream history) {
        
        for(MessageEvent event: history.getMessageEvents()) {
            
            if (event.getClass().equals(MessageDeletedEvent.class))
            {
                apply((MessageDeletedEvent)event);
            }

            if (event.getClass().equals(MessageQuackedEvent.class))
            {
                apply((MessageQuackedEvent)event);
            }
        }
    }

    void apply(MessageDeletedEvent messageDeletedEvent) {
        isDeleted = true;
    }

    private void apply(MessageQuackedEvent messageQuackedEvent) {
        author = messageQuackedEvent.getAuthor();
        content = messageQuackedEvent.getContent();
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
}
