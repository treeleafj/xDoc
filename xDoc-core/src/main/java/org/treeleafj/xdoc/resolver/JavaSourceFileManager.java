package org.treeleafj.xdoc.resolver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类源码路径映射管理器
 */
public class JavaSourceFileManager {

    private static final JavaSourceFileManager instance = new JavaSourceFileManager();

    private static Map<String, String> classPath = new HashMap<>();

    public static JavaSourceFileManager getInstance() {
        return instance;
    }


    /**
     * 递归获取指定目录下面所有的Java文件,包括子目录中的
     *
     * @param file 文件目录
     * @return 所有java文件
     */
    public List<String> getAllJavaFiles(File file) {
        if (!file.exists()) {
            return new ArrayList(0);
        }

        if (file.isFile()) {
            if (file.getName().lastIndexOf(".java") > 0) {
                List list = new ArrayList(1);
                list.add(file.getAbsolutePath());
                return list;
            } else {
                return new ArrayList(0);
            }
        }

        List<String> list = new ArrayList();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    list.addAll(getAllJavaFiles(f));
                }
            }
        }
        return list;
    }

    /**
     * 将指定类名与对应的类源码文件路径存放进来
     *
     * @param name 类名称
     * @param path 类源码文件路径
     */
    public void put(String name, String path) {
        classPath.put(name, path);
    }

    /**
     * 获取指定类名所对应的类源码文件路径
     *
     * @param name 类名
     * @return 源码文件路径, 如果不存在则返回null
     */
    public String getPath(String name) {
        return classPath.get(name);
    }
}
