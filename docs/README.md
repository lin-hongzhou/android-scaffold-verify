# Android 多模块脚手架

> 基于 Gradle Convention Plugin 的 Android 多模块架构脚手架，提供统一的构建配置、架构规则校验和模块化开发模板。

**最后更新：2026-03-26**

---

## 目录

- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [架构设计](#架构设计)
- [Convention Plugin](#convention-plugin)
- [快速开始](#快速开始)
- [添加新模块](#添加新模块)
- [依赖管理](#依赖管理)
- [全局配置](#全局配置)
- [架构规则](#架构规则)
- [Navigation 路由方案](#navigation-路由方案)
- [常见问题](#常见问题)

---

## 技术栈

| 技术 | 版本 |
|------|------|
| Gradle | 8.13 |
| Android Gradle Plugin (AGP) | 8.13.2 |
| Kotlin | 2.0.21 |
| Compose BOM | 2024.09.00 |
| compileSdk | 36 |
| minSdk | 24 |
| targetSdk | 36 |
| Java | 17 |

---

## 项目结构

```
android-scaffold-verify
│
├── app/                            # 壳工程（组装所有模块）
│
├── feature/                        # 业务功能层（每个功能一个 module）
│   ├── feature-user/               # 用户模块
│   ├── feature-map/                # 地图 / 导航
│   ├── feature-location/           # 定位
│   ├── feature-ble/                # 蓝牙
│   ├── feature-setting/            # 设置
│   └── roadtest/                   # 路测
│
├── core/                           # 核心抽象层（接口 & 模型，不含 SDK）
│   ├── model/                      # 通用数据模型
│   ├── ui/                         # 通用 UI 组件（Compose）
│   ├── common/                     # 工具类 / 扩展函数
│   ├── network/                    # 网络 API 抽象接口
│   ├── database/                   # 数据库抽象（Dao 接口）
│   ├── location/                   # 定位抽象
│   ├── ble/                        # 蓝牙抽象
│   └── map/                        # 地图抽象
│
├── infra/                          # 基础设施层（SDK 实现）
│   ├── network-impl/               # Retrofit / OkHttp 实现
│   ├── database-impl/              # Room / DataStore 实现
│   ├── location-impl/              # 定位 SDK 实现
│   ├── ble-impl/                   # Android BLE 实现
│   ├── map-baidu/                  # 百度地图实现
│   ├── permission/                 # 权限管理
│   └── log/                        # 日志
│
├── testing/                        # 测试专用 Fake 实现
│   ├── fake-network/
│   ├── fake-database/
│   └── fake-location/
│
├── benchmarks/                     # 性能测试
│
├── build-logic/                    # Gradle Convention Plugin
│   └── src/main/kotlin/
│       ├── AndroidApplicationConventionPlugin.kt
│       ├── AndroidLibraryConventionPlugin.kt
│       ├── AndroidFeatureConventionPlugin.kt
│       ├── ComposeConventionPlugin.kt
│       ├── internal/
│       │   ├── Versions.kt         # 全局版本配置
│       │   ├── AndroidConfig.kt    # Android 通用配置
│       │   └── KotlinConfig.kt     # Kotlin 通用配置
│       └── com/lhz/buildlogic/
│           └── ArchitectureRulesPlugin.kt  # 架构规则校验
│
├── gradle/
│   └── libs.versions.toml          # Version Catalog（统一依赖版本）
│
├── docs/                           # 架构文档
├── settings.gradle.kts
├── build.gradle.kts
└── gradle.properties
```

---

## 架构设计

### 分层架构

```
┌─────────────────────────────────────────────┐
│                    app                       │  ← 壳工程，组装所有模块
├─────────────────────────────────────────────┤
│  feature-user │ feature-map │ feature-ble   │  ← 业务功能层
├─────────────────────────────────────────────┤
│  core:model │ core:ui │ core:network │ ...  │  ← 核心抽象层
├─────────────────────────────────────────────┤
│  infra:network-impl │ infra:ble-impl │ ...  │  ← 基础设施层（SDK 实现）
└─────────────────────────────────────────────┘
```

### 依赖规则

| 层级 | 可以依赖 | 不可依赖 |
|------|---------|---------|
| **app** | feature, core, infra | — |
| **feature** | core | feature（互相禁止）, infra |
| **core** | core | feature, infra |
| **infra** | core | feature |
| **testing** | core | feature, infra |

> 以上规则由 `ArchitectureRulesPlugin` 在编译期自动校验，违规依赖将导致构建失败。

### 设计原则

1. **接口与实现分离**：`core` 层定义接口，`infra` 层提供实现
2. **Feature 互相隔离**：Feature 之间不能直接依赖，通过 `core` 层共享数据
3. **SDK 依赖下沉**：第三方 SDK 只在 `infra` 层引入，上层通过接口访问
4. **可测试性**：`testing` 层提供 Fake 实现，用于单元测试和 UI 测试

---

## Convention Plugin

脚手架通过 Convention Plugin 统一管理所有模块的构建配置，避免样板代码重复。

### 可用插件

| Plugin ID | 用途 | 自动配置 |
|-----------|------|---------|
| `scaffold.android.application` | app 模块 | AGP Application + Kotlin + compileSdk / minSdk / targetSdk / Java 17 / ProGuard |
| `scaffold.android.library` | Library 模块 | AGP Library + Kotlin + compileSdk / minSdk / Java 17 / consumerProguardFiles |
| `scaffold.android.feature` | Feature 模块 | = `scaffold.android.library` + 自动依赖 `:core:ui` |
| `scaffold.android.compose` | 需要 Compose 的模块 | Kotlin Compose Compiler Plugin + `buildFeatures.compose = true` |
| `company.architecture.rules` | 根项目 | 架构依赖规则校验 |

### 使用示例

```kotlin
// app 模块
plugins {
    id("scaffold.android.application")
    id("scaffold.android.compose")
}

// 普通 Library 模块（core / infra / testing）
plugins {
    id("scaffold.android.library")
}

// Feature 模块（自动依赖 core:ui）
plugins {
    id("scaffold.android.feature")
}

// 带 Compose 的 Feature 模块
plugins {
    id("scaffold.android.feature")
    id("scaffold.android.compose")
}
```

### 使用 Convention Plugin 后的模块构建文件

```kotlin
plugins {
    id("scaffold.android.feature")
    id("scaffold.android.compose")
}

android {
    namespace = "com.lhz.feature.xxx"
}

dependencies {
    // 只写本模块特有的依赖
    implementation(project(":core:model"))
}
```

> compileSdk、minSdk、Java 版本、ProGuard 等样板配置全部由 Convention Plugin 统一管理，无需在每个模块重复声明。

---

## 快速开始

### 环境要求

- Android Studio Ladybug 或更高版本
- JDK 17
- Gradle 8.13（项目自带 Gradle Wrapper）

### 克隆与运行

```bash
git clone <repository-url>
cd android-scaffold-verify
```

在 Android Studio 中打开项目，等待 Gradle Sync 完成后，点击 Run 即可。

### 包名说明

- 基础包名：`com.lhz`
- app：`com.lhz.app`
- core 层：`com.lhz.{module}`（如 `com.lhz.model`、`com.lhz.ui`）
- feature 层：`com.lhz.feature.{module}`（如 `com.lhz.feature.user`）
- infra 层：`com.lhz.{module}.impl`（如 `com.lhz.network.impl`）
- testing 层：`com.lhz.fake.{module}`（如 `com.lhz.fake.network`）

---

## 添加新模块

### 添加 Feature 模块

1. 创建目录 `feature/feature-xxx/`
2. 创建 `build.gradle.kts`：

```kotlin
plugins {
    id("scaffold.android.feature")
    // id("scaffold.android.compose")  // 需要 Compose 时取消注释
}

android {
    namespace = "com.lhz.feature.xxx"
}

dependencies {
    implementation(project(":core:model"))   // 按需添加
}
```

3. 在 `settings.gradle.kts` 中注册：`include(":feature:feature-xxx")`
4. 在 `app/build.gradle.kts` 中添加：`implementation(project(":feature:feature-xxx"))`

### 添加 Core 模块

```kotlin
plugins {
    id("scaffold.android.library")
}

android {
    namespace = "com.lhz.xxx"
}
```

> Core 模块应只包含接口（interface）、数据类（data class）、密封类（sealed class）等抽象定义。

### 添加 Infra 模块

```kotlin
plugins {
    id("scaffold.android.library")
}

android {
    namespace = "com.lhz.xxx.impl"
}

dependencies {
    implementation(project(":core:xxx"))          // 实现 core 层接口
    implementation("com.some.sdk:sdk:1.0.0")      // 引入具体 SDK
}
```

---

## 依赖管理

所有第三方依赖通过 `gradle/libs.versions.toml`（Version Catalog）统一管理。

### 添加新依赖的步骤

**步骤 1**：在 `gradle/libs.versions.toml` 中声明版本和库

```toml
[versions]
retrofit = "2.9.0"

[libraries]
retrofit-core = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
```

**步骤 2**：在模块的 `build.gradle.kts` 中引用

```kotlin
dependencies {
    implementation(libs.retrofit.core)
}
```

### 当前已配置的依赖

| 依赖 | Version Catalog Key | 版本 |
|------|-------------------|------|
| AndroidX Core KTX | `libs.androidx.core.ktx` | 1.17.0 |
| Lifecycle Runtime | `libs.androidx.lifecycle.runtime.ktx` | 2.10.0 |
| Activity Compose | `libs.androidx.activity.compose` | 1.12.2 |
| Compose BOM | `libs.androidx.compose.bom` | 2024.09.00 |
| Material3 | `libs.androidx.compose.material3` | (BOM管理) |
| AppCompat | `libs.androidx.appcompat` | 1.7.1 |
| Material | `libs.material` | 1.13.0 |
| JUnit | `libs.junit` | 4.13.2 |
| AndroidX JUnit | `libs.androidx.junit` | 1.3.0 |
| Espresso | `libs.androidx.espresso.core` | 3.7.0 |

> **注意**：不要在模块中直接硬编码版本号，始终通过 Version Catalog 管理。

---

## 全局配置

### 修改 SDK 版本 / Java 版本

编辑 `build-logic/src/main/kotlin/internal/Versions.kt`：

```kotlin
object Versions {
    const val COMPILE_SDK = 36      // 修改后全部模块自动生效
    const val MIN_SDK = 24
    const val TARGET_SDK = 36
    const val JAVA_VERSION = 17
}
```

### 修改 Gradle / AGP / Kotlin 版本

编辑 `gradle/libs.versions.toml`：

```toml
[versions]
agp = "8.13.2"
kotlin = "2.0.21"
```

> build-logic 通过 Version Catalog 引用这些版本，修改一处即可全局生效。

### 项目级属性

编辑 `gradle.properties`：

```properties
APP_NAMESPACE=com.lhz
org.gradle.parallel=true
android.nonTransitiveRClass=true
```

---

## 架构规则

`ArchitectureRulesPlugin` 在 Gradle 配置阶段自动校验以下规则：

| 规则 | 说明 |
|------|------|
| feature ✗→ feature | Feature 模块之间禁止互相依赖 |
| core ✗→ feature | Core 层不能依赖 Feature 层 |
| core ✗→ infra | Core 层（抽象）不能依赖 Infra 层（实现） |
| feature ✗→ infra | Feature 层不能直接依赖 Infra 层 |

违规示例：

```kotlin
// feature:feature-user/build.gradle.kts
dependencies {
    implementation(project(":feature:feature-setting"))  // ❌ 编译失败
    implementation(project(":infra:network-impl"))       // ❌ 编译失败
}
```

正确做法：

```kotlin
dependencies {
    implementation(project(":core:network"))             // ✅ 依赖抽象接口
    implementation(project(":core:model"))               // ✅ 依赖数据模型
}
```

---

## Navigation 路由方案

在多模块架构中，Feature 之间不能互相依赖但需要互相跳转，推荐以下方案：

### 三层分离

| 层级 | 职责 |
|------|------|
| **core:ui** | 定义所有 Route 常量，通过 `api()` 暴露 navigation-compose 依赖 |
| **feature** | 暴露 `NavGraphBuilder.xxxScreen()` 扩展函数，不持有 NavController |
| **app** | 创建 NavHost，组装所有 Feature 的路由，处理跳转逻辑 |

### 示例

```kotlin
// core:ui — 路由定义
object Routes {
    const val USER = "user"
    const val SETTING = "setting"
}

// feature — 暴露扩展函数，跳转通过回调上抛
fun NavGraphBuilder.userScreen(onNavigateToSetting: () -> Unit) {
    composable(Routes.USER) {
        UserScreen(onSettingClick = onNavigateToSetting)
    }
}

// app — 组装路由
NavHost(navController, startDestination = Routes.USER) {
    userScreen(
        onNavigateToSetting = { navController.navigate(Routes.SETTING) }
    )
    settingScreen(
        onBack = { navController.popBackStack() }
    )
}
```

---

## 常见问题

### Q: 新增一个依赖需要改几个文件？

最少 2 个：`libs.versions.toml`（声明）+ 目标模块的 `build.gradle.kts`（引用）。

### Q: Feature 模块之间如何通信？

通过 `core` 层的共享接口或 `app` 层的 Navigation 回调，不能直接互相依赖。

### Q: 如何添加新的 Convention Plugin？

1. 在 `build-logic/src/main/kotlin/` 下创建 Plugin 类
2. 在 `build-logic/build.gradle.kts` 的 `gradlePlugin` 块中注册
3. 在目标模块中 `id("scaffold.android.xxx")` 使用

### Q: 如何集成 Hilt 依赖注入？

1. 在 `libs.versions.toml` 添加 Hilt 版本、library 和 plugin
2. 在 `build-logic/build.gradle.kts` 的 `dependencies` 中添加 Hilt Gradle Plugin
3. 创建 `HiltConventionPlugin` 统一配置 Hilt
4. 在需要的模块中 `id("scaffold.android.hilt")` 使用

### Q: 为什么 core 不能依赖 infra？

Core 层是抽象层，定义接口和数据模型。Infra 层是实现层，依赖具体 SDK。如果 core 依赖 infra，就会形成循环依赖，且上层模块会被迫引入不需要的 SDK。

---

## 许可证

MIT License
