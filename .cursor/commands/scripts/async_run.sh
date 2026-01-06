#!/bin/bash

# Android DevTools - 异步构建执行脚本
# 用于在后台执行长时间构建任务,支持状态查询和取消

set -e

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 配置文件路径
CONFIG_FILE=".adevtools/config.json"
TEMP_DIR=".adevtools/temp"

# 获取当前时间戳(ISO格式)
get_timestamp() {
  date -u +"%Y-%m-%dT%H:%M:%SZ"
}

# 生成任务ID: task-日期-时间-PID
generate_task_id() {
  local pid=$1
  local datetime=$(date +"%Y%m%d-%H%M%S")
  echo "task-${datetime}-${pid}"
}

# 查找最新的任务目录
find_latest_task() {
  if [ ! -d "$TEMP_DIR" ]; then
    echo ""
    return
  fi
  local latest=$(find "$TEMP_DIR" -maxdepth 1 -type d -name "task-*" 2>/dev/null | sort -r | head -n 1)
  echo "$latest"
}

# 检查是否有构建正在运行
check_running_build() {
  local task_dir=$(find_latest_task)
  if [ -n "$task_dir" ] && [ -f "$task_dir/build_status.json" ]; then
    # 读取状态信息 - 先把JSON转为一行
    local json_oneline=$(tr -d '\n' < "$task_dir/build_status.json")
    local status=$(echo "$json_oneline" | grep -o '"status"[[:space:]]*:[[:space:]]*"[^"]*"' | sed 's/"status"[[:space:]]*:[[:space:]]*"\([^"]*\)"/\1/')
    if [ "$status" = "running" ]; then
      local pid=$(echo "$json_oneline" | grep -o '"pid"[[:space:]]*:[[:space:]]*[0-9]*' | sed 's/"pid"[[:space:]]*:[[:space:]]*\([0-9]*\)/\1/')
      # 检查进程是否真的在运行
      if ps -p "$pid" > /dev/null 2>&1; then
        return 0  # 有构建在运行
      fi
    fi
  fi
  return 1  # 没有构建在运行
}

