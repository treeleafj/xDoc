package org.treeleafj.xdoc.format.http;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.treeleafj.xdoc.format.Format;
import org.treeleafj.xdoc.model.ApiDoc;
import org.treeleafj.xdoc.utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by leaf on 2017/3/18 0018.
 */
public class HtmlForamt implements Format {

    @Override
    public String format(ApiDoc apiDoc) {
        InputStream in = HtmlForamt.class.getResourceAsStream("html.vm");
        if (in != null) {
            try {
                String s = IOUtils.toString(in, "utf-8");

                Map<String, Object> model = new HashMap<>();
                model.put("title", StringUtils.defaultString((String) apiDoc.getProperties().get("title"), "接口文档"));
                model.put("apiModules", apiDoc.getApiModules());

                return s.replace("_apis_json", JsonUtils.toJson(model));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
        return "";
    }
}
