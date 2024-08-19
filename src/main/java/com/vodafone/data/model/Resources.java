package com.vodafone.data.model;

import com.vodafone.data.utils.ResourceSingleton;
import com.vodafone.data.utils.Utils;


import java.nio.file.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class Resources {
    private String resourceName;
    private ResourceType resourceType;
    private String filePath;
    private String resourcePath;
    private String resourceFile;

    public Resources(ResourceType resourceType, String resourceName, String filePath) {
        this.resourceType = resourceType;
        this.resourceName = resourceName;
        this.filePath = filePath;
    }

    public void handleResourcePath() {
        List<Path> xmlFiles = ResourceSingleton.getInstance(filePath).getAllXMLFiles();
       // xmlFiles.sort(Comparator.comparing(Path::toString, String.CASE_INSENSITIVE_ORDER));
        Pattern searchPattern = Pattern.compile(Pattern.quote(resourceName), Pattern.CASE_INSENSITIVE);
        int index = binarySearchPath(xmlFiles, searchPattern);
        if (index >= 0) {
            resourcePath = xmlFiles.get(index).toString();
            String[] splitedResourcePath = resourcePath.split("\\\\");
            resourceFile = splitedResourcePath[splitedResourcePath.length-1];
        }else{
            resourceFile = "Nothing";
        }

    }


    private int binarySearchPath(List<Path> xmlFiles, Pattern searchPattern) {
        int left = 0;
        int right = xmlFiles.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Path midPath = xmlFiles.get(mid);

            if (Utils.containsString(midPath, searchPattern)) {
                return mid;
            }
            int comparison = midPath.toString().compareToIgnoreCase(resourceName);

            if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }


    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getResourceFile() {
        return resourceFile;
    }

    public void setResourceFile(String resourceFile) {
        this.resourceFile = resourceFile;
    }
}
