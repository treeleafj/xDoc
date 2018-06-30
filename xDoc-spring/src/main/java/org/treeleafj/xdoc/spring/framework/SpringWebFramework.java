package org.treeleafj.xdoc.spring.framework;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.treeleafj.xdoc.framework.Framework;
import org.treeleafj.xdoc.model.ApiAction;
import org.treeleafj.xdoc.model.ApiModule;
import org.treeleafj.xdoc.model.ObjectInfo;
import org.treeleafj.xdoc.tag.DocTag;
import org.treeleafj.xdoc.tag.ParamTagImpl;
import org.treeleafj.xdoc.tag.RespTagImpl;
import org.treeleafj.xdoc.tag.SeeTagImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于spirng web框架,扩展api数据
 * <p>
 * Created by leaf on 2018/6/22.
 */
public class SpringWebFramework extends Framework {

    @Override
    public boolean support(Class<?> classz) {
        if (classz.getAnnotation(Controller.class) != null
                || classz.getAnnotation(RestController.class) != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<ApiModule> extend(List<ApiModule> apiModules) {
        if (apiModules == null) {
            return new ArrayList<>(0);
        }
        List<ApiModule> list = new ArrayList<>(apiModules.size());
        for (ApiModule apiModule : apiModules) {
            SpringApiModule sam = new SpringApiModule();

            sam.setComment(apiModule.getComment());
            sam.setType(apiModule.getType());
            boolean isjson = this.isJson(apiModule.getType());
            sam.setJson(isjson);

            RequestMapping classRequestMappingAnno = apiModule.getType().getAnnotation(RequestMapping.class);

            if (classRequestMappingAnno != null) {
                sam.setUris(this.getUris(classRequestMappingAnno.value()));
                sam.setMethods(this.getMethods(classRequestMappingAnno.method()));
            } else {
                sam.setUris(new ArrayList<String>(0));
                sam.setMethods(new ArrayList<String>(0));
            }

            for (ApiAction apiAction : apiModule.getApiActions()) {
                SpringApiAction saa = this.buildSpringApiAction(apiAction, isjson);
                if (saa != null) {
                    sam.getApiActions().add(saa);
                }
            }

            list.add(sam);
        }
        return list;
    }

    /**
     * 构建基于spring web的接口
     *
     * @param apiAction
     * @param isjson    是否json
     * @return
     */
    private SpringApiAction buildSpringApiAction(ApiAction apiAction, boolean isjson) {
        SpringApiAction saa = new SpringApiAction();
        saa.setName(apiAction.getName());
        saa.setComment(apiAction.getComment());
        saa.setMethod(apiAction.getMethod());
        saa.setDocTags(apiAction.getDocTags());

        if (isjson || apiAction.getMethod().getAnnotation(ResponseBody.class) != null) {
            saa.setJson(true);
        }

        saa.setTitle(getTitile(saa));
        saa.setRespbody(getRespbody(saa));

        boolean isMappingMethod = this.setUrisAndMethods(apiAction, saa);

        if (!isMappingMethod) {
            return null;
        }

        saa.setParam(this.getParams(saa));
        saa.setRespParam(this.getRespParam(saa));
        saa.setReturnObj(this.getReturnObj(saa));
        saa.setReturnDesc(this.getReturnDesc(saa));

        return saa;
    }

    private boolean setUrisAndMethods(ApiAction apiAction, SpringApiAction saa) {
        RequestMapping methodRequestMappingAnno = apiAction.getMethod().getAnnotation(RequestMapping.class);
        if (methodRequestMappingAnno != null) {
            saa.setUris(this.getUris(methodRequestMappingAnno.value()));
            saa.setMethods(this.getMethods(methodRequestMappingAnno.method()));
            return true;
        }

        PostMapping postMapping = apiAction.getMethod().getAnnotation(PostMapping.class);
        if (postMapping != null) {
            saa.setUris(this.getUris(postMapping.value()));
            saa.setMethods(this.getMethods(RequestMethod.POST));
            return true;
        }

        GetMapping getMapping = apiAction.getMethod().getAnnotation(GetMapping.class);
        if (getMapping != null) {
            saa.setUris(this.getUris(getMapping.value()));
            saa.setMethods(this.getMethods(RequestMethod.GET));
            return true;
        }

        PutMapping putMapping = apiAction.getMethod().getAnnotation(PutMapping.class);
        if (putMapping != null) {
            saa.setUris(this.getUris(putMapping.value()));
            saa.setMethods(this.getMethods(RequestMethod.PUT));
            return true;
        }

        DeleteMapping deleteMapping = apiAction.getMethod().getAnnotation(DeleteMapping.class);
        if (deleteMapping != null) {
            saa.setUris(this.getUris(deleteMapping.value()));
            saa.setMethods(this.getMethods(RequestMethod.DELETE));
            return true;
        }

        PatchMapping patchMapping = apiAction.getMethod().getAnnotation(PatchMapping.class);
        if (patchMapping != null) {
            saa.setUris(this.getUris(patchMapping.value()));
            saa.setMethods(this.getMethods(RequestMethod.PATCH));
            return true;
        }
        return false;
    }

    protected String getReturnDesc(SpringApiAction saa) {
        DocTag tag = saa.getDocTags().getTag("@return");
        return tag != null ? tag.getValues().toString() : null;
    }

    protected ObjectInfo getReturnObj(SpringApiAction saa) {
        SeeTagImpl tag = (SeeTagImpl) saa.getDocTags().getTag("@see");
        return tag != null ? tag.getValues() : null;
    }

    protected List<ParamInfo> getParams(SpringApiAction saa) {
        List tags = saa.getDocTags().getTags("@param");
        List<ParamInfo> paramInfos = new ArrayList<ParamInfo>(tags.size());
        for (Object tag : tags) {
            ParamTagImpl paramTag = (ParamTagImpl) tag;
            ParamInfo paramInfo = new ParamInfo();
            paramInfo.setParamName(paramTag.getParamName());
            paramInfo.setParamDesc(paramTag.getParamDesc());
            paramInfo.setParamType(paramTag.getParamType());
            paramInfo.setRequire(paramTag.isRequire());
            paramInfos.add(paramInfo);
        }
        return paramInfos;
    }

    protected List<ParamInfo> getRespParam(SpringApiAction saa) {
        List<DocTag> tags = saa.getDocTags().getTags("@resp");
        List<ParamInfo> list = new ArrayList(tags.size());
        for (DocTag tag : tags) {
            RespTagImpl respTag = (RespTagImpl) tag;
            ParamInfo paramInfo = new ParamInfo();
            paramInfo.setParamName(respTag.getParamName());
            paramInfo.setRequire(respTag.isRequire());
            paramInfo.setParamDesc(respTag.getParamDesc());
            paramInfo.setParamType(respTag.getParamType());
            list.add(paramInfo);
        }
        return list;
    }

    protected String getRespbody(SpringApiAction saa) {
        DocTag respbodyTag = saa.getDocTags().getTag("@respbody");
        if (respbodyTag != null) {
            return (String) respbodyTag.getValues();
        }
        return null;
    }

    protected String getTitile(SpringApiAction saa) {
        DocTag titleTag = saa.getDocTags().getTag("@title");
        if (titleTag != null) {
            return (String) titleTag.getValues();
        } else {
            return saa.getComment();
        }
    }

    /**
     * 获取接口的uri
     *
     * @return
     */
    protected List<String> getUris(String[] values) {
        List<String> uris = new ArrayList<String>();
        for (String value : values) {
            uris.add(value);
        }
        return uris;
    }

    /**
     * 获取接口上允许的访问方式
     *
     * @return
     */
    protected List<String> getMethods(RequestMethod... methods) {
        List<String> methodStrs = new ArrayList<String>();
        for (RequestMethod requestMethod : methods) {
            methodStrs.add(requestMethod.name());
        }
        return methodStrs;
    }

    /**
     * 判断整个类里的所有接口是否都返回json
     *
     * @param classz
     * @return
     */
    protected boolean isJson(Class<?> classz) {
        Controller controllerAnno = classz.getAnnotation(Controller.class);
        RestController restControllerAnno = classz.getAnnotation(RestController.class);
        ResponseBody responseBody = classz.getAnnotation(ResponseBody.class);

        if (responseBody != null) {
            return true;
        } else if (controllerAnno != null) {
            return false;
        } else if (restControllerAnno != null) {
            return true;
        }
        return false;
    }
}
