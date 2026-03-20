import com.android.build.api.dsl.LibraryExtension
import internal.AndroidConfig
import internal.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {

        apply(plugin = "com.android.library")
        apply(plugin = "org.jetbrains.kotlin.android")

        extensions.configure<LibraryExtension> {
            AndroidConfig.configureLibrary(this)
        }

        extensions.configure<KotlinAndroidProjectExtension> {
            configureKotlin(this)
        }
    }
}


