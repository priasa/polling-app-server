# polling-app-server
Clone from https://github.com/callicoder/spring-security-react-ant-design-polls-app/tree/master/polling-app-server

**Step #1** 
Start mysql server container with name "mysql-docker-container"

`docker start mysql-docker-container`

**Step #2**
Compile source code 

`$mvn clean package`

**Step #3**
Build docker image 

`docker build -f Dockerfile -t poll-server-app .`

**Step #4**
Preparing Docker Container. Stop current container then remove

`docker stop poll-server-app-container`

`docker rm poll-server-app-container`

**Step #5**
Create and Run Docker Container

`docker run -t -d --name poll-server-app-container --link mysql-docker-container:mysql -p 5000:5000 poll-server-app`

**Step #6**
Monitoring Application Log

`docker exec -it poll-server-app-container /bin/bash`

Then change to "_log_" directory

`$cd log`

`$tail -400f application.log`

**Step #7**
Check result by open Swagger UI at browser

`http://localhost:5000/swagger-ui.html`