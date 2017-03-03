package org.treeleafj.xdoc.model;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 接口业务模块
 *
 * @author leaf
 * @date 2017-03-03 10:32
 */
@Data
public class ApiModule {

    /**
     * 源码在哪个类
     */
    private Class<?> type;

    /**
     * 业务模块的描述
     */
    private String comment;

    /**
     * 业务模块的作者
     */
    private String author;

    /**
     * 此业务模块下有哪些接口
     */
    private List<ApiAction> apiActions = new LinkedList<ApiAction>();
}
