package com.vodafone.data.utils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {


    public static String convertDataBindingToLayoutXML(String camelCase) {
        if (camelCase.endsWith("Binding")) {
            camelCase = camelCase.substring(0, camelCase.length() - "Binding".length());
        }

        String snakeCase = camelCase
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .replaceAll("([A-Z])([A-Z][a-z])", "$1_$2")
                .toLowerCase();
        return snakeCase + ".xml";
    }

    public static String getPackageNameOfApp(String filePath) {
        String packageName = "";
        String[] parts = filePath.split("\\\\");

        int startIndex = -1;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("vodafone")) {
                startIndex = i;
                break;
            }
        }

        if (startIndex != -1 && startIndex + 2 < parts.length) {
            packageName = String.join(".", parts[startIndex], parts[startIndex + 1], parts[startIndex + 2]);
        }

        return packageName;
    }

    private static String transformPathToRes(String originalPath) {
        String searchPart = "java";
        int index = originalPath.indexOf(searchPart);

        if (index != -1) {
            return originalPath.substring(0, index + searchPart.length()).replace(searchPart, "res");
        } else {
            return originalPath;
        }
    }

    public static List<Path> collectXMLFiles(String filePath) {
        List<Path> xmlFiles = new ArrayList<>();
        Path startPath = Paths.get(transformPathToRes(filePath));

        try {
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.toString().endsWith(".xml") &&
                            !file.getParent().toString().contains("src\\main\\res\\layout")
                    ) {
                        xmlFiles.add(file);
                        System.out.println(file.toString());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    // Handle the error
                    exc.printStackTrace();
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return xmlFiles;
    }

    public static boolean containsString(Path file, Pattern searchPattern) {
        try (BufferedReader br = new BufferedReader(new FileReader(file.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = searchPattern.matcher(line);
                if (matcher.find()) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static List<String> getImports(String filePath) {
        List<String> imports = new ArrayList<>();
        Pattern importPattern = Pattern.compile("^import\\s+(.+);?$");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = importPattern.matcher(line);
                if (matcher.find()) {
                    String result = matcher.group(1);
                    if (result.contains(";")) {
                        result = result.substring(0, result.length() - 1);
                    }
                    imports.add(result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imports;
    }

}
