package test;

import main.Message;
import main.messageevent.*;
import org.junit.Before;
import org.junit.Test;

import static main.messageevent.MessageDeletedEvent.EVENT_MESSAGE_DELETED;
import static main.messageevent.MessageQuackedEvent.EVENT_MESSAGE_QUACKED;
import static main.messageevent.MessageReQuackedEvent.EVENT_MESSAGE_REQUACKED;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageShould {

    private MessageEventStream messageEventStream;

    @Before
    public void setUp() {
        messageEventStream = new MessageEventStream();
    }

    @Test
    public void raise_quacked_when_quack() {
        Message message = Message.quack(messageEventStream, "alice", "some content");

        assertHistoryContains(EVENT_MESSAGE_QUACKED);

        assertThat(message).isNotNull();
        assertThat(message.getAuthor()).isEqualTo("alice");
        assertThat(message.getContent()).isEqualTo("some content");
    }

    @Test
    public void raise_deleted_when_delete() {
        messageEventStream.add(new MessageQuackedEvent("bob", "some other content"));
        Message message = new Message(messageEventStream);

        message.delete(messageEventStream, "bob");

        assertHistoryContains(EVENT_MESSAGE_DELETED);
    }

    @Test
    public void not_raise_deleted_when_already_deleted() {
        messageEventStream.add(new MessageQuackedEvent("alice", "some content"));
        messageEventStream.add(new MessageDeletedEvent());
        Message message = new Message(messageEventStream);

        message.delete(messageEventStream, "alice");

        assertHistoryContainsOnlyOnce(EVENT_MESSAGE_DELETED);
    }

    @Test
    public void not_raise_two_deleted_when_user_delete_twice() {
        messageEventStream.add(new MessageQuackedEvent("alice", "some content"));
        Message message = new Message(messageEventStream);

        message.delete(messageEventStream, "alice");
        message.delete(messageEventStream, "alice");

        assertHistoryContainsOnlyOnce(EVENT_MESSAGE_DELETED);
    }

    @Test
    public void not_quack_a_message_with_empty_content() {
        Message.quack(messageEventStream, "bob", "");

        assertHistoryDoesNotContain(EVENT_MESSAGE_QUACKED);
    }

    @Test
    public void not_quack_a_message_that_contains_more_than_42_chars() {
        Message.quack(messageEventStream, "carl", "abcdefghijklmnopqrstuvwxyz abcdefghijklmnop");

        assertHistoryDoesNotContain(EVENT_MESSAGE_QUACKED);
    }

    @Test
    public void not_quack_a_message_that_contains_f_word() {
        Message.quack(messageEventStream, "dude", "seriously dude!what is this f*cking stuff?");

        assertHistoryDoesNotContain(EVENT_MESSAGE_QUACKED);
    }

    @Test
    public void not_allow_delete_when_deleter_is_not_message_author() {
        messageEventStream.add(new MessageQuackedEvent("alice", "some content"));
        Message message = new Message(messageEventStream);

        message.delete(messageEventStream, "bob");

        assertHistoryDoesNotContain(EVENT_MESSAGE_DELETED);
    }

    @Test
    public void raise_re_quacked_when_re_quack() {
        messageEventStream.add(new MessageQuackedEvent("elena", "some awesome content"));
        Message message = new Message(messageEventStream);

        Message reQuackedMessage = message.reQuack(messageEventStream, "franck");

        assertHistoryContains(EVENT_MESSAGE_REQUACKED);
        assertThat(reQuackedMessage.getAuthor()).isEqualTo("franck");
        assertThat(reQuackedMessage.getContent()).isEqualTo("RQ: some awesome content");
    }

    @Test
    public void not_allow_re_quack_of_your_own_messages() {
        messageEventStream.add(new MessageQuackedEvent("god", "my ultimate message"));
        Message message = new Message(messageEventStream);

        message.reQuack(messageEventStream, "god");

        assertHistoryDoesNotContain(EVENT_MESSAGE_REQUACKED);
    }

    @Test
    public void increment_the_number_of_re_quacks_and_update_list_of_re_quackers_when_requack() {
        messageEventStream.add(new MessageQuackedEvent("helen", "super inspiring message"));
        messageEventStream.add(new MessageReQuackedEvent("ian"));
        messageEventStream.add(new MessageReQuackedEvent("jane"));
        messageEventStream.add(new MessageReQuackedEvent("ken"));
        Message message = new Message(messageEventStream);

        message.reQuack(messageEventStream, "laura");

        assertThat(messageEventStream.getMessageEvents()).extracting("name").containsExactly(EVENT_MESSAGE_QUACKED, EVENT_MESSAGE_REQUACKED, EVENT_MESSAGE_REQUACKED, EVENT_MESSAGE_REQUACKED, EVENT_MESSAGE_REQUACKED);
        assertThat(message.getNumberOfReQuacks()).isEqualTo(4);
        assertThat(message.getReQuackedBy()).containsExactly("ian", "jane", "ken", "laura");
    }

    private void assertHistoryContains(String eventName) {
        assertThat(messageEventStream.getMessageEvents()).extracting("name").contains(eventName);
    }

    private void assertHistoryDoesNotContain(String eventName) {
        assertThat(messageEventStream.getMessageEvents()).extracting("name").doesNotContain(eventName);
    }

    private void assertHistoryContainsOnlyOnce(String eventName) {
        assertThat(messageEventStream.getMessageEvents()).extracting("name").containsOnlyOnce(eventName);
    }

}
