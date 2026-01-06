---
description: 深度分析 Android 项目路由和入口组件
---

# /androidDev.init

## 用户输入

$ARGUMENTS

---

## 概述

你将深度分析当前 Android 项目的路由和入口组件，生成 `routes.md` 文档，并验证 URL 可用性。

在整个过程中，你可以使用 `/androidDev.appUsage` 命令来查看 MCP 工具的使用规范和最佳实践。

---

## 前置检查

读取 `.adevtools/config.json` 文件：

**如果不存在**：
- 运行 `adk init --ai <agent名称>` 初始化项目
- agent 是根据上下文和你的环境来分析得出

**如果存在**：
- 验证 `android` 字段存在（项目信息）
- 验证 `packageName` 字段不为空
- 读取 `buildCommand` 字段
- 继续路由分析

---

## 初始化流程

### 阶段1：分析 AndroidManifest.xml

从 `android.project.rootDir` 读取所有 AndroidManifest.xml 文件。

**收集信息**：
- 所有带 intent-filter 的 Activity
- **只有 `android:exported="true"` 的 Activity 可作为 URL 入口**
- 提取 data 标签中的 scheme、host、pathPattern
- 组合成完整的 URL 格式
- 收集所有 Activity（包括 exported=false）的信息

**输出示例**：
- 搜索结果页: `https://m.example.com/search`（exported=true）
- 详情页: `com.example.DetailActivity`（exported=false，Debug 入口候选）

---

### 阶段2：生成初始配置文件

#### routes.md 格式

分为两个或三个部分：可用路由、Debug 入口（如果支持增量编译）、参数说明

```markdown
# 页面路由

## 可用路由

| 页面 | URL | 状态 |
|------|-----|------|
| 搜索结果页 | `https://m.example.com/search?keywords=phone` | ⚠️ 待验证 |

### 参数说明
- `keywords`: 搜索关键词 (required)
- `category`: 分类ID (optional)

---



## 使用说明

1. **已验证的页面**可直接使用
2. **待配置的页面**需要将 `TODO_xxx` 替换为实际值
3. **需业务数据的页面**需要从正常业务流程中获取参数
4. 配置完成后将状态改为 `✅ 已验证`
```

**注意：** Debug 入口部分仅在项目支持增量编译（`android.hasIncrementalBuilder` 为 true）时才包含。

---

### 阶段3：详细分析入口组件

在生成文档和配置文件后，开始正式详细的分析入口组件代码（这一步是比较耗时的）。

#### 3.1 生成分析任务清单

根据阶段1收集的 Activity 信息，生成 `.adevtools/init_todo.md`：

```markdown
# Init 分析任务清单

## 入口组件代码分析

- [ ] SearchActivity
  - 文件: module-search/src/main/java/com/example/SearchActivity.java
  - Manifest: exported=true, URL=https://m.example.com/search
  - [ ] 分析参数解析逻辑
  - [ ] 更新 routes.md

- [ ] DetailActivity
  - 文件: app/src/main/java/com/example/DetailActivity.java
  - Manifest: exported=false, 无 URL (Debug 入口候选)
  - [ ] 分析参数需求
  - [ ] 更新 routes.md
```

#### 3.2 逐个分析入口组件

按照 `init_todo.md` 中的清单逐个分析：

1. 读取目标 Activity 源文件
2. 分析 onCreate 和 onNewIntent 方法
3. 提取参数解析逻辑（如 Uri.getQueryParameter、getIntent().getXXXExtra()）
4. 确定支持的参数、类型及其含义
5. 更新 routes.md 相应条目（参数说明、示例URL）
6. 在 init_todo.md 中勾选对应任务 `[x]`

#### 3.3 更新 routes.md
根据分析到的入口更新文档内容：
- 为所有 exported=false 或无 URL 的页面添加 Debug 入口配置
- 使用 `adevtools://launch?activity=类名&参数` 格式
- 区分三类：无需参数、需要配置参数、需要业务数据
- 使用 android dev mcp 工具来验证各域名是否可以正确的打开，并更新 routes.md 中的验证状态

#### 3.4 完成分析

所有任务完成后删除 `.adevtools/init_todo.md` 文件。

---

### 阶段4：总结并与用户确认

**一次性**向用户展示：

1. **生成的配置文件**
   - .adevtools/routes.md

2. **配置摘要**
   - APP 包名：[packageName]
   - 编译命令：[buildCommand]
   - 路由数量
   - 页面URL：请确认各页面URL是否可以正确打开，如有问题请修改为实际可用的URL
   - Debug 入口状态

3. **需要用户确认或补充的问题**（一次性列出所有问题）
   - 问题1：如果有动态数据类型，询问 MOCK 方式和数据示例
   - 问题2：其他无法自动分析的内容

4. **后续操作选项**
   - 选项1：回复让 AI 修改配置内容
   - 选项2：自己手动修改配置文件
   - 选项3：继续完成 init_todo.md 中的任务，让 AI 验证接下来的 URL，保障 URL 可以真正一键直达
   - 选项4：确认无误，开始使用

---

## 更新流程

当 `.adevtools/routes.md` 已存在时，自动进入更新流程。

### 阶段1：检测变更

1. 读取配置中的 gitCommitId
2. 执行 `git diff <gitCommitId>` 获取变更文件
3. 分析变更文件：
   - Manifest 变更 → 可能有新路由
   - Activity 变更 → 可能有参数变化

### 阶段2：更新 routes.md

根据检测到的变更更新 `.adevtools/routes.md`：

1. **新增 Activity 路由**
   - 为新增的 exported=true 的 Activity 添加到"可用路由"部分
   - 为新增的 exported=false 或无 URL 的 Activity 添加到"Debug 入口"部分

2. **更新已有路由**
   - 如果 Activity 参数逻辑发生变化，更新对应的参数说明
   - 如果 URL Scheme 发生变化，更新对应的 URL

3. **更新 config.json 中的 gitCommitId 和 lastUpdateTime**

### 阶段3：展示更新内容

向用户展示：
- 检测到的变更
- 自动更新的内容
- 需要确认的点

用户可选择修改或确认。

---

## 注意事项

1. 不要假设任何项目特定信息，所有信息都通过分析获得
2. 所有确认问题一次性列出，等待用户统一回复
3. 分析失败时明确告知用户，不要猜测
4. 生成的配置要通用，不绑定特定项目结构
5. 配置完成后，继续按照 init_todo.md 的任务逐一验证 URL 的可用性
