package YGOscannerPC.ui;


import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        statusLabel.setText("Sistema attivo - In attesa di sincronizzazione");
    }
}    

