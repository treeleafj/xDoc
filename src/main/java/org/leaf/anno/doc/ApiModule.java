package org.leaf.anno.doc;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author leaf
 * @date 2017-03-03 10:32
 */
@Data
public class ApiModule {

    private String className;

    private String simpleName;

    private List uri;

    private List methods;

    private String comment;

    private String author;

    private List<ApiAction> apiActions = new LinkedList<ApiAction>();
}
