package com.openwatchproject.watchfaceconverter;

import com.openwatchproject.watchfaceconverter.model.ClockSkinType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.openwatchproject.watchfaceconverter.JavaFXUtils.*;

public class Controller implements Initializable {
    private final static Logger LOGGER = Logger.getLogger(Controller.class.getName());

    @FXML
    private TextField originalFolderTextField;
    @FXML
    private Button originalFolderBrowseButton;
    @FXML
    private RadioButton stockLauncherRadioButton;
    @FXML
    private ToggleGroup originalLauncherType;
    @FXML
    private RadioButton ericsLauncherRadioButton;
    @FXML
    private TextField outputFolderTextField;
    @FXML
    private Button outputFolderBrowseButton;
    @FXML
    private Button convertButton;
    @FXML
    private TextArea logTextArea;
    @FXML
    private TextField originalWidthTextField;
    @FXML
    private TextField originalHeightTextField;

    private Converter converter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        converter = new Converter(logTextArea);
    }

    @FXML
    private void originalFolderBrowse(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        File chosenDirectory = directoryChooser.showDialog(originalFolderBrowseButton.getScene().getWindow());
        if (chosenDirectory != null) {
            originalFolderTextField.setText(chosenDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void outputFolderBrowse(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        File chosenDirectory = directoryChooser.showDialog(outputFolderBrowseButton.getScene().getWindow());
        if (chosenDirectory != null) {
            outputFolderTextField.setText(chosenDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void convertButton(ActionEvent event) {
        File inputFolder = new File(originalFolderTextField.getText());
        File outputFolder = new File(outputFolderTextField.getText());

        if (!inputFolder.isDirectory()) {
            log(LOGGER, logTextArea, Level.SEVERE, "The original ClockSkin folder you selected is not valid!");
            showError("Wrong directory!", "The original ClockSkin folder you selected is not valid!");
            return;
        }
        if (!outputFolder.isDirectory()) {
            log(LOGGER, logTextArea, Level.SEVERE, "The WatchFace output folder you selected is not valid!");
            showError("Wrong directory!", "The WatchFace output folder you selected is not valid!");
            return;
        }

        ClockSkinType type;
        if (stockLauncherRadioButton.isSelected()) {
            type = ClockSkinType.STOCK;
        } else if (ericsLauncherRadioButton.isSelected()) {
            type = ClockSkinType.ERICS;
        } else {
            showError("Unknown ClockSkin type!", "There was an error while getting the ClockSkin type.");
            return;
        }

        int width = 400;
        int height = 400;
        if (!originalWidthTextField.getText().isEmpty()) {
            width = Integer.parseInt(originalWidthTextField.getText());
        }
        if (!originalHeightTextField.getText().isEmpty()) {
            height = Integer.parseInt(originalHeightTextField.getText());
        }

        String filePath = converter.convert(inputFolder, outputFolder, type, width, height);
        if (filePath == null) {
            log(LOGGER, logTextArea, Level.SEVERE, "A problem ocurred while converting the ClockSkin.");
            showError("A problem ocurred while converting the ClockSkin.", "Make sure you've selected the right original ClockSkin folder and launcher type!");
        }
        log(LOGGER, logTextArea, Level.INFO, "The ClockSkin was successfully converted: " + filePath);
        showInfo("ClockSkin converted successfully!", "WatchFace was saved to: " + filePath);
    }
}
