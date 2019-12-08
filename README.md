## XDoc讨论QQ群:820854691

# ❦ XDoc 基于Java注释的接口文档工具

- **基于java注释生成接口文档-对代码无侵入,无需注解,纯代码注释**
- **支持SpringWeb, SpringBoot, JFinal**
- **文档输出格式支持markdown和离线/在线html等**

### 为何使用XDoc?
- **减少外部接口文档的另外编写,在编码过程就一起完成,减少外部维护工作量**
- **修改代码时,少掉翻看外部接口文档的过程,直接看代码注释**
- **接口文档同版本管理一起,可方便回退以及多环境多版本**
- **难道标准的注释不应该作为企业项目的必须规范吗?如果你的规范里没有接口这块的,那么,还不补上?如果补上的还能帮你生成文档,何乐而不为?**

### 如何使用?
#### 1.以SpringBoot为例:
```xml
<!--加入maven依赖-->
<dependency>
    <groupId>com.github.treeleafj</groupId>
    <artifactId>spring-boot-starter-xDoc</artifactId>
    <version>1.1.0</version>
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
xdoc.enable=true #是否启动XDoc,默认是true,生产环境建议改为false
xdoc.sourcePath=F:/java/project/xDoc/samples/sample-springboot/src/main/java   #源码路径,多个路径时用英文逗号隔开
xdoc.title=用户中心接口文档   #用于配置文档页面标题
xdoc.version=1.0   #标识接口文档的版本号
```

**以上准备配置就都做好了**

**接下来,我们只需要像往常一样写个Controller,并写好注释:**
```java
/**
 * 用户模块
 *
 * @author treeleaf
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
     * @see User
     */
    @ResponseBody
    @RequestMapping(value = "register", method = {RequestMethod.POST, RequestMethod.PUT})
    User register(User user) {
        user.setId(UUID.randomUUID().toString());
        return user;
    }
}
```

**写完之后,直接启动项目, 敲入地址: http://localhost:8080/xdoc/index.html**
![demo](https://images.gitee.com/uploads/images/2019/1208/163306_ee88c3c1_12124.png "1.jpg")

### 2.如果想生成离线文档怎么办?
**支持html:**

```java
/**
 * 生成离线的HTML格式的接口文档
 */
@Test
public void buildHtml() throws Exception {
    /**注意!!!路径必须是要能扫描到源码工程的路径,执行生成的文件打开没有接口目录,说明没扫描到,请优先确认自己传入的路径是否正确!!!*/
    FileOutputStream out = new FileOutputStream(new File(userDir, "api.html"));
    XDoc xDoc = new XDoc(new File("F:/java/project/xDoc/samples/sample-springboot/src/main/java"), new SpringWebHttpFramework());
    xDoc.build(out, new HtmlForamt());
}
```

**也支持markdown:**
```java
/**
 * 生成离线的Markdown格式的接口文档
 */
@Test
public void buildMarkdown() {
    /**注意!!!路径必须是要能扫描到源码工程的路径,执行生成的markdown如果没有接口内容,说明没扫描到,请优先确认自己传入的路径是否正确!!!*/
	
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    XDoc xDoc = new XDoc(new File("F:/java/project/xDoc/samples/sample-springboot/src/main/java"), new SpringWebHttpFramework());
    
    xDoc.build(out, new MarkdownFormat());

    System.out.println(out.toString());
}
```

#### 如果不是SpringBoot,只是单纯的SpringWeb,或者是JFinal, 如何使用请参考samples目录下demo

### 现有注释标签用法:
- @title
  接口标题,如果不加这个,默认读的是接口注释上第一行的描述内容
- @param
  接口入参, 格式为: "参数名 参数描述|(参数类型)|(是否必填)"
  其中"参数类型"可不填,默认是`String`, "是否必填"可不填,默认为非必填, "是否必填"的取值有`必填(Y)`,`非必填(N)`,具体常用的格式如下:
  username 用户名
  username 用户名|必填  `或者` username 用户名|Y
  username 用户名|非必填 `或者` username 用户名|N
  username 用户名|String
  username 用户名|String|必填

  针对IDEA的在使用Java自身的@param注释注解时,如果上面的参数名在当前方法入参上是没有的,是会提示错误的,为了解决这种问题,XDoc支持在注释的参数名称前面加上`冒号:`来避开IDEA的检测,如:
  :username 用户名  `或者` user :username 用户名

- @paramObj
  当觉得入参本身就在一个Dto中,但是要一个个@param去加会比较麻烦时,可以用@paramObj指定入参的Dto对象,用法同@see,但是@paramObj支持一个接口方法出现多个,同时,@param与@paramObj混用,@paramObj对象中的某个属性名与@param的参数名冲突时,会优先以@param的为准, 使用可参考samples中的AccountController.java

- @resp
  指定返回的参数,格式同@param

- @respbody
  指定返回数据的demo,暂只支持对json数据进行格式化,仅用于展示,使用可参考samples中的UserController.java

- @see
  指定返回的出参对象,类似@paramObj,不过一个是入参,一个是出参,一个方法只能出现一个@see,同时,跟@resp混用时有属性名冲突,以@resp的为准, 使用可参考samples中的AccountController.java

- @return
  返回信息的描述,内容为纯文本,仅用于展示
  
- @IgnoreApi
  这个是注解,不是放在注释上的,用于标注哪些接口不需要生成接口文档