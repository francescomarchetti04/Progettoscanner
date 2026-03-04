package YGOscanner.hardware;

public interface Esp32ConnectionListener {

    void onConnected();

    void onDisconnected();

    void onError(String error);
}
