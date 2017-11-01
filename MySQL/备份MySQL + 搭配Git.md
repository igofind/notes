# 备份MySQL + 搭配Git

**目的**：利用git的版本控制功能，既能完整的记录数据库变动的历史，又能节省空间（如果数据库没有变化时，git命令执行时能识别没有变动，不会产生提交）。

**数据**：可用于日常开发库的备份。

## Windows

可使用windows自带的计划任务功能，定时执行脚本进行备份。

#### 方式一：单纯mysqldump备份

```powershell
rem *******************************Code Start*****************************
@echo off
rem 日期
set "Ymd=%date:~,4%%date:~5,2%%date:~8,2%"

rem 此处没有换行
"E:\MySQL\MySQL Server 5.6\bin\mysqldump" -uroot -pemroot --skip-dump-date -R us_sys > F:\DBbackup\us_sys\us_sys_%Ymd%.sql

7z a F:\DBbackup\us_sys\us_sys_%Ymd%.7z F:\DBbackup\us_sys\us_sys_%Ymd%.sql

@echo on
rem *******************************Code End*****************************
```

#### 方式二：加入git命令

```powershell
rem *******************************Code Start*****************************
@echo off

rem 设置日期和备份路径
set "Ymd=%date:~,4%%date:~5,2%%date:~8,2%"
set "basepath=F:\DBbackup\em_backup_git\"

rem es为要备份的库名
"E:\MySQL\MySQL Server 5.6\bin\mysqldump" -uroot -pemroot --skip-dump-date -R es > %basepath%src\es\es.sql

rem 使用7z生成压缩文件
7z a %basepath%src\es\es_%Ymd%.7z %basepath%src\es\es.sql

rem us_sys为要备份的库名
"E:\MySQL\MySQL Server 5.6\bin\mysqldump" -uroot -pemroot --skip-dump-date -R us_sys > %basepath%src\us_sys\us_sys.sql

rem 使用7z生成压缩文件
7z a %basepath%src\us_sys\us_sys_%Ymd%.7z %basepath%src\us_sys\us_sys.sql

rem 复制（移动）生成的压缩文件
copy %basepath%src\es\es_%Ymd%.7z %basepath%es.7z
copy %basepath%src\us_sys\us_sys_%Ymd%.7z %basepath%us_sys.7z
del %basepath%src\es\es.sql
del %basepath%src\us_sys\us_sys.sql

rem 使用git时行版本控制
git commit
cd /d %basepath%
git add .
git commit -m "v%Ymd%"

@echo on
rem *******************************Code End*****************************
```

## Linux

（待补充）