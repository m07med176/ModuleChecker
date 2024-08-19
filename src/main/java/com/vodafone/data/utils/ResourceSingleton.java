package com.vodafone.data.utils;

import java.nio.file.Path;
import java.util.List;

public class ResourceSingleton {

    private final List<Path> allXMLFiles;

    public static ResourceSingleton INSTANCE;

    private ResourceSingleton(String filePath) {
        allXMLFiles = Utils.collectXMLFiles(filePath);
    }

    public static ResourceSingleton getInstance(String filePath) {
        if (INSTANCE == null){
            return INSTANCE = new ResourceSingleton(filePath);
        }else {
            return INSTANCE;
        }
    }

    public List<Path> getAllXMLFiles() {
        return allXMLFiles;
    }
}
