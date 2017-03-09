package org.treeleafj.xdoc.utils;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationValue;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.Tag;
import com.sun.tools.javadoc.AnnotationValueImpl;
import org.apache.commons.collections.map.HashedMap;
import org.treeleafj.xdoc.converter.TagConverter;
import org.treeleafj.xdoc.converter.XDocConfig;
import org.treeleafj.xdoc.model.DocTags;
import org.treeleafj.xdoc.tag.DocTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author leaf
 * @date 2017-03-03 10:34
 */
public class DocUtils {

    public static boolean isController(ClassDoc classDoc) {
        for (AnnotationDesc annotationDesc : classDoc.annotations()) {
            if (annotationDesc.annotationType().name().equals("Controller")) {
                return true;
            }
        }
        return false;
    }

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

            TagConverter tagConverter = XDocConfig.getConverter(tag.getClass());
            if (tagConverter == null) {
                tagConverter = XDocConfig.getDefaultConverter();
            }

            DocTag docTag = tagConverter.converter(tag);
            docTags.getList().add(docTag);
        }
        return docTags;
    }
}
