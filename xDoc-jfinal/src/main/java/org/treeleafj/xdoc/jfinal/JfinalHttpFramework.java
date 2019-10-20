package org.treeleafj.xdoc.jfinal;

import com.jfinal.config.Routes;
import com.jfinal.core.ConfigGetter;
import com.jfinal.core.Controller;
import org.treeleafj.xdoc.framework.AbstractHttpFramework;
import org.treeleafj.xdoc.model.ApiModule;
import org.treeleafj.xdoc.model.http.HttpApiAction;

import java.util.*;

/**
 * 基于JFinal框架,扩展api接口数据
 *
 * @auth leaf
 * @date 2019/10/20
 */
public class JfinalHttpFramework extends AbstractHttpFramework {

    @Override
    public boolean support(Class<?> classz) {
        return Controller.class.isAssignableFrom(classz);
    }

    @Override
    public List<ApiModule> extend(List<ApiModule> apiModules) {

        apiModules = super.extend(apiModules);


        Routes routes = ConfigGetter.getRoutes();
        List<Routes.Route> routeItemList = routes.getRouteItemList();

        Map<Class, String> controllerMap = new HashMap<>();
        for (Routes.Route route : routeItemList) {
            controllerMap.put(route.getControllerClass(), route.getControllerKey());
        }

        for (ApiModule apiModule : apiModules) {
            for (int i = 0; i < apiModule.getApiActions().size(); i++) {
                HttpApiAction apiAction = (HttpApiAction) apiModule.getApiActions().get(i);
                apiAction.setJson(false);//TODO 该属性需要去掉
                apiAction.setUris(Arrays.asList(controllerMap.get(apiModule.getType()) + "/" + apiAction.getMethod().getName()));
                apiAction.setMethods(Arrays.asList("ALL"));
            }
        }

        return apiModules;
    }


}
