def JOB_BRANCHES = ["2.6"] // , "2.8"]
for (String JOB_BRANCH : JOB_BRANCHES) {
    pipelineJob("${FOLDER_PATH}/${ITEM_NAME}"){
        MIDSTM_BRANCH="crw-"+JOB_BRANCH+"-rhel-8"

        description('''
Collect sources from pkgs.devel and vsix files and push to rcm-guest so they can be published as part of a GA release. 
        ''')

        properties {
            ownership {
                primaryOwnerId("nboldt")
            }
        }

        logRotator {
            daysToKeep(5)
            numToKeep(5)
            artifactDaysToKeep(5)
            artifactNumToKeep(2)
        }

        parameters{
            stringParam("MIDSTM_BRANCH",MIDSTM_BRANCH,"redhat-developer/codeready-workspaces branch to use")
            booleanParam("PUBLISH_ARTIFACTS_TO_RCM", false, "default false; check box to upload sources + binaries to RCM for a GA release ONLY")
            booleanParam("ARCHIVE_ARTIFACTS_IN_JENKINS", false, "default false; check box to archive artifacts for testing purposes")
        }

        // Trigger builds remotely (e.g., from scripts), using Authentication Token = CI_BUILD
        authenticationToken('CI_BUILD')

        definition {
            cps{
                sandbox(true)
                script(readFileFromWorkspace('jobs/CRW_CI/Releng/' + ITEM_NAME + '.jenkinsfile'))
            }
        }
    }
}
