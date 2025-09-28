def call(Map config) {
    def repoUrl = config.repoUrl ?: error("repoUrl is required")
    def branch = config.branch ?: 'main'
    def credentialsId = config.credentialsId
    
    echo "Checking out ${repoUrl} (${branch})"
    
    def checkoutConfig = [
        $class: 'GitSCM',
        branches: [[name: "*/${branch}"]],
        userRemoteConfigs: [[url: repoUrl]]
    ]
    
    if (credentialsId) {
        checkoutConfig.userRemoteConfigs[0].credentialsId = credentialsId
    }
    
    checkout(checkoutConfig)
    echo "✓ Checkout completed"
}