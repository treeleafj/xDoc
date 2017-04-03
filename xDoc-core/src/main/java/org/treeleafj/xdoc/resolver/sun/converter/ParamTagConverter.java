package org.treeleafj.xdoc.resolver.sun.converter;

import com.sun.javadoc.ParamTag;
import org.treeleafj.xdoc.tag.DocTag;
import org.treeleafj.xdoc.tag.ParamTagImpl;

/**
 * Created by leaf on 2017/3/4.
 */
public class ParamTagConverter implements SunTagConverter<ParamTag> {

    @Override
    public DocTag converter(ParamTag o) {

        //解析 "@param user :username 用户名|必填" 这种注释
        boolean require = false;
        String paramDesc = o.parameterComment();
        if (paramDesc.contains("|")) {
            int endIndex = paramDesc.lastIndexOf("|必填");
            require = endIndex > 0;
            if (require) {
                paramDesc = paramDesc.substring(0, endIndex);
            }
        }

        String paramName = o.parameterName();
        if (paramDesc.startsWith(":")) {
            int index = paramDesc.indexOf(" ");
            if (index > 0) {
                paramName = paramDesc.substring(1, index);
                paramDesc = paramDesc.substring(index + 1);
            }
        }
        return new ParamTagImpl(o.name(), paramName, paramDesc, require);
    }
}
