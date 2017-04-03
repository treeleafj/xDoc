package org.treeleafj.xdoc.resolver.javaparser.converter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leaf on 2017/4/3 0003.
 */
public class JavaParserTagConverterManager {

    private static Map<String, JavaParserTagConverter> registrator = new HashMap<>();

    private static JavaParserTagConverter defaultTagConverter = new DefaultJavaParserTagConverterImpl();

    static {
        registerConverter("@param", new ParamTagConverter());
        registerConverter("@see", new SeeTagConverter());
        registerConverter("@resp", new RespTagConverter());
    }

    public static void registerConverter(String tagName, JavaParserTagConverter tagConverter) {
        registrator.put(tagName, tagConverter);
    }

    public static JavaParserTagConverter getConverter(String tagName) {
        for (Map.Entry<String, JavaParserTagConverter> entry : registrator.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(tagName)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static JavaParserTagConverter getDefaultConverter() {
        return defaultTagConverter;
    }
}
