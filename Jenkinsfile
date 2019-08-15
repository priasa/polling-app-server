pipeline {
    agent any
    tools {
        maven 'maven3'
      }
    stages {
        stage('compile') {
            steps {
                sh "mvn clean package"
            }
        }
        stage('build image') {
            steps {
                sh "sudo docker build -f Dockerfile -t poll-server-app ."
            }
        }
        stage('remove old container') {
            steps {
                sh "sudo docker stop poll-server-app-container"
                sh "sudo docker rm poll-server-app-container"
            }
        }
        stage('create container') {
            steps {
                sh "sudo docker run -t -d --name poll-server-app-container --link mysql-docker-container:mysql -p 5000:5000 poll-server-app"
            }
        }
    }
}