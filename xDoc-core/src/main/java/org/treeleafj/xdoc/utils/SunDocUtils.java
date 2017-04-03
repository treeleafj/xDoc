package org.treeleafj.xdoc.utils;

import com.sun.javadoc.*;
import com.sun.tools.javadoc.AnnotationValueImpl;
import org.apache.commons.collections.map.HashedMap;
import org.treeleafj.xdoc.model.DocTags;
import org.treeleafj.xdoc.resolver.sun.converter.SunTagConverter;
import org.treeleafj.xdoc.resolver.sun.converter.SunTagConverterManager;
import org.treeleafj.xdoc.tag.DocTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Sun javadoc工具类
 *
 * @author leaf
 * @date 2017-03-03 10:34
 */
public class SunDocUtils {

    public static Map<String, Object> getRequestMapperingMeta(ProgramElementDoc elementDoc) {
        AnnotationDesc annotationDesc = null;
        for (AnnotationDesc ad : elementDoc.annotations()) {
            if (ad.annotationType().name().equals("RequestMapping")) {
                annotationDesc = ad;
            }
        }
        if (annotationDesc == null) {
            return null;
        }

        Map<String, Object> map = new HashedMap();
        for (AnnotationDesc.ElementValuePair elementValuePair : annotationDesc.elementValues()) {
            Object val = elementValuePair.value();
            if (val instanceof AnnotationValueImpl) {
                AnnotationValue annoVal = (AnnotationValue) val;
                Object value = annoVal.value();
                if (value.getClass().isArray()) {
                    AnnotationValue[] array = (AnnotationValue[]) value;
                    List<Object> list = new ArrayList();
                    for (AnnotationValue annotationValue : array) {
                        Object v = annotationValue.value();
                        if (v instanceof FieldDoc) {// 注解里的属性是个常量
                            v = ((FieldDoc) v).name();
                        }
                        list.add(v);
                    }
                    map.put(elementValuePair.element().name(), list);
                } else {
                    AnnotationValue one = (AnnotationValue) value;
                    map.put(elementValuePair.element().name(), one.value());
                }
            } else {
                map.put(elementValuePair.element().name(), val.toString());
            }
        }
        return map;
    }

    /**
     * 获取元素上的所有基于便签的注释信息
     *
     * @param elementDoc
     * @return
     */
    public static DocTags getDocsForTag(ProgramElementDoc elementDoc) {
        Tag[] tags = elementDoc.tags();

        DocTags docTags = new DocTags(new ArrayList<DocTag>(tags.length));
        for (Tag tag : tags) {

            SunTagConverter tagConverter = SunTagConverterManager.getConverter(tag.name());
            if (tagConverter == null) {
                tagConverter = SunTagConverterManager.getDefaultConverter();
            }

            DocTag docTag = tagConverter.converter(tag);
            docTags.getList().add(docTag);
        }
        return docTags;
    }
}
