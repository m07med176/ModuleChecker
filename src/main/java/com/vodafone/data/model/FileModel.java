package com.vodafone.data.model;

import java.util.List;

public class FileModel {
    private String fileName;
    private FileExtensionType fileExtension;
    private String packagePath;
    private List<String> imports;

    public FileModel() {
    }

    public FileModel(String fileName, FileExtensionType fileExtension, String packagePath, List<String> imports) {
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.packagePath = packagePath;
        this.imports = imports;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileExtensionType getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(FileExtensionType fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }
}

