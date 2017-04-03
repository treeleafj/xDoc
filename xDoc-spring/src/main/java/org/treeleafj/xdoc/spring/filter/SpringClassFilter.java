package org.treeleafj.xdoc.spring.filter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.treeleafj.xdoc.filter.ClassFilter;

/**
 * Created by leaf on 2017/4/3 0003.
 */
public class SpringClassFilter implements ClassFilter {

    @Override
    public boolean filter(Class<?> classz) {
        if (classz.getAnnotation(RequestMapping.class) != null
                || classz.getAnnotation(Controller.class) != null
                || classz.getAnnotation(RestController.class) != null) {
            return true;
        }
        return false;
    }
}
