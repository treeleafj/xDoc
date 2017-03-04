package org.treeleafj.xdoc.output.spring;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.treeleafj.xdoc.model.*;
import org.treeleafj.xdoc.output.XDocOutput;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
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
            sam.setJson(isJson(apiModule.getType()));

            RequestMapping classRequestMappingAnno = apiModule.getType().getAnnotation(RequestMapping.class);

            sam.setUris(this.getUris(classRequestMappingAnno));
            sam.setMethods(this.getMethods(classRequestMappingAnno));

            for (ApiAction apiAction : apiModule.getApiActions()) {
                SpringApiAction saa = new SpringApiAction();
                saa.setName(apiAction.getName());
                saa.setComment(apiAction.getComment());
                saa.setMethod(apiAction.getMethod());
                saa.setDocTags(apiAction.getDocTags());

                RequestMapping methodRequestMappingAnno = apiAction.getMethod().getAnnotation(RequestMapping.class);

                saa.setUris(this.getUris(methodRequestMappingAnno));
                saa.setMethods(this.getMethods(methodRequestMappingAnno));

                List<DocTag> paramTags = apiAction.getDocTags().getTags("@param");
                for (DocTag paramTag : paramTags) {
                    ParamTagImpl tag = (ParamTagImpl) paramTag;
                    saa.getParam().put(tag.getParamName(), tag.getParamDesc());
                }

                SeeTagImpl seeTag = (SeeTagImpl) apiAction.getDocTags().getTag("@see");
                if (seeTag != null) {
                    saa.setReturnObj(seeTag.getValues());
                }

                DocTag returnTag = apiAction.getDocTags().getTag("@return");
                if (returnTag != null) {
                    saa.setReturnDesc(returnTag.getValues().toString());
                }

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

    private List<String> getUris(RequestMapping requestMappingAnno) {
        String[] values = requestMappingAnno.value();
        List<String> uris = new ArrayList<String>();
        for (String value : values) {
            uris.add(value);
        }
        return uris;
    }

    private List<String> getMethods(RequestMapping requestMappingAnno) {
        List<String> methods = new ArrayList<String>();
        RequestMethod[] method = requestMappingAnno.method();
        for (RequestMethod requestMethod : method) {
            methods.add(requestMethod.name());
        }
        return methods;
    }

    private boolean isJson(Class<?> classz) {
        Controller controllerAnno = classz.getAnnotation(Controller.class);
        RestController restControllerAnno = classz.getAnnotation(RestController.class);

        if (controllerAnno != null) {
            return false;
        } else if (restControllerAnno != null) {
            return true;
        }
        return false;
    }
}
