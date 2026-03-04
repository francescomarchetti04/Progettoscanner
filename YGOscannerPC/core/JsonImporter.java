package YGOscannerPC.core;


import com.google.gson.Gson;
import YGOscannerPC.database.CardDao;
import YGOscannerPC.model.ScanSession;

import java.io.FileReader;

public class JsonImporter {

    public static void importFile(String path) {
        try {
            Gson gson = new Gson();
            ScanSession session = gson.fromJson(new FileReader(path), ScanSession.class);

            session.getResults().forEach(CardDao::insert);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}