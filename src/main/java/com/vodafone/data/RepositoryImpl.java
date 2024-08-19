package com.vodafone.data;

import com.vodafone.data.model.Dependencies;
import com.vodafone.data.model.FileModel;
import com.vodafone.data.model.ResourceType;
import com.vodafone.data.model.Resources;
import com.vodafone.data.providers.DataHandler;
import com.vodafone.data.utils.ResourceSingleton;
import java.util.List;
import java.util.stream.Collectors;

public class RepositoryImpl implements Repository {
    private final int javaCount;
    private final List<FileModel> javaList;

    private final int kotlinCount;
    private final List<FileModel> kotlinList;

    private final int allImportsCount;
    private final List<Dependencies> allImportsList;

    private final int allImportsInAppCount;
    private final List<Dependencies> allImportsInAppList;

    private final int xmlCount;
    private final List<String> xmlList;
    private List<Resources> resourcesList;
    private int resourceCount;

    private final DataHandler dataHandler;

    public RepositoryImpl(String filePath) {
        ResourceSingleton.getInstance(filePath);
        dataHandler = new DataHandler(filePath);
        this.javaList = dataHandler.getJavaFiles();
        this.javaCount = javaList.size();

        this.kotlinList = dataHandler.getKotlinFiles();
        this.kotlinCount = kotlinList.size();

        this.allImportsList = dataHandler.getImportsOutsideModule();
        this.allImportsCount = allImportsList.size();

        this.allImportsInAppList = dataHandler.getImportsOutsideModuleWithinApp();
        this.allImportsInAppCount =  allImportsInAppList.size();

        this.xmlList = dataHandler.getLayouts();
        this.xmlCount = xmlList.size();

        this.resourcesList = dataHandler.collectResources();
        this.resourceCount = resourcesList.size();
    }

    @Override
    public String getJavaCount() {
        return String.valueOf(javaCount);
    }

    @Override
    public List<FileModel> getJavaList() {
        return javaList;
    }

    @Override
    public String getKotlinCount() {
        return String.valueOf(kotlinCount);
    }

    @Override
    public List<FileModel> getKotlinList() {
        return kotlinList;
    }

    @Override
    public String getAllImportsCount() {
        return String.valueOf(allImportsCount);
    }

    @Override
    public List<Dependencies> getAllImportsList() {
        return allImportsList;
    }

    @Override
    public String getAllImportsInAppCount() {
        return String.valueOf(allImportsInAppCount);
    }

    @Override
    public List<Dependencies> getAllImportsInAppList() {
        return allImportsInAppList;
    }

    @Override
    public String getXmlCount() {
        return String.valueOf(xmlCount);
    }

    @Override
    public List<String> getXmlList() {
        return xmlList;
    }

    @Override
    public List<Resources> getAllResources() {
        return resourcesList;
    }

    @Override
    public List<Resources> getResourcesByResourceType(ResourceType resourceType) {
        return getAllResources().stream().filter(resources -> resources.getResourceType() == resourceType)
                    .collect(Collectors.toList());

    }

    @Override
    public String getResourcesCount() {
        return String.valueOf(resourcesList.size());
    }

    @Override
    public String toString() {
        return javaCount+ "\n" +
                kotlinCount + "\n" +
                allImportsCount + "\n" +
                allImportsInAppCount + "\n" +
                xmlCount;
    }

}
