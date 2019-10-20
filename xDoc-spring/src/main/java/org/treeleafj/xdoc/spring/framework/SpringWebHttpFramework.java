package org.treeleafj.xdoc.spring.framework;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.treeleafj.xdoc.framework.AbstractHttpFramework;
import org.treeleafj.xdoc.model.ApiAction;
import org.treeleafj.xdoc.model.ApiModule;
import org.treeleafj.xdoc.model.http.HttpApiAction;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于spirng web框架,扩展api接口数据
 * <p>
 *
 * @author leaf
 * @date 2018/6/22
 */
public class SpringWebHttpFramework extends AbstractHttpFramework {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean support(Class<?> classz) {
        return classz.getAnnotation(Controller.class) != null
                || classz.getAnnotation(RestController.class) != null;
    }

    @Override
    public List<ApiModule> extend(List<ApiModule> apiModules) {
        apiModules = super.extend(apiModules);

        List<ApiModule> newApiModules = new ArrayList<>();

        for (ApiModule apiModule : apiModules) {

            ApiModule newApiModule = new ApiModule();
            newApiModule.setComment(apiModule.getComment());
            newApiModule.setType(apiModule.getType());
            boolean isjson = this.isJson(apiModule.getType());

            for (ApiAction apiAction : apiModule.getApiActions()) {
                HttpApiAction saa = this.buildSpringApiAction(newApiModule, apiAction, isjson);
                if (saa != null) {
                    newApiModule.getApiActions().add(saa);
                }
            }

            newApiModules.add(newApiModule);
        }
        return newApiModules;
    }

    /**
     * 构建基于spring web的接口
     *
     * @param apiAction 请求的Action信息
     * @param isjson    是否json
     * @return 封装后的机遇SpringWeb的Action信息
     */
    private HttpApiAction buildSpringApiAction(ApiModule apiModule, ApiAction apiAction, boolean isjson) {


        HttpApiAction saa = new HttpApiAction();

        try {
            BeanUtils.copyProperties(saa, apiAction);
        } catch (Exception e) {
            logger.error("copy ApiAction to HttpApiAction properties error", e);
            return null;
        }


        if (isjson || apiAction.getMethod().getAnnotation(ResponseBody.class) != null) {
            saa.setJson(true);
        }

        boolean isMappingMethod = this.setUrisAndMethods(apiModule, apiAction, saa);

        if (!isMappingMethod) {
            return null;
        }

        return saa;
    }

    /**
     * 设置请求地址和请求方法
     */
    private boolean setUrisAndMethods(ApiModule apiModule, ApiAction apiAction, HttpApiAction saa) {
        RequestMapping classRequestMappingAnno = apiModule.getType().getAnnotation(RequestMapping.class);
        String[] parentPath = new String[0];
        if (classRequestMappingAnno != null) {
            parentPath = classRequestMappingAnno.value();
        }


        RequestMapping methodRequestMappingAnno = apiAction.getMethod().getAnnotation(RequestMapping.class);
        if (methodRequestMappingAnno != null) {
            saa.setUris(this.getUris(parentPath, methodRequestMappingAnno.value()));
            saa.setMethods(this.getMethods(methodRequestMappingAnno.method()));
            return true;
        }

        PostMapping postMapping = apiAction.getMethod().getAnnotation(PostMapping.class);
        if (postMapping != null) {
            saa.setUris(this.getUris(parentPath, postMapping.value()));
            saa.setMethods(this.getMethods(RequestMethod.POST));
            return true;
        }

        GetMapping getMapping = apiAction.getMethod().getAnnotation(GetMapping.class);
        if (getMapping != null) {
            saa.setUris(this.getUris(parentPath, getMapping.value()));
            saa.setMethods(this.getMethods(RequestMethod.GET));
            return true;
        }

        PutMapping putMapping = apiAction.getMethod().getAnnotation(PutMapping.class);
        if (putMapping != null) {
            saa.setUris(this.getUris(parentPath, putMapping.value()));
            saa.setMethods(this.getMethods(RequestMethod.PUT));
            return true;
        }

        DeleteMapping deleteMapping = apiAction.getMethod().getAnnotation(DeleteMapping.class);
        if (deleteMapping != null) {
            saa.setUris(this.getUris(parentPath, deleteMapping.value()));
            saa.setMethods(this.getMethods(RequestMethod.DELETE));
            return true;
        }

        PatchMapping patchMapping = apiAction.getMethod().getAnnotation(PatchMapping.class);
        if (patchMapping != null) {
            saa.setUris(this.getUris(parentPath, patchMapping.value()));
            saa.setMethods(this.getMethods(RequestMethod.PATCH));
            return true;
        }
        return false;
    }

    /**
     * 获取接口的uri
     */
    protected List<String> getUris(String[] parentPaths, String[] values) {

        if (parentPaths.length == 0) {
            parentPaths = new String[]{""};
        }

        List<String> uris = new ArrayList<>(1);
        for (String parentPath : parentPaths) {
            for (String value : values) {
                String uri;
                if (parentPath.endsWith("/") && value.startsWith("/")) {
                    uri = parentPath.substring(0, parentPath.length() - 1) + value;
                } else if (parentPath.length() > 0 && !parentPath.endsWith("/") && !value.startsWith("/")) {
                    uri = parentPath + '/' + value;
                } else {
                    uri = parentPath + value;
                }
                uris.add(uri);
            }
        }


        return uris;
    }

    /**
     * 获取接口上允许的访问方式
     */
    protected List<String> getMethods(RequestMethod... methods) {
        List<String> methodStrs = new ArrayList<>();
        for (RequestMethod requestMethod : methods) {
            methodStrs.add(requestMethod.name());
        }
        return methodStrs;
    }

    /**
     * 判断整个类里的所有接口是否都返回json
     */
    protected boolean isJson(Class<?> classz) {
        RestController restControllerAnno = classz.getAnnotation(RestController.class);
        ResponseBody responseBody = classz.getAnnotation(ResponseBody.class);
        return responseBody != null || restControllerAnno != null;
    }
}
