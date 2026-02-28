package YGOscanner.model;

import java.util.List;

public class ScanSession {

    private final long sessionId;
    private final List<CardResult> results;

    public ScanSession(long sessionId, List<CardResult> results) {
        this.sessionId = sessionId;
        this.results = results;
    }

    public long getSessionId() {
        return sessionId;
    }

    public List<CardResult> getResults() {
        return results;
    }
}
