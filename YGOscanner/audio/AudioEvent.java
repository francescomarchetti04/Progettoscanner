package YGOscanner.audio;


public class AudioEvent {

    public enum Type {
        CARD_IN,
        CARD_STABLE,
        CARD_OUT
    }

    private final Type type;

    public AudioEvent(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}