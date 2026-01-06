# TestDemo Android 开发工具配置

> 由 Android DevTools MCP 自动生成  
> 生成时间: 2026-01-06

---

## 📁 目录结构

```
.adevtools/
├── config.json              # 项目配置文件
├── routes.md                # 路由文档 (AI 生成)
├── project-analysis.md      # 项目深度分析报告
├── test-routes.json         # 自动化测试脚本
├── android-compile.sh       # 编译检查脚本
├── android-build-release.sh # Release 编译脚本
└── README.md               # 本文件
```

---

## 🚀 快速开始

### 1. 查看项目路由
```bash
cat .adevtools/routes.md
```

### 2. 查看项目分析
```bash
cat .adevtools/project-analysis.md
```

### 3. 运行自动化测试
```bash
# 使用 MCP 工具运行
adk test .adevtools/test-routes.json
```

### 4. 编译项目
```bash
# 检查编译
./.adevtools/android-compile.sh

# Release 编译
./.adevtools/android-build-release.sh
```

---

## 📱 设备信息

**当前连接设备:**
- 设备ID: RFCX114X18X
- 型号: Samsung SM-S9210
- Android 版本: 16 (SDK 36)
- 屏幕: 1080 x 2340

---

## 🎯 主要功能路由

### 启动应用
```bash
adb shell am start -n com.example.demo/.MainActivity
```

### 启动引导页
```bash
adb shell am start -n com.example.demo/.guide.GuideActivity
```

### 启动气泡页面
```bash
adb shell am start -n com.example.demo/.bubble.MainBubbleActivity
```

### 启动编辑器
```bash
adb shell am start -n com.example.demo/.editor.EditorActivity
```

---

## 🔧 开发命令

### 编译和安装
```bash
# 清理构建
./gradlew clean

# 编译 Debug 版本
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug

# 或直接安装 APK
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### 日志查看
```bash
# 查看应用日志
adb logcat -s ADEV_DEBUG

# 查看所有日志
adb logcat | grep com.example.demo

# 清除日志
adb logcat -c
```

### 应用管理
```bash
# 清除应用数据
adb shell pm clear com.example.demo

# 卸载应用
adb uninstall com.example.demo

# 查看应用信息
adb shell dumpsys package com.example.demo
```

---

## 📊 项目配置

### 包名
```
com.example.demo
```

### 模块列表
- `:app` - 主应用模块
- `:demo_widget` - 自定义控件库
- `:demo_core` - 核心功能库

### Git Commit
```
c8bfb36033ad05947749da60316ee4ccc7722427
```

---

## 🧪 测试指南

### 自动化测试脚本说明

`test-routes.json` 包含以下测试流程:

1. ✅ 启动应用到主页
2. ✅ 测试气泡功能入口
3. ✅ 测试引导功能入口
4. ✅ 测试动画功能入口
5. ✅ 测试编辑器功能入口
6. ✅ 返回主页验证

### 手动测试检查清单

#### MainActivity 测试
- [ ] 应用正常启动
- [ ] 所有按钮可见
- [ ] 点击 "bubble" 按钮跳转正常
- [ ] 点击 "guide" 按钮跳转正常
- [ ] 点击 "anim" 按钮跳转正常
- [ ] 点击 "editor" 按钮跳转正常
- [ ] 点击 "pay" 按钮功能正常

#### GuideActivity 测试
- [ ] 引导页正常显示
- [ ] 高亮区域显示正确
- [ ] 多页切换正常
- [ ] 动画效果流畅
- [ ] 点击跳转功能正常

#### Bubble 功能测试
- [ ] 气泡弹窗位置正确
- [ ] 坐标计算准确
- [ ] Dialog 显示正常
- [ ] DialogFragment 功能正常

#### Editor 测试
- [ ] 编辑器正常显示
- [ ] 输入功能正常
- [ ] 自定义功能工作正常

---

## 📖 文档说明

### routes.md
包含完整的应用路由结构:
- Activity 列表和说明
- 页面跳转关系
- Intent 调用方式
- 功能模块说明

### project-analysis.md
包含深度项目分析:
- 技术栈分析
- 代码结构详解
- 功能模块说明
- 关键代码片段
- 开发建议
- 优化方向

### config.json
项目配置信息:
- Android 模块配置
- 包名和构建命令
- Git 提交信息
- 更新时间

---

## 🛠️ AI 辅助开发

### 可用的 AI Commands

在 Cursor 中可以使用以下命令:

1. `/androidDev.init` - 初始化项目分析
2. `/androidDev.dev` - 开发模式
3. `/androidDev.dev.run` - 运行应用
4. `/androidDev.dev.verify` - 验证功能
5. `/androidDev.appUsage` - 应用使用指南

### AI Skills

- `android-app-usage` - APP 实战操作规范

---

## ⚙️ MCP 配置

### 配置文件位置
```
.cursor/mcp.json
```

### AI Agent
```
Cursor
```

### MCP 服务器
```
@ali/android-devtools-mcp
```

---

## 📝 更新日志

### 2026-01-06
- ✅ 初始化项目配置
- ✅ 生成路由文档
- ✅ 生成项目分析报告
- ✅ 创建测试脚本
- ✅ 配置 MCP 服务

---

## 🔗 相关链接

- [Android 官方文档](https://developer.android.com)
- [Gradle 构建指南](https://docs.gradle.org)
- [ADB 命令参考](https://developer.android.com/studio/command-line/adb)

---

## 💡 提示

### 编译问题
如果遇到编译问题，尝试:
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

### 设备连接问题
如果设备未识别:
```bash
adb kill-server
adb start-server
adb devices
```

### 应用崩溃
查看崩溃日志:
```bash
adb logcat -b crash
```

---

*本文档由 Android DevTools MCP 自动维护*

