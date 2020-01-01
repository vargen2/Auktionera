# Auktionera

![](https://github.com/vargen2/Auktionera/workflows/Java%20CI/badge.svg)


## Instructions

In application.yml use this to disable security. Will be authed as "TestUser"
```yml
spring:
  profiles:
    active: noSecurity
```

In application.yml use this to enable security.
```yml
spring:
  profiles:
    active: 
```
