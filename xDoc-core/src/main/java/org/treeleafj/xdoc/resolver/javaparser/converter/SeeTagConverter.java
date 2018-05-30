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
    public DocTag converter(String comment) {
        DocTag _docTag = super.converter(comment);

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
            returnClassz = Class.forName(cu.getPackageDeclaration().get().getNameAsString() + "." + cu.getTypes().get(0).getNameAsString());

        } catch (Exception e) {
            log.warn("读取java原文件失败:{}", path, e.getMessage());
            return null;
        }

        String text = cu.getComment().isPresent() ? CommentUtils.parseCommentText(cu.getComment().get().getContent()) : "";

        List<FieldInfo> fields = analysisFields(returnClassz, cu);
        ObjectInfo objectInfo = new ObjectInfo();
        objectInfo.setType(returnClassz);
        objectInfo.setFieldInfos(fields);
        objectInfo.setComment(text);
        return new SeeTagImpl(_docTag.getTagName(), objectInfo);
    }

    private List<FieldInfo> analysisFields(Class classz, CompilationUnit compilationUnit) {
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(classz);

        final Map<String, String> commentMap = new HashMap();
        new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(FieldDeclaration n, Void arg) {
                String name = n.getVariable(0).getName().asString();

                String comment = "";
                if (n.getComment().isPresent()) {
                    comment = n.getComment().get().getContent();
                }

                if (name.contains("=")) {
                    name = name.substring(0, name.indexOf("=")).trim();
                }

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
                    if (endIndex < 0) {
                        endIndex = comment.lastIndexOf("|Y");
                    }
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
