pipeline {
    agent any
    tools {
        maven 'maven3'
      }
    stages {
        stage('Compile Source') {
            steps {
                sh "mvn clean package"
            }
        }
        stage('Build Image') {
            steps {
                sh "sudo docker build -f Dockerfile -t poll-server-app ."
            }
        }
        stage('Preparing Container') {
            steps {
                def container_exists = sh (
                )
                sh "sudo docker stop poll-server-app-container"
                sh "sudo docker rm poll-server-app-container"
            }
        }
        stage('Create container') {
            steps {
                sh "sudo docker run -t -d --name poll-server-app-container --link mysql-docker-container:mysql -p 5000:5000 poll-server-app"
            }
        }
    }
}