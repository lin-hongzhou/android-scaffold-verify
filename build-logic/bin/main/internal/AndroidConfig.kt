package internal

/**
 * 作者：lhz on 2026/1/23 13:58
 * 邮箱：1154536180@qq.com
 */

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion

internal object AndroidConfig {

    fun configureApplication(extension: ApplicationExtension) {
        extension.apply {

            compileSdk = Versions.COMPILE_SDK

            defaultConfig {
                minSdk = Versions.MIN_SDK
                targetSdk = Versions.TARGET_SDK
                testInstrumentationRunner =
                    "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            compileOptions {
                sourceCompatibility =
                    JavaVersion.toVersion(Versions.JAVA_VERSION)
                targetCompatibility =
                    JavaVersion.toVersion(Versions.JAVA_VERSION)
            }
        }
    }

    fun configureLibrary(extension: LibraryExtension) {
        extension.apply {

            compileSdk = Versions.COMPILE_SDK

            defaultConfig {
                minSdk = Versions.MIN_SDK
                testInstrumentationRunner =
                    "androidx.test.runner.AndroidJUnitRunner"
                consumerProguardFiles("consumer-rules.pro")
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            compileOptions {
                sourceCompatibility =
                    JavaVersion.toVersion(Versions.JAVA_VERSION)
                targetCompatibility =
                    JavaVersion.toVersion(Versions.JAVA_VERSION)
            }
        }
    }
}
