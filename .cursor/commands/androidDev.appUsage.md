---
description: Android DevTools MCP 工具使用规范和最佳实践
---

# MCP 工具使用规范

本文档已迁移为 Cursor Skills 系统，提供更好的 AI 自动激活体验。

## Skills 系统说明

Skills 会在以下情况自动激活：
- 用户提到 Android 操作（点击、输入、滚动等）
- 用户提到购物流程（购物车、规格选择、结算等）
- 执行 `/androidDev.*` 命令（自动加载）
- 手动调用 Cursor Skills 面板

## Skill: android-app-usage

**位置**：`.cursor/skills/android-app-usage/SKILL.md`

**核心内容**：
1. 核心原则
   - 截图理解为先
   - 操作降级策略（text → resourceId → hierarchyId）
   - 验证导向

2. 标准操作流程
   - 查看页面状态
   - 尝试文字点击
   - resourceId 定位
   - hierarchyId 定位
   - 操作验证

3. 实战技巧速查
   - 快速定位技巧
   - 浮窗处理
   - 输入框操作

**参考文档**（详细指导）：
- `references/operation-strategy.md` - 操作降级策略详解
- `references/element-locating.md` - 元素定位技巧详解
- `references/shopping-apps.md` - 购物类 APP 操作指南
- `references/troubleshooting.md` - 常见问题处理指南

## 快速参考

### 核心原则

1. **截图优先**：每次操作前后都截图确认
2. **文字为主**：tap(text) 是主要操作方式
3. **逐步降级**：text → resourceId → hierarchyId
4. **验证结果**：操作后必须确认页面变化

### 元素定位优先级

```
1. text（文字点击）- 最优先
   ↓ 失败
2. resourceId（标识符定位）- 需先 get_ui_elements
   ↓ 不唯一
3. hierarchyId（层级定位）- 必须完整路径
   ↓ 失败
4. 中断操作
```

### 常用操作序列

**查看页面**：
```
screenshot()
```

**获取元素**：
```
get_ui_elements(onlyClickable=true)
```

**点击元素**：
```
tap(text="..." 或 resourceId="...")
```

**输入文本**：
```
input_text("...")
press_key(key="enter")
```

**返回上一页**：
```
press_key(key="back")
```

**等待响应**：
- tap 后等待 1-2 秒
- input_text 后等待 1-2 秒
- open_url 后等待 2-3 秒
- launch_app 后等待 2-3 秒
- swipe 后等待 1 秒

### 重要提醒

1. **禁止猜测 resourceId**
   - 必须先调用 `get_ui_elements()` 获取
   - 通过 center 坐标结合截图确认位置

2. **hierarchyId 必须完整路径**
   - 正确：`FrameLayout[0]/LinearLayout[0]/...`
   - 错误：使用缩略形式（只取后几层）

3. **浮窗不要用 back 关闭**
   - 浮窗是页面的一部分
   - 使用 back 会退出整个页面
   - 应该点击关闭按钮

4. **输入后必须提交**
   - `input_text()` 只输入文字，不自动提交
   - 必须 `press_key(key="enter")` 或点击提交按钮

## 使用建议

1. **日常操作**：直接使用，Skills 会自动激活
2. **复杂流程**：使用 `/androidDev.*` Commands，会自动加载 Skills
3. **遇到困难**：查看 `references/` 下的详细文档
4. **购物场景**：重点参考 `references/shopping-apps.md`

## 详细内容

所有详细内容请查看：`.cursor/skills/android-app-usage/SKILL.md`
