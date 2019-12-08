package org.treeleafj.xdoc.resolver.javaparser.converter;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treeleafj.xdoc.model.FieldInfo;
import org.treeleafj.xdoc.model.ObjectInfo;
import org.treeleafj.xdoc.resolver.JavaSourceFileManager;
import org.treeleafj.xdoc.tag.DocTag;
import org.treeleafj.xdoc.tag.SeeTagImpl;
import org.treeleafj.xdoc.utils.CommentUtils;
import org.treeleafj.xdoc.utils.JavaFileUtils;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 针对@see的转换器
 *
 * @author leaf
 * @date 2017/3/4
 */
public class SeeTagConverter extends DefaultJavaParserTagConverterImpl {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private JavaParser javaParser = new JavaParser();

    @Override
    public DocTag converter(String comment) {
        DocTag docTag = super.converter(comment);

        String path = JavaSourceFileManager.getInstance().getPath((String) docTag.getValues());
        if (StringUtils.isBlank(path)) {
            return null;
        }

        Class<?> returnClassz;
        CompilationUnit cu;
        try (FileInputStream in = new FileInputStream(path)) {
            Optional<CompilationUnit> optional = javaParser.parse(in).getResult();
            if (!optional.isPresent()) {
                return null;
            }
            cu = optional.get();
            if (cu.getTypes().size() <= 0) {
                return null;
            }
            returnClassz = Class.forName(cu.getPackageDeclaration().get().getNameAsString() + "." + cu.getTypes().get(0).getNameAsString());

        } catch (Exception e) {
            log.warn("读取java原文件失败:{}", path, e.getMessage());
            return null;
        }

        String text = cu.getComment().isPresent() ? CommentUtils.parseCommentText(cu.getComment().get().getContent()) : "";

        Map<String, String> commentMap = JavaFileUtils.analysisFieldComments(returnClassz);
        List<FieldInfo> fields = JavaFileUtils.analysisFields(returnClassz, commentMap);

        ObjectInfo objectInfo = new ObjectInfo();
        objectInfo.setType(returnClassz);
        objectInfo.setFieldInfos(fields);
        objectInfo.setComment(text);
        return new SeeTagImpl(docTag.getTagName(), objectInfo);
    }


}
