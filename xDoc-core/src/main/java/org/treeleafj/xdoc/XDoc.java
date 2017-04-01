package org.treeleafj.xdoc;

import org.treeleafj.xdoc.model.ApiModule;
import org.treeleafj.xdoc.output.XDocOutput;
import org.treeleafj.xdoc.resolver.DocTagResolver;
import org.treeleafj.xdoc.resolver.SunDocTagResovler;
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
     * 默认的java注释解析实现
     * @see org.treeleafj.xdoc.resolver.SunDocTagResovler
     * @see org.treeleafj.xdoc.resolver.JavaParserDocTagResolver
     */
    private DocTagResolver docTagResolver = new SunDocTagResovler();

    /**
     * 构建XDoc对象
     *
     * @param srcPath 源码路径
     * @param output  输出方式
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
     * @param output   输出方式
     */
    public XDoc(List<String> srcPaths, XDocOutput output) {
        this.srcPaths = srcPaths;
        this.output = output;
    }

    /**
     * 构建接口文档
     */
    public void build() {

        List<String> files = new ArrayList<>();
        for (String srcPath : srcPaths) {
            files.addAll(FileUtils.getAllFiles(new File(srcPath)));
        }

        docTagResolver.resolve(files);

        List<ApiModule> currentApiModules = ApiModulesHolder.getCurrentApiModules();

        output.output(currentApiModules);
    }
}
