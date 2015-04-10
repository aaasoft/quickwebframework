# quickwebframework

![alt tag](https://github.com/aaasoft/quickwebframework/raw/master/resource/logo.png)

## 介绍
QuickWebFramwork是一个开发模块化WEB程序的框架项目。适用于如嵌入式设备WEB管理后台、小型内部办公系统等小型WEB项目。
### OSGi
    QuickWebFramework内部使用OSGi容器承载各WEB模块插件。开发一个插件就是开发一个OSGi Bundle。支持所有实现OSGi R4 Service Platform规范的OSGi容器，如：Apache Felix、Equinox OSGi等。

### 模块化
    在程序运行时可以任意安装，启用，停止，卸载WEB模块插件而不用重新启动WEB容器。变化即时生效。

### 依赖注入支持
    QuickWebFramework中定义了IoC框架的接口，理论上来说可以支持各种IoC框架。
    目前支持Spring IoC框架。

### 视图框架、MVC框架支持
    目前支持Struts2、Spring MVC、JSP。

### 模板引擎
    QuickWebFramework中定义了模板引擎的接口，理论上来说可以支持各种模板引擎。
    目前支持velocity、freemarker、jsp模板引擎。

### 配置简单
    要使用QuickWebFramework，仅需要在web.xml中配置一个QuickWebFrameworkLoaderListener监听器，一个过滤器，并在WEB-INF目录下面放置一个QuickWebFramework的配置文件即可。

## 路线图
 * 支持mybatis,hibernate等其他持久化框架

![alt tag](https://github.com/aaasoft/quickwebframework/raw/master/resource/qwf_loadmap.png)