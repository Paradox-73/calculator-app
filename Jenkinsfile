pipeline {
    agent any
    environment {
        // Citations 45 and 48: referencing the specific IDs created in Jenkins
        DOCKER_IMAGE = 'paradox73/calculator-app'
        REGISTRY_CREDENTIALS = credentials('dockerhub-creds') 
    }
    stages {
        stage('Build Code') {
            steps {
                echo 'Compiling Java Code...'
                sh 'javac --release 17 Calculator.java'
                sh 'javac --release 17 TestCalculator.java'
            }
        }
        stage('Test Code') {
            steps {
                echo 'Running Tests...'
                sh 'java TestCalculator'
            }
        }
        stage('Build Docker Image') {
            steps {
                echo 'Building Docker Image...'
                // Building image tagged with build number
                sh "docker build -t $DOCKER_IMAGE:${BUILD_NUMBER} ."
                sh "docker build -t $DOCKER_IMAGE:latest ."
            }
        }
        stage('Push to Docker Hub') {
            steps {
                echo 'Pushing to Docker Hub...'
                // Change _PASSWORD to _PSW
                sh 'echo $REGISTRY_CREDENTIALS_PSW | docker login -u $REGISTRY_CREDENTIALS_USR --password-stdin'
                sh "docker push $DOCKER_IMAGE:${BUILD_NUMBER}"
                sh "docker push $DOCKER_IMAGE:latest"
            }
        }
    }
    post {
        always {
            sh 'docker logout'
        }
    }
}