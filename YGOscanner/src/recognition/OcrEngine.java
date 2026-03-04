package YGOscanner.recognition;

import android.graphics.Bitmap;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class OcrEngine {

    public interface OcrCallback {
        void onTextExtracted(String text);
    }

    public void recognizeText(Bitmap bitmap, OcrCallback callback) {

        InputImage image = InputImage.fromBitmap(bitmap, 0);

        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                .process(image)
                .addOnSuccessListener(result -> {
                    callback.onTextExtracted(result.getText());
                })
                .addOnFailureListener(e -> {
                    callback.onTextExtracted("");
                });
    }
}