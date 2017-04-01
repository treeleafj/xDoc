package org.treeleafj.xdoc.resolver.sun.converter;

import com.sun.javadoc.Tag;
import org.treeleafj.xdoc.tag.DocTag;
import org.treeleafj.xdoc.tag.DocTagImpl;

/**
 * Created by leaf on 2017/3/4.
 */
public class DefaultTagConverterImpl implements TagConverter<Tag> {
    @Override
    public DocTag converter(Tag o) {
        return new DocTagImpl(o.name(), o.text());
    }
}
