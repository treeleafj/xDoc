package org.treeleafj.xdoc.test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.junit.Test;
import org.treeleafj.xdoc.XDoc;
import org.treeleafj.xdoc.spring.SpringXDocOutputImpl;
import org.treeleafj.xdoc.spring.format.HtmlForamt;
import org.treeleafj.xdoc.spring.format.MarkdownFormat;

import java.io.*;

/**
 * Created by leaf on 2017/3/3 003.
 */
public class XDocTest {

    @Test
    public void buildMarkdown() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String rootDir = System.getProperty("user.dir");
        org.treeleafj.xdoc.spring.SpringXDocOutputImpl output = new SpringXDocOutputImpl(out, new MarkdownFormat());
        XDoc xDoc = new XDoc(rootDir + "/src/main/java/org/treeleafj", output);
        xDoc.build();

        System.out.println(out.toString());
    }

    @Test
    public void buildHtml() throws Exception {
        FileOutputStream out = new FileOutputStream(new File("E:/api.html"));
        String rootDir = System.getProperty("user.dir");
        org.treeleafj.xdoc.spring.SpringXDocOutputImpl output = new SpringXDocOutputImpl(out, new HtmlForamt());
        XDoc xDoc = new XDoc(rootDir + "/src/main/java/org/treeleafj", output);
        xDoc.build();
    }

    @Test
    public void javaparser() throws FileNotFoundException, ParseException {
// creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream("F:\\Java\\project\\xDoc\\xDoc-spring-test\\src\\main\\java\\org\\treeleafj\\xdoc\\test\\controller\\UserController.java");

        // parse the file
        CompilationUnit cu = JavaParser.parse(in);

        // prints the resulting compilation unit to default system output
//        System.out.println(cu.toString());
        new MethodVisitor().visit(cu, null);
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes.
     */
    public static class MethodVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            System.out.println(n);
            /* here you can access the attributes of the method.
             this method will be called for all methods in this
             CompilationUnit, including inner class methods */
            Comment comment = n.getComment();
            System.out.println(comment);
            super.visit(n, arg);
        }
    }
}