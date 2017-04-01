package org.treeleafj.xdoc.resolver.sun.converter;


import java.util.HashMap;
import java.util.Map;

/**
 * 标签转换器注册器
 * <p>
 * Created by leaf on 2017/3/4.
 */
public class XDocConfig {

    private static Map<String, TagConverter> registrator = new HashMap<>();

    private static TagConverter defaultTagConverter = new DefaultTagConverterImpl();

    static {
        registerConverter("@param", new ParamTagConverter());
        registerConverter("@see", new SeeTagConverter());
        registerConverter("@resp", new RespTagConverter());
    }

    public static void registerConverter(String tagName, TagConverter tagConverter) {
        registrator.put(tagName, tagConverter);
    }

    public static TagConverter getConverter(String tagName) {
        for (Map.Entry<String, TagConverter> entry : registrator.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(tagName)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static TagConverter getDefaultConverter() {
        return defaultTagConverter;
    }
}
