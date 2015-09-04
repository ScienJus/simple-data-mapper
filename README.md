#Simple Data Mapper

这是一个简单的数据库映射Demo，目的是通过JDBC封装，提供对象与数据库表之间的相互映射关系。类似的成熟产品已经有Mybatis了，所以这个Demo也只是写着玩，并不会用在生产环境中。

###依赖

这个项目依赖以下第三方库：

 - spring-boot-starter：提供DataSource的注入和Domain类扫描功能
 - tomcat-jdbc：使用该数据库连接池作为数据源
 - mysql-connector-java：数据库驱动
 - commons-lang：使用了字符串拼接功能
 - spring-boot-starter-test：作为单元测试（测试代码不会上传）
 
###项目结构

项目分为以下三个部分：

 - base：项目的核心代码，封装了JDBC的基础操作，以及在项目启动时对整个项目的初始化
 - generator：根据数据库表结构生成对应的Domian和Dao代码，严格来说这个部分是可以独立运行的
 - instance：其实就是生成后的代码，可以直接在业务层调用进行操作
 
###核心类介绍

**BaseDomain**

所有domain类的父类，changeFields属性可以在insert和update操作时只映射有变化的属性，而不用将所有属性都进行SQL操作。

**SimpleDataMapper**

所有Dao实现类的父类，封装了5个常用的数据库操作：新增，修改，删除，主键查询和条件查询。

**DataMapperInitializer**

在项目启动时会读取Domain类所在的包（需要手动指定），并进行一些配置以及数据源的初始化操作。

**DataMapperGenerator**

根据配置文件通过数据库表结构生成对应的Domain和Dao代码。

###演示方式

1. 登录MySQL数据库，创建一个需要操作的表（在resources/user.sql提供了一个测试表）
2. 修改resources下的generator.json，修改MySQL，包名和存放路径等配置
3. 运行DataMapperGenerator的main方法，代码会生成在指定的路径下
4. 将生成的代码复制到instance包下
5. 修改application.properties，修改MySQL和扫描包名的配置
6. 在Test中编写测试代码，运行即可看到效果

###默认映射关系

表映射：

user_  => User

user_info => UserInfo

字段映射：

username_ => username

user_id => userId

###一些思考

每天都能看到许多人辩论Hibernate和Mybatis哪个更好用。

Hibernate作为一个完全的ORM框架，它的功能非常强大，在项目初期几乎不用去处理表之间的关系，但是同时也被打上了厚重，难以驾驭，性能低的标签。由于自己本身是在互联网公司做App服务端的，不可避免的更加了解和偏爱Mybatis。所以这个Demo也是只有简单的SQL Mapping。

其实写个SQL Mapping很简单，如果是通过代码生成器生成代码就更简单了，最无脑的方法就是给每个方法都配置好SQL语句，在调用时赋值就完了，不过这看起来并不优雅。

好的程序都有两个共通的特点：效率高和写/看着舒服，在这个Demo中其实就是在直接生成代码和运行时动态反射之间权衡，例如直接在代码中生成changeFields的相关操作可以快速的映射出更改字段部分的SQL语句，而不是通过反射对所有字段都进行映射。

目前公司也在用一个跟这个差不多但是功能更加强大（缓存，分片）的持久层框架，目前看来这种做法也还是能够应付生产环境的。



