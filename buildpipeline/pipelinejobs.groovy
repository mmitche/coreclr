// Import the utility functionality.

import jobs.generation.JobReport;
import org.dotnet.ci.pipelines.Pipeline

// The input project name (e.g. dotnet/corefx)
def project = GithubProject
// The input branch name (e.g. master)
def branch = GithubBranchName

// **************************
// Define innerloop testing.  These jobs run on every merge and a subset of them run on every PR, the ones
// that don't run per PR can be requested via a magic phrase.
// **************************

// Create a pipeline for portable windows
def windowsPipeline = Pipeline.createPipelineForGithub(this, project, branch, 'buildpipeline/portable-windows.groovy')
['netcoreapp'].each { targetGroup ->
	[/*'Debug', 'Checked', */'Release'].each { configurationGroup ->
		['x64'].each { architecture ->
            def parameters = ['Config':configurationGroup, 'Architecture':architecture]
            windowsPipeline.triggerPipelineOnEveryGithubPR("Win ${architecture} ${configurationGroup} Build", "(?i).*test\\W+portable\\W+windows\\W+${configurationGroup}\\W+pipeline.*", parameters)
		}
	}
}

JobReport.Report.generateJobReport(out)