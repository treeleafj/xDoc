package org.treeleafj.xdoc.resolver.sun.converter;

import com.sun.javadoc.Tag;
import org.treeleafj.xdoc.tag.DocTag;
import org.treeleafj.xdoc.tag.RespTagImpl;

/**
 * Created by leaf on 2017/3/12 0012.
 */
public class RespTagConverter implements TagConverter<Tag> {

    @Override
    public DocTag converter(Tag tag) {

        RespTagImpl respTag = new RespTagImpl();
        respTag.setName(tag.name());

        String v = tag.text().replaceAll("\\s{1,}", " ");//将多个空格替换成一个

        String[] split = v.split(" ");
        if (split.length >= 2) {
            String paramName = split[0];
            String paramDescs = split[1];
            boolean require = false;
            String type = null;
            String[] array = paramDescs.split("\\|");
            String paramDesc = array[0];
            if (array.length > 1) {
                type = array[1];
                if (array.length > 2) {
                    require = array[2].equalsIgnoreCase("必填");
                }
            }

            respTag.setParamName(paramName);
            respTag.setRequire(require);
            respTag.setParamDesc(paramDesc);
            respTag.setType(type);
        }
        return respTag;
    }
}
