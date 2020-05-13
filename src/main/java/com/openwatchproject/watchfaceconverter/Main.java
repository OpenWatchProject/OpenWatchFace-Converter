package com.openwatchproject.watchfaceconverter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.openwatchproject.watchfaceconverter.JavaFXUtils.setIcon;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("OpenWatch WatchFace Converter");
        primaryStage.setScene(new Scene(root, 600, 400));
        setIcon(primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
