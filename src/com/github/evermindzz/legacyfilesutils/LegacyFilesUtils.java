package com.github.evermindzz.legacyfilesutils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;

public final class LegacyFilesUtils {

    private LegacyFilesUtils() {
        // no instance
    }

    /**
     * Equivalent to Files.readString(Path) from Java 11.
     */
    public static String readString(File file) throws IOException {
        return readString(file, StandardCharsets.UTF_8);
    }

    public static String readString(File file, Charset charset) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        if (content.length() > 0 && content.charAt(content.length() - 1) == '\n') {
            content.deleteCharAt(content.length() - 1);
        }
        return content.toString();
    }

    /**
     * Equivalent to Files.writeString(Path, String) from Java 11.
     */
    public static void writeString(File file, String content) throws IOException {
        writeString(file, content, StandardCharsets.UTF_8, false);
    }

    public static void writeString(File file, String content, Charset charset, boolean append) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset))) {
            writer.write(content);
        }
    }

    /**
     * Equivalent to Paths.get(String).
     * Returns a java.io.File instead of java.nio.file.Path for compatibility.
     */
    public static File toFile(String first, String... more) {
        if (more == null || more.length == 0) {
            return new File(first);
        }
        StringBuilder pathBuilder = new StringBuilder(first);
        for (String segment : more) {
            if (!pathBuilder.toString().endsWith(File.separator)) {
                pathBuilder.append(File.separator);
            }
            pathBuilder.append(segment);
        }
        return new File(pathBuilder.toString());
    }
}

