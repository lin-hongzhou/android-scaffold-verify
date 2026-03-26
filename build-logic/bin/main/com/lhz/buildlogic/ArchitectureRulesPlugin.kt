package com.lhz.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

class ArchitectureRulesPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        if (target != target.rootProject) return

        // Register a lifecycle task to check architecture rules at execution time
        // This avoids concurrent modification issues with parallel configuration
        target.tasks.register("checkArchitectureRules") {
            group = "verification"
            description = "Checks architecture dependency rules"

            doLast {
                target.subprojects.forEach { project ->
                    checkDependencies(project)
                }
            }
        }

        // Also hook into the build lifecycle safely
        target.gradle.projectsEvaluated {
            target.subprojects.forEach { project ->
                checkDependencies(project)
            }
        }
    }

    private fun checkDependencies(project: Project) {

        val fromPath = project.path

        project.configurations
            .filter { conf ->
                try {
                    conf.isCanBeResolved
                } catch (_: Exception) {
                    false
                }
            }
            .forEach { configuration ->

                configuration.dependencies
                    .withType(ProjectDependency::class.java)
                    .forEach { dependency ->

                        @Suppress("DEPRECATION")
                        val toPath = dependency.dependencyProject.path
                        validate(fromPath, toPath)
                    }
            }
    }

    private fun validate(from: String, to: String) {

        // skip self-dependency (normal in Gradle configurations)
        if (from == to) return

        // feature -> feature prohibited
        if (from.startsWith(":feature") && to.startsWith(":feature")) {
            error("Architecture violation: $from must NOT depend on $to")
        }

        // core must NOT depend on feature
        if (from.startsWith(":core") && to.startsWith(":feature")) {
            error("Architecture violation: $from must NOT depend on $to")
        }

        // core must NOT depend on infra
        if (from.startsWith(":core") && to.startsWith(":infra")) {
            error("Architecture violation: $from must NOT depend on $to")
        }

        // feature must NOT depend on infra directly
        if (from.startsWith(":feature") && to.startsWith(":infra")) {
            error("Architecture violation: $from must NOT depend on $to")
        }
    }
}
