package org.treeleafj.xdoc.utils;

import org.treeleafj.xdoc.model.ApiModule;

import java.util.List;

/**
 * Created by leaf on 2017/3/4.
 */
public class ApiModulesHolder {

    private static ThreadLocal<List<ApiModule>> threadLocal = new ThreadLocal<List<ApiModule>>();

    public static void setCurrentApiModules(List<ApiModule> apiModules) {
        threadLocal.set(apiModules);
    }

    public static List<ApiModule> getCurrentApiModules() {
        return threadLocal.get();
    }


}
