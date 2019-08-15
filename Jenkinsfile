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
                sh "docker build -f Dockerfile -t poll-server-app ."
            }
        }
        stage('create container') {
            steps {
                sh "docker run -t --name poll-server-app-container --link mysql-docker-container:mysql -p 5000:5000 poll-server-app"
            }
        }
    }
}