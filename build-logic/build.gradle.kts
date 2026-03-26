plugins {
    `kotlin-dsl`
}
group = "com.company"
repositories {
    google()
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    // 通过 version catalog 引用，与项目版本保持一致
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("architectureRules") {
            id = "company.architecture.rules"
            implementationClass = "com.lhz.buildlogic.ArchitectureRulesPlugin"
        }

        register("androidApplication") {
            id = "scaffold.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "scaffold.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("compose") {
            id = "scaffold.android.compose"
            implementationClass = "ComposeConventionPlugin"
        }

        register("androidFeature") {
            id = "scaffold.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
    }
}