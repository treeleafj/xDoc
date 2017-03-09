package org.treeleafj.xdoc.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leaf
 * @date 2017-03-03 16:52
 */
public class FileUtils {

    public static List<String> getAllFiles(File file) {
        if (!file.exists()) {
            return new ArrayList(0);
        }

        if (file.isFile()) {
            if (file.getName().lastIndexOf(".java") > 0) {
                List list = new ArrayList();
                list.add(file.getAbsolutePath());
                return list;
            } else {
                return new ArrayList(0);
            }
        }

        List list = new ArrayList();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                list.addAll(getAllFiles(f));
            }
        }
        return list;
    }

}
