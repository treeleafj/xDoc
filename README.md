# ❦ XDoc 快速文档构建框架
- **基于java注释生成接口文档**
- **注释支持扩展**
- **支持markdown和在线html等格式的文档**
- **支持spring mvc规范**
- **支持spring-boot直接内嵌启动**

- #最新版本去掉了对sun java doc的依赖

### 1. 基于SpringBoot直接使用:
```xml
<!--加入maven依赖-->
<dependency>
    <groupId>com.github.treeleafj</groupId>
    <artifactId>spring-boot-starter-xDoc</artifactId>
    <version>0.0.9</version>
</dependency>
```

```java
@EnableXDoc //<--- 加上此注解以便启用XDOC在线HTML文档
@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
```

```
#在application.properties配置项目源码的位置,直接在项目里启动时,如果是单模块的maven项目,默认可以不配置
xdoc.enable=true #是否启动XDoc,默认是true,因为可以不填
xdoc.sourcePath=xDoc-spring-test/src/main/java   #源码路径,多个时用英文逗号隔开
xdoc.title=测试接口文档   #用于配置文档页面标题
```

**以上配置就都写好了**

**跟着随便写几个Controller作为Demo接口,便于直接浏览生成效果:**
```java
/**
 * 用户模块
 *
 * @author leaf
 * @date 2017-03-03 10:11
 */
@Controller
@RequestMapping("api/user")
public class UserController {

    /**
     * 查询当前登录用户的基本信息
     *
     * @param user 当前登录用户
     * @return 当前登录用户的基本信息
     * @see User
     */
    @ResponseBody
    @RequestMapping("info")
    public User info(User user) {
        return new User();
    }


    /**
     * 用户注册接口
     *
     * @param user :username 用户名|必填
     * @param user :password 密码|必填
     * @return 当前登录用户的基本信息
     * @title 用户注册
     * @respbody {"id":"123","password":"123456","username":"admin"}
     * @resp id 新生成的用户名|string|必填
     * @resp username 用户名|string|必填
     * @resp password 用户密码|string|非必填
     * @see User
     */
    @ResponseBody
    @RequestMapping(value = "register", method = {RequestMethod.POST, RequestMethod.PUT})
    User register(User user) {
        return new User();
    }
}


/**
 * 用户账户模块
 *
 * @author leaf
 * @date 2017-03-10 10:43
 */
@Controller
@RequestMapping("api/account")
public class AccountController {

    /**
     * 获取登录用户的账户资产信息
     *
     * @return 用户的资产
     * @see Account
     */
    @ResponseBody
    @RequestMapping("info")
    Account info(User user) {
        return new Account();
    }

    /**
     * 查询用户的账户状态,如果返回被冻结,说明用户账户不可用,相应的交易操作也不能用
     *
     * @return 账户状态
     * @title 查询用户账户状态
     * @respbody {"id":"123","frozen":true,"frozenDesc":"非法充值"}
     * @resp frozen 冻结|boolean|必填
     * @resp frozenDesc 冻结描述|string|必填
     * @see User
     */
    @ResponseBody
    @RequestMapping(value = "status", method = {RequestMethod.GET, RequestMethod.POST})
    Map<String, String> status(User user) {
        return new HashMap<>();
    }
}
```

**直接启动项目, 敲入地址: http://localhost:8080/xdoc/index.html**
<img alt="XDoc" src="https://raw.githubusercontent.com/treeleafj/xDoc/master/doc/1.jpg">

<img alt="XDoc" src="https://raw.githubusercontent.com/treeleafj/xDoc/master/doc/2.jpg">

### 2.如果想生成离线文档怎么办?
**支持html:**
```java
@Test
public void test() {
    FileOutputStream out = new FileOutputStream(new File("E:/api.html"));//文档输出保存位置
    String rootDir = System.getProperty("user.dir");
    SpringXDocOutputImpl output = new SpringXDocOutputImpl(out, new HtmlForamt());
    //两个参数,一个是指定源码的绝对路径,另外一个是指定输出的方式,这里采用基SpringMvc扩展的接口文档+html格式的输出
    XDoc xDoc = new XDoc(rootDir + "/src/main/java/org/treeleafj", output);
    xDoc.build();
}
```

**也支持markdown:**
```java
@Test
public void test() {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String rootDir = System.getProperty("user.dir");
    SpringXDocOutputImpl output = new SpringXDocOutputImpl(out, new MarkdownFormat());//这里换成MarkdownFormat
    //两个参数,一个是指定源码的绝对路径,另外一个是指定输出的方式,这里采用基SpringMvc扩展的接口文档+markdown格式的输出
    XDoc xDoc = new XDoc(rootDir + "/src/main/java/org/treeleafj", output);
    xDoc.build();
    System.out.println(out.toString());
}
```

**tip:生产环境不推荐开启此文档**
