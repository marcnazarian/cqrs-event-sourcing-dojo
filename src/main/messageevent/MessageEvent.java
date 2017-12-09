package main.messageevent;

public abstract class MessageEvent {
    
    private String name;

    MessageEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
