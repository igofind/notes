# MySQL 解压版安装 (5.7 为例)

## Windows 版安装

#### 解压

使用常用解压工具解压到自定义目录即可。

#### 配置文件

创建my.ini文件（拷贝即可），放在mysql根目录下，可以只留下 port、basedir、datadir等几个简单的配置项。

**Win下配置文件读取顺序**

| File Name                                | Purpose                                  |
| ---------------------------------------- | ---------------------------------------- |
| `%PROGRAMDATA%\MySQL\MySQL Server 5.7\my.ini`, `%PROGRAMDATA%\MySQL\MySQL Server 5.7\my.cnf` | Global options                           |
| `%WINDIR%\my.ini`, `%WINDIR%\my.cnf`     | Global options                           |
| `C:\my.ini`, `C:\my.cnf`                 | Global options                           |
| `*BASEDIR*\my.ini`, `*BASEDIR*\my.cnf`   | Global options                           |
| `defaults-extra-file`                    | The file specified with [`--defaults-extra-file`](https://dev.mysql.com/doc/refman/5.7/en/option-file-options.html#option_general_defaults-extra-file), if any |
| `%APPDATA%\MySQL\.mylogin.cnf`           | Login path options (clients only)        |

#### 创建服务

以管理员模式启动cmd，在cmd中注册mysql服务


```shell
C:\> "E:\MySQL\MySQL_5.7_F1_3.0\bin\mysqld.exe" install MySQLF1V3 --defaults-file="E:\MySQL\MySQL_5.7_F1_3.0\my.ini"
```

`"E:\MySQL\MySQL_5.7_F1_3.0\bin\mysqld.exe"`请写出完整路径，否则存在多个mysql时，注册的服务中mysqld命令路径会出问题。

#### 启动、停止服务

````powershell
C:\> net start/stop mysqlxxx # 这里的mysqlxxx是注册时的服务名称。
````

#### 重置root密码

在my.ini中加入 `skip-grant-tables`参数，重启mysql服务，使用navicat或命令行无密码登录。

```powershell
C:\> mysql -u root (回车)
```

修改user表中root对应的`authentication_string`字段：

```mysql
mysql > update user set authentication_string = password('new password') where user='root';
```

然后，注释掉`skip-grant-tables`配置，重启mysql服务，使用修改后的密码登录即可。

**密码过期问题：**

在命令行中仍可用旧密码登录，登录后更新密码：

```shell
shell> mysql -u root --port 3308 -p  # 回车后输入旧密码
mysql> set password for 'root'@'%'=password("new password");
```

### 远程用户

```mysql
mysql> CREATE USER 'root'@'%' IDENTIFIED BY 'your password';
mysql> GRANT ALL ON *.* TO 'root'@'%' IDENTIFIED BY 'your password'; # 此命令可以直接新增用户和赋权
mysql> FLUSH PRIVILEGES; # 刷新权限
```

## Linux 版安装

#### 步骤

> Note：如果当前非root用户，所有shell命令前加下sudo

[官方参考：Linux解压版安装说明](https://dev.mysql.com/doc/refman/5.7/en/binary-installation.html)

新增组、用户、解压、初始化mysql、注册服务、环境变量；

```shell
shell> groupadd mysql
shell> useradd -r -g mysql -s /bin/false mysql
shell> cd /usr/local
## 可以解压到 /opt/ 目录下
shell> tar zxvf /path/to/mysql-VERSION-OS.tar.gz
shell> ln -s full-path-to-mysql-VERSION-OS mysql
shell> cd mysql
shell> mkdir mysql-files
shell> chown mysql:mysql mysql-files
shell> chmod 750 mysql-files
shell> bin/mysqld --initialize --user=mysql
shell> bin/mysql_ssl_rsa_setup 
shell> bin/mysqld_safe --user=mysql &
## Next command is optional
## 这一步中会把mysql的安装目录（如果安装在/usr/local/mysql的话）加入到path变量中
shell> cp support-files/mysql.server /etc/init.d/mysql.server
## 使用which mysql查看mysql是否在环境变量中，如果不在就执行这个命令，也可写入/etc/profile中
shell> export PATH=$PATH:/usr/local/mysql/bin
```

#### 配置文件

**Linux系统中配置文件读取顺序**

| File Name             | Purpose                                  |
| --------------------- | ---------------------------------------- |
| `/etc/my.cnf`         | Global options                           |
| `/etc/mysql/my.cnf`   | Global options                           |
| `*SYSCONFDIR*/my.cnf` | Global options                           |
| `$MYSQL_HOME/my.cnf`  | Server-specific options (server only)    |
| `defaults-extra-file` | The file specified with [`--defaults-extra-file`](https://dev.mysql.com/doc/refman/5.7/en/option-file-options.html#option_general_defaults-extra-file), if any |
| `~/.my.cnf`           | User-specific options                    |
| `~/.mylogin.cnf`      | User-specific login path options (clients only) |

配置中可以只留下 port、basedir、datadir等几个简单的配置项

#### 启动、停止服务

```shell
shell> service mysql start/stop
```

#### 重置root密码

如果mysql已启动，先停止mysql，再以下面命令启动：

```sh
# 注意加上 '&' 让mysql在后台运行，如果命令行没有退出（当前不能输入命令）使用 ctrl + d
shell> /usr/local/mysql/bin/mysqld_safe --skip-grant-tables & 
```

执行密码修改：

```mysql
shell> mysql -u root (回车)
mysql> update user set authentication_string = password('new password') where user='root';
mysql> FLUSH PRIVILEGES;
shell> mysqladmin -u root shutdown;
```

