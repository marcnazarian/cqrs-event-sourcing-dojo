package test;

import main.Message;
import main.messageevent.MessageDeletedEvent;
import main.messageevent.MessageEventStream;
import main.messageevent.MessageQuackedEvent;
import org.junit.Before;
import org.junit.Test;

import static main.messageevent.MessageDeletedEvent.EVENT_MESSAGE_DELETED;
import static main.messageevent.MessageQuackedEvent.EVENT_MESSAGE_QUACKED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MessageShould {

    private MessageEventStream messageEventStream;

    @Before
    public void setUp() {
        messageEventStream = new MessageEventStream();
    }

    @Test
    public void raise_quacked_when_quack() {
        Message message = Message.quack(messageEventStream, "alice","some content");

        assertTrue(messageEventStream.contains(EVENT_MESSAGE_QUACKED));
        assertEquals("alice", message.getAuthor());
        assertEquals("some content", message.getContent() );
    }

    @Test
    public void raise_deleted_when_delete() {
        messageEventStream.add(new MessageQuackedEvent("bob","some other content"));
        Message message = new Message(messageEventStream);
                
        message.delete(messageEventStream);

        assertTrue(messageEventStream.contains(EVENT_MESSAGE_DELETED));
    }

    @Test
    public void not_raise_deleted_when_already_deleted() {
        messageEventStream.add(new MessageQuackedEvent("alice","some content"));
        messageEventStream.add(new MessageDeletedEvent());
        Message message = new Message(messageEventStream);

        message.delete(messageEventStream);

        assertTrue(messageEventStream.containsOnlyOnce(EVENT_MESSAGE_DELETED));
    }

    @Test
    public void not_raise_two_deleted_when_user_delete_twice() {
        messageEventStream.add(new MessageQuackedEvent("alice","some content"));
        Message message = new Message(messageEventStream);

        message.delete(messageEventStream);
        message.delete(messageEventStream);

        assertTrue(messageEventStream.containsOnlyOnce(EVENT_MESSAGE_DELETED));
    }

    @Test
    public void not_quack_a_message_with_empty_content() {
        Message.quack(messageEventStream, "bob","");

        assertFalse(messageEventStream.contains(EVENT_MESSAGE_QUACKED));
    }

    @Test
    public void not_quack_a_message_that_contains_more_than_42_chars() {
        Message.quack(messageEventStream, "carl","abcdefghijklmnopqrstuvwxyz abcdefghijklmnop");

        assertFalse(messageEventStream.contains(EVENT_MESSAGE_QUACKED));
    }

    @Test
    public void not_quack_a_message_that_contains_f_word() {
        Message.quack(messageEventStream, "dude","seriously dude!what is this f*cking stuff?");

        assertFalse(messageEventStream.contains(EVENT_MESSAGE_QUACKED));
    }
}
