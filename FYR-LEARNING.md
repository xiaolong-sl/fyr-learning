## Windows Terminal创建远程Linux主机登录快捷方式
```text
* guid：唯一标识，可以从https://www.guidgenerator.com/生成一份。
* hidden：是否隐藏。
* name：标题名，显示在窗口的标签名称。
* commandline：命令行内容，这里通过 windows cmd 的 ssh 连接到远程 linux 服务器。格式如下：
ssh 用户名@主机ip或域名 -p端口号
* icon：图标文件路径，也可以是网络地址，这里为了方便，将图标文件放入 setting 配置文件(profiles.json)所在的文件夹(C:\Users\90980\AppData\Local\Packages\Microsoft.WindowsTerminal_8wekyb3d8bbwe\LocalState)里，前缀为ms-appdata:///Local/即可
```

## 变更Linux主机名
```shell
hostnamectl set-hostname newhostname
```

## distingush between login shell & non-login shell

| Login Way           | loaded script file                       |
|---------------------|------------------------------------------|
| **login shell**     | `/etc/profile，~/.bash_profile，~/.bashrc` |
| **non-login shell** | `~/.bashrc`                              |

## SFTP命令的使用
* 连接远程主机
```shell
sftp root@192.168.137.125
# upload local file to remote user's home directory
sftp> put C:/Users/90980/.ssh/id_rsa.pub

# download remote host file to local user's default home directory

sftp> get anaconda-ks.cfg
```
## 创建免密认证
```shell
# cmd 'ssh-keygen' to generate local public key(id_rsa.pub) & private key(id_rsa)
$ ssh-keygen.exe

# remote host create file to save requester's ssh public key
cd ~
vi tmp.pub
mkdir .ssh && chmod 700 .ssh
touch .ssh/authorized_keys && chmod 600 .ssh/authorized_keys
cat tmp.pub >> .ssh/authorized_keys
rm tmp.pub

# copy localhost ssh pubkey, and append content to remote host's ssh authorized_keys
ssh-copy-id -i .ssh/id_rsa.pub root@centos-102

# Only password-free servers allowed login, disable below ssh options in sshd-config
PasswordAuthentication yes -> PasswordAuthentication no
```

## SCP 上传本地文件到虚拟机
```shell
scp .\Downloads\apache-zookeeper-3.7.0-bin.tar.gz root@192.168.137.101:~/Downloads
上传指定的zookeeper二进制安装包到101主机的root用户家目录的Downloads目录下
```

## 配置Linux server static ip及SSH连接
```shell
# 静态IP设置
IPADDR=192.168.137.101
NETMASK=255.255.255.0
GATEWAY=192.168.137.1
DNS1=192.168.137.1

# ssh禁用DNS
UseDNS no
PasswordAuthentication no
```

## rpm包管理器

| Parameter  | Description                                                        |
|------------|--------------------------------------------------------------------|
| `-q`       | query package whether exists                                       |
| `--nodeps` | --nodeps                         don't verify package dependencies |
| `-U`       | upgrage package, need to append `--oldPackage`                     |
```shell
# downgrade package
rpm -Uvh --oldpackage glib2-2.54.2-2.el7.x86_64.rpm

rpm -Uvh --oldpackage /mnt/cdrom/Packages/glibc-common-2.17-307.el7.1.x86_64.rpm --nodeps

# query package
rpm -q packageName
```

## yum
```shell
yum search --showduplicates glibgomp

yum downgrade libgomp-4.8.5-39.el7.x86_64
```


## 安装开源OpenJDK
```shell
# upload jdk packages
scp D:\Software\OpenJDK11U-jdk_x64_linux_openj9_11.0.10_9_openj9-0.24.0.tar.gz root@192.168.137.101:~

# add into env path
export JAVA_HOME=/usr/local/jdk-11.0.10+9
export CLASSPATH=.:$JAVA_HOME/lib
export PATH=$PATH:$JAVA_HOME/bin

# check if works
java -version

```

