package org.treeleafj.xdoc.spring.format;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.treeleafj.xdoc.model.ApiAction;
import org.treeleafj.xdoc.model.ApiModule;
import org.treeleafj.xdoc.spring.SpringApiAction;
import org.treeleafj.xdoc.spring.SpringApiModule;
import org.treeleafj.xdoc.utils.JsonFormatUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by leaf on 2017/3/4.
 */
public class MarkdownFormat implements Format {

    @Override
    public String format(List<ApiModule> list) {
        StringBuilder sb = new StringBuilder();
        for (ApiModule apiModule : list) {
            SpringApiModule sam = (SpringApiModule) apiModule;
            sb.append(format(apiModule)).append("\n\n");
        }
        return sb.toString();
    }

    @Override
    public String format(ApiModule apiModule) {
        VelocityTemplater templater = new VelocityTemplater("org/treeleafj/xdoc/spring/format/api.vm");

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
            e.printStackTrace();
        }
        return null;
    }
}
