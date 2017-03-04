package org.treeleafj.xdoc.converter;

import com.sun.javadoc.ParamTag;
import org.treeleafj.xdoc.model.DocTag;
import org.treeleafj.xdoc.model.ParamTagImpl;

/**
 * Created by leaf on 2017/3/4.
 */
public class ParamTagConverter implements TagConverter<ParamTag> {

    @Override
    public DocTag converter(ParamTag o) {
        return new ParamTagImpl(o.name(), o.parameterName(), o.parameterComment());
    }
}
