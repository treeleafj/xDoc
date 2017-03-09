package org.treeleafj.xdoc.filter;

/**
 * @author leaf
 * @date 2017-03-03 17:07
 */
public class FilterFactory {

    public static DocFilter getDefaultFilter() {
        return new SpringDocFilterImpl();
    }

}
