package YGOscanner.hardware;

import android.hardware.usb.*;
import android.content.Context;

import java.io.IOException;

public class SerialCommunication {

    private UsbSerialPortWrapper portWrapper;
    private boolean isConnected = false;

    public interface SerialListener {
        void onDataReceived(String data);
        void onError(String error);
    }

    private SerialListener listener;

    public void setListener(SerialListener listener) {
        this.listener = listener;
    }

    public boolean connect(UsbDevice device, UsbManager manager) {
        try {
            portWrapper = new UsbSerialPortWrapper(device, manager);
            portWrapper.open();
            isConnected = true;
            startReading();
            return true;
        } catch (Exception e) {
            if (listener != null) listener.onError(e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        try {
            if (portWrapper != null) {
                portWrapper.close();
            }
        } catch (IOException ignored) {}
        isConnected = false;
    }

    public void send(String data) {
        if (!isConnected) return;

        try {
            portWrapper.write(data.getBytes());
        } catch (IOException e) {
            if (listener != null) listener.onError(e.getMessage());
        }
    }

    private void startReading() {
        new Thread(() -> {
            while (isConnected) {
                try {
                    String data = portWrapper.read();
                    if (data != null && listener != null) {
                        listener.onDataReceived(data);
                    }
                } catch (Exception e) {
                    if (listener != null) listener.onError(e.getMessage());
                }
            }
        }).start();
    }

    public boolean isConnected() {
        return isConnected;
    }
}