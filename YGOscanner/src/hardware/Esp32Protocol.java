package YGOscanner.hardware;

public class Esp32Protocol {

    private static final String CHECK_PREFIX = "CHECK:";
    private static final String END_LINE = "\n";

    public static String buildCheckCommand(String hash) {
        return CHECK_PREFIX + hash + END_LINE;
    }
}