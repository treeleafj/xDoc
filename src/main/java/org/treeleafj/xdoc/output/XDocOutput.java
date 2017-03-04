package org.treeleafj.xdoc.output;

import org.treeleafj.xdoc.model.ApiModule;

import java.util.List;

/**
 * Created by leaf on 2017/3/4.
 */
public interface XDocOutput {

    void output(List<ApiModule> apiModules);
}
