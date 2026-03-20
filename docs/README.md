# Android脚手架



```html
road-navigator
│
├── app                         // 壳工程
│
├── feature                     // 业务功能（每个都是 module）
│   ├── user                    // 用户
│   ├── map                     // 地图 / 导航
│   ├── location                // 定位
│   ├── ble                     // 蓝牙
│   ├── settings                // 设置
│
├── core                        // 核心能力 & 抽象（无 SDK）
│   ├── model                   // 通用数据模型
│   ├── ui                      // 通用 UI 组件
│   ├── network                 // API / WS 抽象
│   ├── database                // 数据库抽象（Dao 接口）
│   ├── location                // 定位抽象
│   ├── ble                     // 蓝牙抽象
│   ├── map                     // 地图抽象
│   ├── common                  // Utils / Dispatcher / Result
│
├── infra                       // 基础设施（强依赖 SDK）
│   ├── network-impl            // Retrofit / OkHttp / WS
│   ├── database-impl           // Room / DataStore
│   ├── location-impl           // Fused / 百度定位
│   ├── ble-impl                // Android BLE
│   ├── map-baidu               // 百度地图实现
│   ├── permission              // 权限
│   ├── log                     // 日志
│
├── testing                     // 测试专用 module
│   ├── fake-network
│   ├── fake-database
│   ├── fake-location
│
├── benchmarks                  // 性能测试
│
├── build-logic                 // Gradle 约定插件（可先空）
│
├── docs                        // 架构文档
│
├── gradle
├── settings.gradle.kts
├── build.gradle.kts
└── gradle.properties

```

