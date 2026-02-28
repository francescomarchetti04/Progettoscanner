package YGOscanner.recognition;

import android.graphics.Bitmap;
import YGOscanner.model.CardResult;

public class CardRecognitionEngine {

    private final ImageNormalizer normalizer = new ImageNormalizer();
    private final ImageHasher hasher = new ImageHasher();

    public CardResult recognize(Bitmap raw) {

        Bitmap normalized = normalizer.normalize(raw);

        String name = "Dark Magician";
        String code = "LOB-005";
        String hash = hasher.hash(normalized);

        return new CardResult(name, code, hash, 94.2);
    }
}