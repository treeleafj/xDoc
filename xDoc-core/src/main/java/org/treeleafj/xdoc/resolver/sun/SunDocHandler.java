package org.treeleafj.xdoc.resolver.sun;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import org.treeleafj.xdoc.filter.ClassFilter;
import org.treeleafj.xdoc.filter.ClassFilterFactory;
import org.treeleafj.xdoc.model.ApiAction;
import org.treeleafj.xdoc.model.ApiModule;
import org.treeleafj.xdoc.model.DocTags;
import org.treeleafj.xdoc.utils.ApiModulesHolder;
import org.treeleafj.xdoc.utils.ClassUtils;
import org.treeleafj.xdoc.utils.SunDocUtils;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 基于tools.jar中的sun javadoc的核心解析器,负责将类上的注释,类属性,方法属性等解析出来
 *
 * @author leaf
 * @date 2017-03-03 17:02
 */
public class SunDocHandler {

    public static boolean start(RootDoc root) throws ClassNotFoundException, NoSuchMethodException {
        ClassDoc[] classDocs = root.classes();
        List<ApiModule> apiModules = new LinkedList<>();
        for (int i = 0; i < classDocs.length; i++) {
            ClassDoc aClass = classDocs[i];

            Class<?> moduleType = Class.forName(aClass.qualifiedTypeName());
            ClassFilter classFilter = ClassFilterFactory.getDefaultFilter();
            if (!classFilter.filter(moduleType)) {
                continue;
            }

            ApiModule apiModule = new ApiModule();
            apiModule.setType(moduleType);
            apiModule.setComment(aClass.commentText());

            MethodDoc[] methods = aClass.methods(false);

            for (MethodDoc method : methods) {
                Class[] paramTypes = paramTypes(method);
                Method m = moduleType.getDeclaredMethod(method.name(), paramTypes);
                DocTags docTags = SunDocUtils.getDocsForTag(method);

                ApiAction apiAction = new ApiAction();
                apiAction.setComment(method.commentText());
                apiAction.setName(method.name());
                apiAction.setDocTags(docTags);
                apiAction.setMethod(m);
                apiModule.getApiActions().add(apiAction);
            }

            apiModules.add(apiModule);
        }
        ApiModulesHolder.setCurrentApiModules(apiModules);//设置当前的解析结果
        return true;
    }

    /**
     * 获取指定方法的所有入参类型,便于反射
     *
     * @param methodDoc
     * @return
     * @throws ClassNotFoundException
     */
    private static Class[] paramTypes(MethodDoc methodDoc) throws ClassNotFoundException {
        Parameter[] parameters = methodDoc.parameters();
        Class[] types = new Class[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            String className = parameters[i].type().qualifiedTypeName();
            types[i] = ClassUtils.toBae(className);
            if (types[i] == null) {
                types[i] = Class.forName(className);
            }
        }
        return types;
    }
}
