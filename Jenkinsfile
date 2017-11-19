node ('nimble-jenkins-slave') {
    stage('Download Latest') {
        git(url: 'https://github.com/nimble-platform/registration-service.git', branch: 'master')
        sh 'git submodule init'
        sh 'git submodule update'
    }

    stage ('Build docker image') {
        sh 'mvn clean install'
        sh 'docker build -t nimbleplatform/registration-service:${BUILD_NUMBER} .'
    }

    stage ('Push docker image') {
        withDockerRegistry([credentialsId: 'NimbleDocker']) {
            sh 'docker push nimbleplatform/registration-service:${BUILD_NUMBER}'
        }
    }

    stage ('Deploy') {
        sh ''' sed -i 's/IMAGE_TAG/'"$BUILD_NUMBER"'/g' kubernetes/deploy.yaml '''
        sh 'kubectl apply -f kubernetes/deploy.yaml -n prod --validate=false'
        sh 'kubectl apply -f kubernetes/svc.yaml -n prod --validate=false'
    }

    stage ('Print-deploy logs') {
        sh 'sleep 60'
        sh 'kubectl  -n prod logs deploy/registration-service -c registration-service'
    }
}