package org.treeleafj.xdoc.resolver.javaparser.converter;

import org.treeleafj.xdoc.tag.DocTag;

/**
 * Created by leaf on 2017/4/3 0003.
 */
public interface JavaParserTagConverter<T extends String> {
    DocTag converter(T o);
}
