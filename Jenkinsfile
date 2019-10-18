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
        stage('Preparing Container') {
            steps {
                sh "docker ps -a  | grep poll-server-app-container && docker stop poll-server-app-container"
                sh "docker ps -a  | grep poll-server-app-container && docker rm poll-server-app-container"
            }
        }
        stage('Create container') {
            steps {
                sh "docker run -t -d --name poll-server-app-container --link mysql-docker-container:mysql -p 5000:5000 poll-server-app"
            }
        }
    }
}