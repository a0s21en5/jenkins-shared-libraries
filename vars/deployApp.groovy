def call(Map config) {
    def composeFile = config.composeFile ?: 'docker-compose.yml'
    def serviceName = config.serviceName
    def projectName = config.projectName
    def environment = config.environment ?: [:]
    def pullImages = config.pullImages != false // default true
    
    echo "Deploying with ${composeFile}"
    
    def envVars = environment.collect { k, v -> "${k}=${v}" }
    def cmd = "docker-compose -f ${composeFile}"
    if (projectName) cmd += " -p ${projectName}"
    
    withEnv(envVars) {
        sh "${cmd} down --remove-orphans || true"
        if (pullImages) sh "${cmd} pull || true"
        
        def upCmd = "${cmd} up -d"
        if (serviceName) upCmd += " ${serviceName}"
        
        sh upCmd
        sh "${cmd} ps"
    }
    
    echo "✓ Deployment completed"
}