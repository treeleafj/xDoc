package org.treeleafj.xdoc.handler;

import com.alibaba.fastjson.JSON;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;
import org.treeleafj.xdoc.filter.DocFilter;
import org.treeleafj.xdoc.filter.FilterFactory;
import org.treeleafj.xdoc.model.ApiAction;
import org.treeleafj.xdoc.model.ApiModule;
import org.treeleafj.xdoc.model.Param;
import org.treeleafj.xdoc.utils.DocUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author leaf
 * @date 2017-03-03 17:02
 */
public class CoreDocHandler {

    public static boolean start(RootDoc root) throws ClassNotFoundException, NoSuchMethodException {
        DocFilter filter = FilterFactory.getDefaultFilter();
        ClassDoc[] classDocs = filter.filter(root.classes());

        for (int i = 0; i < classDocs.length; i++) {
            ClassDoc aClass = classDocs[i];

            Class<?> moduleType = Class.forName(aClass.qualifiedTypeName());

            ApiModule apiModule = new ApiModule();
            apiModule.setType(moduleType);
            apiModule.setComment(aClass.commentText());

            MethodDoc[] methods = aClass.methods();

            for (MethodDoc method : methods) {
                ApiAction apiAction = new ApiAction();
                apiAction.setComment(method.commentText());
                apiAction.setName(method.name());

                Class[] paramTypes = paramTypes(method);
                Method m = moduleType.getMethod(method.name(), paramTypes);

                Map<String, Object> docs = DocUtils.getDocs(method);
                Map<String, String> params = (Map<String, String>) docs.get("@param");
                if (params != null) {
                    apiAction.getParams().putAll(params);
                }
                apiAction.setMethod(m);
                apiAction.setReturnDesc((String) docs.get("@return"));
                apiAction.setReturnInfo((Param) docs.get("@see"));
                apiModule.getApiActions().add(apiAction);
            }

            System.out.println(JSON.toJSONString(apiModule));

        }
        return true;
    }

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
