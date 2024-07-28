pipeline {
    agent any

    environment {
        // Ortak ortam değişkenleri burada tanımlanabilir
        DOCKER_IMAGE = "spring-boot-app:${env.BUILD_NUMBER}"
        DOCKER_REPOSITORY_NAME = "spring-boot-app"
        DOCKER_HUB= "wellpast"
    }

    stages {
        stage('JTest') {
            parallel {
                stage('Run Tests') {
                    steps {
                        // Maven ile projenin testlerini çalıştırma
                        sh 'mvn test'
                    }
                    post {
                        always {
                            junit 'target/surefire-reports/*.xml'
                        }
                    }
                }
                stage('Secret Key Detection Analysis') {
                steps {
                    sh '''
                    virtualenv venv && . venv/bin/activate && pip install detect-secrets &&
                    detect-secrets scan . > detect-secrets.json
                    '''
                    archiveArtifacts 'detect-secrets.json'
                }
            }
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
                archiveArtifacts 'target/*.jar'
            }
        }

        stage('Security Scans') {
            parallel {
                stage('SCA - Snyk Scan') {
                    steps {
                        script {
                            echo 'Testing...'
                            snykSecurity(
                                snykInstallation: 'Snyk',
                                snykTokenId: 'snyk-api-token'
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
                        withSonarQubeEnv('sonarqube-server') {
                            sh """
                                mvn clean verify sonar:sonar \
                                    -Dsonar.dependencyCheck.summarize=true \
                                    -Dsonar.dependencyCheck.xmlReportPath=target/surefire-reports/*.xml \
                                    -Dsonar.projectKey=devops_project \
                                    -Dsonar.projectName='devops_project' \
                                    -Dsonar.host.url=http://localhost:9000
                            """
                            echo 'SonarQube Analysis Completed'
                        }
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKER_IMAGE}", "-f backend-service.dockerfile .")
                }
            }
        }

        stage('Container Secuirty Analysis - Trivy Scan') {
            steps {
                script {
                    sh "curl -sOL https://github.com/aquasecurity/trivy/releases/download/v0.53.0/trivy_0.53.0_Linux-64bit.tar.gz" 
                    sh "tar -xvf trivy_0.53.0_Linux-64bit.tar.gz"
                    sh "trivy image --exit-code 0 --config config/.trivy.yaml ${DOCKER_IMAGE}"
                    archiveArtifacts 'trivy_result.txt'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
                        sh "echo ${dockerHubPassword} | docker login -u ${dockerHubUser} --password-stdin"
                        sh "docker tag ${DOCKER_IMAGE} ${dockerHubUser}/${DOCKER_REPOSITORY_NAME}:${BUILD_NUMBER}"
                        sh "docker push ${dockerHubUser}/${DOCKER_REPOSITORY_NAME}:${BUILD_NUMBER}"
                        sh 'docker logout'
                    }
                }
            }
        }

        stage('Deploy Dev Stage on docker') {
            steps {
                sh "docker-compose down"
                sh "MY_IMAGE=${DOCKER_IMAGE} docker-compose up -d"
            }
        }
        stage('Approve Deployment') {
            steps {
                script {
                    input message: 'Do you approve the deployment to the production environment? This will deploy the latest version of the application to the Kubernetes cluster.', ok: 'Deploy'
                }
            }
        }
        stage('Deploy Prod Stage on k8s') {
            steps {
                withCredentials([
                    string(credentialsId: 'tokenk8s', variable: 'api_token')
                ]) {
                    script {
                        try {
                            sh "helm install mywebapp-release ./k8s --set images.spring_boot_app=${DOCKER_HUB}/${DOCKER_IMAGE}"
                        } catch (Exception e) {
                            echo "Installation failed, performing upgrade instead"
                            sh "helm upgrade mywebapp-release ./k8s --set images.spring_boot_app=${DOCKER_HUB}/${DOCKER_IMAGE}"
                        }
                    }
                }
            }
        }
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
