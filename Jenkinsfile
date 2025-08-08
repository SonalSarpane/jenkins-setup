pipeline {
    agent any

    tools {
        // Define Maven tool, assuming "Maven-3.8.6" is configured in Jenkins
        maven 'Maven-3.8.6'
        jdk 'JDK-17'  // Specify the JDK version configured in Jenkins
    }

    triggers {
        // Configure GitHub webhook trigger
        githubPush()
    }

    options {
        // Pipeline-specific options
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 1, unit: 'HOURS')
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout') {
            steps {
                // Clean workspace before checkout
                cleanWs()
                checkout scm
            }
        }

        stage('Build') {
            steps {
                // Run Maven clean and compile
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                // Run tests and generate reports
                sh 'mvn test'
            }
            post {
                always {
                    // Publish TestNG reports
                    testNG(reportFilePath: '**/target/surefire-reports/testng-results.xml')
                }
            }
        }
    }

    post {
        always {
            // Clean workspace after build
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed! Please check the logs for details.'
        }
    }
}
