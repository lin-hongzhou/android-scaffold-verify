package com.lhz.buildlogic

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

        // core 不允许依赖 infra（core 是抽象层，不应依赖实现层）
        if (from.startsWith(":core") && to.startsWith(":infra")) {
            error("Architecture violation: $from must NOT depend on $to")
        }

        // feature 不允许直接依赖 infra（应通过 core 抽象层）
        if (from.startsWith(":feature") && to.startsWith(":infra")) {
            error("Architecture violation: $from must NOT depend on $to")
        }
    }
}
