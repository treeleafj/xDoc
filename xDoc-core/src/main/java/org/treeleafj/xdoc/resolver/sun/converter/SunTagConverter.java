package org.treeleafj.xdoc.resolver.sun.converter;

import com.sun.javadoc.Tag;
import org.treeleafj.xdoc.tag.DocTag;

/**
 * Created by leaf on 2017/3/4.
 */
public interface SunTagConverter<T extends Tag> {

    DocTag converter(T o);

}
