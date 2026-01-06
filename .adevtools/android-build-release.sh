#!/bin/bash
#
# Android Release 编译脚本
# 生成正式的 Release APK
#

set -e

PROJECT_ROOT="/Users/leeyu35/工作区/android/TestDemo"
cd "$PROJECT_ROOT"

echo "📦 开始 Release 编译..."
echo "项目路径: $PROJECT_ROOT"
echo ""

# 执行 Release 编译
./gradlew assembleRelease

EXIT_CODE=$?

echo ""
if [ $EXIT_CODE -eq 0 ]; then
    echo "✅ Release 编译成功！"
    
    # 查找生成的 APK
    find "$PROJECT_ROOT" -name "*-release.apk" -type f | head -5
else
    echo "❌ Release 编译失败"
    exit 1
fi
