import Controllers.PixelatorController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainFX extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXMLFiles/pixelator.fxml"));
        Parent root = loader.load();

        PixelatorController pixelatorController = loader.getController();
        pixelatorController.setStage(stage);

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root, 700,500);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene((scene));
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
