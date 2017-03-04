package org.treeleafj.xdoc.converter;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.SeeTag;
import org.apache.commons.beanutils.PropertyUtils;
import org.treeleafj.xdoc.model.DocTag;
import org.treeleafj.xdoc.model.FieldInfo;
import org.treeleafj.xdoc.model.ObjectInfo;
import org.treeleafj.xdoc.model.SeeTagImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leaf on 2017/3/4.
 */
public class SeeTagConverter implements TagConverter<SeeTag> {

    @Override
    public DocTag converter(SeeTag o) {
        ClassDoc aClass = o.referencedClass().findClass(o.referencedClassName());
        String text = aClass.commentText();
        String classType = aClass.qualifiedTypeName();
        try {
            Class<?> classz = Class.forName(classType);
            List<FieldInfo> fields = analysisFields(classz, aClass);
            ObjectInfo objectInfo = new ObjectInfo();
            objectInfo.setType(Class.forName(classType));
            objectInfo.setFieldInfos(fields);
            objectInfo.setComment(text);
            return new SeeTagImpl(o.name(), objectInfo);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private List<FieldInfo> analysisFields(Class classz, ClassDoc classDoc) {
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(classz);
        FieldDoc[] fieldDocs = classDoc.fields(false);
        List<FieldInfo> fields = new ArrayList<FieldInfo>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getName().equals("class")) {
                continue;
            }
            FieldInfo field = new FieldInfo();
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
