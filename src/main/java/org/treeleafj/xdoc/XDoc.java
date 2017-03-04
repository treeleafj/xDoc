package org.treeleafj.xdoc;

import com.alibaba.fastjson.JSON;
import org.treeleafj.xdoc.handler.CoreDocHandler;
import org.treeleafj.xdoc.model.ApiModule;
import org.treeleafj.xdoc.utils.ApiModulesHolder;
import org.treeleafj.xdoc.utils.FileUtils;

import java.io.File;
import java.util.List;

/**
 * @author leaf
 * @date 2017-03-03 16:25
 */
public class XDoc {

    /**
     * 源码路径
     */
    private String srcPath;

    /**
     * 目标路径
     */
    private String targetPath;

    private Class<?> docHandlerClass = CoreDocHandler.class;

    public XDoc(String srcPath, String targetPath) {
        this.srcPath = srcPath;
        this.targetPath = targetPath;
    }

    public void build() {
        List<String> files = FileUtils.getAllFiles(new File(srcPath));

        StringBuilder sb = new StringBuilder();
        for (Object o : files) {
            sb.append(o).append(" ");
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        files.add(0, "-doclet");
        files.add(1, docHandlerClass.getName());

        String[] docArgs = files.toArray(new String[files.size()]);

        com.sun.tools.javadoc.Main.execute(docArgs);

        List<ApiModule> currentApiModules = ApiModulesHolder.getCurrentApiModules();
        System.out.println(JSON.toJSONString(currentApiModules));
    }
}
