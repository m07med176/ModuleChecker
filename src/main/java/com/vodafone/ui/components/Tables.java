package com.vodafone.ui.components;

import com.vodafone.data.model.ResourceType;
import com.vodafone.data.model.Resources;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class Tables {


    public TableView tableOfResources(){
        TableView tableOfResources = new TableView<>();

        TableColumn<Resources, String> resourceName = new TableColumn<>("Resource Name");
        resourceName.setCellValueFactory(new PropertyValueFactory<>("resourceName"));

        TableColumn<Resources, ResourceType> resourceType = new TableColumn<>("Resource Type");
        resourceType.setCellValueFactory(new PropertyValueFactory<>("resourceType"));

        TableColumn<Resources, String> filePath = new TableColumn<>("File Path");
        filePath.setCellValueFactory(new PropertyValueFactory<>("filePath"));

        TableColumn<Resources, String> resourceFile = new TableColumn<>("Resource File");
        resourceFile.setCellValueFactory(new PropertyValueFactory<>("resourceFile"));

        tableOfResources.getColumns().add(resourceName);
        tableOfResources.getColumns().add(resourceType);
        tableOfResources.getColumns().add(filePath);
        tableOfResources.getColumns().add(resourceFile);
        return tableOfResources;
    }

    public TableView tableOfDependencies(){
        TableView tableOfDependencies = new TableView<>();

        TableColumn<Resources, String> resourceName = new TableColumn<>("Dependencies Count");
        resourceName.setCellValueFactory(new PropertyValueFactory<>("dependenciesCount"));

        TableColumn<Resources, String> filePath = new TableColumn<>("Dependencies Path");
        filePath.setCellValueFactory(new PropertyValueFactory<>("dependenciesPath"));


        tableOfDependencies.getColumns().add(resourceName);
        tableOfDependencies.getColumns().add(filePath);

        return tableOfDependencies;
    }


}
