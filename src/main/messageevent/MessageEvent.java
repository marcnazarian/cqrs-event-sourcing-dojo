package main.messageevent;

public abstract class MessageEvent {
    
    private String name;

    public MessageEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
