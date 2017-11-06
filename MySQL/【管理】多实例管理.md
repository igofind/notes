# MySQL启停 + 多实例管理

## [启停管理](https://dev.mysql.com/doc/refman/5.7/en/starting-server.html)

[自启动 automatic-start [官方]](https://dev.mysql.com/doc/refman/5.7/en/automatic-start.html)

### [mysqld](https://dev.mysql.com/doc/refman/5.7/en/automatic-start.html)

```shell
# 此命令输出所有可用的参数及变量 全平台可用
shell> mysqld --verbose --help
```



### [mysql 服务](https://dev.mysql.com/doc/refman/5.7/en/windows-start-service.html)

```powershell
# 请使用MySQL的真实安装路径和真实的配置文件路径替换下列命令中的路径，
C:\> "C:\MySQL\MySQL Server 5.7\bin\mysqld" --install xx_name --defaults-file=C:\my-opts.cnf
# 删除服务 
C:\> "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysqld" --remove xx_name
# 启动
C:\> net start xx_name
# 停止
C:\> net stop xx_name
```

> **注意**
>
> - 只适用于windows系统。
>
> - 注册服务需要使用管理员权限。
>
> - 需要先关闭运行的mysql实例（可关闭所有方式启动的mysql实例）：
>
>   ```powershell
>   C:\> "C:\MySQL\MySQL Server 5.7\bin\mysqladmin" -u root -p shutdown
>   ```
>
> - "C:\MySQL\MySQL Server 5.7\bin\mysqld" 路径建议写完整。
>
> - 使用--install参数注册的服务都会随系统启动自启动，如果不想自启动，可替换为--install-manual



### [mysqld_safe](https://dev.mysql.com/doc/refman/5.7/en/mysqld-safe.html) [官方推荐]

```sh
# 不指定配置文件
shell> /path/to/mysqld_safe --user=mysql
# 指定配置文件
shell> /path/to/mysqld_safe --defaults-file=/path/to/my-opts.cnf --user=mysql
```

> **注意**
>
> - 在Unix和Unix-like系统中使用。
> - 使用包管理器安装的mysql中不包含mysqld_safe命令。
> - [参数列表](https://dev.mysql.com/doc/refman/5.7/en/mysqld-safe.html#option_mysqld_safe_mysqld) 



### [systemd](https://dev.mysql.com/doc/refman/5.7/en/using-systemd.html)

```sh
# 主流Linux发行版（常作为服务器的发行版）
shell> service mysqld {start|stop|restart|status}
# 需要在支持systemctl命令的Linux发行版中使用
shell> systemctl {start|stop|restart|status} mysqld
```

> **注意**
>
> - 在支持 `service` 或 `systemctl` 命令的**RPM系**和**Debian系**的Linux系统中使用。
> - 在Debian系的发行版中和SLES系统，服务名为 **mysql** 而不是 mysqld 。



### [mysql.server](https://dev.mysql.com/doc/refman/5.7/en/mysql-server.html)

仅作了解。 在[System V-style](https://en.wikipedia.org/wiki/UNIX_System_V)（首批商用Unix系统）的系统中，执行mysql.server（mysql自带，一般放在/etc/init.d/目录下）脚本。本质上使用的是`mysqld_safe` 。

```shell
# 本质上是通过systemctl和service命令
shell> ./mysql.server start
shell> ./mysql.server stop

# 实际运行效果
sehll> ./mysql.server stop
[ ok ] Stopping mysql.server (via systemctl): mysql.server.service.

# 开机自启：先拷贝mysql.server到/etc/init.d/目录下
shell> cp mysql.server /etc/init.d/mysql
shell> chmod +x /etc/init.d/mysql
# 开机自启：RPM系
shell> chkconfig --add mysql
# 开机自启：RPM系，在有些系统上，下面的语句也要执行
shell> chkconfig --level 345 mysql on
# 开机自启：debian系
shell> update-rc.d mysql defaults

```

> **注意**
>
> - 用rpm包安装的mysql实例，mysql.server可能被安装在/etc/init.d目录下，名称或为mysqld或mysql。
> - 用源码或Binary文件安装的，在support-files中可找到mysql.server文件。

### [daemon](https://dev.mysql.com/doc/refman/5.7/en/osx-installation-launchd.html)

仅作了解。在OS X中使用守护进程，本质上使用的是`mysqld_safe`。



## [多实例](https://dev.mysql.com/doc/refman/5.7/en/multiple-servers.html)

### [同一实例，多个data目录](https://dev.mysql.com/doc/refman/5.7/en/multiple-data-directories.html)

#### 须有差异的配置

同一个mysql程序，启动多次，每次启动使用不同的data目录，以此模拟出多实例效果。

以此种方式启动时，每个实例的以下参数须配置不同的值：

- [--port=*port_num*](https://dev.mysql.com/doc/refman/5.7/en/server-options.html#option_mysqld_port)
- [--socket={*file_name*|*pipe_name*}](https://dev.mysql.com/doc/refman/5.7/en/server-options.html#option_mysqld_socket)
- [--shared-memory-base-name=*name*](https://dev.mysql.com/doc/refman/5.7/en/server-options.html#option_mysqld_shared-memory-base-name)
- [--pid-file=*file_name*](https://dev.mysql.com/doc/refman/5.7/en/server-options.html#option_mysqld_pid-file)

如果启用log相关的配置，以下参数也须不同：

- [--general_log_file=*file_name*](https://dev.mysql.com/doc/refman/5.7/en/server-system-variables.html#sysvar_general_log_file)
- [--log-bin[=*file_name*]](https://dev.mysql.com/doc/refman/5.7/en/replication-options-binary-log.html#option_mysqld_log-bin)
- [--slow_query_log_file=*file_name*](https://dev.mysql.com/doc/refman/5.7/en/server-system-variables.html#sysvar_slow_query_log_file)
- [--log-error[=*file_name*]](https://dev.mysql.com/doc/refman/5.7/en/server-options.html#option_mysqld_log-error)

如果可以，为了更好的性能，使用下面的参数，以在多个物理磁盘之间分配负载：

- [--tmpdir=*dir_name*](https://dev.mysql.com/doc/refman/5.7/en/server-options.html#option_mysqld_tmpdir)

#### 设置多个目录

使用 [--datadir=*dir_name*](https://dev.mysql.com/doc/refman/5.7/en/server-options.html#option_mysqld_datadir) 来指定data目录。

- 创建一个新data目录

  解压版的mysql安装包中，包含有初始的data目录，可以从安装包中解压出新的data目录。

  > **注意**：高版本的mysql中似乎不再包含data目录了。可使用以下命令生成：
  >
  > - windows
  >
  >   ```powershell
  >   C:\> bin\mysqld --initialize
  >   ```
  >
  > - linux
  >
  >   ```shell
  >   shell> bin/mysqld --initialize --user=mysql
  >   ```

- 拷贝一个现有data目录

  > **注意**：最好将data目录所属的mysql的配置文件也一并拷贝，并做适当修改。

### 多个实例

#### [Windows](https://dev.mysql.com/doc/refman/5.7/en/multiple-windows-servers.html)

**使用命令行方式**

创建配置文件

- my-opts1.cnf

  ```ini
  [mysqld]
  datadir = C:/mydata1
  port = 3307
  enable-named-pipe
  socket = mypipe1
  ```

- my-opts2.cnf

  ```ini
  [mysqld]
  datadir = C:/mydata2
  port = 3308
  enable-named-pipe
  socket = mypipe1
  ```

- ......

```powershell
# 启动：使用 --defaults-file 指定配置文件
C:\> C:\mysql\bin\mysqld --defaults-file=C:\my-opts1.cnf
C:\> C:\mysql\bin\mysqld --defaults-file=C:\my-opts2.cnf

# 关闭
C:\> C:\mysql\bin\mysqladmin --port=3307 --host=127.0.0.1 -u=root -p shutdown
C:\> C:\mysql\bin\mysqladmin --port=3308 --host=127.0.0.1 -u=root -p shutdown
```



**使用服务方式**

使用全局配置文件（适用于当MySQL版本相同时，也可用各自的配置文件）

```ini
# options for mysqld1 service
[mysqld1]
basedir = C:/mysql-5.5.9
port = 3307
enable-named-pipe
socket = mypipe1

# options for mysqld2 service
[mysqld2]
basedir = C:/mysql-5.7.21
port = 3308
enable-named-pipe
socket = mypipe2
```

```powershell
# 注册服务
C:\> C:\mysql-5.5.9\bin\mysqld --install mysqld1
C:\> C:\mysql-5.7.21\bin\mysqld --install mysqld2
# 启动服务
C:\> NET START mysqld1
C:\> NET START mysqld2
# 关闭服务
C:\> NET STOP mysqld1
C:\> NET STOP mysqld2
```

使用各自的配置文件（适用于当MySQL版本不同时）

- my-opts1.cnf

  ```ini
  [mysqld]
  basedir = C:/mysql-5.5.9
  port = 3307
  enable-named-pipe
  socket = mypipe1
  ```

- my-opts2.cnf

  ```ini
  [mysqld]
  basedir = C:/mysql-5.7.21
  port = 3308
  enable-named-pipe
  socket = mypipe2
  ```

```powershell
# 注册服务
C:\> C:\mysql-5.5.9\bin\mysqld --install mysqld1 --defaults-file=C:\my-opts1.cnf
C:\> C:\mysql-5.7.21\bin\mysqld --install mysqld2 --defaults-file=C:\my-opts2.cnf
```



#### [Linux](https://dev.mysql.com/doc/refman/5.7/en/multiple-unix-servers.html)

###### 使用命令行方式启动

mysqld_safe：

```shell
# 使用不同配置文件
shell> mysqld_safe --defaults-file=/path/to/my.cnf
shell> mysqld_safe --defaults-file=/path/to/my.cnf2

# 相似的方式（可选）
shell> MYSQL_UNIX_PORT=/tmp/mysqld-new.sock
shell> MYSQL_TCP_PORT=3307
shell> export MYSQL_UNIX_PORT MYSQL_TCP_PORT
shell> mysql_install_db --user=mysql
shell> mysqld_safe --datadir=/path/to/datadir &
```

[mysqld_multi](https://dev.mysql.com/doc/refman/5.7/en/mysqld-multi.html)：

语法：**mysqld_multi** [*options*] {start|stop|reload|report} [*GNR*[,*GNR*] ...]

- 同一实例，启动多个mysql服务

```ini
# 配置示例
[mysqld_multi]
mysqld     = /usr/local/mysql/bin/mysqld_safe
mysqladmin = /usr/local/mysql/bin/mysqladmin
user       = multi_admin
password   = my_password

[mysqld2]
socket     = /tmp/mysql.sock2
port       = 3307
pid-file   = /usr/local/mysql/data2/hostname.pid2
datadir    = /usr/local/mysql/data2
language   = /usr/local/mysql/share/mysql/english
user       = unix_user1

[mysqld3]
mysqld     = /path/to/mysqld_safe
ledir      = /path/to/mysqld-binary/
mysqladmin = /path/to/mysqladmin
socket     = /tmp/mysql.sock3
port       = 3308
pid-file   = /usr/local/mysql/data3/hostname.pid3
datadir    = /usr/local/mysql/data3
language   = /usr/local/mysql/share/mysql/swedish
user       = unix_user2
......
```

```shell
# 使用[mysqld2]的配置 启动
shell> mysqld_multi start 2
shell> mysqld_multi stop 2
# 范围停启
shell> mysqld_multi stop 8,10-13
```

- 多个实例，同一配置（**易混**），启动多个mysql服务

```ini
[mysqld2]
socket     = /tmp/mysql.sock2
port       = 3307
pid-file   = /usr/local/mysql/data2/hostname.pid2
datadir    = /usr/local/mysql/data2

[mysqld3]
socket     = /tmp/mysql.sock3
port       = 3308
pid-file   = /usr/local/mysql/data3/hostname.pid3
datadir    = /usr/local/mysql/data3
......
```

```shell
# 使用相同配置文件
shell> /path/to/mysql2/mysqld_multi start 2
shell> /path/to/mysql3/mysqld_multi start 3
```



###### 随系统自启动

（未完）