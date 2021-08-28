#How to build::
1. git clone https://github.com/shofiq4744/cardinity-taskmanager.git
2. cd cardinity-taskmanager
3. Change the MySQL credential and hostname(if needed) on application.yml file
2. mvn clean install (note use mvn generate-sources if the source not generated)
3. cd target
4. java -jar TaskManagerApplication-1.0.jar </br>

#Use technology::
1. Java 11
2. spring boot 2.5.4
3. MySQL
4. Maven </br>
Additional application dependency</br>
1. QueryDsl
2. JWT token

There are two default user will be generated on DB (table->users) when application start.
1. username: admin password: admin Role: ADMIN
2. username: user password: user Role: USER

#How To Test the application
The applicaion use swagger for documentation, The swagger-ui have option for test end-points
URL for API documentation and test http://localhost:9000/swagger-ui.html
STEP 1: Login to the authenticate end-point which return access token
STEP 2: use the access token (Bearer " "+token) to communicate secure end-point
Search api test case (use and operation for search)
{
  "isExpire": true, 		# filter expire task(due date before today)
  "projectByUser": "string",# filter task by project creator
  "projectId": 1,			# filter task by project id
  "status": "string",		# filter task by status(open,in-progress,closed)
  "taskByUser": "string"	# filter task by task creator
}
for example if you want all task then pass all crireria null
if you want only expire task just pass isExpire=true
if you want specific project just pass project id
if you want specific status just pass the status name
if you want specific user project just pass projectByUser=username
if you want specific user task just pass taskByUser=username



