# Quickly-Use-Jetpack-Compose

一个用于快速学习和复用 Jetpack Compose 实践的示例项目。项目以 Compose、Hilt、Navigation、ViewModel、Flow 和模块化工程为基础，尽量把常见移动端能力整理成可运行的示例。

# 协议

[查看隐私协议](PRIVACY.md)

# 架构

Quickly-Use-Jetpack-Compose 的架构参考 Android 官方最佳实践项目 [Now in Android App](https://github.com/android/nowinandroid)。

## 架构组件

+ **模块化**：按 app、core、feature、flavor、res 等模块组织代码，降低耦合。
+ **依赖注入**：使用 Hilt 管理全局和局部依赖。
+ **数据层**：采用 Repository 模式，集成 **Room** 数据库和 **Ktor (OkHttp)** 网络请求。
+ **UI 驱动**：单 Activity 架构，使用 Navigation 管理页面跳转，结合 Compose + ViewModel + Flow 实现响应式 UI。

# 设计系统

项目包含一套自定义 Compose 设计系统，偏微信风格，不直接套用 Material 3 的视觉样式。

+ **WeTheme**：替代 MaterialTheme，竖屏按 375dp 设计宽度适配。
+ **WeColorScheme**：定义颜色体系，支持系统、动态、浅色、深色和蓝色主题。
+ **WeTypography**：定义字体大小体系。
+ **WeIndication**：定义触摸、悬停和焦点反馈。
+ **WeDimen**：定义尺寸规范。
+ **WeIcons**：使用 ImageVector 绘制图标。
+ **WeWidget**：顶部栏、底部栏、Button、Toast、ActionSheet、单选、多选、开关等通用组件。
+ **View**：Banner (BannerView)、可点击富文本 (ClickableAnnotatedText)、拖拽排序 (DragList)、错误页 (ErrorView)、Loading、禁止截屏 (SecureComposeView) 等常用 Compose 组件。

# Module 目录简介

+ **app**：应用入口，汇总各 feature 并统一处理 Navigation。
+ **build-logic**：自定义 Gradle Convention 插件，统一管理 Compose、Hilt、Serialization、Library、Application 等构建配置。
+ **core-logic**：
    - `common`：日志、Toast、协程调度、缓存等基础工具。
    - `database`：基于 **Room** 的持久化存储。
    - `network`：基于 **Ktor** 的网络请求封装。
    - `repository`：业务数据层，包含聊天 (Google AI Chat)、用户信息、产品等仓库。
    - `authenticate`：Google 登录、生物认证逻辑封装。
    - `notification`：通知管理，包含 Firebase Cloud Messaging。
    - `location`：定位能力封装。
    - `language`：多语言切换逻辑。
+ **core-ui**：设计系统核心实现和通用 UI 组件库。
+ **core-launcher**：封装 `ActivityResultLauncher`，提供一行代码调用相册、相机、联系人、手机号选择及权限申请的能力。
+ **feature**：
    - `main`：应用主框架，包含首页 Pager 容器。
    - `samples`：UI 交互示例，包含 **自定义日历**、**绘画画板**、**嵌套滚动** 等。
    - `settings`：应用偏好设置（多语言、字体大小、主题切换）。
    - `integrations`：网络与系统能力集成示例（HTTP、Firebase、生物认证）。
    - `chat`：基于 **Google AI Gemini** 的智能聊天示例。
    - `video`：基于 **Media3** 的视频播放器。
    - `webview`：通用的 WebView 容器。
+ **flavor**：提供 `gp` (Google Play) 和 `sam` (Samsung) 渠道差异化实现示例。
+ **res**：统一管理字符串、图片、多语言等资源文件。
+ **baseline-profile**：配置启动性能优化。

# 开发与运行

建议使用最新版本 Android Studio 打开项目。运行时切换到 `app` 配置后启动。

## 密钥与签名

密钥文件存放在根目录的 `keystore` 目录中。签名相关配置在 `AndroidApplicationConventionPlugin.kt`。

## 构建变体 (Build Variants)

项目预设了 `gp` 和 `sam` 两个 productFlavors，分别对应不同的 ApplicationId 和签名配置，可在 Android Studio 的 Build Variants 面板中切换。

# 运行效果

| 示例 | 截图 |
| --- | --- |
| 绘画画板 | <img src="docs/images/PainterScreen.png" width="320"/> |
| 多语言 | <img src="docs/images/LanguageSwitch.gif" width="320"/> |
| Lazy 列表排序 | <img src="docs/images/LazySort.gif" width="320"/> |
| 自定义日历 | <img src="docs/images/CustomizeCalendar.gif" width="320"/> |
| 动态切换图标 | <img src="docs/images/SwitchAppLogo.gif" width="320"/> |
| AI 聊天和通知 | <img src="docs/images/AiChat.gif" width="320"/> |
| 网络异常处理 | <img src="docs/images/HttpScreen.gif" width="320"/> |
| Banner | <img src="docs/images/BannerView.gif" width="320"/> |
| 定位、图片、联系人 | <img src="docs/images/LocationPhotoContacts.gif" width="320"/> |
