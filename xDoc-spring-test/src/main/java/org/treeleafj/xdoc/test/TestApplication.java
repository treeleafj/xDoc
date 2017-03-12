package org.treeleafj.xdoc.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.treeleafj.xdoc.boot.EnableXDoc;

/**
 * @author leaf
 * @date 2017-03-09 15:46
 */
@EnableXDoc
@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
