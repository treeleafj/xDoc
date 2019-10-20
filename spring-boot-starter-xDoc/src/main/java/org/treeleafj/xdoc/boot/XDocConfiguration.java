package org.treeleafj.xdoc.boot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author leaf
 * @date 2017-03-09 15:29
 */
public class XDocConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "xdoc", name = "enable", matchIfMissing = true)
    public XDocSpringController xDocController() {
        return new XDocSpringController();
    }
}
