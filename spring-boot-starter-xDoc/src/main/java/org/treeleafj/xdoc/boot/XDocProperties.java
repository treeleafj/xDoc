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
     * 源码相对路径
     */
    private String sourcePath;

    /**
     * 源码决定路径
     */
    private String sourceAbsolutePath;

}
