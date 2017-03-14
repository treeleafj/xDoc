package org.treeleafj.xdoc;

import org.treeleafj.xdoc.model.ApiModule;
import org.treeleafj.xdoc.output.XDocOutput;
import org.treeleafj.xdoc.utils.ApiModulesHolder;
import org.treeleafj.xdoc.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leaf
 * @date 2017-03-03 16:25
 */
public class XDoc {

    /**
     * 源码路径
     */
    private List<String> srcPaths;

    /**
     * 输出方式
     */
    private XDocOutput output;

    /**
     * 默认的文档处理类(基于sun javadoc)
     */
    private Class<?> docHandlerClass = CoreDocHandler.class;

    /**
     * 构建XDoc对象
     *
     * @param srcPath 源码路径
     * @param output 输出方式
     */
    public XDoc(String srcPath, XDocOutput output) {
        List<String> srcPaths = new ArrayList(1);
        srcPaths.add(srcPath);
        this.srcPaths = srcPaths;
        this.output = output;
    }

    /**
     * 构建XDoc对象
     *
     * @param srcPaths 源码路径,支持多个
     * @param output 输出方式
     */
    public XDoc(List<String> srcPaths, XDocOutput output) {
        this.srcPaths = srcPaths;
        this.output = output;
    }

    public void build() {

        List<String> files = new ArrayList<>();
        for (String srcPath : srcPaths) {
            files.addAll(FileUtils.getAllFiles(new File(srcPath)));
        }

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

        output.output(currentApiModules);
    }
}
