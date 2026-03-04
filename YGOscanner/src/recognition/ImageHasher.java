package YGOscanner.recognition;

import android.graphics.Bitmap;
import java.security.MessageDigest;
import java.io.ByteArrayOutputStream;

public class ImageHasher {

    public String hash(Bitmap bitmap) {

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(stream.toByteArray());

            StringBuilder sb = new StringBuilder();
            for (byte b : digest)
                sb.append(String.format("%02x", b));

            return sb.toString();

        } catch (Exception e) {
            return null;
        }
    }
}