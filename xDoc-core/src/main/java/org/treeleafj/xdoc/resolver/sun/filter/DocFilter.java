package org.treeleafj.xdoc.resolver.sun.filter;

import com.sun.javadoc.ClassDoc;

/**
 * 文档过滤器,过滤掉不需要解析的类信息
 *
 * @author leaf
 * @date 2017-03-03 17:06
 */
public interface DocFilter {

    ClassDoc[] filter(ClassDoc[] classDoc);
}
