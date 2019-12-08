package org.treeleafj.xdoc.format.http;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treeleafj.xdoc.format.Format;
import org.treeleafj.xdoc.model.*;
import org.treeleafj.xdoc.model.http.HttpApiAction;
import org.treeleafj.xdoc.model.http.HttpParam;
import org.treeleafj.xdoc.utils.JsonUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by leaf on 2017/3/4.
 */
public class MarkdownFormat implements Format {

    private Logger log = LoggerFactory.getLogger(getClass());

    private VelocityTemplater templater = new VelocityTemplater("org/treeleafj/xdoc/format/http/markdown.vm");

    @Override
    public String format(ApiDoc apiDoc) {
        StringBuilder sb = new StringBuilder();
        for (ApiModule apiModule : apiDoc.getApiModules()) {
            sb.append(format(apiModule)).append("\n\n");
        }
        return sb.toString();
    }

    private String format(ApiModule apiModule) {

        for (ApiAction apiAction : apiModule.getApiActions()) {
            HttpApiAction saa = (HttpApiAction) apiAction;
            if (saa.isJson() && StringUtils.isNotBlank(saa.getRespbody())) {
                saa.setRespbody(JsonUtils.formatJson(saa.getRespbody()));
            }

            ObjectInfo returnObj = saa.getReturnObj();
            if (returnObj != null && returnObj.getFieldInfos() != null) {
                //将@resp标签跟@return标签中重复的属性进行去重,以@resp的为准
                Set<String> paramNames = new HashSet<>();
                for (HttpParam param : saa.getRespParam()) {
                    paramNames.add(param.getParamName());
                }

                for (FieldInfo fieldInfo : returnObj.getFieldInfos()) {
                    if (paramNames.contains(fieldInfo.getName())) {
                        continue;
                    }
                    HttpParam param = toHttpParam(fieldInfo);
                    saa.getRespParam().add(param);
                }
            }

            if (saa.getParamObjs().size() > 0) {
                //将@param跟@paramObj标签中重复的属性进行去重,以@param中的为准
                Set<String> paramNames = new HashSet<>();
                for (HttpParam param : saa.getParams()) {
                    paramNames.add(param.getParamName());
                }

                for (ObjectInfo paramObj : saa.getParamObjs()) {
                    for (FieldInfo fieldInfo : paramObj.getFieldInfos()) {
                        if (paramNames.contains(fieldInfo.getName())) {
                            continue;
                        }
                        HttpParam param = toHttpParam(fieldInfo);
                        saa.getParams().add(param);
                    }
                }
            }
        }

        try {
            Map<String, Object> map = PropertyUtils.describe(apiModule);
            return templater.parse(map);
        } catch (Exception e) {
            log.error("输出markdown文档格式失败", e);
        }
        return null;
    }

    private HttpParam toHttpParam(FieldInfo fieldInfo) {
        HttpParam param = new HttpParam();
        param.setParamType(fieldInfo.getSimpleTypeName());
        param.setParamDesc(fieldInfo.getComment());
        param.setParamName(fieldInfo.getName());
        param.setRequire(fieldInfo.isRequire());
        return param;
    }
}
