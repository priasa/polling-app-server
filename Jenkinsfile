pipeline {
    agent any
    tools {
        maven 'maven-3.6.2'
      }
    stages {
        stage('Compile Source') {
            steps {
                sh "mvn clean package"
            }
        }
        stage('Build Image') {
            steps {
                sh "docker build -f Dockerfile -t poll-server-app ."
            }
        }
        stage ('Preparing Database'){
            steps {
                script {
                    containerId = sh (script: "docker ps -q -f name=mysql-docker-container -f status=running", returnStdout: true).trim()
                    if (containerId == '') {
                        echo "Start MySQL"
                        sh 'docker start mysql-docker-container'
                        sleep 60
                        containerId = sh (script: "docker ps -q -f name=mysql-docker-container -f status=running", returnStdout: true).trim()
                    }
                    echo "MySQL Container ID is ==> ${containerId}"
                }
            }
        }
        stage('Preparing Container') {
            steps {
                sh 'docker stop poll-server-app-container || echo "docker container poll-server-app-container is not currently running"'
                sh 'docker rm poll-server-app-container || echo "docker container poll-server-app-container is not currently running"'
            }
        }
        stage('Create container') {
            steps {
                sh "docker run -t -d --name poll-server-app-container --link mysql-docker-container:mysql -p 5000:5000 poll-server-app"
            }
        }
    }
}