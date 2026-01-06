---
name: android-app-usage
description: Android APP 实战操作规范,需要操作Android手机时要符合该规范。处理购物、点餐、订票等交易类任务时，必须先 Read("android-app-usage/references/shopping-apps.md") 获取详细流程。
allowed-tools: mcp_android-devtools-mcp_*, Read, Grep
---

# 任务类型识别（执行前必读）

**在开始任何操作前，请先识别任务类型并按要求读取对应指南：**

##  购物/点餐/订票类任务（强制）

**关键词**: 购物、买东西、点外卖、订餐、订票、下单、支付、购买、加购物车

**操作**: **立即执行** `Read("android-app-usage/references/shopping-apps.md")`

**原因**: 交易类任务有特殊的操作流程（购物车、规格选择、支付等），本文档无法涵盖所有细节

---

# Android APP 实战操作规范

## 核心原则

### 1. 截图理解为先

**每次操作前后都必须截图确认**：
- 操作前：了解页面状态，确认加载完成
- 操作后：验证结果，确认页面变化

### 2. 操作降级策略

**优先级**: text → resourceId → hierarchyId

- **优先**: text 文字点击（最简单、准确）
- **其次**: resourceId（需先 get_ui_elements）
- **最后**: hierarchyId（完整路径，Token消耗大）

### 3. 验证导向

**操作后必须确认结果**：
- 点击后截图验证页面跳转
- 输入后截图验证文字显示
- 滑动后截图验证内容变化

---

## 标准操作流程

### 1. 查看当前页面

```
screenshot()
```

**目的**：了解页面情况，确认加载完成，识别目标元素位置

### 2. 优先文字点击

```
tap(text="按钮文字")
```

- **主要操作方式**，适用于有明确文字的元素
- 准确性高，Token占用小
- 关闭弹窗或返回时，优先用 `press_key(key="back")` 而非文字点击
- **注意**：截图中的文字可能在图片内，无法通过text定位

### 3. 文字失败 → resourceId

```
get_ui_elements(onlyClickable=true)
tap(resourceId="xxx")
```

- **默认不传 `includeHierarchyId`**，减少Token消耗
- **禁止猜测resourceId**，必须先获取元素列表

### 4. resourceId失败 → hierarchyId

```
get_ui_elements(onlyClickable=true, includeHierarchyId=true)
tap(hierarchyId="完整路径")
```

- **仅当text和resourceId都无法精确定位时使用**
- **必须使用完整hierarchyId路径**（从根节点开始）
- **Token消耗大**，需谨慎使用

### 5. 操作验证

```
# 点击后
screenshot()  # 确认页面变化

# 失败处理
press_key(key="back")
screenshot()  # 确认恢复
```

---

## 元素定位详解

### resourceId使用

**获取步骤**：
1. `screenshot()` 了解页面
2. `get_ui_elements(onlyClickable=true)` 获取元素
3. 找到目标元素的 resourceId
4. `tap(resourceId="xxx")` 点击

**注意**: 
- **禁止猜测**，必须先get_ui_elements
- resourceId可能不唯一，如不唯一则使用hierarchyId

### hierarchyId完整指南

**特点**：
- **精确定位**，不会混淆
- 适用于动态列表项、无text/resourceId的元素
- **Token消耗大**，需 `includeHierarchyId=true`

**正确用法**：
```
# ✅ 完整路径
tap(hierarchyId="FrameLayout[0]/LinearLayout[0]/FrameLayout[0]/content/ViewGroup[0]/RecyclerView[0]/ViewGroup[3]/TextView[2]")

# ❌ 缩略形式（会匹配错误元素）
tap(hierarchyId="ViewGroup[3]/TextView[2]")
```

**为什么必须完整**：只有完整路径能保证唯一性，不同元素可能有相同的后缀路径

---

## 浮窗处理

### 识别

截图后发现页面被遮挡（直播浮窗、广告弹窗、优惠券弹窗）

### 关闭策略

**1. 优先评估是否需要关闭**：
- 如果不影响目标元素点击，可不关闭
- **警告**：很多浮窗与页面绑定，`press_key(key="back")` 会退出整个页面！

