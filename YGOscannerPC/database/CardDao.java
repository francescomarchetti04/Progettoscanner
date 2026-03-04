package YGOscannerPC.database;

import YGOscannerPC.model.CardResult;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CardDao {

    public static void insert(CardResult card) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO cards(name, hash, code, timestamp) VALUES(?,?,?,?)")) {

            ps.setString(1, card.getName());
            ps.setString(2, card.getHash());
            ps.setString(3, card.getCode());
            ps.setLong(4, card.getTimestamp());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}