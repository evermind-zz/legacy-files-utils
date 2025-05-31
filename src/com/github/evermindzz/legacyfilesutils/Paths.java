package com.github.evermindzz.legacyfilesutils;

import java.io.File;

public final class Paths {

    private Paths() {
        // Utility class
    }

    public static File get(String first, String... more) {
        File path = new File(first);
        for (String part : more) {
            path = new File(path, part);
        }
        return path;
    }
}

