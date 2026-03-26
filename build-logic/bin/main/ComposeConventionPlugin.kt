/**
 * 作者：lhz on 2026/1/23 14:01
 * 邮箱：1154536180@qq.com
 */

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class ComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {

        // Kotlin 2.0+ 使用 Compiler Plugin 方式，不再需要手动指定 kotlinCompilerExtensionVersion
        apply(plugin = "org.jetbrains.kotlin.plugin.compose")

        plugins.withId("com.android.application") {
            extensions.configure<ApplicationExtension> {
                buildFeatures { compose = true }
            }
        }

        plugins.withId("com.android.library") {
            extensions.configure<LibraryExtension> {
                buildFeatures { compose = true }
            }
        }
    }
}
