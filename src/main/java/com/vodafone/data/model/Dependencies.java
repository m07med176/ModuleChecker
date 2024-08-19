package com.vodafone.data.model;

public class Dependencies {
    private String dependenciesCount;
    private String dependenciesPath;

    public Dependencies(String dependenciesCount, String dependenciesPath) {
        this.dependenciesCount = dependenciesCount;
        this.dependenciesPath = dependenciesPath;
    }

    public String getDependenciesCount() {
        return dependenciesCount;
    }

    public void setDependenciesCount(String dependenciesCount) {
        this.dependenciesCount = dependenciesCount;
    }

    public String getDependenciesPath() {
        return dependenciesPath;
    }

    public void setDependenciesPath(String dependenciesPath) {
        this.dependenciesPath = dependenciesPath;
    }
}
