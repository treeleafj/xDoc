package org.treeleafj.xdoc.spring;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.treeleafj.xdoc.model.ApiAction;
import org.treeleafj.xdoc.model.ApiModule;
import org.treeleafj.xdoc.model.ObjectInfo;
import org.treeleafj.xdoc.output.XDocOutput;
import org.treeleafj.xdoc.spring.format.Format;
import org.treeleafj.xdoc.spring.format.MarkdownFormat;
import org.treeleafj.xdoc.tag.DocTag;
import org.treeleafj.xdoc.tag.ParamTagImpl;
import org.treeleafj.xdoc.tag.SeeTagImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于spring-web的接口文档生成工具
 *
 * Created by leaf on 2017/3/4.
 */
public class SpringXDocOutputImpl implements XDocOutput {

    private Format format = new MarkdownFormat();

    private OutputStream out;

    public SpringXDocOutputImpl(OutputStream out) {
        this.out = out;
    }

    public SpringXDocOutputImpl(OutputStream out, Format format) {
        this.out = out;
        this.format = format;
    }

    @Override
    public void output(List<ApiModule> apiModules) {
        List<SpringApiModule> list = new ArrayList<SpringApiModule>(apiModules.size());
        for (ApiModule apiModule : apiModules) {
            SpringApiModule sam = new SpringApiModule();

            sam.setComment(apiModule.getComment());
            sam.setType(apiModule.getType());
            boolean isjson = this.isJson(apiModule.getType());
            sam.setJson(isjson);

            RequestMapping classRequestMappingAnno = apiModule.getType().getAnnotation(RequestMapping.class);

            sam.setUris(this.getUris(classRequestMappingAnno));
            sam.setMethods(this.getMethods(classRequestMappingAnno));

            for (ApiAction apiAction : apiModule.getApiActions()) {
                SpringApiAction saa = this.buildSpringApiAction(apiAction, isjson);
                sam.getApiActions().add(saa);
            }

            list.add(sam);
        }

        if (out != null) {
            String s = format.format(list);
            try {
                IOUtils.write(s, out, "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

        RequestMapping methodRequestMappingAnno = apiAction.getMethod().getAnnotation(RequestMapping.class);

        saa.setUris(this.getUris(methodRequestMappingAnno));
        saa.setMethods(this.getMethods(methodRequestMappingAnno));
        saa.getParam().addAll(this.getParams(saa));
        saa.setReturnObj(this.getReturnObj(saa));
        saa.setReturnDesc(this.getReturnDesc(saa));

        return saa;
    }

    protected String getReturnDesc(SpringApiAction saa) {
        DocTag tag = saa.getDocTags().getTag("@return");
        return tag != null ? tag.getValues().toString() : null;
    }

    protected ObjectInfo getReturnObj(SpringApiAction saa) {
        SeeTagImpl tag = (SeeTagImpl) saa.getDocTags().getTag("@see");
        return tag != null ? tag.getValues() : null;
    }

    protected List<ParamTagImpl> getParams(SpringApiAction saa) {
        List paramTags = saa.getDocTags().getTags("@param");
        return paramTags;
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
     * @param requestMappingAnno
     * @return
     */
    protected List<String> getUris(RequestMapping requestMappingAnno) {
        String[] values = requestMappingAnno.value();
        List<String> uris = new ArrayList<String>();
        for (String value : values) {
            uris.add(value);
        }
        return uris;
    }

    /**
     * 获取接口上允许的访问方式
     *
     * @param requestMappingAnno
     * @return
     */
    protected List<String> getMethods(RequestMapping requestMappingAnno) {
        List<String> methods = new ArrayList<String>();
        RequestMethod[] method = requestMappingAnno.method();
        for (RequestMethod requestMethod : method) {
            methods.add(requestMethod.name());
        }
        return methods;
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
