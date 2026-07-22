# OnlyTP

[English](README.md) | [中文](README_zh.md)

OnlyTP 是一个轻量级的 Minecraft NeoForge 与 Fabric 模组，为您的游戏体验提供便捷的传送功能。无论您是在探索广阔的世界，还是要帮助有需要的朋友，OnlyTP 都能让您在 Minecraft 服务器中的旅行变得简单高效。

## ✨ 功能特色

- **🎯 简单命令**: 直观的传送命令，易于记忆和使用
- **🌟 增强体验**: 传送时带有精美的粒子效果和音效反馈
- **⚡ 多加载器支持**: 使用同一套公共代码原生构建 NeoForge 与 Fabric 版本

## 🎮 使用方法

### 基础命令
- **`/tlp <玩家名>`**: 传送到指定的在线玩家位置

### 功能详情
- **粒子效果**: 在出发地和到达地都会出现美丽的传送门粒子效果
- **音效反馈**: 沉浸式的传送门音效增强传送体验
- **玩家通知**: 传送者和目标玩家都会收到清晰的状态消息

### 🔒 安全提示
不建议在竞技性 PvP 服务器上使用此模组，因为它可能为玩家提供战术优势。

## 🔧 兼容性

- **Minecraft 版本**: 1.21 至 1.21.11，以及 26.1 至 26.2
- **加载器**: NeoForge 或 Fabric Loader；请使用同时匹配加载器与 Minecraft 版本的 jar
- **Java**: Minecraft 1.21.x 使用 Java 21；Minecraft 26.x 使用 Java 25
- **依赖**: Fabric 构建需要 Fabric API；NeoForge 构建没有额外模组依赖
- **安装要求**: 服务端必需，客户端可选
  - 在客户端安装可获得本地化提示
  - 仅服务端安装时所有玩家使用服务器语言

## 🛠️ 开发

`master` 分支同时包含两个加载器实现：

- `common/` - 公共命令逻辑、资源和 Minecraft 版本兼容层
- `neoforge/` - NeoForge 入口与元数据
- `fabric/` - Fabric 入口与元数据

为选定的 Minecraft 版本构建两个可发布 jar：

```bash
./gradlew build
```

产物分别位于 `neoforge/build/libs/` 和 `fabric/build/libs/`。

## 📄 许可证

本项目基于 MIT 许可证开源 - 详情请查看 [LICENSE.txt](LICENSE.txt) 文件。
