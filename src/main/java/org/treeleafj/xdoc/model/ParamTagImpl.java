package org.treeleafj.xdoc.model;

/**
 * Created by leaf on 2017/3/4.
 */
public class ParamTagImpl extends DocTag {

    private String name;

    private String paramName;

    private String paramDesc;

    public ParamTagImpl(String name, String paramName, String paramDesc) {
        this.name = name;
        this.paramName = paramName;
        this.paramDesc = paramDesc;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValues() {
        return paramName + " " + this.paramDesc;
    }

    public String getParamName() {
        return paramName;
    }

    public String getParamDesc() {
        return paramDesc;
    }
}
