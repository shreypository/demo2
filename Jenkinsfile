pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Pull code from GitHub
                git branch: 'main',
                    url: 'https://github.com/your-username/your-repo.git',
                    credentialsId: 'your-github-credentials-id'
            }
        }

        stage('Build') {
            steps {
                // Run build (Maven example)
                sh 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                // Run tests explicitly
                sh 'mvn test'
            }
            post {
                always {
                    // Publish JUnit test results
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }

    post {
        success {
            echo '✅ Build and tests completed successfully!'
        }
        failure {
            echo '❌ Build or tests failed. Check console output for details.'
        }
    }
}
