package org.treeleafj.xdoc.tag;

import org.treeleafj.xdoc.model.ObjectInfo;

public class ParamObjTagImpl extends DocTag<ObjectInfo> {

    private ObjectInfo objectInfo;

    public ParamObjTagImpl(String tagName, ObjectInfo objectInfo) {
        super(tagName);
        this.objectInfo = objectInfo;
    }

    @Override
    public ObjectInfo getValues() {
        return objectInfo;
    }
}
