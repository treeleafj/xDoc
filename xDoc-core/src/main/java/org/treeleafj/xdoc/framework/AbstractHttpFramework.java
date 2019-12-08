package org.treeleafj.xdoc.framework;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treeleafj.xdoc.model.ApiAction;
import org.treeleafj.xdoc.model.ApiModule;
import org.treeleafj.xdoc.model.ObjectInfo;
import org.treeleafj.xdoc.model.http.HttpApiAction;
import org.treeleafj.xdoc.model.http.HttpParam;
import org.treeleafj.xdoc.tag.*;
import org.treeleafj.xdoc.utils.TagUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供获取通用注释上的信息
 */
public abstract class AbstractHttpFramework implements Framework {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<ApiModule> extend(List<ApiModule> apiModules) {
        for (ApiModule apiModule : apiModules) {
            for (int i = 0; i < apiModule.getApiActions().size(); i++) {
                ApiAction oldApiAction = apiModule.getApiActions().get(i);
                HttpApiAction newApiAction = new HttpApiAction();
                try {
                    BeanUtils.copyProperties(newApiAction, oldApiAction);
                } catch (Exception e) {
                    logger.error("copy ApiAction to HttpApiAction properties error", e);
                    return new ArrayList<>(0);
                }

                newApiAction.setTitle(this.getTitile(newApiAction));
                newApiAction.setRespbody(this.getRespbody(newApiAction));
                newApiAction.setParams(this.getParams(newApiAction));
                newApiAction.setRespParam(this.getResp(newApiAction));
                newApiAction.setReturnObj(this.getSeeObj(newApiAction));
                newApiAction.setReturnDesc(this.getReturnDesc(newApiAction));
                newApiAction.setParamObjs(this.getParamObjs(newApiAction));

                apiModule.getApiActions().set(i, newApiAction);
            }
        }

        return apiModules;
    }

    /**
     * 获取@title上的信息
     */
    protected String getTitile(ApiAction aa) {
        DocTag titleTag = TagUtils.findTag(aa.getDocTags(), "@title");
        if (titleTag != null) {
            return (String) titleTag.getValues();
        } else {
            return aa.getComment();
        }
    }

    /**
     * 获取@respbody上的信息
     */
    protected String getRespbody(ApiAction aa) {
        DocTag respbodyTag = TagUtils.findTag(aa.getDocTags(), "@respbody");
        if (respbodyTag != null) {
            return (String) respbodyTag.getValues();
        }
        return null;
    }


    /**
     * 获取@param注释上的信息
     */
    protected List<HttpParam> getParams(ApiAction aa) {
        List tags = TagUtils.findTags(aa.getDocTags(), "@param");
        List<HttpParam> paramInfos = new ArrayList<>(tags.size());
        for (Object tag : tags) {
            ParamTagImpl paramTag = (ParamTagImpl) tag;
            HttpParam paramInfo = new HttpParam();
            paramInfo.setParamName(paramTag.getParamName());
            paramInfo.setParamDesc(paramTag.getParamDesc());
            paramInfo.setParamType(paramTag.getParamType());
            paramInfo.setRequire(paramTag.isRequire());
            paramInfos.add(paramInfo);
        }
        return paramInfos;
    }

    /**
     * 获取@resp注释上的信息
     */
    protected List<HttpParam> getResp(ApiAction aa) {
        List<DocTag> tags = TagUtils.findTags(aa.getDocTags(), "@resp");
        List<HttpParam> list = new ArrayList(tags.size());
        for (DocTag tag : tags) {
            RespTagImpl respTag = (RespTagImpl) tag;
            HttpParam paramInfo = new HttpParam();
            paramInfo.setParamName(respTag.getParamName());
            paramInfo.setRequire(respTag.isRequire());
            paramInfo.setParamDesc(respTag.getParamDesc());
            paramInfo.setParamType(respTag.getParamType());
            list.add(paramInfo);
        }
        return list;
    }

    /**
     * 获取@return注释上的描述语
     */
    protected String getReturnDesc(ApiAction aa) {
        DocTag tag = TagUtils.findTag(aa.getDocTags(), "@return");
        return tag != null ? tag.getValues().toString() : null;
    }

    /**
     * 获取@see注释上的对象
     */
    protected ObjectInfo getSeeObj(ApiAction aa) {
        SeeTagImpl tag = (SeeTagImpl) TagUtils.findTag(aa.getDocTags(), "@see");
        return tag != null ? tag.getValues() : null;
    }

    /**
     * 获取@paramObj注解上的对象
     */
    private List<ObjectInfo> getParamObjs(HttpApiAction aa) {
        List<DocTag> tags = TagUtils.findTags(aa.getDocTags(), "@paramObj");
        List<ObjectInfo> paramObjs = new ArrayList<>(tags.size());
        for (DocTag tag : tags) {
            ParamObjTagImpl paramObjTag = (ParamObjTagImpl) tag;
            paramObjs.add(paramObjTag.getValues());
        }
        return paramObjs;
    }
}
