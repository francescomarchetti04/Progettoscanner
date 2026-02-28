package YGOscanner.model;


public class CardResult {

    private final String name;
    private final String code;
    private final String hash;
    private final double confidence;
    private boolean synced = false;

    public CardResult(String name, String code, String hash, double confidence) {
        this.name = name;
        this.code = code;
        this.hash = hash;
        this.confidence = confidence;
    }

    public String getName() { return name; }
    public String getCode() { return code; }
    public String getHash() { return hash; }
    public double getConfidence() { return confidence; }
    public boolean isSynced() { return synced; }
    public void setSynced(boolean synced) { this.synced = synced; }
}