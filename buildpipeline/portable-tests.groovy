@Library('dotnet-ci') _

// Incoming parameters
// Config - Build configuration. Note that we don't using 'Configuration' since it's used
//          in the build scripts and this can cause problems.
// Outerloop - If true, runs outerloop, if false runs just innerloop

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