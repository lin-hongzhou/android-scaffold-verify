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
    // 关键：引入 Android Gradle Plugin API
    implementation("com.android.tools.build:gradle:8.3.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
}

gradlePlugin {
    plugins {
        register("architectureRules") {
            id = "company.architecture.rules"
            implementationClass = "com.company.buildlogic.ArchitectureRulesPlugin"
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
    }
}