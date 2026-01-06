# TestDemo 项目路由文档

> 生成时间: 2026-01-06  
> Git Commit: c8bfb36033ad05947749da60316ee4ccc7722427  
> 包名: com.example.demo

---

## 📱 设备信息

**已连接设备：**
- **设备ID**: RFCX114X18X
- **型号**: SM-S9210 (Samsung)
- **Android 版本**: 16
- **SDK 版本**: 36
- **屏幕尺寸**: 1080 x 2340

---

## 🏗️ 项目结构

### 模块信息

| 模块名 | 类型 | 路径 |
|--------|------|------|
| :app | application | `/Users/leeyu35/工作区/android/TestDemo/app` |
| :demo_widget | library | `/Users/leeyu35/工作区/android/TestDemo/demo_widget` |
| :demo_core | library | `/Users/leeyu35/工作区/android/TestDemo/demo_core` |

---

## 🗺️ Activity 路由图

### 主入口 - MainActivity
**类路径**: `com.example.demo.MainActivity`  
**布局**: `activity_main.xml`  
**功能**: 应用主页面，提供多个功能模块的入口

**导航路径**:
```
MainActivity (启动页)
├─ AutoMobiumActivity      [R.id.bubble] → 气泡测试页面
├─ GuideActivity           [R.id.guide] → 引导页功能
├─ ActivityOne             [R.id.anim] → 动画演示
├─ WxPay                   [R.id.pay] → 微信支付功能
└─ EditorActivity          [R.id.editor] → 编辑器功能
```

---

## 📋 详细 Activity 列表

### 1. MainActivity
- **包名**: `com.example.demo.MainActivity`
- **功能**: 应用首页，展示所有功能模块入口
- **Intent Filter**: `android.intent.action.MAIN` (LAUNCHER)
- **导出状态**: `android:exported="true"`

### 2. 气泡相关 Activity

#### 2.1 AutoMobiumActivity
- **包名**: `com.example.demo.AutoMobiumActivity`
- **布局**: `automobium_main.xml`
- **功能**: 自动化测试演示页面，显示 Toast 提示
- **导出状态**: 未导出
- **从MainActivity访问**: 通过 R.id.bubble 按钮

#### 2.2 MainBubbleActivity
- **包名**: `com.example.demo.bubble.MainBubbleActivity`
- **布局**: `main.xml`
- **功能**: 气泡弹窗主页面，演示自定义弹窗效果
- **导出状态**: `android:exported="false"`
- **特性**: 
  - 自定义坐标弹窗
  - 通知栏高度计算
  - 支持多种弹窗样式

**子导航**:
```
MainBubbleActivity
├─ HappyBubbleActivity              [R.id.happyBubble]
├─ DialogFragmentApiUseDemoActivity [R.id.dialogFragment]
└─ Custom Dialog                    [R.id.dialog] (内联弹窗)
```

#### 2.3 HappyBubbleActivity
- **包名**: `com.example.demo.bubble.HappyBubbleActivity`
- **功能**: HappyBubble 库使用演示

#### 2.4 TestDialogActivity
- **包名**: `com.example.demo.bubble.TestDialogActivity`
- **功能**: Dialog 测试页面

#### 2.5 SetClickedViewTestActivity
- **包名**: `com.example.demo.bubble.SetClickedViewTestActivity`
- **功能**: 点击视图测试

#### 2.6 DialogFragmentApiUseDemoActivity
- **包名**: `com.example.demo.bubble.DialogFragmentApiUseDemoActivity`
- **功能**: DialogFragment API 使用演示

### 3. 引导页相关 Activity

#### 3.1 GuideActivity
- **包名**: `com.example.demo.guide.GuideActivity`
- **布局**: `guide_main.xml`
- **功能**: 新手引导功能演示
- **特性**:
  - 多页引导模式
  - 高亮区域显示
  - 自定义动画效果
  - 支持引导页切换监听

**子导航**:
```
GuideActivity
├─ GridViewActivity      [R.id.tv]
└─ TestFragmentActivity  [R.id.btn]
```

