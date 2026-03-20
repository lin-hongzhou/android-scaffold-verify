package com.company.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

class ArchitectureRulesPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        if (target != target.rootProject) return

        target.subprojects.forEach { project ->
            project.afterEvaluate {
                checkDependencies(project)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun checkDependencies(project: Project) {

        val fromPath = project.path

        project.configurations
            .filter { it.isCanBeResolved }
            .forEach { configuration ->

                configuration.dependencies
                    .withType(ProjectDependency::class.java)
                    .forEach { dependency ->

                        val toPath = dependency.dependencyProject.path
                        validate(fromPath, toPath)
                    }
            }
    }

    private fun validate(from: String, to: String) {

        // feature -> feature 禁止
        if (from.startsWith(":feature") && to.startsWith(":feature")) {
            error("Architecture violation: $from must NOT depend on $to")
        }

        // core 不允许依赖 feature
        if (from.startsWith(":core") && to.startsWith(":feature")) {
            error("Architecture violation: $from must NOT depend on $to")
        }

        // domain 不允许依赖 data
        if (from.startsWith(":domain") && to.startsWith(":data")) {
            error("Architecture violation: $from must NOT depend on $to")
        }
    }
}