# 读取要执行的命令
# 参数1: 可选的脚本路径(如果提供,使用该路径;否则从config.json读取buildCommand)
get_command_to_run() {
  local script_path=$1
  
  if [ -n "$script_path" ]; then
    # 如果提供了脚本路径参数,直接使用
    if [ ! -f "$script_path" ]; then
      echo -e "${RED}错误: 脚本文件不存在: $script_path${NC}" >&2
      exit 1
    fi
    if [ ! -x "$script_path" ]; then
      echo -e "${YELLOW}警告: 脚本没有执行权限,正在添加...${NC}" >&2
      chmod +x "$script_path"
    fi
    echo "$script_path"
    return
  fi
  
  # 否则从config.json读取buildCommand
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

# 格式化时间(秒 -> 人类可读)
format_time() {
  local seconds=$1
  local minutes=$((seconds / 60))
  local remaining_seconds=$((seconds % 60))
  
  if [ $minutes -gt 0 ]; then
    echo "${minutes}分${remaining_seconds}秒"
  else
    echo "${seconds}秒"
  fi
}

# 计算已执行时间
calculate_elapsed() {
  local start_time=$1
  # macOS 的 date 命令需要使用 -u 处理 UTC 时间
  local start_ts=$(TZ=UTC date -j -f "%Y-%m-%dT%H:%M:%SZ" "$start_time" "+%s" 2>/dev/null)
  if [ -z "$start_ts" ] || [ "$start_ts" = "0" ]; then
    # 如果解析失败,尝试使用当前时间
    start_ts=$(date "+%s")
  fi
  local current_ts=$(date "+%s")
  local elapsed=$((current_ts - start_ts))
  echo "$elapsed"
}

# 根据已执行时间给出轮询建议
get_polling_suggestion() {
  local elapsed=$1
  
  if [ $elapsed -lt 120 ]; then
    # 前2分钟
    echo "构建仍在进行 (已执行$(format_time $elapsed)，建议等待20-40秒后再次查询)"
  else
    # 2分钟后
    echo "构建仍在进行 (已执行$(format_time $elapsed)，建议等待5-10秒后再次查询)"
  fi
}

# 启动构建
# 参数1: 可选的脚本路径
start_build() {
  local script_path=$1
  
  # 检查是否已有构建在运行
  if check_running_build; then
    local running_task=$(find_latest_task)
    echo -e "${RED}错误: 已有构建在运行${NC}"
    echo "任务目录: $running_task"
    echo "请等待构建完成或使用 'cancel' 取消"
    exit 1
  fi
  
  # 获取要执行的命令
  local build_cmd=$(get_command_to_run "$script_path")
  
  # 检查命令是否可执行
  local cmd_path=$(echo "$build_cmd" | awk '{print $1}')
  if [ ! -f "$cmd_path" ]; then
    echo -e "${RED}错误: 命令不存在: $cmd_path${NC}"
    exit 1
  fi
  
  if [ ! -x "$cmd_path" ]; then
    echo -e "${YELLOW}警告: 命令没有执行权限,正在添加...${NC}"
    chmod +x "$cmd_path"
  fi
  
  # 创建临时目录
  mkdir -p "$TEMP_DIR"
  
  # 在后台执行构建,获取PID
  (
    cd "$(dirname "$cmd_path")"
    eval "$build_cmd" > /dev/null 2>&1
  ) &
  local build_pid=$!
  
  # 生成任务ID
  local task_id=$(generate_task_id $build_pid)
  local task_dir="$TEMP_DIR/$task_id"
  mkdir -p "$task_dir"
  
  local start_time=$(get_timestamp)
  
  # 创建状态文件
  cat > "$task_dir/build_status.json" <<EOF
{
  "taskId": "$task_id",
  "pid": $build_pid,
  "status": "running",
  "startTime": "$start_time",
  "lastUpdateTime": "$start_time",
  "elapsedTime": 0,
  "exitCode": null,
  "cancelled": false
}
EOF
  
  # 启动后台监控进程
  (
    # 重定向构建输出到日志文件
    tail --pid=$build_pid -f /dev/null 2>/dev/null &
    local tail_pid=$!
    
    # 收集构建输出
    (
      cd "$(dirname "$cmd_path")"
      eval "$build_cmd"
    ) > "$task_dir/build_output.log" 2>&1 &
    local actual_build_pid=$!
    
    # 等待构建完成
    wait $actual_build_pid
    local exit_code=$?
    
    # 更新状态文件
    local end_time=$(get_timestamp)
    local elapsed=$(calculate_elapsed "$start_time")
    
    cat > "$task_dir/build_status.json" <<EOF
{
  "taskId": "$task_id",
  "pid": $build_pid,
  "status": "completed",
  "startTime": "$start_time",
  "lastUpdateTime": "$end_time",
  "elapsedTime": $elapsed,
  "exitCode": $exit_code,
  "cancelled": false
}
EOF
    
    # 清理tail进程
    kill $tail_pid 2>/dev/null || true
  ) &
  
  # 输出启动信息
  echo -e "${GREEN}Status: Build started successfully${NC}"
  echo "Task ID: $task_id"
  echo "PID: $build_pid"
}

# 查询状态
status_query() {
  local task_dir=$(find_latest_task)
  
  if [ -z "$task_dir" ]; then
    echo -e "${YELLOW}未找到构建任务${NC}"
    exit 0
  fi
  
  local status_file="$task_dir/build_status.json"
  local log_file="$task_dir/build_output.log"
  
  if [ ! -f "$status_file" ]; then
    echo -e "${RED}错误: 状态文件不存在${NC}"
    exit 1
  fi
  
  # 读取状态信息 - 先把JSON转为一行
  local json_oneline=$(tr -d '\n' < "$status_file")
  local task_id=$(echo "$json_oneline" | grep -o '"taskId"[[:space:]]*:[[:space:]]*"[^"]*"' | sed 's/"taskId"[[:space:]]*:[[:space:]]*"\([^"]*\)"/\1/')
  local pid=$(echo "$json_oneline" | grep -o '"pid"[[:space:]]*:[[:space:]]*[0-9]*' | sed 's/"pid"[[:space:]]*:[[:space:]]*\([0-9]*\)/\1/')
  local status=$(echo "$json_oneline" | grep -o '"status"[[:space:]]*:[[:space:]]*"[^"]*"' | sed 's/"status"[[:space:]]*:[[:space:]]*"\([^"]*\)"/\1/')
  local start_time=$(echo "$json_oneline" | grep -o '"startTime"[[:space:]]*:[[:space:]]*"[^"]*"' | sed 's/"startTime"[[:space:]]*:[[:space:]]*"\([^"]*\)"/\1/')
  local cancelled=$(echo "$json_oneline" | grep -o '"cancelled"[[:space:]]*:[[:space:]]*[a-z]*' | sed 's/"cancelled"[[:space:]]*:[[:space:]]*\([a-z]*\)/\1/')
  
  # 检查进程状态
  local is_running=false
  if ps -p "$pid" > /dev/null 2>&1; then
    is_running=true
  fi
  
  # 计算已执行时间
  local elapsed=$(calculate_elapsed "$start_time")
  
  # 如果进程不在运行但状态还是running,更新状态
  if [ "$is_running" = false ] && [ "$status" = "running" ]; then
    status="completed"
    local end_time=$(get_timestamp)
    # 更新状态文件(简化版)
    sed -i '' "s/\"status\"[[:space:]]*:[[:space:]]*\"running\"/\"status\": \"completed\"/g" "$status_file"
    sed -i '' "s/\"lastUpdateTime\"[[:space:]]*:[[:space:]]*\"[^\"]*\"/\"lastUpdateTime\": \"$end_time\"/g" "$status_file"
  fi
  
  # 输出状态信息
  echo "Status: $status"
  echo "Elapsed: ${elapsed}s ($(format_time $elapsed))"
  
  if [ "$status" = "running" ]; then
    echo "PID: $pid"
  fi
  
  if [ "$cancelled" = "true" ]; then
    echo "Note: Build was cancelled by user"
  fi
  
  echo ""
  
  # 输出日志
  if [ -f "$log_file" ]; then
    local log_lines=$(wc -l < "$log_file" | tr -d ' ')
    if [ "$log_lines" -gt 0 ]; then
      if [ "$status" = "running" ]; then
        echo "Recent logs (last 50 lines):"
      else
        echo "Final logs (last 50 lines):"
      fi
      echo "================================================================================"
      tail -n 50 "$log_file"
      echo "================================================================================"
    fi
  fi
  
  # 给出轮询建议
  if [ "$status" = "running" ]; then
    echo ""
    echo "Suggestion: $(get_polling_suggestion $elapsed)"
  fi
}

# 取消构建
cancel_build() {
  local task_dir=$(find_latest_task)
  
  if [ -z "$task_dir" ]; then
    echo -e "${YELLOW}未找到构建任务${NC}"
    exit 0
  fi
  
  local status_file="$task_dir/build_status.json"
  
  if [ ! -f "$status_file" ]; then
    echo -e "${RED}错误: 状态文件不存在${NC}"
    exit 1
  fi
  
  # 读取状态信息 - 先把JSON转为一行
  local json_oneline=$(tr -d '\n' < "$status_file")
  local task_id=$(echo "$json_oneline" | grep -o '"taskId"[[:space:]]*:[[:space:]]*"[^"]*"' | sed 's/"taskId"[[:space:]]*:[[:space:]]*"\([^"]*\)"/\1/')
  local pid=$(echo "$json_oneline" | grep -o '"pid"[[:space:]]*:[[:space:]]*[0-9]*' | sed 's/"pid"[[:space:]]*:[[:space:]]*\([0-9]*\)/\1/')
  local status=$(echo "$json_oneline" | grep -o '"status"[[:space:]]*:[[:space:]]*"[^"]*"' | sed 's/"status"[[:space:]]*:[[:space:]]*"\([^"]*\)"/\1/')
  
  if [ "$status" != "running" ]; then
    echo -e "${YELLOW}构建已完成,无需取消${NC}"
    exit 0
  fi
  
  # 检查进程是否存在
  if ! ps -p "$pid" > /dev/null 2>&1; then
    echo -e "${YELLOW}构建进程已结束${NC}"
    exit 0
  fi
  
  # 终止进程
  echo -e "${YELLOW}正在取消构建...${NC}"
  kill -TERM "$pid" 2>/dev/null || true
  sleep 1
  
  # 如果还在运行,强制终止
  if ps -p "$pid" > /dev/null 2>&1; then
    kill -KILL "$pid" 2>/dev/null || true
  fi
  
  # 更新状态
  sed -i '' 's/"status"[[:space:]]*:[[:space:]]*"running"/"status": "completed"/g' "$status_file"
  sed -i '' 's/"cancelled"[[:space:]]*:[[:space:]]*false/"cancelled": true/g' "$status_file"
  
  echo -e "${GREEN}Build cancelled successfully${NC}"
  echo "Task ID: $task_id"
}

# 清理任务目录
clean_tasks() {
  if [ ! -d "$TEMP_DIR" ]; then
    echo "没有需要清理的任务"
    exit 0
  fi
  
  local task_count=$(find "$TEMP_DIR" -maxdepth 1 -type d -name "task-*" 2>/dev/null | wc -l | tr -d ' ')
  
  if [ "$task_count" -eq 0 ]; then
    echo "没有需要清理的任务"
    exit 0
  fi
  
  echo -e "${YELLOW}正在清理 $task_count 个任务...${NC}"
  rm -rf "$TEMP_DIR"/task-*
  echo -e "${GREEN}清理完成${NC}"
}

# 显示帮助
show_help() {
  echo "Android DevTools - 异步执行脚本"
  echo ""
  echo "用法: $0 <command> [script_path]"
  echo ""
  echo "命令:"
  echo "  start [script_path]  - 启动异步执行"
  echo "                         如果提供 script_path,执行指定脚本"
  echo "                         否则从 config.json 读取 buildCommand"
  echo "  status               - 查询执行状态"
  echo "  cancel               - 取消执行"
  echo "  clean                - 清理任务目录"
  echo "  help                 - 显示帮助信息"
  echo ""
  echo "示例:"
  echo "  $0 start                        # 执行 config.json 中的 buildCommand"
  echo "  $0 start ./my_script.sh         # 执行指定脚本"
  echo "  $0 status                       # 查询状态"
  echo "  $0 cancel                       # 取消执行"
  echo "  $0 clean                        # 清理任务"
}

# 主函数
main() {
  local command=$1
  local script_path=$2
  
  case "$command" in
    start)
      start_build "$script_path"
      ;;
    status)
      status_query
      ;;
    cancel)
      cancel_build
      ;;
    clean)
      clean_tasks
      ;;
    help|--help|-h)
      show_help
      ;;
    *)
      echo -e "${RED}错误: 未知命令 '$command'${NC}"
      echo ""
      show_help
      exit 1
      ;;
  esac
}

main "$@"
