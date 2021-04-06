package Controllers;

import ThreadExcutioner.Executor;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sun.security.validator.ValidatorException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class PixelatorController {

    private Stage stage;
    BufferedImage result = null;

    @FXML
    ImageView beforeImageView;

    @FXML
    ImageView afterImageView;

    @FXML
    TextField numberOfSectorsField;

    @FXML
    Button chooseFileButton;

    @FXML
    Button pixelateButton;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void handleClose() {
        stage.close();
    }

    @FXML
    public void chooseFile()
    {
        try
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("src/main/pictures/"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image file","*.png","*.jpg"));
            File file = fileChooser.showOpenDialog(stage);
            if(file!=null)
            {
                FileInputStream stream = new FileInputStream(file.getAbsoluteFile());
                Image image = new Image(stream);
                if(image.getHeight()<400 || image.getHeight()>1000)
                    throw new ValidatorException("The height should be over 400 and under 1000 pixels!");
                if(image.getWidth()<400 || image.getWidth()>1000)
                    throw new ValidatorException("The width should be over 400 and under 1000 pixels!");
                beforeImageView.setImage(image);
                beforeImageView.setFitWidth(320);
                try {
                    result = ImageIO.read(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.setTitle("Error");
            message.setContentText(e.getMessage());
            message.showAndWait();
        }
        catch (ValidatorException ve)
        {
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.setTitle("Error");
            message.setContentText(ve.getMessage());
            message.showAndWait();
        }
    }

    public void pixelate()
    {
        try
        {
            int numberOfSectors = Integer.parseInt(numberOfSectorsField.getText());
            new Executor(result, numberOfSectors);

            FileInputStream stream = new FileInputStream("image-out.jpg");
            afterImageView.setImage(new Image(stream));
        }
        catch(NumberFormatException nfe)
        {
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.setTitle("Number of sectors error!");
            message.setContentText("Please insert an integer!");
            message.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
