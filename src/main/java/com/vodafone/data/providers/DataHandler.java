package com.vodafone.data.providers;

import com.vodafone.data.Repository;
import com.vodafone.data.RepositoryImpl;
import com.vodafone.data.model.*;
import com.vodafone.data.utils.Utils;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DataHandler {
    private final String filePath;
    private final List<FileModel> fileModelList;


    public DataHandler(String filePath) {
        this.filePath = filePath;
        this.fileModelList = collectFiles(filePath);
    }

    private ArrayList<FileModel> collectFiles(String directoryPath){
        ArrayList<FileModel> files = new ArrayList<>();

        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            File[] filesList = directory.listFiles();
            if (filesList != null) {
                for (File file : filesList) {
                    if (file.isFile()) {
                        String[] fileNameAndType = file.getName().split("\\.");
                        files.add(
                                new FileModel(
                                        fileNameAndType[0],
                                        FileExtensionType.valueOf(fileNameAndType[1]),
                                        file.getPath(),
                                        Utils.getImports(file.getAbsolutePath())
                                )
                        );
                    } else if (file.isDirectory()) {
                        files.addAll(0,collectFiles(file.getAbsolutePath()));
                    }
                }
            } else {
                System.out.println("The directory is empty.");
            }
        } else {
            System.out.println("The specified path is not a directory.");
        }

        return files;
    }

    private List<Dependencies> filterDuplicateWithCount(List<String> imports) {
        Map<String, Integer> countMap = new LinkedHashMap<>();
        for (String s : imports) {
            countMap.put(s, countMap.getOrDefault(s, 0) + 1);
        }

        List<Dependencies> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            result.add(new Dependencies(String.valueOf(entry.getValue()),entry.getKey()));
        }
        return result;
    }

    public List<String> getLayouts() {
        return fileModelList.
                stream()
                .flatMap(fileModel -> fileModel.getImports().stream())
                .filter(s -> s.contains("databinding")
                        && !s.contains("Bindable")
                        && !s.contains("DataBindingUtil")
                        && !s.contains("BaseObservable")
                ).sorted()
                .map(s -> {
                    String[] items = s.split("\\.");
                    String layoutClassName = items[items.length - 1];
                    return Utils.convertDataBindingToLayoutXML(layoutClassName);
                })
                .collect(Collectors.toList());
    }

    public List<FileModel> getJavaFiles() {
        return fileModelList.stream().filter(fileModel -> fileModel.getFileExtension() == FileExtensionType.java).collect(Collectors.toList());
    }

    public List<FileModel> getKotlinFiles() {
        return fileModelList.stream().filter(fileModel -> fileModel.getFileExtension() == FileExtensionType.kt).collect(Collectors.toList());
    }

    public List<Dependencies> getImportsOutsideModule() {
        String[] moduleNames = filePath.split("\\\\");
        String moduleName = moduleNames[moduleNames.length - 1];
        return filterDuplicateWithCount(
                fileModelList.
                        stream()
                        .flatMap(fileModel -> fileModel.getImports().stream()).filter(s -> !s.contains(moduleName))
                        .sorted()
                        .collect(Collectors.toList())
        );
    }

    public List<Dependencies> getImportsOutsideModuleWithinApp() {
        String[] moduleNames = filePath.split("\\\\");
        String moduleName = moduleNames[moduleNames.length - 1];
        return filterDuplicateWithCount(
                fileModelList.
                        stream()
                        .flatMap(fileModel -> fileModel.getImports().stream())
                        .filter(s -> !s.contains(moduleName) && s.contains(Utils.getPackageNameOfApp(filePath)))
                        .filter(s -> !s.contains("databinding"))
                        .filter(s -> !s.contains("BuildConfig"))
                        .filter(s -> !s.contains("R"))
                        .filter(s -> !s.contains("AnaVodafoneApplication"))
                        .sorted()
                        .collect(Collectors.toList())
        );
    }

    // region Resources
    private List<FileModel> getFilesUsedResources() {
        return fileModelList.
                stream()
                .peek(fileModel -> {
                            List<String> resourceFiles = fileModel.getImports().stream().filter(importPath -> {
                                Pattern pattern = Pattern.compile("([a-zA-Z_][a-zA-Z0-9_]*\\.)+R\\b");
                                Matcher matcher = pattern.matcher(importPath);
                                return matcher.find();
                            }).sorted().collect(Collectors.toList());
                            fileModel.setImports(resourceFiles);
                        }
                )
                .filter(fileModel -> fileModel.getImports().size() != 0)
                .collect(Collectors.toList());
    }
    private List<Resources> findResourcePatterns(String filePath) {
        File file = new File(filePath);
        Pattern pattern = Pattern.compile("R\\.[a-zA-Z_]+\\.[a-zA-Z0-9_]+");

        List<String> resourcePatterns = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    resourcePatterns.add(matcher.group());
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return resourcePatterns.stream().map(resource -> {
            String[] resourceParts = resource.split("\\.");
            return new Resources(ResourceType.valueOf(resourceParts[1]), resourceParts[2], filePath);
        }).collect(Collectors.toList());
    }

    public List<Resources> collectResources() {
        List<Resources> resourcesList = new ArrayList<>();
        getFilesUsedResources().forEach(fileModel -> {
            resourcesList.addAll(findResourcePatterns(fileModel.getPackagePath()));
        });

        return resourcesList;
    }

    // endregion Resources


}
