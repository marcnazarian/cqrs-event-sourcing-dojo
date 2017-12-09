package test;

import main.Message;
import main.messageevent.MessageDeletedEvent;
import main.messageevent.MessageEventStream;
import main.messageevent.MessageQuackedEvent;
import org.junit.Before;
import org.junit.Test;

import static main.messageevent.MessageDeletedEvent.EVENT_MESSAGE_DELETED;
import static main.messageevent.MessageQuackedEvent.EVENT_MESSAGE_QUACKED;
import static main.messageevent.MessageReQuackedEvent.EVENT_MESSAGE_REQUACKED;
import static org.junit.Assert.*;

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
                
        message.delete(messageEventStream, "bob");

        assertTrue(messageEventStream.contains(EVENT_MESSAGE_DELETED));
    }

    @Test
    public void not_raise_deleted_when_already_deleted() {
        messageEventStream.add(new MessageQuackedEvent("alice","some content"));
        messageEventStream.add(new MessageDeletedEvent());
        Message message = new Message(messageEventStream);

        message.delete(messageEventStream, "alice");

        assertTrue(messageEventStream.containsOnlyOnce(EVENT_MESSAGE_DELETED));
    }

    @Test
    public void not_raise_two_deleted_when_user_delete_twice() {
        messageEventStream.add(new MessageQuackedEvent("alice","some content"));
        Message message = new Message(messageEventStream);

        message.delete(messageEventStream, "alice");
        message.delete(messageEventStream, "alice");

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

    @Test
    public void not_allow_delete_when_deleter_is_not_message_author() {
        messageEventStream.add(new MessageQuackedEvent("alice","some content"));
        Message message = new Message(messageEventStream);

        message.delete(messageEventStream, "bob");

        assertFalse(messageEventStream.contains(EVENT_MESSAGE_DELETED));
    }

    @Test
    public void raise_re_quacked_when_re_quack() {
        messageEventStream.add(new MessageQuackedEvent("elena","some awesome content"));
        Message message = new Message(messageEventStream);

        Message reQuackedMessage = message.reQuack(messageEventStream, "franck");

        assertTrue(messageEventStream.contains(EVENT_MESSAGE_REQUACKED));
        assertEquals("franck", reQuackedMessage.getAuthor());
        assertEquals("RQ: some awesome content", reQuackedMessage.getContent());
    }

    @Test
    public void not_allow_re_quack_of_your_own_messages() {
        messageEventStream.add(new MessageQuackedEvent("god","my ultimate message"));
        Message message = new Message(messageEventStream);

        message.reQuack(messageEventStream, "god");

        assertFalse(messageEventStream.contains(EVENT_MESSAGE_REQUACKED));
    }
}
