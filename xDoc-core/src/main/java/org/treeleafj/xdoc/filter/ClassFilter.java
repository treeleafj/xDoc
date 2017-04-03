package org.treeleafj.xdoc.filter;

/**
 * Created by leaf on 2017/4/3 0003.
 */
public interface ClassFilter {

    /**
     * 过滤类型
     *
     * @param classz
     * @return 是否放行
     */
    boolean filter(Class<?> classz);


}
