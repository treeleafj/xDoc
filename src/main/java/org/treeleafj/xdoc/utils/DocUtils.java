package org.treeleafj.xdoc.utils;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationValue;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.SeeTag;
import com.sun.javadoc.Tag;
import com.sun.tools.javadoc.AnnotationValueImpl;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.map.HashedMap;
import org.treeleafj.xdoc.model.Param;
import org.treeleafj.xdoc.model.ParamField;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Administrator
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

    public static Map<String, Object> getDocs(ProgramElementDoc elementDoc) {
        Tag[] tags = elementDoc.tags();
        Map<String, Object> map = new HashedMap();
        for (Tag tag : tags) {

            if (tag instanceof ParamTag) {
                ParamTag _tag = (ParamTag) tag;
                Map<String, String> o = (Map<String, String>) map.get(tag.name());
                if (o == null) {
                    o = new TreeMap<String, String>();
                    map.put(tag.name(), o);
                }
                o.put(_tag.parameterName(), _tag.parameterComment());
            } else if (tag instanceof SeeTag) {
                SeeTag _tag = (SeeTag) tag;

                ClassDoc aClass = _tag.referencedClass().findClass(_tag.referencedClassName());
                String text = aClass.commentText();
                String classType = aClass.qualifiedTypeName();
                try {
                    Class<?> classz = Class.forName(classType);
                    List<ParamField> fields = analysisFields(classz, aClass);
                    Param param = new Param();
                    param.setType(Class.forName(classType));
                    param.setParamFields(fields);
                    param.setComment(text);
                    map.put(_tag.name(), param);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {//其它认为是TagImpl
                map.put(tag.name(), tag.text());
            }
        }
        System.out.println(map);
        return map;
    }


    private static List<ParamField> analysisFields(Class classz, ClassDoc classDoc) {
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(classz);
        FieldDoc[] fieldDocs = classDoc.fields(false);
        List<ParamField> fields = new ArrayList<ParamField>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getName().equals("class")) {
                continue;
            }
            ParamField field = new ParamField();
            field.setTypeName(propertyDescriptor.getPropertyType().getSimpleName());
            field.setType(propertyDescriptor.getPropertyType().getName());
            field.setName(propertyDescriptor.getName());

            for (FieldDoc fieldDoc : fieldDocs) {
                if (fieldDoc.name().equals(propertyDescriptor.getName())) {
                    field.setComment(fieldDoc.commentText());
                }
            }

            fields.add(field);
        }
        return fields;
    }
}
