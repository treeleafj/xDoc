package org.treeleafj.xdoc.tag;

/**
 * Created by leaf on 2017/3/4.
 */
public class DocTagImpl extends DocTag {

    private String name;

    private String value;

    public DocTagImpl(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValues() {
        return value;
    }
}
