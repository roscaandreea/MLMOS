node {
    def test_app

    stage('Clone repository') {
        //https://stackoverflow.com/questions/38461705/checkout-jenkins-pipeline-git-scm-with-credentials
        git 'http://172.17.0.1/becali/cloud-presentation.git'
        sh 'pwd'
        sh 'ls -l'
    }

    stage('Build test image') {
        test_app = docker.build("dummy_server", "./dummy_server") //nodesource/node:4.4
    }

    stage('Test image') {
        //sh 'docker cp . node:/root'
        sh 'docker ps'
        test_app.inside {
            sh 'pwd'
            sh 'ls -l'
            sh 'ls -l dummy_server/'
            //sh 'echo "Tests passed"'
            sh 'npm install -g vows'
            sh 'cd dummy_server && npm install && npm install vows'
            sh 'cd dummy_server && npm test'
        }
    }

    stage('Push image') {
        /* Finally, we'll push the image with two tags:
         * First, the incremental build number from Jenkins
         * Second, the 'latest' tag.
         * Pushing multiple tags is cheap, as all the layers are reused. */
        //second param to withRegistry is credentials, stored in Jenkins credentials
        docker.withRegistry('https://localhost:5000') {
            sh 'echo ${BUILD_NUMBER}'
            test_app.push("${env.BUILD_NUMBER}")
            test_app.push("latest")
        }

        sh 'echo "Image pushed"'
    }
}