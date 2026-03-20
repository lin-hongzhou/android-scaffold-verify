/**

 * 作者：lhz on 2026/1/23 14:01
 * 邮箱：1154536180@qq.com

 */

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import internal.Versions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {

        plugins.withId("com.android.application") {
            extensions.configure<ApplicationExtension> {
                enableCompose(this)
            }
        }

        plugins.withId("com.android.library") {
            extensions.configure<LibraryExtension> {
                enableCompose(this)
            }
        }
    }
}

private fun enableCompose(extension: Any) {
    when (extension) {
        is ApplicationExtension -> {
            extension.buildFeatures { compose = true }
            extension.composeOptions {
                kotlinCompilerExtensionVersion =
                    Versions.KOTLIN_COMPILER
            }
        }

        is LibraryExtension -> {
            extension.buildFeatures { compose = true }
            extension.composeOptions {
                kotlinCompilerExtensionVersion =
                    Versions.KOTLIN_COMPILER
            }
        }
    }
}


