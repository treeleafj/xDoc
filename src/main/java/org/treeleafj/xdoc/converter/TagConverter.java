package org.treeleafj.xdoc.converter;

import com.sun.javadoc.Tag;
import org.treeleafj.xdoc.model.DocTag;

/**
 * Created by leaf on 2017/3/4.
 */
public interface TagConverter<T extends Tag> {

    DocTag converter(T o);

}
