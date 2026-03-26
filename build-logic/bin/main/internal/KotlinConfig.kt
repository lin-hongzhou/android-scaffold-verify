package internal

/**

 * 作者：lhz on 2026/1/23 13:58
 * 邮箱：1154536180@qq.com

 */

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureKotlin(
    extension: KotlinAndroidProjectExtension
) {
    extension.jvmToolchain(Versions.JAVA_VERSION)
}