**2. 确需关闭时**：
- 优先从截图识别关闭按钮文字
- 无明显文字时，`get_ui_elements` 查找 text/resourceId 包含 close/dismiss 的元素
- 通过 hierarchyId 精确定位关闭按钮

**重要**：尽量不用 `press_key(key="back")` 关闭浮窗，会丢失页面状态

---

## 输入框操作

### 快速提交

```
# 常规方式
input_text("搜索内容")
tap(text="搜索")

# 快速方式（推荐）
input_text("搜索内容")
press_key(key="enter")  # 直接回车
```

### 输入验证时机

```
input_text("测试内容")
wait(1)  # 等待1秒，否则截图可能看不到输入
screenshot()
```

**重要**：
- `input_text` 仅输入文字，不会自动提交
- 输入后必须 `press_key(key="enter")` 或点击提交按钮

---

## 按键操作

### 返回键 (Back)

**使用场景**：
- 关闭弹窗（注意：不是浮窗！）
- 返回上一页
- 误操作恢复

**特点**：
- 需要返回上一页时用 back，而不是 tap
- back 是最准确的返回方式

### Home键

**使用场景**：切换应用

**流程**：
```
press_key(key="home")
screenshot()  # 确认回到桌面
launch_app(packageName="...")
```

**注意**：如果在其他APP内，需连续两次 `press_key(key="home")` 确保到桌面首页

---

## 等待时间

不同操作需要不同的等待时间：

- **tap**: 0秒
- **input_text + enter**: 1秒
- **open_url**: waitSeconds=2-3秒
- **launch_app**: 3-5秒
- **swipe**: 0秒
使用Agent自带的 sleep 工具进行等待
---

## 常见问题处理

### 1. 页面加载状态

**空白页面识别**：
- 纯白/纯黑屏幕
- 加载动画或进度条
- 只有骨架屏

**处理**：
```
# 等待加载
wait(2)
screenshot()

# 仍空白
wait(3)
screenshot()

# 多次等待仍空白
press_key(key="back")  # 返回
```

### 2. 安全拦截

**截图拦截**：
- 截图返回大小为0，或显示"无法截屏"
- 原因：敏感页面（支付、个人信息）反爬保护

**处理**：
```
# 尝试 get_ui_elements
get_ui_elements()

# 如能获取元素，通过元素信息操作
# 如无法获取，返回
press_key(key="back")
```

**UI树拦截**：
- `get_ui_elements()` 返回无效信息或空列表
- 原因：特殊防护APP（如微信）、自渲染引擎（如淘宝SKU页）

**处理**：
```
press_key(key="back")  # 立即返回
# 不再继续此链路操作
```

**完全拦截**：
- 截图和UI树都无法获取

**处理**：
```
press_key(key="back")
# 向用户说明：该页面有安全拦截，无法自动化操作
```

### 3. 推广页识别

**特征**：
- 标题包含"推荐"、"精选"、"活动"
- 内容与目标无关
- 有明显广告特征

**处理**：
```
# 识别到推广页
press_key(key="back")  # 立即返回
wait(1)
screenshot()
```

**注意**：不要在推广页继续操作，立即返回重新规划

### 4. 弹窗干扰

**权限请求弹窗**：
```
tap(text="允许")  # 保障后续流程
```

**广告弹窗**：
```
# 优先查找"跳过"文字
tap(text="跳过")

# 无文字时查找关闭按钮
get_ui_elements()
tap(resourceId="close" 或 contentDesc="关闭")
```

### 5. 误入其他页面

**识别**：截图显示与预期完全不同的页面（推广页、活动页等）

**处理**：
```
press_key(key="back")  # 快速返回
screenshot()
# 重新评估定位策略
```

---

## 关键原则总结

1. **截图优先** - 每次操作前后都截图
2. **文字为主** - tap(text) 是主要方式
3. **逐步降级** - text → resourceId → hierarchyId
4. **禁止猜测** - 使用resourceId/hierarchyId前必须get_ui_elements
5. **输入确认** - input_text后必须press_key(key="enter")或点击提交
6. **验证结果** - 操作后必须确认页面变化
7. **善用按键** - back关闭弹窗（不是浮窗），Home切换应用
8. **知难而退** - 多次失败后向用户说明
9. **页面加载** - 遇到空白页等待后重新截图
10. **安全拦截** - 遇到拦截立即返回
