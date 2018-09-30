package org.treeleafj.xdoc.resolver.javaparser.converter;

import org.treeleafj.xdoc.tag.DocTag;
import org.treeleafj.xdoc.tag.ParamTagImpl;
import org.treeleafj.xdoc.utils.Constant;

/**
 * 针对@param的转换器
 * @author leaf
 * @date 2017/3/4
 */
public class ParamTagConverter extends DefaultJavaParserTagConverterImpl {

    @Override
    public DocTag converter(String comment) {
        DocTag docTag = super.converter(comment);
        String val = (String) docTag.getValues();
        String[] array = val.split("[ \t]+");
        String paramName = null;
        String paramDesc = "";
        String paramType = "String";
        boolean require = false;
        //解析 "user :username 用户名|必填" 这种注释内容
        //或者 "username 用户名|必填" 这种注释内容
        //或者 "username 用户名|String|必填" 这种注释内容
        //上面的"必填"两个字也可以换成英文的"Y"

        if (array.length > 0) {
            //先将第一个认为是参数名称
            paramName = array[0];
            if (array.length > 1) {

                int start = 1;
                if (array[1].startsWith(":") && array[1].length() > 1) {
                    //获取 :username这种类型的参数名称
                    paramName = array[1].substring(1);
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
                require = Constant.YES_ZH.equals(requireString) || Constant.YES_EN.equalsIgnoreCase(requireString);
            } else if (descs.length == 2) {
                String requireString = descs[1].trim();
                require = Constant.YES_ZH.equals(requireString) || Constant.YES_EN.equalsIgnoreCase(requireString);

                //如果最后一个不是是否必填的描述,则认为是类型描述
                if (!require && !(Constant.NOT_EN.equalsIgnoreCase(requireString) || Constant.NOT_ZH.equals(requireString))) {
                    paramType = requireString;
                }
            }
        }

        return new ParamTagImpl(docTag.getTagName(), paramName, paramDesc, paramType, require);
    }
}
