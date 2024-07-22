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
        stage('Test') {
            steps {
                // Maven ile projenin derlenmesi ve testlerin çalıştırılması
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    jacoco execPattern: 'target/jacoco.exec'
                }
            }
        }

        stage('Build') {
            steps {
                // Maven ile projenin derlenmesi ve testlerin çalıştırılması
                sh 'mvn clean package -DskipTests'
                archiveArtifacts 'target/*.jar'
            }
        }

        stage('SCA - Snyk Scan') {
            steps {
               script {
                   echo 'Testing...'
                    snykSecurity(
                        snykInstallation: 'Snyk',
                        snykTokenId: 'snyk-api-token',
                        // place other parameters here
                    )
               }
           }
        }

        stage('SAST - SonarQube') {
            environment {
                scannerHome = tool 'SonarQube'
            }
            steps {
                withSonarQubeEnv('SonarQubeSecret') {
                    sh "${scannerHome}/bin/sonar-scanner \
                        -Dsonar.dependencyCheck.summarize=true \
                        -Dsonar.dependencyCheck.xmlReportPath=target/surefire-reports/*.xml \
                        -Dsonar.projectKey=devops_project \
                        -Dsonar.projectName='devops_project' \
                        -Dsonar.host.url=http://localhost:9000"
                    echo 'SonarQube Analysis Completed'
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
