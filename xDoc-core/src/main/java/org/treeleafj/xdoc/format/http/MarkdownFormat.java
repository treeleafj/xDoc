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

import java.util.List;
import java.util.Map;

/**
 * Created by leaf on 2017/3/4.
 */
public class MarkdownFormat implements Format {

    private Logger log = LoggerFactory.getLogger(getClass());

    private VelocityTemplater templater = new VelocityTemplater("org/treeleafj/xdoc/format/http/api.vm");

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
                for (HttpParam httpParam : saa.getRespParam()) {
                    for (int i = returnObj.getFieldInfos().size() - 1; i >= 0; i--) {
                        FieldInfo fieldInfo = returnObj.getFieldInfos().get(i);
                        if (StringUtils.equals(httpParam.getParamName(), fieldInfo.getName())) {
                            returnObj.getFieldInfos().remove(i);
                            break;
                        }
                    }
                }

                for (FieldInfo fieldInfo : returnObj.getFieldInfos()) {
                    HttpParam param = new HttpParam();
                    param.setParamType(fieldInfo.getSimpleTypeName());
                    param.setParamDesc(fieldInfo.getComment());
                    param.setParamName(fieldInfo.getName());
                    param.setRequire(fieldInfo.isRequire());
                    saa.getRespParam().add(param);
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
}
