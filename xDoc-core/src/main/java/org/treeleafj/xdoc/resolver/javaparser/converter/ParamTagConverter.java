package org.treeleafj.xdoc.resolver.javaparser.converter;

import org.treeleafj.xdoc.tag.DocTag;
import org.treeleafj.xdoc.tag.ParamTagImpl;

/**
 * Created by leaf on 2017/3/4.
 */
public class ParamTagConverter extends DefaultJavaParserTagConverterImpl {

    @Override
    public DocTag converter(String o) {
        DocTag _docTag = super.converter(o);
        String _val = (String) _docTag.getValues();
        String[] array = _val.split("[ \t]");
        String paramName = null;
        String paramDesc = null;
        //解析 "@param user :username 用户名|必填" 这种注释
        boolean require = false;

        if (array.length > 0) {
            paramName = array[0];
            if (array.length > 1) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < array.length; i++) {
                    sb.append(array[i]).append(' ');
                }
                paramDesc = sb.toString();
            }
        }

        if (paramDesc.contains("|")) {
            int endIndex = paramDesc.lastIndexOf("|必填");
            require = endIndex > 0;
            if (require) {
                paramDesc = paramDesc.substring(0, endIndex);
            }
        }


        if (paramDesc.startsWith(":")) {
            int index = paramDesc.indexOf(" ");
            if (index > 0) {
                paramName = paramDesc.substring(1, index);
                paramDesc = paramDesc.substring(index + 1);
            }
        }
        return new ParamTagImpl(_docTag.getName(), paramName, paramDesc, require);
    }
}
