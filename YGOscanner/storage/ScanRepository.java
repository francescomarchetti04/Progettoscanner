package YGOscanner.storage;

package com.ygoscanner.storage;

import android.content.Context;

import com.google.gson.Gson;
import YGOscanner.model.CardResult;
import YGOscanner.model.ScanSession;

import java.util.ArrayList;
import java.util.List;

public class ScanRepository {

    private final LocalStorageManager storageManager;
    private final List<CardResult> currentSessionResults = new ArrayList<>();

    public ScanRepository(Context context) {
        this.storageManager = new LocalStorageManager(context);
    }

    public void addResult(CardResult result) {
        currentSessionResults.add(result);
    }

    public void saveSession() {
        try {
            ScanSession session = new ScanSession(System.currentTimeMillis(), currentSessionResults);
            String json = new Gson().toJson(session);
            storageManager.saveJson(json, "session_" + session.getSessionId() + ".json");
            currentSessionResults.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}