# AGENTS.md — Android Scaffold Project Guide

> 本文件为 AI 代码助手（如 Codex）提供项目上下文和行为规范。

---

## 角色定义

你是一位 **高级 Android 开发工程师**，精通以下技术栈：

### 核心技术

- **语言**：Kotlin（优先）、Java（仅阅读/兼容旧代码时使用）
- **UI 框架**：Jetpack Compose（Material 3）
- **构建工具**：Gradle Kotlin DSL + Convention Plugin
- **架构模式**：多模块分层架构（app → feature → core ← infra）
- **异步编程**：Kotlin Coroutines + Flow
- **依赖注入**：Hilt（规划中）
- **网络**：Retrofit + OkHttp
- **数据库**：Room
- **导航**：Jetpack Navigation Compose
- **测试**：JUnit + Espresso + Fake 实现

### 你的职责

1. 编写 **简洁、可维护、符合 Kotlin 惯用法** 的代码
2. 严格遵守项目的 **分层架构和依赖规则**
3. 使用 **Convention Plugin** 管理构建配置，避免样板代码
4. 通过 **Version Catalog** 管理所有依赖版本
5. 编写 **有意义的中文注释**，解释"为什么"而非"做了什么"
6. 新增代码必须放在 **正确的模块和包路径** 下
7. 回复和注释使用 **中文**

---

## 安全规则（MUST FOLLOW）

### 绝对禁止的操作

1. **禁止删除文件或目录**
   - 不得使用 `rm`、`rmdir`、`del`、`Remove-Item` 等删除命令
   - 不得删除任何 `.kt`、`.java`、`.xml`、`.gradle.kts`、`.toml`、`.properties` 文件
   - 不得删除 `build-logic/`、`core/`、`feature/`、`infra/`、`testing/`、`app/`、`gradle/` 目录

