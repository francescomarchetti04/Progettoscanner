package YGOscanner.hardware;

import android.hardware.usb.*;
import android.content.Context;

public class Esp32Manager implements SerialCommunication.SerialListener {

    private final SerialCommunication serialCommunication;
    private Esp32ConnectionListener connectionListener;
    private CheckResultListener checkResultListener;

    public interface CheckResultListener {
        void onResult(Esp32Response response);
    }

    public Esp32Manager() {
        serialCommunication = new SerialCommunication();
        serialCommunication.setListener(this);
    }

    public void setConnectionListener(Esp32ConnectionListener listener) {
        this.connectionListener = listener;
    }

    public void connect(Context context, UsbDevice device, UsbManager manager) {
        boolean success = serialCommunication.connect(device, manager);
        if (success) {
            if (connectionListener != null) connectionListener.onConnected();
        } else {
            if (connectionListener != null) connectionListener.onError("Connection failed");
        }
    }

    public void disconnect() {
        serialCommunication.disconnect();
        if (connectionListener != null) connectionListener.onDisconnected();
    }

    public void checkHash(String hash, CheckResultListener listener) {
        if (!serialCommunication.isConnected()) {
            listener.onResult(new Esp32Response(Esp32Response.Status.NOT_CONNECTED, ""));
            return;
        }

        this.checkResultListener = listener;
        String command = Esp32Protocol.buildCheckCommand(hash);
        serialCommunication.send(command);
    }

    @Override
    public void onDataReceived(String data) {
        Esp32Response response = Esp32Response.parse(data);
        if (checkResultListener != null) {
            checkResultListener.onResult(response);
        }
    }

    @Override
    public void onError(String error) {
        if (connectionListener != null) {
            connectionListener.onError(error);
        }
    }

    public boolean isConnected() {
        return serialCommunication.isConnected();
    }
}