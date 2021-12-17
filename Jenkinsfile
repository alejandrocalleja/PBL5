pipeline {
  agent any
  stages {
    stage('Build Project') {
      steps {
        sh 'mvn clean install'
      }
    }

    stage('Static Analysis') {
      steps {
        withSonarQubeEnv('sonar-server') {
          sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=decklearn'
        }
      }
    }

    stage('Quality Gate') {
      steps {
        timeout(time: 10, unit: 'SECONDS') {
          waitForQualityGate true
        }
      }
    }

    stage('Unit Test') {
      steps {
        sh 'mvn clean test'
      }
    }

    stage('Publish Docker') {z
      steps {
        withCredentials(bindings: [usernamePassword(credentialsId: 'dockerhub-credentials', passwordVariable: 'pass', usernameVariable: 'name')]) {
          sh 'mvn compile jib:build -Djib.to.auth.username=$name -Djib.to.auth.password=$pass'
        }

      }
    }

  }
  post {
        always {
            junit 'build/reports/**/*.xml'
        }
    }
  tools {
    maven 'maven'
  }
}