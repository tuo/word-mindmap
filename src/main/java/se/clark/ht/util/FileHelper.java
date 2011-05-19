package se.clark.ht.util;

import java.io.File;

public class FileHelper {

    public static boolean deleteDir(File dir) {
        if (!dir.exists()) {
            return false;
        }
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }
}