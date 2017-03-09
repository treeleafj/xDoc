package org.treeleafj.xdoc.model;

import lombok.Data;

import java.util.List;

/**
 * @author leaf
 * @date 2017-03-03 12:14
 */
@Data
public class FieldInfo {

    private String name;

    private Class<?> type;

    private String simpleTypeName;

    private String comment;

    private List<FieldInfo> fieldInfos;

}
