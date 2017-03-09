package org.treeleafj.xdoc.model;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * 接口信息
 *
 * @author leaf
 * @date 2017-03-03 11:09
 */
@Data
public class ApiAction {

    /**
     * 展示用的标题
     */
    private String title;

    /**
     * 接口方法名称
     */
    private String name;

    /**
     * 接口方法
     */
    private Method method;

    /**
     * 接口的描述
     */
    private String comment;

    /**
     * 方法上标注的注解
     */
    private DocTags docTags;
}
