pipeline {
    agent any

    environment {
        // Ortak ortam değişkenleri burada tanımlanabilir
        DOCKER_IMAGE = "spring-boot-app:${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                // Kod deposunu çekme
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                // Maven ile projenin derlenmesi ve testlerin çalıştırılması
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKER_IMAGE}", "-f backend-service.dockerfile .")
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: DOCKER_HUB_CREDENTIALS, passwordVariable: 'DOCKER_HUB_PASSWORD', usernameVariable: 'DOCKER_HUB_USERNAME')]) {
                        sh 'echo $DOCKER_HUB_PASSWORD | docker login -u $DOCKER_HUB_USERNAME --password-stdin'
                        sh "docker tag ${DOCKER_IMAGE}:${env.BUILD_NUMBER} ${DOCKER_HUB_USERNAME}/${DOCKER_IMAGE}:${env.BUILD_NUMBER}"
                        sh "docker push ${DOCKER_HUB_USERNAME}/${DOCKER_IMAGE}:${env.BUILD_NUMBER}"
                        sh 'docker logout'
                    }
                }
            }
        }

        // stage('Deploy') {
        //     steps {
        //         // Docker-compose kullanarak uygulamanın dağıtılması
        //         sh 'docker-compose down'
        //         sh 'docker-compose up -d'
        //     }
        // }
    }

    post {
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
