
import com.android.build.api.dsl.ApplicationExtension
import internal.AndroidConfig
import internal.configureKotlin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {

        apply(plugin = "com.android.application")
        apply(plugin = "org.jetbrains.kotlin.android")

        extensions.configure<ApplicationExtension> {
            AndroidConfig.configureApplication(this)
        }

        extensions.configure<KotlinAndroidProjectExtension> {
            configureKotlin(this)
        }
    }
}



