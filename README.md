## XDoc讨论QQ群:820854691

# ❦ XDoc 快速文档构建框架

- **基于java注释生成接口文档**
- **注释支持扩展**
- **接口框架支持扩展**
- **默认支持markdown和离线/在线html等格式的文档**
- **默认支持spring mvc规范**
- **默认支持spring-boot直接内嵌启动**


### 1. 基于SpringBoot直接使用:
```xml
<!--加入maven依赖-->
<dependency>
    <groupId>com.github.treeleafj</groupId>
    <artifactId>spring-boot-starter-xDoc</artifactId>
    <version>1.0.0</version>
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
xdoc.title=用户中心接口文档   #用于配置文档页面标题
xdoc.version=1.0   #标识接口文档的版本号
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
     * 登录
     *
     * @param username 用户名|必填
     * @param password 密码
     * @return 当前登录用户的基本信息
     * @resp code 返回码(0000表示登录成功,其它表示失败)|string|必填
     * @resp msg 登录信息|string
     * @resp username 登录成功后返回的用户名|string
     */
    @ResponseBody
    @PostMapping("login")
    public Map<String, String> login(String username, String password) {
        Map<String, String> model = new HashMap<>();
        model.put("code", "0000");
        model.put("msg", "登录成功");
        model.put("username", username);
        return model;
    }


    /**
     * 用户注册
     *
     * @param user :username 用户名|必填
     * @param user :password 密码
     * @return 注册后生成的用户的基本信息
     * @respbody {"id":"123","password":"123456","username":"admin"}
     * @title 注册
     * @see User
     * @resp score 分数
     */
    @ResponseBody
    @RequestMapping(value = "register", method = {RequestMethod.POST, RequestMethod.PUT})
    User register(org.treeleafj.xdoc.test.vo.User user, @RequestParam(value = "abc", required = false)List<MultipartFile> list) {
        user.setId(UUID.randomUUID().toString());
        return user;
    }
}
```

**直接启动项目, 敲入地址: http://localhost:8080/xdoc/index.html**
<img alt="XDoc" src="https://raw.githubusercontent.com/treeleafj/xDoc/master/doc/1.jpg">

<img alt="XDoc" src="https://raw.githubusercontent.com/treeleafj/xDoc/master/doc/2.jpg">

<img alt="XDoc" src="https://raw.githubusercontent.com/treeleafj/xDoc/master/doc/3.jpg">

### 2.如果想生成离线文档怎么办?
**支持html:**
```java
@Test
public void buildHtml() throws Exception {
    //生成离线的HTML格式的接口文档
    String userDir = System.getProperty("user.dir");
    FileOutputStream out = new FileOutputStream(new File(userDir, "api.html"));
    
    /**注意!!!路径必须是要能扫描到源码工程的路径,本文user.dir只是作者IDE中的工程目录,不一定是你的工程目录,执行生成的文件打开没有接口目录,说明没扫描到,请优先确认自己传入的路径是否正确!!!*/
    XDoc xDoc = new XDoc(userDir + "/src/main/java/org/treeleafj", new SpringWebFramework());
    
    //1.1.0版本后改为SpringWebHttpFramework
    //XDoc xDoc = new XDoc(userDir + "/src/main/java/org/treeleafj", new SpringWebHttpFramework());
    
    xDoc.build(out, new HtmlForamt());
}
```

**也支持markdown:**
```java
@Test
public void buildMarkdown() {
    //生成离线的Markdown格式的接口文档
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String rootDir = System.getProperty("user.dir");
    
    /**注意!!!路径必须是要能扫描到源码工程的路径,本文user.dir只是作者IDE中的工程目录,不一定是你的工程目录,执行生成的文件打开没有接口目录,说明没扫描到,请优先确认自己传入的路径是否正确!!!*/
    XDoc xDoc = new XDoc(rootDir + "/src/main/java/org/treeleafj", new SpringWebFramework());
    
    //1.1.0版本后改为SpringWebHttpFramework
    //XDoc xDoc = new XDoc(userDir + "/src/main/java/org/treeleafj", new SpringWebHttpFramework());
    
    xDoc.build(out, new MarkdownFormat());

    System.out.println(out.toString());
}
```

**tip:生产环境不推荐开启此文档,所以配置文件中配置好:**
```txt
xdoc.enable=false
```
