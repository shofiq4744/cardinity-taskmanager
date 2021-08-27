#Application General information
This is a project and task management application package as a standard jar.
There are two default user will be generated when application start with ADMIN and USER role.
1. username: admin password: admin Role: ADMIN
2. username: user password: user Role: USER

#How to build
1. git clone https://github.com/shofiq4744/cardinity-taskmanager.git
2. Go to project root directory
3. Change the MySQL connection password and hostname(if needed)
2. mvn clean install
3. cd target
4. java -jar TaskManagerApplication-1.0.jar
The application will run on 9000 port. You can check API documentation 
all the end-point on http://localhost:9000/swagger-ui.html

You can directly test all the end-point from swagger-ui 
1. Execute auth end-point which will return an access token
2. Use the access token after Bearer +token on the Authorization header
3. Execute rest of end-point using the token

#Use technology
1. Java 11
2. spring boot 2.5.4
3. MySQL
4. Maven
Additional application dependency
1. QueryDsl
2. JWT token