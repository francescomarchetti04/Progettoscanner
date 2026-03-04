package YGOscanner.recognition;

import android.graphics.Bitmap;

public class ImageNormalizer {

    public Bitmap normalize(Bitmap input) {

        if (input == null) return null;

        return Bitmap.createScaledBitmap(input, 600, 900, true);
    }
}