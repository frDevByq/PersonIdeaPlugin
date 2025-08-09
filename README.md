# IDEA Personal Assistant Plugin / IDEA 个人辅助插件

<!-- Plugin description -->
This is a personal assistant plugin for IntelliJ IDEA that enhances code readability by displaying comments directly in the IDE interface. The plugin shows comment content in project file tree and structure views, with flexible display options and customizable settings.
<!-- Plugin description end -->

[English](#english) | [中文](#chinese)

---

## English

### Overview

This is a personal assistant plugin for IntelliJ IDEA that enhances code readability by displaying comments directly in the IDE interface.

### Features

- **File Tree Comment Display**: Shows comments content next to elements in the project file tree
- **Flexible Display Options**: Choose to display only comments or include identifiers (class/method names)
- **Structure View Integration**: Comments are visible in the Structure view (Ctrl+F12)
- **Customizable Settings**: Toggle identifier display on/off through plugin settings

### Display Modes

#### With Identifiers Disabled
- Method/class names appear in gray on the right side
- Comments are prominently displayed

#### With Identifiers Enabled
- Method/class names appear on the left side
- Comments content appears on the right side

### Structure View Support

The plugin extends comment display to:
- Structure view panel
- File structure popup (Ctrl+F12)
- Helps clarify code organization and logic flow

### Compatibility

- **IDEA Version**: Compatible with IntelliJ IDEA 2025.1
- **Based on**: [show-comment](https://github.com/LinWanCen/show-comment)

### Key Improvements Over Original

- Added structure tree comment support
- Upgraded Gradle version
- Updated IDEA plugin SDK version
- Enhanced compatibility with newer IDEA versions

### Installation

1. Download the plugin from releases
2. Go to File → Settings → Plugins
3. Click the gear icon and select "Install Plugin from Disk"
4. Select the downloaded plugin file
5. Restart IntelliJ IDEA

### Usage

1. Install and enable the plugin
2. Configure display options in Settings → Tools → [Plugin Name]
3. View comments in file tree and structure views
4. Use Ctrl+F12 to see enhanced file structure with comments

---

## Chinese

### 概述

这是一个IntelliJ IDEA个人辅助插件，通过在IDE界面中直接显示注释内容来增强代码可读性。

### 功能特性

- **文件树注释显示**：在项目文件树中显示元素的注释内容
- **灵活显示选项**：可选择仅显示注释或包含标识符（类名/方法名）
- **结构视图集成**：在结构视图中显示注释（Ctrl+F12）
- **可自定义设置**：通过插件设置开关标识符显示

### 显示模式

#### 关闭标识符显示
- 方法/类名在右边以灰色显示
- 注释内容突出显示

#### 开启标识符显示
- 方法/类名在左边显示
- 注释内容在右边显示

### 结构视图支持

插件扩展了注释显示功能至：
- 结构视图面板
- 文件结构弹窗（Ctrl+F12）
- 帮助理清代码组织和逻辑流程

### 兼容性

- **IDEA版本**：兼容IntelliJ IDEA 2025.1
- **基于项目**：[show-comment](https://github.com/LinWanCen/show-comment)

### 相比原仓库的主要改进

- 添加了结构树注释支持
- 升级了Gradle版本
- 更新了IDEA插件SDK版本
- 增强了与新版本IDEA的兼容性

### 安装方法

1. 从releases下载插件文件
2. 进入 文件 → 设置 → 插件
3. 点击齿轮图标，选择"从磁盘安装插件"
4. 选择下载的插件文件
5. 重启IntelliJ IDEA

### 使用方法

1. 安装并启用插件
2. 在 设置 → 工具 → [插件名称] 中配置显示选项
3. 在文件树和结构视图中查看注释
4. 使用Ctrl+F12查看带注释的增强文件结构

### 致谢

感谢原作者 [LinWanCen](https://github.com/LinWanCen) 提供的 [show-comment](https://github.com/LinWanCen/show-comment) 项目基础。