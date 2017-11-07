package org.treeleafj.xdoc.resolver.javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treeleafj.xdoc.filter.ClassFilterFactory;
import org.treeleafj.xdoc.model.ApiAction;
import org.treeleafj.xdoc.model.ApiModule;
import org.treeleafj.xdoc.model.DocTags;
import org.treeleafj.xdoc.resolver.DocTagResolver;
import org.treeleafj.xdoc.resolver.javaparser.converter.JavaParserTagConverter;
import org.treeleafj.xdoc.resolver.javaparser.converter.JavaParserTagConverterManager;
import org.treeleafj.xdoc.tag.DocTag;
import org.treeleafj.xdoc.utils.ApiModulesHolder;
import org.treeleafj.xdoc.utils.ClassMapperUtils;
import org.treeleafj.xdoc.utils.CommentUtils;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 基于开源JavaParser实现的解析
 * <p>
 * Created by leaf on 2017/4/1 0001.
 */
public class JavaParserDocTagResolver implements DocTagResolver {

    private Logger log = LoggerFactory.getLogger(JavaParserDocTagResolver.class);

    @Override
    public void resolve(List<String> files) {

        for (String file : files) {//缓存文件
            try (FileInputStream in = new FileInputStream(file)) {
                CompilationUnit cu = JavaParser.parse(in);
                if (cu.getTypes().size() <= 0) {
                    continue;
                }

                TypeDeclaration typeDeclaration = cu.getTypes().get(0);
                final Class<?> moduleType = Class.forName(cu.getPackageDeclaration().get().getNameAsString() + "." + typeDeclaration.getNameAsString());

                ClassMapperUtils.put(moduleType.getName(), file);//缓存路径
                ClassMapperUtils.put(moduleType.getSimpleName(), file);//缓存路径
            } catch (Exception e) {
                log.error("读取文件失败:{}", file, e);
            }
        }

        List<ApiModule> apiModules = new LinkedList<>();

        for (String file : files) {
            try (FileInputStream in = new FileInputStream(file)) {

                CompilationUnit cu = JavaParser.parse(in);
                if (cu.getTypes().size() <= 0) {
                    continue;
                }

                TypeDeclaration typeDeclaration = cu.getTypes().get(0);
                final Class<?> moduleType = Class.forName(cu.getPackageDeclaration().get().getNameAsString() + "." + typeDeclaration.getNameAsString());

                if (!ClassFilterFactory.getDefaultFilter().filter(moduleType)) {
                    continue;
                }

                final ApiModule apiModule = new ApiModule();
                apiModule.setType(moduleType);
                if (typeDeclaration.getComment().isPresent()) {
                    String commentText = CommentUtils.parseCommentText(typeDeclaration.getComment().get().getContent());
                    commentText = commentText.split("\n")[0].split("\r")[0];
                    apiModule.setComment(commentText);
                }

                new VoidVisitorAdapter<Void>() {
                    @Override
                    public void visit(MethodDeclaration m, Void arg) {
                        Method method = parseToMenthod(moduleType, m);
                        if (method == null) {
                            log.warn("查找不到方法:{}.{}", moduleType.getSimpleName(), m.getNameAsString());
                            return;
                        }
                        List<String> comments = CommentUtils.asCommentList(StringUtils.defaultIfBlank(m.getComment().get().getContent(), ""));
                        List<DocTag> docTagList = new ArrayList<>(comments.size());

                        for (int i = 0; i < comments.size(); i++) {
                            String c = comments.get(i);
                            String tagType = CommentUtils.findTagType(c);
                            if (StringUtils.isBlank(tagType)) {
                                continue;
                            }
                            JavaParserTagConverter converter = JavaParserTagConverterManager.getConverter(tagType);
                            if (converter == null) {
                                converter = JavaParserTagConverterManager.getDefaultConverter();
                            }
                            DocTag docTag = converter.converter(c);
                            if (docTag != null) {
                                docTagList.add(docTag);
                            } else {
                                log.warn("识别不了:{}", c);
                            }
                        }

                        DocTags docTags = new DocTags(docTagList);
                        ApiAction apiAction = new ApiAction();
                        if (m.getComment().isPresent()) {
                            apiAction.setComment(CommentUtils.parseCommentText(m.getComment().get().getContent()));
                        }
                        apiAction.setName(m.getNameAsString());
                        apiAction.setDocTags(docTags);
                        apiAction.setMethod(method);
                        apiModule.getApiActions().add(apiAction);

                        super.visit(m, arg);
                    }
                }.visit(cu, null);

                apiModules.add(apiModule);

            } catch (Exception e) {
                log.error("解析{}失败", file, e);
                continue;
            }
        }

        ApiModulesHolder.setCurrentApiModules(apiModules);//设置当前的解析结果
    }

    /**
     * 获取指定方法的所有入参类型,便于反射
     *
     * @param declaration
     * @return
     */
    private static Method parseToMenthod(Class type, MethodDeclaration declaration) {
        List<Parameter> parameters = declaration.getParameters();
        parameters = parameters == null ? new ArrayList<Parameter>(0) : parameters;
        Method[] methods = type.getDeclaredMethods();
        for (Method m : methods) {
            if (!m.getName().equals(declaration.getNameAsString())) {
                continue;
            }
            if (m.getParameterTypes().length != parameters.size()) {
                continue;
            }

            boolean b = true;

            for (int j = 0; j < m.getParameterTypes().length; j++) {
                Class<?> paramClass = m.getParameterTypes()[j];
                Type ptype = parameters.get(j).getType();
                if (ptype == null) {
                    continue;
                }
                String paranTypeName = ptype.toString();
                int index = paranTypeName.lastIndexOf(".");
                if (index > 0) {
                    paranTypeName = paranTypeName.substring(index + 1);
                }
                //处理泛型
                index = paranTypeName.indexOf("<");
                if (index > 0) {
                    paranTypeName = paranTypeName.substring(0, index);
                }

                if (!paramClass.getSimpleName().equals(paranTypeName)) {
                    b = false;
                    break;
                }
            }
            if (b) {
                return m;
            }
        }
        return null;
    }
}
