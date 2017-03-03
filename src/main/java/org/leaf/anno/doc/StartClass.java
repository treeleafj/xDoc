package org.leaf.anno.doc;

import com.alibaba.fastjson.JSON;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author leaf
 * @date 2017-03-03 10:08
 */
public class StartClass {

    private static Logger log = LoggerFactory.getLogger(StartClass.class);

    public static boolean start(RootDoc root) {
        ClassDoc[] classes = root.classes();
        for (int i = 0; i < classes.length; ++i) {
            ClassDoc aClass = classes[i];

            if (DocUtils.isController(aClass)) {
                Map<String, Object> map = DocUtils.getRequestMapperingMeta(aClass);

                ApiModule apiModule = new ApiModule();
                apiModule.setClassName(aClass.qualifiedTypeName());
                apiModule.setSimpleName(aClass.name());
                apiModule.setUri((List) map.get("value"));
                apiModule.setComment(aClass.commentText());

                MethodDoc[] methods = aClass.methods();
                for (MethodDoc method : methods) {
                    ApiAction apiAction = new ApiAction();
                    apiAction.setComment(method.commentText());
                    apiAction.setName(method.name());
                    map = DocUtils.getRequestMapperingMeta(method);
                    if (map != null) {
                        apiAction.setUri((List) map.get("value"));
                        apiAction.setMethods((List) map.get("method"));
                    }

                    Map<String, Object> docs = DocUtils.getDocs(method);
                    Map<String, String> params = (Map<String, String>) docs.get("@param");
                    if (params != null) {
                        apiAction.getParams().putAll(params);
                    }
                    apiAction.setReturnMsg((String) docs.get("@return"));
                    apiAction.setReturnInfo((VOInfo) docs.get("@see"));
                    apiModule.getApiActions().add(apiAction);
                }

                System.out.println(JSON.toJSONString(apiModule));
            }
        }
        return true;
    }

    public static void main(String[] args) {

        File file = new File("E:/project/anno-doc/src/main/java/org/leaf/anno");
        List<String> files = files(file);
        System.out.println(files);

        StringBuilder sb = new StringBuilder();
        for (Object o : files) {
            sb.append(o).append(" ");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        files.add(0, "-doclet");
        files.add(1, StartClass.class.getName());

//        String[] docArgs = new String[]{"-doclet", StartClass.class.getName(), files.get(0), files.get(1)};
        String[] docArgs = files.toArray(new String[files.size()]);
        com.sun.tools.javadoc.Main.execute(docArgs);
    }

    public static List<String> files(File file) {

        if (!file.exists()) {
            return new ArrayList(0);
        }

        if (file.isFile()) {
            if (file.getName().lastIndexOf(".java") > 0) {
                List list = new ArrayList();
                list.add(file.getAbsolutePath());
                return list;
            } else {
                return new ArrayList(0);
            }
        }

        List list = new ArrayList();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                list.addAll(files(f));
            }
        }
        return list;
    }
}
