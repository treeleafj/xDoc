package org.treeleafj.xdoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import org.treeleafj.xdoc.filter.DocFilter;
import org.treeleafj.xdoc.filter.FilterFactory;
import org.treeleafj.xdoc.model.ApiAction;
import org.treeleafj.xdoc.model.ApiModule;
import org.treeleafj.xdoc.model.DocTags;
import org.treeleafj.xdoc.utils.ApiModulesHolder;
import org.treeleafj.xdoc.utils.DocUtils;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * 核心解析器,负责将类上的注释,类属性,方法属性等解析出来
 *
 * @author leaf
 * @date 2017-03-03 17:02
 */
public class CoreDocHandler {

    public static boolean start(RootDoc root) throws ClassNotFoundException, NoSuchMethodException {
        DocFilter filter = FilterFactory.getDefaultFilter();
        ClassDoc[] classDocs = filter.filter(root.classes());

        List<ApiModule> apiModules = new LinkedList<ApiModule>();
        for (int i = 0; i < classDocs.length; i++) {
            ClassDoc aClass = classDocs[i];

            ApiModule apiModule = new ApiModule();
            Class<?> moduleType = Class.forName(aClass.qualifiedTypeName());

            apiModule.setType(moduleType);
            apiModule.setComment(aClass.commentText());

            MethodDoc[] methods = aClass.methods(false);

            for (MethodDoc method : methods) {
                Class[] paramTypes = paramTypes(method);
                Method m = moduleType.getDeclaredMethod(method.name(), paramTypes);
                DocTags docTags = DocUtils.getDocsForTag(method);

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
            types[i] = toBae(className);
            if (types[i] == null) {
                types[i] = Class.forName(className);
            }
        }
        return types;
    }

    /**
     * 将基本类型的转为class类型
     *
     * @param className
     * @return
     */
    private static Class toBae(String className) {
        if ("byte".equals(className)) {
            return byte.class;
        } else if ("short".equals(className)) {
            return short.class;
        } else if ("int".equals(className)) {
            return int.class;
        } else if ("long".equals(className)) {
            return long.class;
        } else if ("float".equals(className)) {
            return float.class;
        } else if ("double".equals(className)) {
            return double.class;
        } else if ("boolean".equals(className)) {
            return boolean.class;
        } else if ("char".equals(className)) {
            return char.class;
        } else {
            return null;
        }
    }
}
