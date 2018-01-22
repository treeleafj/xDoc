package org.treeleafj.xdoc.resolver.javaparser.converter;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treeleafj.xdoc.model.FieldInfo;
import org.treeleafj.xdoc.model.ObjectInfo;
import org.treeleafj.xdoc.utils.CommentUtils;
import org.treeleafj.xdoc.tag.DocTag;
import org.treeleafj.xdoc.tag.SeeTagImpl;
import org.treeleafj.xdoc.utils.ClassMapperUtils;

import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leaf on 2017/3/4.
 */
public class SeeTagConverter extends DefaultJavaParserTagConverterImpl {

    private Logger log = LoggerFactory.getLogger(SeeTagConverter.class);

    @Override
    public DocTag converter(String o) {
        DocTag _docTag = super.converter(o);

        String path = ClassMapperUtils.getPath((String) _docTag.getValues());
        if (StringUtils.isBlank(path)) {
            return null;
        }

        Class<?> returnClassz;
        CompilationUnit cu;
        try (FileInputStream in = new FileInputStream(path)) {
            cu = JavaParser.parse(in);
            if (cu.getTypes().size() <= 0) {
                return null;
            }
            returnClassz = Class.forName(cu.getPackage().getName().toString() + "." + cu.getTypes().get(0).getName());

        } catch (Exception e) {
            log.error("读取java原文件失败:{}", path, e);
            return null;
        }

        String text = cu.getComment() != null ? CommentUtils.parseCommentText(cu.getComment().toString()) : "";

        List<FieldInfo> fields = analysisFields(returnClassz, cu);
        ObjectInfo objectInfo = new ObjectInfo();
        objectInfo.setType(returnClassz);
        objectInfo.setFieldInfos(fields);
        objectInfo.setComment(text);
        return new SeeTagImpl(_docTag.getName(), objectInfo);
    }

    private List<FieldInfo> analysisFields(Class classz, CompilationUnit compilationUnit) {
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(classz);

        final Map<String, String> commentMap = new HashMap();
        new VoidVisitorAdapter<Void>() {
            public void visit(FieldDeclaration n, Void arg) {
                int i = 1;
                String name =  String.valueOf(n.getChildrenNodes().get(i));
                while (String.valueOf(n.getChildrenNodes().get(i - 1)).startsWith("@")) {
                    i++;
                    if (i >= n.getChildrenNodes().size()) {
                        break;
                    }
                    name = String.valueOf(n.getChildrenNodes().get(i));
                }

                String comment = n.getComment() != null ? n.getComment().toString() : "";
                commentMap.put(name, CommentUtils.parseCommentText(comment));
            }
        }.visit(compilationUnit, null);

        List<FieldInfo> fields = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if (propertyDescriptor.getName().equals("class")) {
                continue;
            }
            FieldInfo field = new FieldInfo();
            field.setType(propertyDescriptor.getPropertyType());
            field.setSimpleTypeName(propertyDescriptor.getPropertyType().getSimpleName());
            field.setName(propertyDescriptor.getName());
            String comment = commentMap.get(propertyDescriptor.getName());
            if (StringUtils.isBlank(comment)) {
                field.setComment("");
                field.setRequire(false);
                fields.add(field);
            } else {
                boolean require = false;
                if (comment.contains("|")) {
                    int endIndex = comment.lastIndexOf("|必填");
                    require = endIndex > 0;
                    if (require) {
                        comment = comment.substring(0, endIndex);
                    }
                }

                field.setComment(comment);
                field.setRequire(require);
                fields.add(field);
            }
        }
        return fields;
    }
}