## zookeeper安装
```shell
# open ports
firewall-cmd --zone=public --permanent --add-port={2888,3888,2181}/tcp
firewall-cmd --reload

# customized data & logs dir
dataDir=//usr/local/apache-zookeeper-3.7.0-bin/data
dataLogDir=//usr/local/apache-zookeeper-3.7.0-bin/logs

# zoo.cfg append below cluster servers info
server.101=centos101:2888:3888
server.102=centos102:2888:3888
server.103=centos103:2888:3888

# cluster scripts to start/stop/status
#!/bin/bash

case $1 in
"start") {
    for h in centos101 centos102 centos103
    do
        echo "...... ready to start zookeeper on server $h ......"
        ssh root@$h "/usr/local/apache-zookeeper-3.7.0-bin/bin/zkServer.sh start"
    done
};;
"stop") {
    for h in centos101 centos102 centos103
    do
        echo "...... ready to stop zookeeper on server $h ......"
        ssh root@$h "/usr/local/apache-zookeeper-3.7.0-bin/bin/zkServer.sh stop"
    done
};;
"status") {
    for h in centos101 centos102 centos103
    do
        echo "...... ready to check zookeeper status on server $h ......"
        ssh root@$h "/usr/local/apache-zookeeper-3.7.0-bin/bin/zkServer.sh status"
    done
};;
esac

# encounter below error message when running cluster scripts
"Error: JAVA_HOME is not set and java could not be found in PATH."
 it causes by initilzed script cannot be executed when login via non-login shell way.
 solution to fix it is that java home env info set into shell file under folder /etc/profile.d/

# zookeeper visualized client tool
[https://github.com/vran-dev/PrettyZoo/releases]

$ stat /xiaosu
cZxid = 0x300000006  # transaction id when creating znode
ctime = Sat Feb 05 19:43:54 CST 2022  # creating znode time
mZxid = 0x300000006 # last updated transaction id
mtime = Sat Feb 05 19:43:54 CST 2022 # last updated time
pZxid = 0x300000006 # last updated transaction id for child znode
cversion = 0 # child znode version, represent update times
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 0
numChildren = 0
```


## Kafka cluster specification
```shell
* scp uploads local package to remote virtual linux server or linux Curl command to download package on virtual linux server

* Command tar to uncompress package

```
*** Configuration file changes ***
| Parameter | Value | Description |
| --- | --- | --- |
| `broker.id` | 101 | the id of the broker, set be a unique integer |
| `listeners` | PLAINTEXT://centos101:9092 | which network card will be listenered by current node |
| `advertised.listeners` | PLAINTEXT://centos101:9092 | clear ip addr & port that client can use to access current node( If not set,it uses the value for "listeners" if configured ) |
| `zookeeper.connect` | centos101:2181,centos102:2181,centos103:2181 | --- |
| `log.dirs` | /usr/local/kafka_2.13-3.1.0/logs | --- |

## GCC 源码安装
```shell
# unpack source package & check prerequirement packages
./contrib/download_prerequisites

# mirror url for prerequirement
https://gcc.gnu.org/pub/gcc/infrastructure/

# gmp
./configure --prefix=/usr/local/gmp-6.1.0

# mpfr
./configure --prefix=/usr/local/mpfr-3.1.4 --with-gmp=/usr/local/gmp-6.1.0

# mpc
./configure --prefix=/usr/local/mpc-1.0.3 --with-mpfr=/usr/local/mpfr-3.1.4 --with-gmp=/usr/local/gmp-6.1.0

# isl
./configure --prefix=/usr/local/isl-0.18 --with-gmp-prefix=/usr/local/gmp-6.1.0/

# gcc, notice: c++ must be installed firstly
./configure --prefix=/usr/local/gcc-10.2.0 --with-gmp=/usr/local/gmp-6.1.0 --with-gmp-include=/usr/local/gmp-6.1.0/include --with-gmp-lib=/usr/local/gmp-6.1.0/lib --with-mpfr=/usr/local/mpfr-3.1.4 --with-mpfr-lib=/usr/local/mpfr-3.1.4/lib --with-mpc=/usr/local/mpc-1.0.3 --with-mpc-lib=/usr/local/mpc-1.0.3/lib --with-isl=/usr/local/isl-0.18 --with-isl-include=/usr/local/isl-0.18/include --with-isl-lib=/usr/local/isl-0.18/lib --enable-checking=release --enable-languages=c,c++ --disable-multilib --with-system-zlib
```

## PostgreSQL安装
```shell
# github下载源码包 & scp上传到Linux virtual machine

tar xzf postgresql-13.4.tar.gz

[root@localhost postgresql-13.4]# ./configure --help

./configure --prefix=/usr/local/postgresql-13.4 --with-segsize=1 --with-blocksize=16 --with-wal-blocksize=32

make -j8
make install

groupadd postgre
useradd -g postgre postgre
mkdir pgdata
chown -R postgre:postgre postgresql-13.4

bin/initdb -D /usr/local/postgresql-13.4/pgdata/

firewall-cmd --zone=public --permanent --add-port=5432/tcp
firewall-cmd --reload

# login account info: username is the same with password
```

| Operation           | Command                                                        |
|---------------------|----------------------------------------------------------------|
| `start`             | ***bin/pg_ctl -D /usr/local/postgresql-13.4/pgdata/ start***   |
| `stop`              | ***bin/pg_ctl -D /usr/local/postgresql-13.4/pgdata/ stop***    |
| `restart`           | ***bin/pg_ctl -D /usr/local/postgresql-13.4/pgdata/ restart*** |
| `list of databases` | ***\l***                                                       |
| `list of Roles`     | ***\du***                                                      |

| config file name | changes |
| --- | --- |
| `pg_hba.conf` | # IPv4 local connections:<br>host all all 0.0.0.0/0 md5 |
| `postgresql.conf` | listen_addresses = '*' |

