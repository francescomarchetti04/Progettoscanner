package YGOscanner.hardware;

public class Esp32Response {

    public enum Status {
        NEW,
        DUPLICATE,
        ERROR,
        NOT_CONNECTED
    }

    private final Status status;
    private final String rawMessage;

    public Esp32Response(Status status, String rawMessage) {
        this.status = status;
        this.rawMessage = rawMessage;
    }

    public Status getStatus() {
        return status;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public static Esp32Response parse(String message) {
        if (message == null) {
            return new Esp32Response(Status.ERROR, "NULL");
        }

        message = message.trim();

        switch (message) {
            case "NEW":
                return new Esp32Response(Status.NEW, message);
            case "DUPLICATE":
                return new Esp32Response(Status.DUPLICATE, message);
            default:
                return new Esp32Response(Status.ERROR, message);
        }
    }
}