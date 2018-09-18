# V2.0版本使用说明

## 使用说明

### 说明总纲

1. 配置setting.properties，使程序适应你的工作环境
2. 配置copyFile.properties，指出你要复制的文件

### setting.properties --工程配置文件

采用项目名=文件列表的方式，其中class文件可以书写为A.class，A或A.java
webapps下文件必须书写后缀名！=必须
当然=项目名对应文件列表可以自动过滤不存在的文件，为方便，可以给所有项目名配相同的文件列表，程序会自动筛选复制；如果不同项目或包有重名文件请自行过滤，谢谢

### copyFile.properties --具体的复制文件内容

parentPaths=这是项目bin路径||tomcat下的webApps中项目名称：必须写到bin目录或项目名
aimClassPath=这是希望复制到的类文件路径，程序在此目录下追加类的文件夹层次
aimOtherPath=这是webapps下文件的复制路径，程序在此目录下追加文件夹层次

## 应用结构
|___run.bat
|___info.md
|___zhanghy.jar
|___src.zip
|___properties模板.zip
|___hisProperties
|___properties
    |___copyFile.properties
    |___setting.properties

> V3.0发展方向：增加多个webapp的支持（实际可实现）；增加多properties支持 ；增加命令行选项：支持交互复制文件； 增加命令行选项：选择是否覆盖已经复制的文件
