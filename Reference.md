## check if ip & port is connective
```shell
# test whether remote server can acess through specified port
curl -vv telnet://192.168.137.125:3306
```

## HikariCP数据源完整配置项

| name                        | 描述                                                                               | 构造器默认值                                | 默认配置validate之后的值     | validate重置                                                                                                                                                                                               |
|-----------------------------|----------------------------------------------------------------------------------|---------------------------------------|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| autoCommit                  | 自动提交从池中返回的连接                                                                     | TRUE                                  | TRUE                 | –                                                                                                                                                                                                        |
| connectionTimeout           | 等待来自池的连接的最大毫秒数                                                                   | SECONDS.toMillis(30) = 30000          | 30000                | 如果小于250毫秒，则被重置回30秒                                                                                                                                                                                       |
| idleTimeout                 | 连接允许在池中闲置的最长时间                                                                   | MINUTES.toMillis(10) = 600000         | 600000               | 如果idleTimeout+1秒>maxLifetime 且 maxLifetime>0，则会被重置为0（代表永远不会退出）；如果idleTimeout!=0且小于10秒，则会被重置为10秒                                                                                                          |
| maxLifetime                 | 池中连接最长生命周期                                                                       | MINUTES.toMillis(30) = 1800000        | 1800000              | 如果不等于0且小于30秒则会被重置回30分钟                                                                                                                                                                                   |
| connectionTestQuery         | 如果您的驱动程序支持JDBC4，我们强烈建议您不要设置此属性                                                   | null                                  | null                 | –                                                                                                                                                                                                        |
| minimumIdle                 | 池中维护的最小空闲连接数                                                                     | -1                                    | 10                   | minIdle<0或者minIdle>maxPoolSize,则被重置为maxPoolSize                                                                                                                                                          |
| maximumPoolSize             | 池中最大连接数，包括闲置和使用中的连接                                                              | -1                                    | 10                   | 如果maxPoolSize小于1，则会被重置当minIdle<=0被重置为DEFAULT_POOL_SIZE则为10;如果minIdle>0则重置为minIdle的值                                                                                                                      |
| metricRegistry              | 该属性允许您指定一个 Codahale / Dropwizard MetricRegistry 的实例，供池使用以记录各种指标                  | null                                  | null                 | –                                                                                                                                                                                                        |
| healthCheckRegistry         | 该属性允许您指定池使用的Codahale / Dropwizard HealthCheckRegistry的实例来报告当前健康信息                | null                                  | null                 | –                                                                                                                                                                                                        |
| poolName                    | 连接池的用户定义名称，主要出现在日志记录和JMX管理控制台中以识别池和池配置                                           | null                                  | HikariPool-1         | –                                                                                                                                                                                                        |
| initializationFailTimeout   | 如果池无法成功初始化连接，则此属性控制池是否将 fail fast                                                | 1                                     | 1                    | –                                                                                                                                                                                                        |
| isolateInternalQueries      | 是否在其自己的事务中隔离内部池查询，例如连接活动测试                                                       | FALSE                                 | FALSE                | –                                                                                                                                                                                                        |
| allowPoolSuspension         | 控制池是否可以通过JMX暂停和恢复                                                                | FALSE                                 | FALSE                | –                                                                                                                                                                                                        |
| readOnly                    | 从池中获取的连接是否默认处于只读模式                                                               | FALSE                                 | FALSE                | –                                                                                                                                                                                                        |
| registerMbeans              | 是否注册JMX管理Bean（MBeans）                                                            | FALSE                                 | FALSE                | –                                                                                                                                                                                                        |
| catalog                     | 为支持 catalog 概念的数据库设置默认 catalog                                                   | driver default                        | null                 | –                                                                                                                                                                                                        |
| connectionInitSql           | 该属性设置一个SQL语句，在将每个新连接创建后，将其添加到池中之前执行该语句                                           | null                                  | null                 | –                                                                                                                                                                                                        |
| driverClassName             | HikariCP将尝试通过仅基于jdbcUrl的DriverManager解析驱动程序，但对于一些较旧的驱动程序，还必须指定driverClassName    | null                                  | null                 | –                                                                                                                                                                                                        |
| transactionIsolation        | 控制从池返回的连接的默认事务隔离级别                                                               | null                                  | null                 | –                                                                                                                                                                                                        |
| validationTimeout           | 连接将被测试活动的最大时间量                                                                   | SECONDS.toMillis(5) = 5000            | 5000                 | 如果小于250毫秒，则会被重置回5秒                                                                                                                                                                                       |
| leakDetectionThreshold      | 记录消息之前连接可能离开池的时间量，表示可能的连接泄漏                                                      | 0                                     | 0                    | 如果大于0且不是单元测试，则进一步判断                                                                                                                                                                                      | (leakDetectionThreshold < SECONDS.toMillis(2) or (leakDetectionThreshold > maxLifetime && maxLifetime > 0)，会被重置为0 . 即如果要生效则必须>0，而且不能小于2秒，而且当maxLifetime > 0时不能大于maxLifetime |
| dataSource                  | 这个属性允许你直接设置数据源的实例被池包装，而不是让HikariCP通过反射来构造它                                       | null                                  | null                 | –                                                                                                                                                                                                        |
| schema                      | 该属性为支持模式概念的数据库设置默认模式                                                             | driver default                        | null                 | –                                                                                                                                                                                                        |
| threadFactory               | 此属性允许您设置将用于创建池使用的所有线程的java.util.concurrent.ThreadFactory的实例                      | null                                  | null                 | –                                                                                                                                                                                                        |
| scheduledExecutor           | 此属性允许您设置将用于各种内部计划任务的java.util.concurrent.ScheduledExecutorService实例              | null                                  | null                 | –                                                                                                                                                                                                        |

## MySQL & MariaDB 配置查询

| 操作描述   | 命令                           |
|--------|------------------------------|
| 查询字符编码 | SHOW VARIABLES LIKE '%char%' |

```sql
CREATE USER employees IDENTIFIED BY 'employees';
GRANT ALL privileges ON employees.* TO 'employees'@'%' IDENTIFIED BY 'employees' WITH GRANT option;
FLUSH privileges;
```
## maven 可继承元素

| 元素                   | 备注                              |
|----------------------|---------------------------------|
| groupId              | 项目组ID，项目坐标的核心元素                 |
| version              | 项目版本，项目坐标的核心元素                  |
| description          | 项目的描述信息                         |
| organization         | 项目的组织信息                         |
| properties           | 自定义的Maven属性                     |
| dependencies         | 项目的依赖配置                         |
| repositories         | 项目的仓库配置                         |
| build                | 包括项目的源码目录配置、输出目录配置、插件配置、插件管理配置等 |
| reporting            | 包括项目的报告输出目录配置、报告插件配置等           |
| dependencyManagement | 项目的依赖管理配置                       |
| pluginManagement     | 插件管理                            |

## 生成SSL证书

```shell
keytool -import -keystore jdk1.8.0_77/jre/lib/security/cacerts -file ~/test.cer -alias test

```

```shell

# change eclipse( juno ) options when loading activiti-designer plugin
--launcher.XXMaxPermSize
128M
--launcher.XXMaxPermSize
128m
-vm
C:/Program Files/OpenJDK/jdk-8.0.262.10-hotspot/bin/javaw.exe
```

## TLS证书问题
```shell
// keytool -genkey -alias (别名) -dname "CN=(姓名),OU=(组织单位名称),O=(组织名称),L=(城市名称),ST=(省),C=(国家)" -storetype (密钥仓库类型) -keyalg (生证书的算法名称) -keysize (密钥长度,证书大小) -keystore (指定生成证书的位置和证书名称) -validity (证书有效期，天单位)
keytool -genkey -alias uublog -dname "CN=Lhc,OU=SCYD,O=SCYD,L=CD,ST=SiChuan,C=CN" -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 365


# shell script
# server / client-ext.cnf
subjectAltName=DNS:*.client.com,DNS:*.client.org,IP:0.0.0.0

rm *.pem

# 1.生成CA的私钥和自签名证书
openssl req -x509 -newkey rsa:2048 -days 365 -passout pass:rocky -keyout ca-key.pem -out ca-cert.pem -subj "/C=CH/ST=GuangDong/L=ShenZhen/O=com/OU=personal/CN=sataboy/emailAddress=sataboy@email.com"
echo "CA's self-signed certificate"
# 为RSA密钥去除口令保护
openssl x509 -in ca-cert.pem -noout -text

# 2.生成服务器的私钥和证书签名请求(CSR)
openssl req -newkey rsa:2048 -passout pass:server.key.pwd -keyout server-key.pem -out server-req.pem -subj "/C=CH/ST=GuangDong/L=GuangZhou/O=Computer/OU=Go/CN=Harry/emailAddress=Harry@email.com"

# 3.使用CA的私钥签署服务器的CSR并生成签名证书
openssl x509 -req -in server-req.pem -days 365 -passin pass:rocky -CA ca-cert.pem -CAkey ca-key.pem -CAcreateserial -out server-cert.pem -extfile server-ext.cnf

echo "Server's certificate signed by CA"
openssl x509 -in server-cert.pem -noout -text

echo "Verify ca-cert.pem server-cert.pem"
openssl verify -CAfile ca-cert.pem server-cert.pem

# 4.生成客户端的私钥和证书签名请求(CSR)
openssl req -newkey rsa:2048 -passout pass:client.key.pwd -keyout client-key.pem -out client-req.pem -subj "/C=CH/ST=FuJian/L=XiaMen/O=Client/OU=Gopher/CN=Alice/emailAddress=Alice@email.com"

# 5.用CA的私钥签署客户端的CSR并生成签名证书
openssl x509 -req -in client-req.pem -days 365 -passin pass:client.key.pwd -CA ca-cert.pem -CAkey ca-key.pem -CAcreateserial -out client-cert.pem -extfile client-ext.cnf

echo "client's certificate signed by CA"
openssl x509 -in client-cert.pem -noout -text

echo "Verify ca-cert.pem client-cert.pem"
openssl verify -CAfile ca-cert.pem client-cert.pem

# Generating the PKCS12 Certificate
openssl pkcs12 -export -passout pass:pkcs12 -clcerts -in ca-cert.pem -inkey ca-key.pem -out certificate.p12 -name "certificate"

# PKCS#12 to JKS
keytool -importkeystore -srckeystore certificate.p12 -srcstoretype pkcs12 -destkeystore rocky.jks -storepass rocky.jks
$ Importing keystore certificate.p12 to rocky.jks...
$ Enter source keystore password: pkcs12
$ Entry for alias certificate successfully imported.
$ Import command completed:  1 entries successfully imported, 0 entries failed or cancelled

# check entire certificate chain
keytool -list -v -keystore server.jks



# server.jks
 openssl pkcs12 -export -passout pass:pkcs12 -clcerts -in server-cert.pem -inkey server-key.pem -out server.p12 -name "server"
Enter pass phrase for server-key.pem: rocky

keytool -importkeystore -srckeystore server.p12 -srcstoretype pkcs12 -destkeystore server.jks -storepass server.jks
Importing keystore server.p12 to server.jks...
Enter source keystore password: pkcs12
Entry for alias server successfully imported.
Import command completed:  1 entries successfully imported, 0 entries failed or cancelled
```

| 证书名称            | 证书说明                  |
|-----------------|-----------------------|
| ca-key.pem      | CA的私钥                 |
| ca-cert.pem     | CA的证书文件               |
| ca-cert.srl     | 保存证书的唯一序列号            |
| client-ext.cnf  | 客户端证书扩展信息             |
| client-req.pem  | 客户端的证书签名请求（CSR）文件     |
| client-key.pem  | 客户端的私钥                |
| client-cert.pem | 客户端的证书                |
| server-ext.cnf  | 服务器证书扩展信息             |
| server-req.pem  | 服务器的证书签名请求（CSR）文件     |
| server-key.pem  | 服务器的私钥                |
| server-cert.pem | 服务器的证书                |

| openssl命令参数        | 参数作用说明                 |
|--------------------|------------------------|
| req -x509          | 输出X509格式的自签名证书         |
| -newkey rsa:2048   | 输出4096位的RSA格式的私钥       |
| -days 365          | 证书有效期为365天             |
| -nodes             | 不进行对私钥的加密              |
| -keyout ca-key.pem | 将私钥写入ca-key.pem文件中     |
| -out ca-cert.pem   | 将签名证书写入ca-cert.pem文件中  |
| -subj              | 身份信息                   |
