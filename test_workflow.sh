#!/bin/bash

# TDD Test: Verify workflow.txt Environment Preparation Section
# Acceptance Criteria:
# - Contains '## 1. 环境准备'
# - Contains JDK/Node/MySQL/Maven version requirements
# - Contains database initialization SQL commands
# - Contains mvn/npm startup commands

set -e

WORKFLOW_FILE="workflow.txt"
PASS_COUNT=0
FAIL_COUNT=0

pass() { echo "  PASS: $1"; PASS_COUNT=$((PASS_COUNT + 1)); }
fail() { echo "  FAIL: $1"; FAIL_COUNT=$((FAIL_COUNT + 1)); }

echo "=== TDD Test: workflow.txt 环境准备章节 ==="
echo ""

# --- Section existence ---
echo "[Section]"
grep -q "## 1. 环境准备" "$WORKFLOW_FILE" && pass "包含 '## 1. 环境准备' 章节标题" || fail "缺少 '## 1. 环境准备' 章节标题"

# --- Version requirements ---
echo "[Version Requirements]"
grep -qi "jdk.*1.8\|java.*1.8" "$WORKFLOW_FILE" && pass "JDK 1.8+ 版本要求" || fail "缺少 JDK 版本要求"
grep -qi "node.*14\|node.js.*14" "$WORKFLOW_FILE" && pass "Node.js 14+ 版本要求" || fail "缺少 Node.js 版本要求"
grep -qi "mysql.*5.7\|mysql.*5.7+" "$WORKFLOW_FILE" && pass "MySQL 5.7+ 版本要求" || fail "缺少 MySQL 版本要求"
grep -qi "maven.*3.6\|maven.*3.6+" "$WORKFLOW_FILE" && pass "Maven 3.6+ 版本要求" || fail "缺少 Maven 版本要求"

# --- Database initialization ---
echo "[Database Initialization]"
grep -qi "create.*database.*library\|mysql.*-e.*create" "$WORKFLOW_FILE" && pass "数据库创建命令" || fail "缺少数据库创建命令"
grep -qi "mysql.*<.*\.sql\|import.*sql" "$WORKFLOW_FILE" && pass "SQL 导入命令" || fail "缺少 SQL 导入命令"

# --- Startup commands ---
echo "[Startup Commands]"
grep -qi "mvn.*spring-boot:run" "$WORKFLOW_FILE" && pass "后端启动命令 (mvn)" || fail "缺少后端启动命令"
grep -qi "npm.*run.*dev\|npm.*install" "$WORKFLOW_FILE" && pass "前端启动命令 (npm)" || fail "缺少前端启动命令"

# --- Summary ---
echo ""
echo "=== Results: $PASS_COUNT passed, $FAIL_COUNT failed ==="
[ $FAIL_COUNT -eq 0 ] && echo "All tests PASSED!" || exit 1
