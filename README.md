# 项目介绍

# 开发环境

- JDK 17
- MySQL 8
- Maven 3.8.8
- nvm——node.js v22.18.0

# 技术选型

后端

- Java 17
- SpringBoot 2
- MySQL
- MyBatis + MyBatisPlus
- RabbitMQ 消息队列
- Redis + Redisson限流
- 第三方AI SDK ([讯飞星火大模型](https://github.com/briqt/xunfei-spark4j))
- Easy Excel（表格的上传和解析）

前端：[前端项目地址](https://github.com/Agan-ippe/zhiyixi-frontend)

- React
- Ant Design Pro + Umi
- Echarts 可视化开发库

# 架构图

![智易析项目架构图](https://raw.githubusercontent.com/Agan-ippe/typora_pic/main/imgs/20250906180442777.png)

# 项目亮点





# 快速上手

> 所有需要修改的地方都标记了 `todo`，双击Shift输入todo，便于大家找到修改的位置~

### MySQL 数据库

1）修改 `application.yml` 的数据库配置为你自己的：

```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/my_db
    username: my_name
    password: my_pwd
```



2）执行 `sql/create_table.sql` 中的数据库语句，自动创建库表

3）启动项目，访问 `http://localhost:8101/api/doc.html` 即可打开接口文档，不需要写前端就能在线调试接口了~



### Redis 分布式登录

1）修改 `application.yml` 的 Redis 配置为你自己的：

```yml
spring:
  redis:
    database: 1
    host: localhost
    port: 6379
    timeout: 5000
    password: 123456
```



2）修改 `application.yml` 中的 session 存储方式：

```yaml
spring:
  session:
    store-type: redis
```



3）移除 `MainApplication` 类开头 `@SpringBootApplication` 注解内的 exclude 参数：

修改前：

```
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
```



修改后：

```
@SpringBootApplication
```



### Elasticsearch 搜索引擎



1）修改 `application.yml` 的 Elasticsearch 配置为你自己的：

```
spring:
  elasticsearch:
    uris: http://localhost:9200
    username: root
    password: 123456
```



2）复制 `sql/post_es_mapping.json` 文件中的内容，通过调用 Elasticsearch 的接口或者 Kibana Dev Tools 来创建索引（相当于数据库建表）

```
PUT post_v1
{
 参数见 sql/post_es_mapping.json 文件
}
```



这步不会操作的话需要补充下 Elasticsearch 的知识，或者自行百度一下~

3）开启同步任务，将数据库的帖子同步到 Elasticsearch

找到 job 目录下的 `FullSyncPostToEs` 和 `IncSyncPostToEs` 文件，取消掉 `@Component` 注解的注释，再次执行程序即可触发同步：

```
// todo 取消注释开启任务
//@Component
```



### 业务代码生成器



支持自动生成 Service、Controller、数据模型代码，配合 MyBatisX 插件，可以快速开发增删改查等实用基础功能。

找到 `generate.CodeGenerator` 类，修改生成参数和生成路径，并且支持注释掉不需要的生成逻辑，然后运行即可。

```
// 指定生成参数
String packageName = "com.yupi.springbootinit";
String dataName = "用户评论";
String dataKey = "userComment";
String upperDataKey = "UserComment";
```



生成代码后，可以移动到实际项目中，并且按照 `// todo` 注释的提示来针对自己的业务需求进行修改。







