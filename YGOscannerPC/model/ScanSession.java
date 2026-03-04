package YGOscannerPC.model;

import java.util.List;

public class ScanSession {

    private long sessionId;
    private List<CardResult> results;

    public List<CardResult> getResults() {
        return results;
    }
}