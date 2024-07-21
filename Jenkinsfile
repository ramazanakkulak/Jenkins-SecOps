pipeline {
    agent any

    environment {
        // Ortak ortam değişkenleri burada tanımlanabilir
        DOCKER_IMAGE = "spring-boot-app:${env.BUILD_NUMBER}"
        DOCKER_REPOSITORY_NAME = "spring-boot-app"
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

        stage('Dependency Check - SYNK SCAN') {
            steps {
               script {
                   withCredentials([string(credentialsId: 'snyk-api-token', variable: 'SNYK_TOKEN')]) {
                        // Authenticate with Snyk
                        echo ${SNYK_TOKEN}
                        sh "snyk auth ${SNYK_TOKEN}"
                        // Run Snyk test
                        sh "snyk test"
                    }
               }
           }
        }

        // stage('Build Docker Image') {
        //     steps {
        //         script {
        //             docker.build("${DOCKER_IMAGE}", "-f backend-service.dockerfile .")
        //         }
        //     }
        // }

        // stage('Push Docker Image') {
        //     steps {
        //         script {
        //             withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
        //                 sh "echo ${dockerHubPassword} | docker login -u ${dockerHubUser} --password-stdin"
        //                 sh "docker tag ${DOCKER_IMAGE} ${dockerHubUser}/${DOCKER_REPOSITORY_NAME}:${BUILD_NUMBER}"
        //                 sh "docker push ${dockerHubUser}/${DOCKER_REPOSITORY_NAME}:${BUILD_NUMBER}"
        //                 sh 'docker logout'
        //             }
        //         }
        //     }
        // }
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
