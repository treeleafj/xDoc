package org.treeleafj.xdoc.model;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leaf on 2018/6/22.
 */
@Data
public class ApiDoc {

    /**
     * 附带的属性
     */
    private Map<String, Object> properties = new HashMap<>();

    /**
     * 所有API模块
     */
    private List<ApiModule> apiModules;

    public ApiDoc(List<ApiModule> apiModules) {
        this.apiModules = apiModules;
    }
}
