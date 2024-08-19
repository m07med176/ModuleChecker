package com.vodafone.ui;

import com.vodafone.data.RepositoryImpl;
import com.vodafone.data.model.Dependencies;
import com.vodafone.data.model.FileModel;
import com.vodafone.data.Repository;
import com.vodafone.data.model.ResourceType;
import com.vodafone.data.model.Resources;
import com.vodafone.ui.components.Tables;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class HomeController {
    @FXML private VBox root;

    @FXML private Label selectedPathLabel;

    @FXML private VBox resultHolder;
    @FXML private Label javaCount;
    @FXML private Label kotlinCount;
    @FXML private Label allDependenciesCount;
    @FXML private Label allDependenciesInAppCount;
    @FXML private Label xmlLayoutCount;
    @FXML private Label resourceCount;
    private ListView<String> listView;
    private Repository repository;
    private TableView tableOfResources;
    private TableView tableOfDependencies;

    @FXML
    public void initialize() {
        listView = new ListView<>();
        tableOfResources = new Tables().tableOfResources();
        tableOfDependencies = new Tables().tableOfDependencies();
    }





    private void fetchData(String directoryPath){
        Stage loadingDialog = Utils.createLoadingDialog();
        Task<Repository> fetchDataTask = new Task<Repository>() {
            @Override
            protected Repository call(){
                return new RepositoryImpl(directoryPath);
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    loadingDialog.close();
                    repository = getValue();
                    updateCounts();
                    resultHolder.getChildren().clear();
                    Utils.showAlert("Data fetched successfully!");
                });
            }


            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    loadingDialog.close();
                    resultHolder.getChildren().clear();
                    Utils.showAlert("Failed to fetch data!");
                });
            }
        };

        loadingDialog.show();
        new Thread(fetchDataTask).start();
    }

    private void updateCounts() {
        if (repository !=null){
            kotlinCount.setText("Files of Kotlin: "+repository.getKotlinCount());
            javaCount.setText("Files of Java: "+repository.getJavaCount());
            allDependenciesCount.setText("Imports outside module: "+repository.getAllImportsCount());
            allDependenciesInAppCount.setText("Imports outside module Within App : "+repository.getAllImportsInAppCount());
            xmlLayoutCount.setText( "XML layouts : "+repository.getXmlCount());
            resourceCount.setText("Resources Count : "+repository.getResourcesCount());
        }
    }


    // region Events
    @FXML
    private void onClickToSelectDirectory() {
        File selectedDirectory = Utils.directoryChooser().showDialog(root.getScene().getWindow());
        if (selectedDirectory != null) {
            selectedPathLabel.setText("Selected Directory: " + selectedDirectory.getAbsolutePath());
            fetchData(selectedDirectory.getAbsolutePath());
        } else {
            selectedPathLabel.setText("No directory selected");
        }
    }

    @FXML
    private void onCLickXmlLayoutCount(){
        if (repository !=null){
            ObservableList<String> items = FXCollections.observableArrayList();
            items.addAll(repository.getXmlList());
            listView.setItems(items);
            resultHolder.getChildren().clear();
            resultHolder.getChildren().add(listView);
        }
    }

    @FXML
    private void onCLickJavaCount(){
        if (repository !=null){
            ObservableList<String> items = FXCollections.observableArrayList();
            items.addAll(repository.getJavaList().stream().map(FileModel::getPackagePath).collect(Collectors.toList()));
            listView.setItems(items);
            resultHolder.getChildren().clear();
            resultHolder.getChildren().add(listView);
        }
    }

    @FXML
    private void onCLickKotlinCount(){
        if (repository !=null){
            ObservableList<String> items = FXCollections.observableArrayList();
            items.addAll(repository.getKotlinList().stream().map(FileModel::getPackagePath).collect(Collectors.toList()));
            listView.setItems(items);
            resultHolder.getChildren().clear();
            resultHolder.getChildren().add(listView);
        }
    }

    @FXML
    private void onAllDependenciesCount(){
        if (repository !=null){
            ObservableList<Dependencies> items = FXCollections.observableArrayList();
            items.addAll(repository.getAllImportsList());
            tableOfDependencies.setItems(items);
            resultHolder.getChildren().clear();
            resultHolder.getChildren().add(tableOfDependencies);
        }
    }

    @FXML
    private void onCLickAllDependenciesInAppCount(){
        if (repository !=null){
            ObservableList<Dependencies> items = FXCollections.observableArrayList();
            items.addAll(repository.getAllImportsInAppList());
            tableOfDependencies.setItems(items);
            resultHolder.getChildren().clear();
            resultHolder.getChildren().add(tableOfDependencies);
        }
    }

    @FXML
    private void onCLickResourceCount(){
        if (repository !=null){
            Stage loadingDialog = Utils.createLoadingDialog();
            Task<List<Resources>> fetchDataTask = new Task<List<Resources>>() {
                @Override
                protected List<Resources> call(){
                    return repository.getAllResources().stream().peek(Resources::handleResourcePath).collect(Collectors.toList());
                }

                @Override
                protected void succeeded() {
                    Platform.runLater(() -> {
                        loadingDialog.close();
                        ObservableList<Resources> data = FXCollections.observableArrayList();
                        List<Resources> result = getValue();
                        data.addAll(result);
                        tableOfResources.setItems(data);
                        resultHolder.getChildren().clear();
                        resultHolder.getChildren().add(tableOfResources);
                        Utils.showAlert("DOne to fetch data!");
                    });
                }


                @Override
                protected void failed() {
                    Platform.runLater(() -> {
                        loadingDialog.close();
                        Utils.showAlert("Failed to fetch data!");
                    });
                }
            };

            loadingDialog.show();
            new Thread(fetchDataTask).start();


        }
    }


    // endregion Events

}
