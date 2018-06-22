package org.treeleafj.xdoc;

import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treeleafj.xdoc.format.Formater;
import org.treeleafj.xdoc.framework.Framework;
import org.treeleafj.xdoc.model.ApiModule;
import org.treeleafj.xdoc.resolver.DocTagResolver;
import org.treeleafj.xdoc.resolver.javaparser.JavaParserDocTagResolver;
import org.treeleafj.xdoc.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * XDoc主入口,核心处理从这里开始
 *
 * @author leaf
 * @date 2017-03-03 16:25
 */
public class XDoc {

    private static final String CHARSET = "utf-8";

    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 源码路径
     */
    private List<String> srcPaths;

    /**
     * api框架类型
     */
    @Setter
    private Framework framework;

    /**
     * 默认的java注释解析器实现
     * <p>
     * 备注:基于sun doc的解析方式已经废弃,若需要请参考v1.0之前的版本
     *
     * @see org.treeleafj.xdoc.resolver.javaparser.JavaParserDocTagResolver
     */
    @Setter
    private DocTagResolver docTagResolver = new JavaParserDocTagResolver();

    /**
     * 构建XDoc对象
     *
     * @param srcPath 源码路径
     */
    public XDoc(String srcPath, Framework framework) {
        this(Arrays.asList(srcPath), framework);
    }

    /**
     * 构建XDoc对象
     *
     * @param srcPaths 源码路径,支持多个
     */
    public XDoc(List<String> srcPaths, Framework framework) {
        this.srcPaths = srcPaths;
        this.framework = framework;
    }

    /**
     * 解析源码并返回对应的接口数据
     *
     * @return API接口数据
     */
    public List<ApiModule> resolve() {
        List<String> files = new ArrayList<>();
        for (String srcPath : this.srcPaths) {
            files.addAll(FileUtils.getAllJavaFiles(new File(srcPath)));
        }

        List<ApiModule> apiModules = this.docTagResolver.resolve(files, framework);

        if (framework != null) {
            apiModules = framework.extend(apiModules);
        }
        return apiModules;
    }

    /**
     * 构建接口文档
     */
    public void build(OutputStream out, Formater formater) {
        List<ApiModule> apiModules = this.resolve();

        if (apiModules != null && out != null && formater != null) {
            String s = formater.format(apiModules);
            try {
                IOUtils.write(s, out, CHARSET);
            } catch (IOException e) {
                log.error("接口文档写入文件失败", e);
            } finally {
                IOUtils.closeQuietly(out);
            }
        }
    }
}
