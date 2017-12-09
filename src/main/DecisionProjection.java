package main;

import main.messageevent.*;

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

            if (event.getClass().equals(MessageReQuackedEvent.class))
            {
                apply((MessageReQuackedEvent)event);
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

    void apply(MessageReQuackedEvent messageReQuackedEvent) {
        
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
