package com.disclaimedgoat.Utilities.DataManagement;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    private final Path rootPath;

    public FileManager(String rootPath) {
        this.rootPath = Paths.get(rootPath);

        createDirectory(rootPath);
    }

    public File getFile(String...filename) {
        return getFile(true, filename);
    }

    public File getFile(boolean create, String...filename) {
        String filepath = composeFilePath(filename);
        File file = new File(filepath);

        if(create && !file.exists()) {
            try {
                System.out.println("Creating new file");
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

    public static boolean printToFile(File file, String...content) {
        try(PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {

            for(String s : content) writer.println(s);

        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public static boolean printFormatted(File file, String format, Object...content) {
        try(PrintWriter writer = new PrintWriter(new FileWriter(file))) {

            writer.printf(format, content);

        } catch (IOException e) {
            return false;
        }

        return true;
    }

}
