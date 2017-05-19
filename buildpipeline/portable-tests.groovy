@Library('dotnet-ci') _

// Builds and submits tests given the input parameters
def call(String config) {
    simpleNode('Windows_NT','latest') {
        stage ('Checkout source') {
            checkout scm
        }

        def logFolder = getLogFolder()

        stage ('Clean') {
            bat '.\\clean.cmd'
        }
        stage ('Sync') {
            // Sync the tests from the containers that should have been created in the upstream pipeline
            bat '.\\sync.cmd -p'
        }
        // Now we depend on the tests build pipeline
        stage ('Build Tests') {
            
        }
    }
}

return this