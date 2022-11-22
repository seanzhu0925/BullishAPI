pipeline {
    agent any

    environment {
        dockerimageName='seanzhu0925/bulishapi-docker'
        dockerimage = ""
    }

    stages {

        stage('Initialize'){
            def dockerHome = tool 'myDocker'
            env.PATH = "${dockerHome}/bin:${env.PATH}"
        }

        stage('Checkout Source Code') {
            steps {
                 git 'https://github.com/seanzhu0925/BullishAPI.git'
            }
        }

        stage('Build Images') {
            steps {
                script {
                    dockerImage = docker.build dockerImageName
                }
            }
        }

        stage('Push to Dockerhub') {
            environment {
                registryCredential = 'dockerhublogin'
            }
            when {
                branch 'master'
            }
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', registryCredential){
                        dockerImage.push("latest")
                    }
                }
            }
        }

        stage('Deploying App to Kubernetes') {
            steps {
                kubernetesDeploy(configs: "bulishapi-service.yml", kubeconfigId: "kubernetes")
            }
        }
    }
}