#!/bin/bash

# Android DevTools - 异步构建脚本
# 读取 config.json 中的 buildCommand 并使用 async_run.sh 异步执行

set -e

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# 配置文件路径
CONFIG_FILE=".adevtools/config.json"

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ASYNC_RUN_SCRIPT="$SCRIPT_DIR/async_run.sh"

# 读取构建命令
get_build_command() {
  if [ ! -f "$CONFIG_FILE" ]; then
    echo -e "${RED}错误: 配置文件不存在: $CONFIG_FILE${NC}" >&2
    exit 1
  fi
  
  # 使用更宽松的正则,支持多行JSON和前导空格
  local build_cmd=$(grep -o '"buildCommand"[[:space:]]*:[[:space:]]*"[^"]*"' "$CONFIG_FILE" | sed 's/"buildCommand"[[:space:]]*:[[:space:]]*"\([^"]*\)"/\1/')
  if [ -z "$build_cmd" ]; then
    echo -e "${RED}错误: buildCommand 未配置在 $CONFIG_FILE${NC}" >&2
    exit 1
  fi
  
  echo "$build_cmd"
}

# 主函数
main() {
  local command=$1
  
  # 检查 async_run.sh 是否存在
  if [ ! -f "$ASYNC_RUN_SCRIPT" ]; then
    echo -e "${RED}错误: 找不到 async_run.sh: $ASYNC_RUN_SCRIPT${NC}" >&2
    exit 1
  fi
  
  case "$command" in
    start)
      # 读取 buildCommand
      local build_cmd=$(get_build_command)
      # 调用 async_run.sh start 并传入脚本路径
      "$ASYNC_RUN_SCRIPT" start "$build_cmd"
      ;;
    status|cancel|clean)
      # 其他命令直接转发给 async_run.sh
      "$ASYNC_RUN_SCRIPT" "$command"
      ;;
    help|--help|-h|"")
      echo "Android DevTools - 异步构建脚本"
      echo ""
      echo "用法: $0 <command>"
      echo ""
      echo "命令:"
      echo "  start    - 读取 config.json 中的 buildCommand 并异步执行"
      echo "  status   - 查询构建状态"
      echo "  cancel   - 取消构建"
      echo "  clean    - 清理任务目录"
      echo "  help     - 显示帮助信息"
      echo ""
      echo "示例:"
      echo "  $0 start         # 启动构建"
      echo "  $0 status        # 查询状态"
      echo "  $0 cancel        # 取消构建"
      echo "  $0 clean         # 清理任务"
      ;;
    *)
      echo -e "${RED}错误: 未知命令 '$command'${NC}" >&2
      echo ""
      main help
      exit 1
      ;;
  esac
}

main "$@"

