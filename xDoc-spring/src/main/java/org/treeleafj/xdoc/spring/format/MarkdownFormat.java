package org.treeleafj.xdoc.spring.format;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treeleafj.xdoc.format.Format;
import org.treeleafj.xdoc.model.ApiAction;
import org.treeleafj.xdoc.model.ApiDoc;
import org.treeleafj.xdoc.model.ApiModule;
import org.treeleafj.xdoc.spring.framework.SpringApiAction;
import org.treeleafj.xdoc.utils.JsonFormatUtils;

import java.util.Map;

/**
 * Created by leaf on 2017/3/4.
 */
public class MarkdownFormat implements Format {

    private Logger log = LoggerFactory.getLogger(getClass());

    private VelocityTemplater templater = new VelocityTemplater("org/treeleafj/xdoc/spring/format/api.vm");

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
            SpringApiAction saa = (SpringApiAction) apiAction;
            if (saa.isJson() && StringUtils.isNotBlank(saa.getRespbody())) {
                saa.setRespbody(JsonFormatUtils.formatJson(saa.getRespbody()));
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
