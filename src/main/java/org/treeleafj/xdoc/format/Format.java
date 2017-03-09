package org.treeleafj.xdoc.format;

import org.treeleafj.xdoc.output.spring.SpringApiModule;

import java.util.List;

/**
 * Created by leaf on 2017/3/4.
 */
public interface Format {

    String format(List<SpringApiModule> list);

    String format(SpringApiModule apiModule);
}
