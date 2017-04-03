package org.treeleafj.xdoc.resolver.sun.converter;


import java.util.HashMap;
import java.util.Map;

/**
 * 基于sun javadoc的标签转换管理器
 * <p>
 * Created by leaf on 2017/3/4.
 */
public class SunTagConverterManager {

    private static Map<String, SunTagConverter> registrator = new HashMap<>();

    private static SunTagConverter defaultTagConverter = new DefaultSunTagConverterImpl();

    static {
        registerConverter("@param", new ParamTagConverter());
        registerConverter("@see", new SeeTagConverter());
        registerConverter("@resp", new RespTagConverter());
    }

    public static void registerConverter(String tagName, SunTagConverter tagConverter) {
        registrator.put(tagName, tagConverter);
    }

    public static SunTagConverter getConverter(String tagName) {
        for (Map.Entry<String, SunTagConverter> entry : registrator.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(tagName)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static SunTagConverter getDefaultConverter() {
        return defaultTagConverter;
    }
}
