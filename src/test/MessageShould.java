package test;

import main.Message;
import main.messageevent.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

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

        assertContains(messageEventStream, EVENT_MESSAGE_QUACKED);

        assertNotNull(message);
        assertEquals("alice", message.getAuthor());
        assertEquals("some content", message.getContent() );
    }

    @Test
    public void raise_deleted_when_delete() {
        messageEventStream.add(new MessageQuackedEvent("bob","some other content"));
        Message message = new Message(messageEventStream);
                
        message.delete(messageEventStream, "bob");

        assertContains(messageEventStream, EVENT_MESSAGE_DELETED);
    }

    @Test
    public void not_raise_deleted_when_already_deleted() {
        messageEventStream.add(new MessageQuackedEvent("alice","some content"));
        messageEventStream.add(new MessageDeletedEvent());
        Message message = new Message(messageEventStream);

        message.delete(messageEventStream, "alice");

        assertContainsOnlyOne(messageEventStream, EVENT_MESSAGE_DELETED);
    }

    @Test
    public void not_raise_two_deleted_when_user_delete_twice() {
        messageEventStream.add(new MessageQuackedEvent("alice","some content"));
        Message message = new Message(messageEventStream);

        message.delete(messageEventStream, "alice");
        message.delete(messageEventStream, "alice");

        assertContainsOnlyOne(messageEventStream, EVENT_MESSAGE_DELETED);
    }

    @Test
    public void not_quack_a_message_with_empty_content() {
        Message.quack(messageEventStream, "bob","");

        assertDoesNotContain(messageEventStream, EVENT_MESSAGE_QUACKED);
    }

    @Test
    public void not_quack_a_message_that_contains_more_than_42_chars() {
        Message.quack(messageEventStream, "carl","abcdefghijklmnopqrstuvwxyz abcdefghijklmnop");

        assertDoesNotContain(messageEventStream, EVENT_MESSAGE_QUACKED);
    }

    @Test
    public void not_quack_a_message_that_contains_f_word() {
        Message.quack(messageEventStream, "dude","seriously dude!what is this f*cking stuff?");

        assertDoesNotContain(messageEventStream, EVENT_MESSAGE_QUACKED);
    }

    @Test
    public void not_allow_delete_when_deleter_is_not_message_author() {
        messageEventStream.add(new MessageQuackedEvent("alice","some content"));
        Message message = new Message(messageEventStream);

        message.delete(messageEventStream, "bob");

        assertDoesNotContain(messageEventStream, EVENT_MESSAGE_DELETED);
    }

    @Test
    public void raise_re_quacked_when_re_quack() {
        messageEventStream.add(new MessageQuackedEvent("elena","some awesome content"));
        Message message = new Message(messageEventStream);

        Message reQuackedMessage = message.reQuack(messageEventStream, "franck");

        assertContains(messageEventStream, EVENT_MESSAGE_REQUACKED);
        assertEquals("franck", reQuackedMessage.getAuthor());
        assertEquals("RQ: some awesome content", reQuackedMessage.getContent());
    }

    @Test
    public void not_allow_re_quack_of_your_own_messages() {
        messageEventStream.add(new MessageQuackedEvent("god","my ultimate message"));
        Message message = new Message(messageEventStream);

        message.reQuack(messageEventStream, "god");

        assertDoesNotContain(messageEventStream, EVENT_MESSAGE_REQUACKED);
    }

    @Test
    public void increment_the_number_of_re_quacks_and_update_list_of_re_quackers_when_requack() {
        messageEventStream.add(new MessageQuackedEvent("helen","super inspiring message"));
        messageEventStream.add(new MessageReQuackedEvent("ian"));
        messageEventStream.add(new MessageReQuackedEvent("jane"));
        messageEventStream.add(new MessageReQuackedEvent("ken"));
        Message message = new Message(messageEventStream);

        message.reQuack(messageEventStream, "laura");

        assertContains(messageEventStream, EVENT_MESSAGE_REQUACKED, 4);
        assertEquals(4, message.getNumberOfReQuacks());
        assertEquals(Arrays.asList("ian", "jane", "ken", "laura"), message.getReQuackedBy());
    }

    private static void assertContains(MessageEventStream messageEventStream, String eventName) {
        assertTrue(getNumberOfEventsNamed(messageEventStream.getMessageEvents(), eventName) >= 1);
    }

    private void assertContainsOnlyOne(MessageEventStream messageEventStream, String eventName) {
        assertContains(messageEventStream, eventName, 1);
    }

    private static void assertContains(MessageEventStream messageEventStream, String eventName, int nbOccurrences) {
        assertEquals(nbOccurrences, getNumberOfEventsNamed(messageEventStream.getMessageEvents(), eventName));
    }

    private static void assertDoesNotContain(MessageEventStream messageEventStream, String eventName) {
        assertEquals(0, getNumberOfEventsNamed(messageEventStream.getMessageEvents(), eventName));
    }

    private static long getNumberOfEventsNamed(Collection<MessageEvent> messageEvents, String eventName) {
        return messageEvents.stream().filter(e->e.getName().equals(eventName)).count();
    }
}
