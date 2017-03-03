package org.treeleafj.xdoc.filter;

import com.sun.javadoc.ClassDoc;
import org.treeleafj.xdoc.utils.DocUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于spring mvc的文档过滤器,只允许标有@Controller的类
 *
 * @author leaf
 * @date 2017-03-03 17:09
 */
public class SpringDocFilterImpl implements DocFilter {

    @Override
    public ClassDoc[] filter(ClassDoc[] classDocs) {
        List<ClassDoc> list = new ArrayList();
        for (ClassDoc classDoc : classDocs) {
            if (DocUtils.isController(classDoc)) {
                list.add(classDoc);
            }
        }
        return list.toArray(new ClassDoc[list.size()]);
    }

}