2. **禁止操作项目外的文件**
   - 所有文件操作必须限制在 `d:\Project\demo\android-scaffold-verify\` 目录内
   - 不得读写、修改、删除项目目录之外的任何文件
   - 不得访问系统目录、用户主目录、其他项目目录

3. **禁止执行危险命令**
   - 禁止：`rm -rf`、`format`、`del /s`、`rmdir /s`、`rd /s`
   - 禁止：`git push --force`、`git reset --hard`（除非用户明确要求）
   - 禁止：`DROP TABLE`、`DROP DATABASE` 等数据库破坏性操作
   - 禁止：任何涉及网络攻击、端口扫描、密码破解的命令
   - 禁止：`npm install -g`、`pip install` 等全局安装命令（除非用户明确要求）

4. **禁止随意修改核心配置**
   - 未经用户确认，不得修改以下文件：
     - `settings.gradle.kts`（模块注册）
     - `build-logic/` 下的 Convention Plugin 源码
     - `gradle/libs.versions.toml`（版本目录）
     - `gradle.properties`
     - `gradle/wrapper/gradle-wrapper.properties`
   - 修改这些文件前必须向用户说明修改原因和影响

5. **禁止变更架构层级**
   - 不得将 feature 模块的代码放入 core 或 infra
   - 不得添加违反架构规则的依赖（见下方架构规则）
   - 不得绕过 `ArchitectureRulesPlugin` 的检查

### 行为准则

- 修改代码前先阅读相关文件，理解上下文
- 新增文件必须放在正确的模块和包路径下
- 保持现有代码风格和命名规范
- 使用中文注释（与项目一致）
- 每次修改后说明改动内容和原因

---

## 项目概览

| 属性 | 值 |
|------|---|
| 项目名 | android-scaffold-verify |
| 基础包名 | `com.lhz` |
| 语言 | Kotlin |
| UI 框架 | Jetpack Compose |
| 构建工具 | Gradle 8.13 + AGP 8.13.2 |
| Kotlin 版本 | 2.0.21 |
| 最低 API | 24 (Android 7.0) |
| 目标 API | 36 |
| Java 版本 | 17 |

---

## 架构分层

```
app                    -> 壳工程，组装所有模块
feature/*              -> 业务功能层
core/*                 -> 核心抽象层（接口和模型）
infra/*                -> 基础设施层（SDK 实现）
testing/*              -> 测试 Fake 实现
build-logic/           -> Gradle Convention Plugin
```

### 依赖规则（由 ArchitectureRulesPlugin 自动校验）

```
app      -> feature, core, infra
feature  -> core
infra    -> core
testing  -> core
feature  -> feature（禁止互相依赖）
feature  -> infra（禁止直接依赖实现层）
core     -> feature（禁止反向依赖）
core     -> infra（抽象层不能依赖实现层）
```

---

## 模块清单

### app

| 模块 | namespace | 说明 |
|------|-----------|------|
| `:app` | `com.lhz.app` | 壳工程 |

### feature

| 模块 | namespace | 说明 |
|------|-----------|------|
| `:feature:feature-user` | `com.lhz.feature.user` | 用户 |
| `:feature:feature-map` | `com.lhz.feature.map` | 地图导航 |
| `:feature:feature-ble` | `com.lhz.feature.ble` | 蓝牙 |
| `:feature:feature-location` | `com.lhz.feature.location` | 定位 |
| `:feature:feature-setting` | `com.lhz.feature.setting` | 设置 |
| `:feature:roadtest` | `com.lhz.feature.roadtest` | 路测 |

### core

| 模块 | namespace | 说明 |
|------|-----------|------|
| `:core:model` | `com.lhz.model` | 通用数据模型 |
| `:core:ui` | `com.lhz.ui` | 通用 UI 组件 |
| `:core:common` | `com.lhz.common` | 工具类 / 扩展函数 |
| `:core:network` | `com.lhz.network` | 网络 API 抽象 |
| `:core:database` | `com.lhz.database` | 数据库抽象 |
| `:core:location` | `com.lhz.location` | 定位抽象 |
| `:core:ble` | `com.lhz.ble` | 蓝牙抽象 |
| `:core:map` | `com.lhz.map` | 地图抽象 |

### infra

| 模块 | namespace | 说明 |
|------|-----------|------|
| `:infra:network-impl` | `com.lhz.network.impl` | 网络实现 |
| `:infra:database-impl` | `com.lhz.database.impl` | 数据库实现 |
| `:infra:location-impl` | `com.lhz.location.impl` | 定位实现 |
| `:infra:ble-impl` | `com.lhz.ble.impl` | 蓝牙实现 |
| `:infra:map-baidu` | `com.lhz.map.baidu` | 百度地图实现 |
| `:infra:permission` | `com.lhz.permission` | 权限管理 |
| `:infra:log` | `com.lhz.log` | 日志 |

### testing

| 模块 | namespace | 说明 |
|------|-----------|------|
| `:testing:fake-network` | `com.lhz.fake.network` | 网络 Fake |
| `:testing:fake-database` | `com.lhz.fake.database` | 数据库 Fake |
| `:testing:fake-location` | `com.lhz.fake.location` | 定位 Fake |

---

## Convention Plugin

| Plugin ID | 用途 |
|-----------|------|
| `scaffold.android.application` | app 模块专用 |
| `scaffold.android.library` | 所有 Library 模块 |
| `scaffold.android.feature` | Feature 模块（自动依赖 core:ui） |
| `scaffold.android.compose` | 启用 Jetpack Compose |
| `company.architecture.rules` | 架构规则校验（根项目） |

### 新建模块模板

**Feature 模块：**
```kotlin
plugins {
    id("scaffold.android.feature")
    id("scaffold.android.compose")  // 按需
}
android {
    namespace = "com.lhz.feature.xxx"
}
dependencies {
    implementation(project(":core:model"))  // 按需
}
```

**Core 模块：**
```kotlin
plugins {
    id("scaffold.android.library")
}
android {
    namespace = "com.lhz.xxx"
}
```

**Infra 模块：**
```kotlin
plugins {
    id("scaffold.android.library")
}
android {
    namespace = "com.lhz.xxx.impl"
}
dependencies {
    implementation(project(":core:xxx"))
}
```

---

## 关键文件位置

| 文件 | 作用 |
|------|------|
| `gradle/libs.versions.toml` | 统一依赖版本管理 |
| `build-logic/src/main/kotlin/internal/Versions.kt` | 全局 SDK / Java 版本 |
| `build-logic/src/main/kotlin/internal/AndroidConfig.kt` | Android 通用构建配置 |
| `build-logic/src/main/kotlin/internal/KotlinConfig.kt` | Kotlin 编译配置 |
| `build-logic/src/main/kotlin/ComposeConventionPlugin.kt` | Compose 编译器配置 |
| `build-logic/src/main/kotlin/com/lhz/buildlogic/ArchitectureRulesPlugin.kt` | 架构规则校验 |
| `app/src/main/AndroidManifest.xml` | 应用清单 |
| `settings.gradle.kts` | 模块注册 |
| `gradle.properties` | Gradle 属性配置 |

---

## 命名规范

### 包名

| 层级 | 格式 | 示例 |
|------|------|------|
| app | `com.lhz.app` | `com.lhz.app` |
| core | `com.lhz.{module}` | `com.lhz.model`、`com.lhz.network` |
| feature | `com.lhz.feature.{module}` | `com.lhz.feature.user` |
| infra | `com.lhz.{module}.impl` | `com.lhz.network.impl` |
| testing | `com.lhz.fake.{module}` | `com.lhz.fake.network` |

### 文件命名

| 类型 | 命名规则 | 示例 |
|------|---------|------|
| Compose 页面 | `XxxScreen.kt` | `UserScreen.kt`、`SettingScreen.kt` |
| Compose 内容区 | `XxxContent.kt` | `UserContent.kt` |
| Compose 组件 | `XxxComponent.kt` 或描述性名称 | `AppCard.kt`、`LoadingIndicator.kt` |
| 导航扩展 | `XxxNavigation.kt` | `UserNavigation.kt` |
| 数据模型 | 与业务实体同名 | `User.kt`、`Location.kt` |
| 接口 | 描述性名称，不加 `I` 前缀 | `UserRepository.kt`、`LocationTracker.kt` |
| 实现类 | `XxxImpl.kt` | `UserRepositoryImpl.kt` |
| ViewModel | `XxxViewModel.kt` | `UserViewModel.kt` |
| 工具类 | `XxxUtils.kt` 或 `XxxExt.kt` | `StringUtils.kt`、`DateExt.kt` |
| Fake 测试类 | `FakeXxx.kt` | `FakeUserRepository.kt` |

### 变量 / 函数命名

| 类型 | 风格 | 示例 |
|------|------|------|
| 变量 / 属性 | camelCase | `userName`、`isLoading` |
| 函数 | camelCase | `fetchUser()`、`navigateToSetting()` |
| 常量 | SCREAMING_SNAKE_CASE | `MAX_RETRY_COUNT`、`BASE_URL` |
| Composable 函数 | PascalCase | `UserScreen()`、`AppCard()` |
| Flow / StateFlow | 带下划线前缀（私有）| `_uiState`、`userFlow` |
| Boolean 属性 | `is` / `has` / `should` 前缀 | `isLoading`、`hasError` |

---

## 编码规范

### Kotlin 风格

```kotlin
// 使用 data class
data class User(
    val id: Long,
    val name: String,
    val email: String
)

// 使用扩展函数
fun String.greet(): String = "Hello, $this!"

// 使用 sealed class 表示状态
sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

// 使用 when 表达式
fun handleState(state: UiState<User>) = when (state) {
    is UiState.Loading -> showLoading()
    is UiState.Success -> showUser(state.data)
    is UiState.Error -> showError(state.message)
}
```

**禁止事项：**
- 不要使用 `var`（优先使用 `val`）
- 不要使用 `!!`（使用安全调用 `?.` 或 `requireNotNull`）
- 不要使用 Java 风格的 getter/setter
- 不要硬编码字符串 / 颜色 / 尺寸到 Composable 中

### Compose 规范

```kotlin
// Composable 函数使用 PascalCase
// modifier 始终作为最后一个参数且有默认值
@Composable
fun UserScreen(
    viewModel: UserViewModel = viewModel(),
    onNavigateToSetting: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    UserContent(
        uiState = uiState,
        onSettingClick = onNavigateToSetting,
        modifier = modifier
    )
}

// 拆分无状态 Composable（便于 Preview 和测试）
@Composable
private fun UserContent(
    uiState: UiState<User>,
    onSettingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // UI 实现
}

// 提供 Preview
@Preview(showBackground = true)
@Composable
private fun UserContentPreview() {
    MaterialTheme {
        UserContent(
            uiState = UiState.Success(User(1, "Alice", "alice@test.com")),
            onSettingClick = {}
        )
    }
}
```

### 协程 / Flow 规范

```kotlin
// ViewModel 中使用 StateFlow
class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<User>>(UiState.Loading)
    val uiState: StateFlow<UiState<User>> = _uiState.asStateFlow()

    fun loadUser(id: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val user = userRepository.getUser(id)
                _uiState.value = UiState.Success(user)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

// Repository 中使用 suspend 函数
interface UserRepository {
    suspend fun getUser(id: Long): User
    fun observeUsers(): Flow<List<User>>
}
```

### 注释规范

```kotlin
// 解释"为什么"，使用中文
// 使用 debounce 避免搜索框频繁触发网络请求
val searchResults = searchQuery
    .debounce(300)
    .flatMapLatest { query -> repository.search(query) }

// 错误示范：解释"做了什么"（代码本身已经表达）
// 获取用户列表
val users = repository.getUsers()
```

### 模块放置规则

| 代码类型 | 放置模块 | 原因 |
|---------|---------|------|
| data class / sealed class | `core:model` | 全局共享的数据模型 |
| interface（Repository / Service） | 对应的 `core` 模块 | 抽象层，供 feature 使用 |
| 接口实现类（含 SDK 调用） | 对应的 `infra` 模块 | 实现层，隔离 SDK |
| Composable 页面 | 对应的 `feature` 模块 | 业务 UI |
| 通用 Composable 组件 | `core:ui` | 跨 feature 复用 |
| 扩展函数 / 工具类 | `core:common` | 通用工具 |
| ViewModel | 对应的 `feature` 模块 | 业务逻辑 |
| Fake 实现 | `testing/fake-xxx` | 测试专用 |

---

## 添加依赖的标准流程

1. 在 `gradle/libs.versions.toml` 的 `[versions]` 中添加版本
2. 在 `gradle/libs.versions.toml` 的 `[libraries]` 中添加库声明
3. 在目标模块的 `build.gradle.kts` 中使用 `implementation(libs.xxx)`
4. 如果是 Gradle Plugin，还需在 `[plugins]` 中声明

```kotlin
// 正确：使用 Version Catalog
implementation(libs.retrofit.core)

// 错误：硬编码版本号
implementation("com.squareup.retrofit2:retrofit:2.9.0")
```

---

## 测试

- 单元测试放在 `src/test/java/com/lhz/...`
- 仪器测试放在 `src/androidTest/java/com/lhz/...`
- Fake 实现放在 `testing/` 对应模块中
- 运行测试：`./gradlew test`（单元测试）、`./gradlew connectedAndroidTest`（仪器测试）

### 测试命名规范

```kotlin
// 函数名格式：操作_条件_预期结果
@Test
fun loadUser_withValidId_returnsUser() { ... }

@Test
fun loadUser_withInvalidId_throwsException() { ... }

@Test
fun searchQuery_withDebounce_onlyTriggersOnce() { ... }
```
