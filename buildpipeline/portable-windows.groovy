@Library('dotnet-ci') _

// Incoming parameters
// Config - Build configuration. Note that we don't using 'Configuration' since it's used
//          in the build scripts and this can cause problems.
// Architecture - architecure to target in the build
// Outerloop - If true, runs outerloop, if false runs just innerloop

def submittedHelixJson = null

// Now we depend on the tests build pipeline
stage ('Build Tests') {
    // Loads the other script so it can be invoked 
    def buildTests = 'buildpipeline/portable-tests.groovy'
    buildTests(Config, Architecture)
}

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
        bat ".\\build.cmd ${Architecture} ${Config} skiptests skipbuildpackages -skiprestore -disableoss -portable"
    }
    // Build the packages
    stage ('Build Packages') {
        bat ".\\build-packages.cmd -BuildArch=${Architecture} -BuildType=${Config} -portable"
    }
}