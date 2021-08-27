#Application General information::</br>
This is a project and task management application package as a standard jar.</br>
There are two default user will be generated on DB (table->users) when application start.
1. username: admin password: admin Role: ADMIN
2. username: user password: user Role: USER

#How to build::
1. git clone https://github.com/shofiq4744/cardinity-taskmanager.git
2. cd cardinity-taskmanager
3. Change the MySQL connection password and hostname(if needed) on application.yml file
2. mvn clean install (note use mvn generate-sources if the source not generated)
3. cd target
4. java -jar TaskManagerApplication-1.0.jar </br>
The application will run on 9000 port. You can check API documentation 
on http://localhost:9000/swagger-ui.html

You can directly test all the end-point from swagger-ui 
1. Execute auth end-point which will return an access token
2. Use the access token after Bearer (Bearer+" "+token) on the Authorization header
3. Execute rest of end-point using the token

#Use technology::
1. Java 11
2. spring boot 2.5.4
3. MySQL
4. Maven </br>
Additional application dependency</br>
1. QueryDsl
2. JWT token