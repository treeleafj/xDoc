package org.treeleafj.xdoc.tag;

import org.treeleafj.xdoc.model.ObjectInfo;

/**
 * Created by leaf on 2017/3/4.
 */
public class SeeTagImpl extends DocTag {

    private String name;

    private ObjectInfo objectInfo;

    public SeeTagImpl(String name, ObjectInfo objectInfo) {
        this.name = name;
        this.objectInfo = objectInfo;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ObjectInfo getValues() {
        return objectInfo;
    }
}
