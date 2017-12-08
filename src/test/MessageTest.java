package test;

import main.Message;
import main.messageevent.MessageEvents;
import org.junit.Before;
import org.junit.Test;

import static main.messageevent.MessageDeletedEvent.EVENT_MESSAGE_DELETED;
import static main.messageevent.MessageQuackedEvent.EVENT_MESSAGE_QUACKED;
import static main.messageevent.MessageReQuackedEvent.EVENT_MESSAGE_REQUACKED;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MessageTest {

    private MessageEvents messageEvents;
    private MessageEvents reQuackedMessageEvents;

    @Before
    public void setUp() {
        messageEvents = new MessageEvents();
        reQuackedMessageEvents = new MessageEvents();
    }

    @Test
    public void should_raise_quacked_when_quack() {
        Message.quack("alice", "some content", messageEvents);

        assertTrue(messageEvents.contains(EVENT_MESSAGE_QUACKED));
    }

    @Test
    public void should_raise_deleted_when_delete() {
        Message message = Message.quack("bob", "some other content", messageEvents);
        message.delete("bob");

        assertTrue(messageEvents.contains(EVENT_MESSAGE_DELETED));
    }

    @Test
    public void should_not_raise_deleted_when_already_deleted() {
        Message message = Message.quack("alice", "some content", messageEvents);
        message.delete("alice");
        message.delete("alice");

        assertTrue(messageEvents.containsOnlyOnce(EVENT_MESSAGE_DELETED));
    }

    @Test
    public void should_be_deleted_by_own_author_only() {
        Message message = Message.quack("alice", "some content", messageEvents);
        message.delete("bob");

        assertFalse(messageEvents.contains(EVENT_MESSAGE_DELETED));
    }

    @Test
    public void should_not_quack_a_message_with_empty_content() {
        Message.quack("charline", "", messageEvents);

        assertFalse(messageEvents.contains(EVENT_MESSAGE_QUACKED));
    }

    @Test
    public void should_raise_re_quacked_when_re_quack() {
        Message message = Message.quack("erik", "some very interesting stuff", messageEvents);
        message.reQuack("fiona", reQuackedMessageEvents);

        assertTrue(messageEvents.contains(EVENT_MESSAGE_REQUACKED));
        assertTrue(reQuackedMessageEvents.contains(EVENT_MESSAGE_QUACKED));
    }

    @Test
    public void should_not_allow_to_re_quacked_your_own_message() {
        Message message = Message.quack("greg", "greg's quack", messageEvents);
        message.reQuack("greg", reQuackedMessageEvents);

        assertFalse(messageEvents.contains(EVENT_MESSAGE_REQUACKED));
        assertFalse(reQuackedMessageEvents.contains(EVENT_MESSAGE_QUACKED));
    }

    @Test
    public void should_not_quack_a_message_that_contains_more_than_42_chars() {
        Message.quack("helen", "abcdefghijklmnopqrstuvwxyz abcdefghijklmnop", messageEvents);

        assertFalse(messageEvents.contains(EVENT_MESSAGE_QUACKED));
    }

    @Test
    public void should_not_quack_a_message_that_contains_f_word() {
        Message.quack("dude", "seriously dude!what is this f*cking stuff?", messageEvents);

        assertFalse(messageEvents.contains(EVENT_MESSAGE_QUACKED));
    }
}
