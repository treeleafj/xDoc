package org.treeleafj.xdoc.tag;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by leaf on 2017/3/12 0012.
 */
@Data
public class RespTagImpl extends DocTag {

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

    /**
     * 类型
     */
    private String type;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValues() {
        String s = paramName + " " + paramDesc;
        if (StringUtils.isNotBlank(type)) {
            s += " " + type;
        }
        s += " " + (require ? "必填" : "非必填");
        return s;
    }
}
