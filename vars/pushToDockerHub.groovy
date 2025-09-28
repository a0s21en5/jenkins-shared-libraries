def call(Map config) {
    def imageName = config.imageName ?: error("imageName is required")
    def imageTag = config.imageTag ?: 'latest'
    def repository = config.repository ?: error("repository is required")
    def dockerCredentialsId = config.dockerCredentialsId ?: error("dockerCredentialsId is required")
    def targetTag = config.targetTag ?: imageTag
    
    def localImage = "${imageName}:${imageTag}"
    def remoteImage = "${repository}:${targetTag}"
    
    echo "Pushing ${localImage} → ${remoteImage}"
    
    withCredentials([usernamePassword(
        credentialsId: dockerCredentialsId,
        usernameVariable: 'DOCKER_USER',
        passwordVariable: 'DOCKER_PASS'
    )]) {
        sh '''
            echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
            docker tag ''' + localImage + ''' ''' + remoteImage + '''
            docker push ''' + remoteImage + '''
            docker logout
        '''
    }
    
    echo "✓ Pushed ${remoteImage}"
}