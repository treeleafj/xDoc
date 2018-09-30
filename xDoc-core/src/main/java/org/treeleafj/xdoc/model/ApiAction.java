package org.treeleafj.xdoc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.treeleafj.xdoc.tag.DocTag;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 接口信息,一个接口类里面会有多个接口,每个接口都抽象成ApiAction
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
    @JsonIgnore
    private transient Method method;

    /**
     * 接口的描述
     */
    private String comment;

    /**
     * 方法上标注的注解
     */
    private List<DocTag> docTags;
}
