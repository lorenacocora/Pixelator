package Controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class PixelatorController {

    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    public void handleClose() {
        stage.close();
    }
}