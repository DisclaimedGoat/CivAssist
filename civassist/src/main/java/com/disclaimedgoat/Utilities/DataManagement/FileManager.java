package com.disclaimedgoat.Utilities.DataManagement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    private final Path rootPath;

    public FileManager(String rootPath) {
        this.rootPath = Paths.get(rootPath);
    }

    public FileManager() { rootPath = Paths.get("civassist/"); }

    public File getFile(String filename) {
        return getFile(filename, true);
    }

    public File getFile(String filename, boolean create) {
        String filepath = composeFilePath(filename);
        File file = new File(filepath);

        if(create) {
            try {
                file.createNewFile();
            } catch (IOException ignored) { }
        }

        return file;
    }

    private String composeFilePath(String... filenames) {
        Path path = rootPath;
        for(String s : filenames) path = path.resolve(s);
        return path.toString();
    }

    public boolean createDirectory(String path, String name) {
        String filepath = composeFilePath(path, name);
        return new File(filepath).mkdirs();
    }

    public boolean createDirectory(String name) {
        return createDirectory("", name);
    }

}
