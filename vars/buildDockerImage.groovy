def call(Map config) {
    def imageName = config.imageName ?: error("imageName is required")
    def imageTag = config.imageTag ?: 'latest'
    def dockerfilePath = config.dockerfilePath ?: './Dockerfile'
    def buildArgs = config.buildArgs ?: ''
    def buildContext = config.buildContext ?: '.'
    
    def fullImageName = "${imageName}:${imageTag}"
    echo "Building ${fullImageName}"
    
    def cmd = "docker build -t ${fullImageName} -f ${dockerfilePath}"
    if (buildArgs) cmd += " ${buildArgs}"
    cmd += " ${buildContext}"
    
    sh cmd
    echo "✓ Built ${fullImageName}"
}