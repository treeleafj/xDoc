package org.treeleafj.xdoc.filter;

import com.sun.javadoc.ClassDoc;

/**
 * @author leaf
 * @date 2017-03-03 17:06
 */
public interface DocFilter {

    ClassDoc[] filter(ClassDoc[] classDoc);
}
