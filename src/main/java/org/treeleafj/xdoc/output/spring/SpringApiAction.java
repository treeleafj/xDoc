package org.treeleafj.xdoc.output.spring;

import lombok.Data;
import org.treeleafj.xdoc.model.ApiAction;
import org.treeleafj.xdoc.model.ObjectInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by leaf on 2017/3/4.
 */
@Data
public class SpringApiAction extends ApiAction {

    private List<String> uris;

    private List<String> methods;

    private Map<String, String> param = new TreeMap<String, String>();

    private ObjectInfo returnObj;

    private String returnDesc;
}
