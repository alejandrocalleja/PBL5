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
    
    stage('Remove docker container and image') {
      when {
        branch 'develop'
      }
      steps {
        sh 'docker ps -f name=decklearn -q | xargs --no-run-if-empty docker container stop'
        sh 'docker container ls -a -fname=decklearn -q | xargs -r docker container rm'
        sh 'docker images -q -f dangling=true | xargs --no-run-if-empty docker rmi'
      }
    }

    stage('Create docker image') {
      when {
        branch 'develop'
      }
      steps {
        withCredentials(bindings: [usernamePassword(credentialsId: 'dockerhub-credentials', passwordVariable: 'pass', usernameVariable: 'name')]) {
          sh 'mvn compile jib:dockerBuild -Djib.to.auth.username=$name -Djib.to.auth.password=$pass'
        }
      }
    }

    stage('Run decklearn container') {
      when {
        branch 'develop'
      }
      steps {
        sh 'docker run -d -p 8080:8080 --name decklearn alejandrocalleja/decklearn'
      }
    }

    stage('Publish docker image') {
      when {
        branch 'release'
      }
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