## Redis
```shell
make distclean  && make BUILD_TLS=yes && make PREFIX=/usr/local/redis-6.2.6 install


```

## MariaDB
```shell
# create maria user
useradd -r -s /sbin/nologin mysql

# cmake compile
cmake . \
-DCMAKE_INSTALL_PREFIX=/usr/local/mariadb-10.6.7 \
-DMYSQL_DATADIR=/usr/local/mariadb-10.6.7/data \
-DSYSCONFDIR=/etc \
-DMYSQL_USER=maria \
-DWITH_INNOBASE_STORAGE_ENGINE=1 \
-DWITH_ARCHIVE_STORAGE_ENGINE=1 \
-DWITH_BLACKHOLE_STORAGE_ENGINE=1 \
-DWITH_PARTITION_STORAGE_ENGINE=1 \
-DWITHOUT_MROONGA_STORAGE_ENGINE=1 \
-DWITH_DEBUG=0 \
-DWITH_READLINE=1 \
-DWITH_SSL=system \
-DWITH_ZLIB=system \
-DWITH_LIBWRAP=0 \
-DENABLED_LOCAL_INFILE=1 \
-DMYSQL_UNIX_ADDR=/usr/local/mariadb-10.6.7/data/maria.sock \
-DDEFAULT_CHARSET=utf8 \
-DDEFAULT_COLLATION=utf8_general_ci
# compile error messages:
CMake Error at cmake/submodules.cmake:38 (MESSAGE):
  No MariaDB Connector/C! Run

      GIT_EXECUTABLE-NOTFOUND submodule update --init --recursive

  Then restart the build.
Call Stack (most recent call first):
  CMakeLists.txt:150 (INCLUDE)

# 关闭服务
bin/mysqladmin -uroot -p shutdown
```

## ffmpeg & yasm
```shell
# ffmpeg依赖yasm,需首先安装yasm

```

## Curl 高级用法
```shell

curl -w "@format_curl" http://localhost:8080/hello -o /dev/null -s | python -m json.tool
```
| Parameter | Description                                          |
|-----------|------------------------------------------------------|
| `-w `     | ***--write-format***                                 |
| `-o `     | ***--output(file) Write to file instead of stdout*** |
| `-s `     | ***--silent        Silent mode***                    |


## OpenLDAP
```shell
# Refer to README to check dependencies
./configure --prefix=/usr/local/cyrus-sasl-2.1.27

./configure --prefix=/usr/local/libsodium-1.0.18

./configure --prefix=/usr/local/openldap-2.6.1

make depend
make -j8
make install
```

## Git 服务器
```shell
./configure --prefix=/usr/local/git-2.33.0
make -j8 && make install

# create new user & dir
useradd git
passwd git
cd /usr/local/git-2.33.0
mkdir repository
# change dir owner & group
chown -R git:git /usr/local/git-2.33.0

# generate ssh private & public keys
su git
ssh-keygen

# append physical machine's ssh pub key to virtual linux machine's user 'git''s authorized_keys file
su git
cat id_rsa.pub >> .ssh/authorized_keys

# create soft link to redirect command git-upload-pack to default location
ln -s /usr/local/git-2.33.0/bin/git-upload-pack /usr/bin/git-upload-pack

# create an empty repository
su git
bin/git init --bare repository/first.git

# open some limit configs if git client wants to clone from git server
vi /etc/ssh/sshd_config

RSAAuthentication yes
PubkeyAuthentication yes
AuthorizedKeysFile .ssh/authorized_keys

# restart sshd service after changes
service sshd restart

# git client clone repository
git clone git@192.138.137.101:/repos/first.git

$ git clone git@192.168.137.101:/repos/first.git
Cloning into 'first'...
git@192.168.137.101's password:
Permission denied, please try again.
git@192.168.137.101's password:
warning: You appear to have cloned an empty repository.

# forbit user git login via ssh
usermod -s /usr/local/git-2.33.0/bin/git-shell git
```

## Jenkins 安装
```shell

# tomcat 10 to run jenkins war will encounter error 404
due to tomcat 10 migrates from Java EE to Jakarta EE, so some javax.* api is replaced to jakarta.* api, we can use tomcat 10 owned jar tool to convert old war. like below:
'java -jar lib/jakartaee-migration-1.0.0-shaded.jar webapps/jenkins.war jenkins.war'

# AWT is not properly configured on this server. Perhaps you need to run your container with "-Djava.awt.headless=true"? See also: https://www.jenkins.io/redirect/troubleshooting/java.awt.headless

reference url:
https://noobient.com/2014/10/01/jenkins-service-unavailable/

```

## PXE & kickstart无人值守安装Linux系统
```shell
yum install -y httpd dhcp tftp-server xinetd syslinux
systemctl start httpd
systemctl start dhcp
systemctl start tftp
systemctl start xinetd

systemctl enable httpd
systemctl enable dhcp
systemctl enable tftp
systemctl enable xinetd

vi /etc/
```