package org.treeleafj.xdoc.boot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author leaf
 * @date 2017-03-09 15:43
 */
@Data
@ConfigurationProperties("xdoc")
public class XDocProperties {

    /**
     * 是否启动XDOC,此值便于在生产等环境启动程序时增加参数进行控制
     */
    private boolean enable = true;

    /**
     * 界面标题描述
     */
    private String title = "XDoc 接口文档";

    /**
     * 源码相对路径(支持多个,用英文逗号隔开)
     */
    private String sourcePath;

}
