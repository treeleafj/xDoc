package org.treeleafj.xdoc.tag;

import lombok.Data;

/**
 * 对@Param注释的封装
 * <p>
 * Created by leaf on 2017/3/4.
 */
@Data
public class ParamTagImpl extends DocTag {

    /**
     * 标签名
     */
    private String name;

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

    public ParamTagImpl(String name, String paramName, String paramDesc) {
        this.name = name;
        this.paramName = paramName;
        this.paramDesc = paramDesc;
    }

    public ParamTagImpl(String name, String paramName, String paramDesc, boolean require) {
        this.name = name;
        this.paramName = paramName;
        this.paramDesc = paramDesc;
        this.require = require;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValues() {
        return paramName + " " + this.paramDesc;
    }

}
