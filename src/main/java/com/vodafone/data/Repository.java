package com.vodafone.data;

import com.vodafone.data.model.Dependencies;
import com.vodafone.data.model.FileModel;
import com.vodafone.data.model.ResourceType;
import com.vodafone.data.model.Resources;
import javafx.util.Pair;

import java.util.List;

public interface Repository {
    String getJavaCount();

    List<FileModel> getJavaList();

    String getKotlinCount();

    List<FileModel> getKotlinList();

    String getAllImportsCount();

    List<Dependencies> getAllImportsList();

    String getAllImportsInAppCount();

    List<Dependencies> getAllImportsInAppList();

    String getXmlCount();

    List<String> getXmlList();

    List<Resources> getAllResources();
    List<Resources> getResourcesByResourceType(ResourceType resourceType);
    String getResourcesCount();
}
