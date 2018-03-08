package org.treeleafj.xdoc.resolver.javaparser.converter;

import org.treeleafj.xdoc.tag.DocTag;
import org.treeleafj.xdoc.tag.ParamTagImpl;

/**
 * Created by leaf on 2017/3/4.
 */
public class ParamTagConverter extends DefaultJavaParserTagConverterImpl {

    @Override
    public DocTag converter(String comment) {
        DocTag _docTag = super.converter(comment);
        String _val = (String) _docTag.getValues();
        String[] array = _val.split("[ \t]");
        String paramName = null;
        String paramDesc = "";
        String paramType = "String";
        boolean require = false;
        //解析 "user :username 用户名|必填" 这种注释内容
        //或者 "username 用户名|必填" 这种注释内容
        //或者 "username 用户名|String|必填" 这种注释内容

        if (array.length > 0) {
            paramName = array[0];//先将第一个认为是参数名称
            if (array.length > 1) {

                int start = 1;
                if (array[1].startsWith(":")) {
                    paramName = array[1];//获取 :username这种类型的参数名称
                    start = 2;
                }

                StringBuilder sb = new StringBuilder();
                for (int i = start; i < array.length; i++) {
                    sb.append(array[i]).append(' ');
                }
                paramDesc = sb.toString();
            }
        }

        String[] descs = paramDesc.split("\\|");
        if (descs.length > 0) {
            paramDesc = descs[0];
            if (descs.length > 2) {
                paramType = descs[1];
                String requireString = descs[descs.length - 1].trim();
                require = requireString.equals("必填") || requireString.equalsIgnoreCase("Y");
            } else if (descs.length == 2) {
                String requireString = descs[1].trim();
                require = requireString.equals("必填") || requireString.equalsIgnoreCase("Y");

                //如果最后一个不是是否必填的描述,则认为是类型描述
                if (!require && !(requireString.equalsIgnoreCase("N") || requireString.equals("非必填"))) {
                    paramType = requireString;
                }
            }
        }

        return new ParamTagImpl(_docTag.getTagName(), paramName, paramDesc, paramType, require);
    }
}
