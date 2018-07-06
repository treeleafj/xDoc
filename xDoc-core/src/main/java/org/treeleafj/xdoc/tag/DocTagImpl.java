package org.treeleafj.xdoc.tag;

/**
 * 简单文本型注释标签实现
 * <p>
 * Created by leaf on 2017/3/4.
 */
public class DocTagImpl extends DocTag<String> {

    private String value;

    public DocTagImpl(String tagName, String value) {
        super(tagName);
        this.value = value;
    }

    @Override
    public String getValues() {
        return value;
    }
}
