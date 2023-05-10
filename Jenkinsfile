pipeline {
     agent { label 'jenkins_agent' }
     tools {
        maven 'maven'

    }

    environment{
        CI = true
        DOCKER_IMAGE_NAME = 'teamc/spring-backend'
        DOCKER_IMAGE_TAG = 'latest'
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
    }

    stages {
        

        stage('Build and Test Backend') {
       
            steps {
                sh '''
                    cd ./backend 
                    mvn clean package
                    '''
            }
        }

        stage('SonarQube analysis') {
            when {
                branch 'main'
            }
            steps {
                withSonarQubeEnv('sonarserver') {
                    sh '''
                    cd ./backend

                    mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install sonar:sonar
                    '''
                }
            }
        }


        stage('Build Cli') {
            
            steps {
                sh '''
                    cd ./cli
                    mvn clean package
                    '''
            }
        }

        
        
        stage('Deploy Backend') {
            
            when {
                changeset "backend/**"
                branch 'main'
            }
            steps{
                    sh '''
                        cd ./backend
                        mvn deploy 
                    '''

                   
            }
            
        }


        stage('Deploy Cli'){
            
            when {
                changeset "cli/**"
                branch 'main'
            }
            steps{
                 sh '''
                    cd ./cli
                    mvn deploy
                    '''
            }
        }

        stage('Jars Download from Jfrog Artifactory'){
            when{
                branch 'main'
            }
            agent { label 'vm'}
            environment {
                GREP_VALUE = '<value>'
                SED_VERSION = 's/.*<value>\\([^<]*\\)<\\/value>.*/\\1/'
                SED_FOLDER = 's#.*<latest>\\([^<]*\\)</latest>.*#\\1#'
            }
            steps{
                dir("backend") {
                    script {
                        env.LAST_BACKEND_FOLDER = sh(script: 'curl -s http://134.59.213.148:8012/artifactory/libs-snapshot-local/fr/univ-cotedazur/MultiFidelityCard-Backend/maven-metadata.xml -u admin:isa_DevOps_12 | grep latest | sed ${SED_FOLDER}', returnStdout: true).trim()
                        env.LAST_BACKEND_VERSION = sh(script: 'curl -s http://134.59.213.148:8012/artifactory/libs-snapshot-local/fr/univ-cotedazur/MultiFidelityCard-Backend/${LAST_BACKEND_FOLDER}/maven-metadata.xml -u admin:isa_DevOps_12 | grep ${GREP_VALUE} | head -1 | sed ${SED_VERSION}', returnStdout: true).trim()
                        echo "Last folder : ${env.LAST_BACKEND_FOLDER}\nLast version found : ${env.LAST_BACKEND_VERSION}"
                        sh "curl http://134.59.213.148:8012/artifactory/libs-snapshot-local/fr/univ-cotedazur/MultiFidelityCard-Backend/${env.LAST_BACKEND_FOLDER}/MultiFidelityCard-Backend-${env.LAST_BACKEND_VERSION}.jar -u admin:isa_DevOps_12 --output ./backend-jar.jar"
                    }
                }


                dir("cli") {
                    script {
                        env.LAST_BACKEND_FOLDER = sh(script: 'curl -s http://134.59.213.148:8012/artifactory/libs-snapshot-local/fr/univ-cotedazur/MultiFidelityCard-Cli/maven-metadata.xml -u admin:isa_DevOps_12 | grep latest | sed ${SED_FOLDER}', returnStdout: true).trim()
                        env.LAST_BACKEND_VERSION = sh(script: 'curl -s http://134.59.213.148:8012/artifactory/libs-snapshot-local/fr/univ-cotedazur/MultiFidelityCard-Cli/${LAST_BACKEND_FOLDER}/maven-metadata.xml -u admin:isa_DevOps_12 | grep ${GREP_VALUE} | head -1 | sed ${SED_VERSION}', returnStdout: true).trim()
                        echo "Last folder : ${env.LAST_BACKEND_FOLDER}\nLast version found : ${env.LAST_BACKEND_VERSION}"
                        sh "curl http://134.59.213.148:8012/artifactory/libs-snapshot-local/fr/univ-cotedazur/MultiFidelityCard-Cli/${env.LAST_BACKEND_FOLDER}/MultiFidelityCard-Cli-${env.LAST_BACKEND_VERSION}.jar -u admin:isa_DevOps_12 --output ./Cli-jar.jar"
                    }
                }
            }
        }


       




        stage('docker build  and push Backend'){
            agent {
                label 'vm'
            }
            environment {
                SED_VALUE = "s#.*<latest>\\([^<]*\\)-SNAPSHOT</latest>.*#\\1#"
            }
            
            when {
                changeset "backend/**"
                branch 'main'
            }

            
            steps{
                   
                   sh '''
                        cd ./backend 
                        echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin
                        docker build --build-arg JAR_FILE=backend-jar.jar  -t spring-backend:backend .
                        docker tag spring-backend:latest hamza125/multi-fidelity:backend.v1.$BUILD_ID
                        docker push  hamza125/multi-fidelity:backend.v1.$BUILD_ID
                    ''' 
            
                   
            }
        }

        stage('docker build and push Cli'){
            agent {
                label 'vm'
            }
            when {
                changeset "cli/**"
                branch 'main'
            }
            steps{
                    sh '''
                        cd ./cli 
                        docker build --build-arg JAR_FILE=Cli-jar.jar -t spring-cli:latest .
                        echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin
                        docker tag spring-cli:latest hamza125/multi-fidelity:cli.v1.$BUILD_ID
                        docker push  hamza125/multi-fidelity:cli.v1.$BUILD_ID
                    ''' 
            }
            
        }


        stage('docker build and push Bank'){
            agent {
                label 'vm'
            }
            when {
                changeset "bank/**"
                branch 'main'
            }
            steps{
                    sh '''
                        cd ./bank 

                        docker build  -t external-bank-service:latest .
                        echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin
                        docker tag external-bank-service:latest   hamza125/multi-fidelity:external-bank-service.v1.$BUILD_ID
                        docker push  hamza125/multi-fidelity:external-bank-service.v1.$BUILD_ID
                    ''' 
            }
            
        }
        

        stage('docker build and push Notifier'){
            agent {
                label 'vm'
            }
            when {
               changeset "notifier/**"
               branch 'main'
            }
            steps{
                    sh '''
                        cd ./notifier

                        docker build  -t external-notifier-service:latest .
                        echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin
                        docker tag external-notifier-service:latest   hamza125/multi-fidelity:external-notifier-service.v1.$BUILD_ID
                        docker push  hamza125/multi-fidelity:external-notifier-service.v1.$BUILD_ID
                    ''' 
            }
            
        }

        stage('docker build and push Parking'){
            agent {
                label 'vm'
            }
            when {
                changeset "parking/**"
                branch 'main'
            }
            steps{
                    sh '''
                        cd ./parking 

                        docker build  -t external-parking-service:latest .
                        echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin
                        docker tag external-parking-service:latest   hamza125/multi-fidelity:external-parking-service.v1.$BUILD_ID
                        docker push  hamza125/multi-fidelity:external-parking-service.v1.$BUILD_ID
                    ''' 
            }
            
        }

    }
}
