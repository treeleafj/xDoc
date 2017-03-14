package org.treeleafj.xdoc.spring.format;

import org.treeleafj.xdoc.model.ApiModule;

import java.util.List;

/**
 * Created by leaf on 2017/3/4.
 */
public interface Format {

    String format(List<ApiModule> list);

    String format(ApiModule apiModule);
}
