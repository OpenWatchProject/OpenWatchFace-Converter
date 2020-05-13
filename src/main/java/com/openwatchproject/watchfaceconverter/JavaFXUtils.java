package com.openwatchproject.watchfaceconverter;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaFXUtils {
    public static final Image ICON = new Image(JavaFXUtils.class.getResourceAsStream("/icon.png"));

    public static void showInfo(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(text);
        setIcon((Stage) alert.getDialogPane().getScene().getWindow());
        alert.show();
    }

    public static void showError(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(text);
        setIcon((Stage) alert.getDialogPane().getScene().getWindow());
        alert.show();
    }

    public static void log(Logger logger, TextArea textArea, Level level, String text) {
        logger.log(level, text);

        if (!textArea.getText().isEmpty()) {
            textArea.appendText("\n");
        }
        if (level == Level.INFO){
            textArea.appendText("[INFO] " + text);
        } else if (level == Level.WARNING) {
            textArea.appendText("[WARNING] " + text);
        } else if (level == Level.SEVERE) {
            textArea.appendText("[ERROR] " + text);
        }
    }

    public static void setIcon(Stage stage) {
        stage.getIcons().add(ICON);
    }
}
