package org.treeleafj.xdoc.format;

import org.treeleafj.xdoc.model.ApiDoc;

/**
 * 文档输出格式
 * <p>
 * Created by leaf on 2018/6/22.
 */
public interface Format {

    String format(ApiDoc apiDoc);
}
