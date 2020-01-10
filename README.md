![](https://github.com/vargen2/Auktionera/workflows/Java%20CI/badge.svg)
![](https://github.com/vargen2/Auktionera/workflows/Create%20Release/badge.svg)

# Auktionera

Project I made in a course at ITHS called complex Java 18. 

Backend "rest-api" made with Spring boot, MySQL and RabbitMQ. Authentiaction with "social login", OAuth2 and JWT.

Frontend made with javascript framework Vue.js, Vue-cli and Vuetify.

## Instructions to run on localhost

### 1. Preparations

- Java 11
- Maven
- MySQL installed on localhost
	- port: 3306
   	- username: root
   	- password: root
- RabbitMQ installed on localhost
	- https://www.rabbitmq.com/download.html
- Node.js
	- https://nodejs.org/en/

### 2. Backend

Swagger: http://localhost:8080/swagger-ui.html

localhost:8080

	/backend

Test

	mvn surefire:test

Run

	mvn spring-boot:run


### 3. Frontend

localhost:8081

	/frontend

Run

	npm install
	npm run serve

## Help from

https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-1/

https://github.com/callicoder/spring-boot-react-oauth2-social-login-demo
