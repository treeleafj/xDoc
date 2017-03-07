package org.treeleafj.xdoc.output.spring;

import lombok.Data;
import org.treeleafj.xdoc.model.ApiAction;
import org.treeleafj.xdoc.model.ObjectInfo;
import org.treeleafj.xdoc.tag.ParamTagImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leaf on 2017/3/4.
 */
@Data
public class SpringApiAction extends ApiAction {

    /**
     * 访问的uri地址
     */
    private List<String> uris;

    /**
     * 允许的访问方法:POST,GET,DELETE,PUT等, 如果没有,则无限制
     */
    private List<String> methods;

    /**
     * 入参
     */
    private List<ParamTagImpl> param = new ArrayList<ParamTagImpl>();

    /**
     * 返回对象
     */
    private ObjectInfo returnObj;

    /**
     * 返回描述
     */
    private String returnDesc;

    /**
     * 返回的数据
     */
    private String respbody;
}
