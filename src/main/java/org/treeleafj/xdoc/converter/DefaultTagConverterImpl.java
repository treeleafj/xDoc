package org.treeleafj.xdoc.converter;

import com.sun.javadoc.Tag;
import org.treeleafj.xdoc.model.DocTagImpl;
import org.treeleafj.xdoc.model.DocTag;

/**
 * Created by leaf on 2017/3/4.
 */
public class DefaultTagConverterImpl implements TagConverter<Tag> {
    @Override
    public DocTag converter(Tag o) {
        return new DocTagImpl(o.name(), o.text());
    }
}
