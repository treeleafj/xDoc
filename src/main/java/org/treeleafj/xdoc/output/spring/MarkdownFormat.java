package org.treeleafj.xdoc.output.spring;

import org.apache.commons.beanutils.PropertyUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by leaf on 2017/3/4.
 */
public class MarkdownFormat implements Format {

    @Override
    public String format(List<SpringApiModule> list) {
        StringBuilder sb = new StringBuilder();
        for (SpringApiModule apiModule : list) {
            sb.append(format(apiModule)).append("\n\n");
        }
        return sb.toString();
    }

    @Override
    public String format(SpringApiModule apiModule) {
        VelocityTemplater templater = new VelocityTemplater("org/treeleafj/xdoc/output/spring/api.vm");
        try {
            Map<String, Object> map = PropertyUtils.describe(apiModule);
            return templater.parse(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
