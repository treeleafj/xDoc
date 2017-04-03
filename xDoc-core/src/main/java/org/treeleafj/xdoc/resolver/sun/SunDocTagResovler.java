package org.treeleafj.xdoc.resolver.sun;

import org.treeleafj.xdoc.resolver.DocTagResolver;

import java.util.List;

/**
 * Created by leaf on 2017/4/1 0001.
 */
public class SunDocTagResovler implements DocTagResolver {
    @Override
    public void resolve(List<String> files) {
        StringBuilder sb = new StringBuilder();
        for (Object o : files) {
            sb.append(o).append(" ");
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        files.add(0, "-doclet");
        files.add(1, SunDocHandler.class.getName());

        String[] docArgs = files.toArray(new String[files.size()]);

        com.sun.tools.javadoc.Main.execute(docArgs);
    }
}
