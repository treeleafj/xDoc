package org.treeleafj.xdoc.format;

import org.treeleafj.xdoc.model.ApiModule;

import java.util.List;

/**
 * 文档输出格式
 * <p>
 * Created by leaf on 2018/6/22.
 */
public interface Formater {

    String format(List<ApiModule> list);
}
