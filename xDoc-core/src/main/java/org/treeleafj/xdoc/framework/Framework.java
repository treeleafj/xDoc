package org.treeleafj.xdoc.framework;

import org.treeleafj.xdoc.model.ApiModule;

import java.util.List;

/**
 * 抽象各种API框架的特性,用于在基于xDoc-core渲染出来的ApiModule基础中,进行再度包装
 * <p>
 *
 * @author leaf
 * @date 2018/6/22
 */
public interface Framework {

    /**
     * 扩展API数据
     *
     * @param apiModules 原始基本的Api数据
     * @return 扩展后的api数据
     */
    List<ApiModule> extend(List<ApiModule> apiModules);

    /**
     * 判断该类是否适合该框架
     *
     * @param classz 扫描到的类
     * @return 是支持
     */
    boolean support(Class<?> classz);
}
