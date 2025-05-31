package com.github.evermindzz.legacyfilesutils;

import java.io.*;
import java.nio.charset.Charset;

public final class Files {

    private Files() {
    }

    public static boolean delete(File file) {
        return file.delete();
    }

    public static boolean deleteIfExists(File file) {
        return file.exists() && file.delete();
    }

    public static boolean exists(File file) {
        return file.exists();
    }

    public static long size(File file) {
        return file.length();
    }

    public static void copy(InputStream source, File dest, StandardCopyOption standardCopyOption) throws IOException {
        if (standardCopyOption == StandardCopyOption.REPLACE_EXISTING) {
            try {
                deleteIfExists(dest);
            } catch (SecurityException ignored) {
            }
        } else {
            throw new UnsupportedOperationException(standardCopyOption.name() + " not supported");
        }
        copy(source, dest);
    }

    public enum StandardCopyOption {
        REPLACE_EXISTING
    }

    public static void copy(InputStream source, File dest) throws IOException {
        try (OutputStream out = new FileOutputStream(dest)) {
             _copy(source, out);
        }
    }

    public static void copy(InputStream source, OutputStream dest) throws IOException {
             _copy(source, dest);
    }

    public static void copy(File source, File dest) throws IOException {
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(dest)) {
             _copy(in, out);
        }
    }

    private static void _copy(InputStream in, OutputStream out) throws IOException {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
    }

    public static File createTempFile(File dir, String prefix, String suffix) throws IOException {
        File temp = File.createTempFile(prefix, suffix, dir);
        temp.deleteOnExit();
        return temp;
    }

    public static File createTempDirectory(String prefix, File dir) throws IOException {
        File tempDir = new File(dir, prefix + System.nanoTime());
        if (!tempDir.mkdirs()) {
            throw new IOException("Failed to create temp directory: " + tempDir.getAbsolutePath());
        }
        tempDir.deleteOnExit();
        return tempDir;
    }

    public static BufferedWriter newBufferedWriter(File file, Charset charset) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
    }
    public static BufferedWriter newBufferedWriter(File file) throws IOException {
        return newBufferedWriter(file, Charset.forName("UTF-8"));

    }

    public static String readString(File file) throws IOException {
        return readString(file, Charset.forName("UTF-8"));
    }

    public static String readString(File file, Charset charset) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }

    public static void writeString(File file, String data, Charset charset) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset))) {
            writer.write(data);
        }
    }

    public static void writeString(File file, String data) throws IOException {
        writeString(file, data, Charset.forName("UTF-8"));
    }
    public static File createDirectories(File dir) throws IOException {
    if (!dir.exists()) {
        if (!dir.mkdirs()) {
            throw new IOException("Failed to create directory: " + dir.getAbsolutePath());
        }
    } else if (!dir.isDirectory()) {
        throw new IOException("File exists and is not a directory: " + dir.getAbsolutePath());
    }
    return dir;
}

    public enum StandardOpenOption {
        READ,
        WRITE,
        APPEND,
        TRUNCATE_EXISTING,
        CREATE,
        CREATE_NEW,
        DELETE_ON_CLOSE,
        SPARSE,
        SYNC,
        DSYNC;
    }
    public static BufferedWriter newBufferedWriter(File file, Charset charset, StandardOpenOption... options) throws IOException {
        // Determine if the file should be appended
        boolean append = false;
        for (StandardOpenOption option : options) {
            if (option == StandardOpenOption.APPEND) {
                append = true;
                break;
            }
        }

        // Create OutputStream with append option
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset));
    }

    public static BufferedWriter newBufferedWriter(File file, StandardOpenOption... options) throws IOException {
        return newBufferedWriter(file, Charset.forName("UTF-8"), options);
    }
}

