package main;

import main.messageevent.*;

public class Message {

    private static final int MAX_SIZE_OF_QUACK = 42;
    private static final CharSequence THE_F_WORD = "f*ck";

    private final MessageEventStream history;
    private DecisionProjection decisionProjection;

    public Message(MessageEventStream history) {
        this.history = history;
        this.decisionProjection = new DecisionProjection(history);
    }

    public static Message quack(MessageEventStream history, String author, String content) {
        if (isQuackNotValid(content)) {
            return null;
        }

        history.add(new MessageQuackedEvent(author, content));

        return new Message(history);
    }

    private static boolean isQuackNotValid(String content) {
        return content.isEmpty() || isTooLong(content) || content.contains(THE_F_WORD);
    }

    private static boolean isTooLong(String content) {
        return content.length() > MAX_SIZE_OF_QUACK;
    }

    public void delete(MessageEventStream history, String deleter) {
        if (decisionProjection.isDeleted() || !decisionProjection.getAuthor().equals(deleter)) {
            return;
        }

        MessageDeletedEvent messageDeletedEvent = new MessageDeletedEvent();
        history.add(messageDeletedEvent);
        decisionProjection.apply(messageDeletedEvent);
    }

    public Message reQuack(MessageEventStream history, String reQuackAuthor) {
        if (decisionProjection.getAuthor().equals(reQuackAuthor)) {
            return null;
        }

        String reQuackedContent = "RQ: " + getContent();

        MessageReQuackedEvent messageReQuackedEvent = new MessageReQuackedEvent(reQuackAuthor, decisionProjection.getAuthor(), reQuackedContent);
        history.add(messageReQuackedEvent);
        decisionProjection.apply(messageReQuackedEvent);

        MessageEventStream reQuackedMessageHistory = new MessageEventStream();
        return Message.quack(reQuackedMessageHistory, reQuackAuthor, reQuackedContent);
    }

    public String getAuthor() {
        return decisionProjection.getAuthor();
    }

    public String getContent() {
        return decisionProjection.getContent();
    }

}
