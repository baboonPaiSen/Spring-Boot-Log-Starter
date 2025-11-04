# Spring Boot Log Starter

一个用于Spring Boot应用的日志和异常处理starter，提供了统一的异常处理、日志记录和响应封装功能。

## 功能特性

- 统一异常处理机制
- 自动日志记录（包括请求和响应）
- 统一响应格式封装
- 支持MDC（Mapped Diagnostic Context）跟踪ID
- 可配置的日志过滤和处理选项
- 自动装配，无需额外配置

## 安装

在你的Spring Boot项目中添加以下依赖：

```xml
<dependency>
    <groupId>cn.com.wind</groupId>
    <artifactId>wind-spring-boot-starter-log</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 配置选项

在`application.yml`或`application.properties`中配置以下选项：

```yaml
log:
  exception:
    defaultExceptionHandler:
      enabled: true
    responseDataAdvice:
      enabled: true
  methodLogAspect:
    enabled: true
  httpRequestMDCFilter:
    enabled: true
  dispose:
    errorMsgShowMaxCount: 5
  filter:
    globalSwitch: false
  eagle:
    globalSwitch: false
    send: false
```

## 使用说明

### 自动装配

该starter支持自动装配，只需在项目中引入依赖，即可自动启用所有功能。无需额外的配置或注解。

### 统一响应格式

所有控制器的响应将被自动封装为统一格式：

```json
{
  "ts": 1640995200000,
  "data": { /* 业务数据 */ },
  "code": "1",
  "msg": "success"
}
```

### 异常处理

全局异常处理器会自动捕获并处理常见异常，返回统一格式的错误响应。

### 日志记录

通过AOP切面自动记录控制器的请求和响应信息。

### 忽略处理

使用`@IgnoreResponseAdvice`注解可以忽略特定控制器或方法的统一响应封装和异常处理。

## 许可证

MIT License
