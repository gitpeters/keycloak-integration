# keycloak-integration
Spring Security implementation using keycloak as the authentication server and spring boot as the resource server

## Disclaimer
This project is for learning purpose only, in no account should it be used in production.

## Key features
This project uses keycloak as `Authentication server` and SpringBoot as the `Resource server`.

## How to setup Keycloak
- Go to [keycloak website](https://www.keycloak.org/downloads)
- Download the latest version of keycloack. There are 3 processes of downloading keycloak, namely `Server`, `Quickstart` and `Client Adpaters`. In this project, I used `Server` process;
  under the server process, you have the option of downloading the zip file or starting keycloak using docker. (In this project, the zip file download option was used)
- Download and unzip the keycloak file
- Navigate into `...\keycloak-24.0.4\conf` open the `keycloak.conf` file in any of your editor and configure your database connection that keycloak will communicate to:
  ``` yaml
  # Basic settings for running in production. Change accordingly before deploying the server.
  # Database
  # The database vendor.
  db=mysql
  # The username of the database user.
  db-username=root
  # The password of the database user.
  db-password=password
  # The full database JDBC URL. If not provided, a default URL is set based on the selected database vendor.
  db-url=jdbc:mysql://localhost:3306/keycloak
  # Observability
  # If the server should expose healthcheck endpoints.
  #health-enabled=true
  # If the server should expose metrics endpoints.
  #metrics-enabled=true
  http-port=9090

- Save the changes, move back to `...\keycloak-24.0.4\bin` open your command prompt in this path and running the following command:
  ``` cmd
    kc.bat start-dev
Keycloak server will start on `http://localhost:9090`, here you'll be ask to create keycloak administrator user account, provide information that you will remember.
- Login with your credentials to the keycloak dashboard
- Create your `realm` and `client`
- Create one user with admin role and assign `role_realm_admin` role to the user. This will enable this user to perform realm functionality.

## SpringBoot Setup
- Go to [spring initialization website](https://start.spring.io/)
- Create a new project
- Add the following dependencies:
  ```maven
    <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
			<version>2.2.0</version>
		</dependency>

		<!--		Keycloak dependencies to configure authorization	-->
		<dependency>
			<groupId>org.keycloak</groupId>
			<artifactId>keycloak-servlet-filter-adapter</artifactId>
			<version>24.0.4</version>
			<scope>provided</scope>
		</dependency>

		<dependency>
			<groupId>org.keycloak</groupId>
			<artifactId>keycloak-spring-boot-starter</artifactId>
			<version>24.0.4</version>
		</dependency>

		<dependency>
			<groupId>org.keycloak</groupId>
			<artifactId>keycloak-admin-client</artifactId>
			<version>24.0.4</version>
		</dependency>
- Setup your spring security configuration, refer to `src/main/java/com/peters/Keycloak/config/SecurityConfig_old.java`

