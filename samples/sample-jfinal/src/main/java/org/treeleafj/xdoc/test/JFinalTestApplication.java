package org.treeleafj.xdoc.test;

import com.jfinal.config.*;
import com.jfinal.json.JacksonFactory;
import com.jfinal.kit.PropKit;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;
import org.treeleafj.xdoc.jfinal.XDocJfinalController;
import org.treeleafj.xdoc.test.controller.AccountController;
import org.treeleafj.xdoc.test.controller.CommController;
import org.treeleafj.xdoc.test.controller.UserController;

public class JFinalTestApplication extends JFinalConfig {


    public static void main(String[] args) {
        UndertowServer.start(JFinalTestApplication.class, 8081, true);
    }

    @Override
    public void configConstant(Constants constants) {
        constants.setJsonFactory(new JacksonFactory());
        constants.setDevMode(true);

        PropKit.use("application.txt");
    }

    @Override
    public void configRoute(Routes routes) {
        //启用xDoc
        routes.add("/xdoc", XDocJfinalController.class);
        //添加测试用的类
        routes.add("/account", AccountController.class);
        routes.add("/comm", CommController.class);
        routes.add("/user", UserController.class);
    }

    @Override
    public void configEngine(Engine engine) {

    }

    @Override
    public void configPlugin(Plugins plugins) {

    }

    @Override
    public void configInterceptor(Interceptors interceptors) {

    }

    @Override
    public void configHandler(Handlers handlers) {

    }
}
