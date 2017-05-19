@Library('dotnet-ci') _

// Incoming parameters
// Config - Build configuration. Note that we don't using 'Configuration' since it's used
//          in the build scripts and this can cause problems.
// Outerloop - If true, runs outerloop, if false runs just innerloop

def submittedHelixJson = null

simpleNode('Windows_NT','latest') {
    stage ('Checkout source') {
        checkout scm
    }

    def logFolder = getLogFolder()

    stage ('Clean') {
        bat '.\\clean.cmd'
    }
    stage ('Sync') {
        bat '.\\sync.cmd -p'
    }
    stage ('Build Product') {
        // Why is release always being passed?
        bat ".\\build.cmd x64 ${Config} skiptests skipbuildpackages -skiprestore -disableoss -portable"
    }
    // Now we depend on the tests build pipeline
    stage ('Build Tests') {

    }
}