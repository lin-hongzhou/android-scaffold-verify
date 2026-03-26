/**

 * 作者：lhz on 2026/1/23 14:00
 * 邮箱：1154536180@qq.com

 */

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidFeatureConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {

        pluginManager.apply("scaffold.android.library")

        dependencies.add(
            "implementation",
            project(":core:ui")
        )
    }
}
