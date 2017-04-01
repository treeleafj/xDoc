package org.treeleafj.xdoc.resolver;

import java.util.List;

/**
 * 注释解析器接口,所有的解析器实现都要继承此接口
 * <p>
 * 现有的实现有基于sun javadoc, 也有基于开源的javaparser
 * <p>
 * Created by leaf on 2017/4/1 0001.
 */
public interface DocTagResolver {

    /**
     * 执行解析
     *
     * @param files 要解析的所有java源代码文件的绝对路径
     */
    void resolve(List<String> files);
}
