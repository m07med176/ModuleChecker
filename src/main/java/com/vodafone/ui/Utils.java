package com.vodafone.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class Utils {
    public static DirectoryChooser directoryChooser(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");
        directoryChooser.setInitialDirectory(new File("D:\\AnaVodafone-Android\\app\\src\\main\\java\\vodafone\\vis\\engezly"));
        return directoryChooser;
    }
    public static Stage createLoadingDialog() {
        Stage loadingDialog = new Stage();
        loadingDialog.setTitle("Loading");

        VBox vbox = new VBox(new Label("Please wait..."));
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        loadingDialog.setScene(new Scene(vbox));
        loadingDialog.setWidth(600);
        loadingDialog.setHeight(200);

        return loadingDialog;
    }

    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
