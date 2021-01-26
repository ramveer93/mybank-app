pipeline {
    environment {
    	registry = "ramveer110/mybank-springboot-app.jar"
    	registryCredential = 'docker_registry_cred'
    	dockerImage = ''
    }
    agent any
    tools {
        maven 'maven-3.6.3'
        jdk 'jdk-9.0.4'
    }
    stages {
        stage ('Initialize') {
            steps {
                bat '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }

        stage ('Build') {
            steps {
                bat 'mvn -Dmaven.test.failure.ignore=true install' 
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml' 
                }
            }
        }
        stage ('Test') {
            steps {
                bat 'mvn clean install' 
            }
        }
        stage('Building Docker image'){
           steps{
             script{
               dockerImage = docker.build registry + ":$BUILD_NUMBER"
             }
           }
        }
        stage('Deploy Image') {
          steps{
            script {
               docker.withRegistry( '', registryCredential ) {
               dockerImage.push()
               }
             }
            }
          }
        stage('Remove Unused docker image') {
          steps{
           sh "docker rmi $registry:$BUILD_NUMBER"
          }
         }   
   }
}