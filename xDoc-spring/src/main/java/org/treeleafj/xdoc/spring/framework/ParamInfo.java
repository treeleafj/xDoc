package org.treeleafj.xdoc.spring.framework;

import lombok.Data;

/**
 * Created by leaf on 2017/3/12 0012.
 */
@Data
public class ParamInfo {

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
}
