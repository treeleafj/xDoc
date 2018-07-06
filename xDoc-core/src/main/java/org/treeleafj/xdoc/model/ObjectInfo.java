package org.treeleafj.xdoc.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leaf
 * @date 2017-03-03 12:14
 */
@Data
public class ObjectInfo {

    private String name;

    /**
     * 源码在哪个类
     */
    private Class<?> type;

    private String comment;

    private List<FieldInfo> fieldInfos = new ArrayList<>();

}