#### 3.2 FirstActivity
- **包名**: `com.example.demo.guide.FirstActivity`
- **功能**: 引导第一页

#### 3.3 GridViewActivity
- **包名**: `com.example.demo.guide.GridViewActivity`
- **功能**: GridView 演示

#### 3.4 TestFragmentActivity
- **包名**: `com.example.demo.guide.TestFragmentActivity`
- **功能**: Fragment 测试页面

#### 3.5 RecyclerViewActivity
- **包名**: `com.example.demo.guide.RecyclerViewActivity`
- **功能**: RecyclerView 演示

#### 3.6 ScrollViewActivity
- **包名**: `com.example.demo.guide.ScrollViewActivity`
- **功能**: ScrollView 演示

### 4. 动画相关 Activity

#### 4.1 ActivityOne
- **包名**: `com.example.demo.anim.ActivityOne`
- **功能**: 动画演示页面 1
- **导出状态**: `android:exported="false"`

#### 4.2 ActivityTwo
- **包名**: `com.example.demo.anim.ActivityTwo`
- **主题**: `@style/Theme.TestDemo.NoActionBar`
- **功能**: 动画演示页面 2
- **导出状态**: `android:exported="false"`

### 5. 编辑器相关 Activity

#### 5.1 EditorActivity
- **包名**: `com.example.demo.editor.EditorActivity`
- **功能**: 自定义编辑器功能演示
- **从MainActivity访问**: 通过 R.id.editor 按钮

---

## 🔧 核心功能模块

### 支付模块
- **类**: `com.example.demo.pay.WxPay`
- **功能**: 微信支付集成
- **调用方式**: 在 MainActivity 中通过 R.id.pay 按钮触发
- **支持**: 微信支付参数配置和调用

### 自定义控件
- **CustomEditText**: `com.example.demo.editor.CustomEditText`
- **ObservableScrollView**: `com.example.demo.guide.ObservableScrollView`
- **NewbieGuide**: 新手引导库 (`com.example.demo.widget.guide.*`)

### Fragment 模块
- **FastScrollFragment**: 快速滚动列表演示
- **AbcFragment**: 测试 Fragment
- **MyDialogFragment**: 对话框 Fragment

---

## 🎯 测试建议

### 1. 主流程测试
```
启动应用 → 进入 MainActivity → 点击各功能入口 → 验证页面跳转
```

### 2. 引导功能测试
```
GuideActivity → 验证多页引导 → 测试高亮区域 → 验证动画效果
```

### 3. 弹窗功能测试
```
MainBubbleActivity → 测试气泡弹窗 → 验证坐标计算 → 测试 DialogFragment
```

### 4. 编辑器测试
```
EditorActivity → 测试自定义输入 → 验证编辑功能
```

---

## 📝 开发注意事项

1. **支付功能**: 微信支付集成需要配置正确的 AppID 和签名
2. **引导页**: 使用 `setLabel()` 区分不同引导层，避免冲突
3. **弹窗坐标**: 需要考虑通知栏高度的影响
4. **动画效果**: ActivityOne 和 ActivityTwo 使用自定义过渡动画
5. **Fragment 管理**: 注意 Fragment 生命周期和状态保存

---

## 🚀 快速开始命令

### 编译项目
```bash
./gradlew assembleDebug
```

### 安装到设备
```bash
./gradlew installDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### 启动应用
```bash
adb shell am start -n com.example.demo/.MainActivity
```

### 启动特定 Activity
```bash
# 启动引导页
adb shell am start -n com.example.demo/.guide.GuideActivity

# 启动气泡页面
adb shell am start -n com.example.demo/.bubble.MainBubbleActivity

# 启动编辑器
adb shell am start -n com.example.demo/.editor.EditorActivity
```

---

## 📊 项目统计

- **总 Activity 数**: 17 个
- **总 Fragment 数**: 3+ 个
- **主要功能模块**: 5 个（引导、气泡、动画、编辑器、支付）
- **自定义 View 组件**: 3+ 个

---

*本文档由 AI 自动生成和分析，如有变更请及时更新*

