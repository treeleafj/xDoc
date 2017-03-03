package org.treeleafj.xdoc.model;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 接口信息
 *
 * @author leaf
 * @date 2017-03-03 11:09
 */
@Data
public class ApiAction {

    /**
     * 接口方法名称
     */
    private String name;

    /**
     * 接口方法
     */
    private Method method;

    /**
     * 接口uri
     */
    private List uri;

    /**
     * 限制的访问方式:POST,GET等
     */
    private List<String> methods;

    /**
     * 接口的描述
     */
    private String comment;

    /**
     * 接口的作者
     */
    private String author;

    /**
     * 接口返回描述
     */
    private String returnDesc;

    /**
     * 接口返回的信息
     */
    private Param returnInfo;

    /**
     * 接口的入参
     */
    private Map<String, String> params = new TreeMap<String, String>();
}
