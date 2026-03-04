package YGOscanner.camera;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import YGOscanner.model.CardResult;
import YGOscanner.recognition.CardRecognitionEngine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraModule {

    private final Context context;
    private final LifecycleOwner owner;
    private final OnResultListener listener;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private ImageCapture imageCapture;
    private final CardRecognitionEngine recognitionEngine = new CardRecognitionEngine();

    public interface OnResultListener {
        void onRecognized(CardResult result);
    }

    public CameraModule(Context context, LifecycleOwner owner, OnResultListener listener) {
        this.context = context;
        this.owner = owner;
        this.listener = listener;
        startCamera();
    }

    private void startCamera() {

        ListenableFuture<ProcessCameraProvider> provider =
                ProcessCameraProvider.getInstance(context);

        provider.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = provider.get();

                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                CameraSelector selector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(owner, selector, imageCapture);

            } catch (Exception ignored) {}

        }, ContextCompat.getMainExecutor(context));
    }

    public void capture() {

        imageCapture.takePicture(executor,
                new ImageCapture.OnImageCapturedCallback() {

                    @Override
                    public void onCaptureSuccess(ImageProxy image) {

                        Bitmap bitmap = ImageUtils.imageProxyToBitmap(image);
                        CardResult result = recognitionEngine.recognize(bitmap);

                        listener.onRecognized(result);
                        image.close();
                    }
                });
    }

    public void shutdown() {
        executor.shutdown();
    }
}