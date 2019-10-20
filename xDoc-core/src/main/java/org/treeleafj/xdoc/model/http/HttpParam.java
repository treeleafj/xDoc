package org.treeleafj.xdoc.model.http;

import lombok.Data;

/**
 * Created by leaf on 2017/3/12 0012.
 */
@Data
public class HttpParam {

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
    private String paramType;
}
