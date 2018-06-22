package org.treeleafj.xdoc.tag;

import lombok.Data;

/**
 * 对@Param注释的封装
 * <p>
 * Created by leaf on 2017/3/4.
 */
@Data
public class ParamTagImpl extends DocTag<String> {

    /**
     * 参数名
     */
    private String paramName;

    /**
     * 参数描述
     */
    private String paramDesc;

    /**
     * 是否必填,默认false
     */
    private boolean require;

    /**
     * 参数类型
     */
    private String paramType;

    public ParamTagImpl(String tagName, String paramName, String paramDesc, String paramType, boolean require) {
        super(tagName);
        this.paramName = paramName;
        this.paramDesc = paramDesc;
        this.paramType = paramType;
        this.require = require;
    }

    @Override
    public String getValues() {
        return paramName + " " + this.paramDesc;
    }

}
