package player.shellvoice.camera6.event;

public class XEvent {

    public final Object[] message;

    public XEvent(Object... message) {
        this.message = message;
    }

    public Object[] getMessage() {
        return message;
    }
}
