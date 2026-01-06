#!/bin/bash
#
# Android 快速编译检查脚本
# 用于 AI 生码后检测代码编译错误
#

set -e

PROJECT_ROOT="/Users/leeyu35/工作区/android/TestDemo"
cd "$PROJECT_ROOT"

echo "🔍 开始快速编译检查..."
echo "项目路径: $PROJECT_ROOT"
echo ""

# 记录开始时间
START_TIME=$(date +%s)

# 执行快速编译检查
# 只编译 Java 和 Kotlin 代码，不生成 APK
./gradlew compileDebugJava compileDebugKotlin \
  --daemon \
  --build-cache \
  --parallel \
  -Dorg.gradle.caching=true \
  -Dkotlin.incremental=true \
  --console=plain \
  2>&1 | tee /tmp/android-compile-check.log

EXIT_CODE=${PIPESTATUS[0]}

# 记录结束时间
END_TIME=$(date +%s)
ELAPSED=$((END_TIME - START_TIME))

echo ""
echo "================================"

if [ $EXIT_CODE -eq 0 ]; then
    echo "✅ 编译检查通过！"
    echo "⏱️  耗时: ${ELAPSED} 秒"
    exit 0
else
    echo "❌ 发现编译错误："
    echo "================================"
    
    # 提取错误摘要
    grep -E "\.java:[0-9]+:|\.kt:[0-9]+:" /tmp/android-compile-check.log \
        | head -20 || true
    
    echo "================================"
    echo "完整日志：/tmp/android-compile-check.log"
    echo "⏱️  耗时: ${ELAPSED} 秒"
    exit 1
fi
