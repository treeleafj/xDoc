package org.treeleafj.xdoc.resolver.javaparser.converter;

import org.treeleafj.xdoc.tag.DocTag;
import org.treeleafj.xdoc.tag.ParamObjTagImpl;
import org.treeleafj.xdoc.tag.SeeTagImpl;

public class ParamObjTagConverter extends SeeTagConverter {

    @Override
    public DocTag converter(String comment) {
        SeeTagImpl seeTag = (SeeTagImpl) super.converter(comment);
        if (seeTag != null) {
            return new ParamObjTagImpl(seeTag.getTagName(), seeTag.getValues());
        }
        return null;
    }
}
