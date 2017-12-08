package main;

import main.messageevent.*;

public class Message {

    private static final int MAX_SIZE_OF_QUACK = 42;
    private static final CharSequence THE_F_WORD = "f*ck";
    
    private final String author;
    private final String content;
    private final MessageEvents messageEvents;

    private Message(String author, String content, MessageEvents messageEvents) {
        this.author = author;
        this.content = content;
        this.messageEvents = messageEvents;
    }

    public static Message quack(String author, String content, MessageEvents messageEvents) {
        if (isQuackNotValid(content)) {
            return null;
        }

        Message message = new Message(author, content, messageEvents);
        message.raiseEvent(new MessageQuackedEvent(author, content));
        return message;
    }

    private static boolean isQuackNotValid(String content) {
        return content.isEmpty() || isTooLong(content) || content.contains(THE_F_WORD);
    }

    private static boolean isTooLong(String content) {
        return content.length() > MAX_SIZE_OF_QUACK;
    }

    private void raiseEvent(MessageEvent messageEvent) {
        messageEvents.add(messageEvent);
    }

    public void delete(String deletedBy) {
        if (hasBeenDeleted() || isDeletedBySomeoneElseThanAuthor(deletedBy)) {
            return;
        }
        raiseEvent(new MessageDeletedEvent(deletedBy));
    }

    private boolean hasBeenDeleted() {
        return messageEvents.contains(MessageDeletedEvent.EVENT_MESSAGE_DELETED);
    }

    private boolean isDeletedBySomeoneElseThanAuthor(String deletedBy) {
        return !author.equals(deletedBy);
    }

    public Message reQuack(String author, MessageEvents messageEvents) {
        if (this.author.equals(author)) {
            return null;
        }
        
        String reQuackedContent = "RQ: " + this.content;
        Message reQuackedMessage = quack(author, reQuackedContent, messageEvents);

        this.raiseEvent(new MessageReQuackedEvent(this.author, this.content));
        
        return reQuackedMessage;
    }
}
