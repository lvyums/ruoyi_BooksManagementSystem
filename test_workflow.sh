#!/bin/bash
# TDD Test: Verify workflow.txt contains required development workflow section
# Task T3: 添加开发工作流章节

PASS=0
FAIL=0
WORKFLOW_FILE="workflow.txt"

echo "=== TDD Test: workflow.txt 开发工作流章节验证 ==="
echo ""

# Test 1: File exists
if [ -f "$WORKFLOW_FILE" ]; then
    echo "[PASS] Test 1: workflow.txt 文件存在"
    PASS=$((PASS + 1))
else
    echo "[FAIL] Test 1: workflow.txt 文件不存在"
    FAIL=$((FAIL + 1))
    echo ""
    echo "Status: failed"
    echo "Summary: workflow.txt file does not exist"
    exit 1
fi

# Test 2: Contains '## 2. 开发工作流' header
if grep -q "## 2\. 开发工作流" "$WORKFLOW_FILE"; then
    echo "[PASS] Test 2: 包含 '## 2. 开发工作流' 章节标题"
    PASS=$((PASS + 1))
else
    echo "[FAIL] Test 2: 缺少 '## 2. 开发工作流' 章节标题"
    FAIL=$((FAIL + 1))
fi

# Test 3: Contains 需求分析 step
if grep -q "需求分析" "$WORKFLOW_FILE"; then
    echo "[PASS] Test 3: 包含 '需求分析' 步骤"
    PASS=$((PASS + 1))
else
    echo "[FAIL] Test 3: 缺少 '需求分析' 步骤"
    FAIL=$((FAIL + 1))
fi

# Test 4: Contains 数据库设计 step
if grep -q "数据库设计" "$WORKFLOW_FILE"; then
    echo "[PASS] Test 4: 包含 '数据库设计' 步骤"
    PASS=$((PASS + 1))
else
    echo "[FAIL] Test 4: 缺少 '数据库设计' 步骤"
    FAIL=$((FAIL + 1))
fi

# Test 5: Contains 后端开发 step
if grep -q "后端开发" "$WORKFLOW_FILE"; then
    echo "[PASS] Test 5: 包含 '后端开发' 步骤"
    PASS=$((PASS + 1))
else
    echo "[FAIL] Test 5: 缺少 '后端开发' 步骤"
    FAIL=$((FAIL + 1))
fi

# Test 6: Contains 前端开发 step
if grep -q "前端开发" "$WORKFLOW_FILE"; then
    echo "[PASS] Test 6: 包含 '前端开发' 步骤"
    PASS=$((PASS + 1))
else
    echo "[FAIL] Test 6: 缺少 '前端开发' 步骤"
    FAIL=$((FAIL + 1))
fi

# Test 7: Contains 测试验证 step
if grep -q "测试验证" "$WORKFLOW_FILE"; then
    echo "[PASS] Test 7: 包含 '测试验证' 步骤"
    PASS=$((PASS + 1))
else
    echo "[FAIL] Test 7: 缺少 '测试验证' 步骤"
    FAIL=$((FAIL + 1))
fi

# Test 8: Contains ASCII flow chart (box drawing characters)
if grep -q "┌\|┐\|└\|┘\|↓" "$WORKFLOW_FILE"; then
    echo "[PASS] Test 8: 包含 ASCII 流程图（使用了框线字符）"
    PASS=$((PASS + 1))
else
    echo "[FAIL] Test 8: 缺少 ASCII 流程图"
    FAIL=$((FAIL + 1))
fi

# Summary
echo ""
echo "=== 测试结果 ==="
echo "通过: $PASS / 8"
echo "失败: $FAIL / 8"

if [ $FAIL -eq 0 ]; then
    echo ""
    echo "Status: success"
    echo "Summary: 所有测试通过，workflow.txt 包含完整的开发工作流章节"
    exit 0
else
    echo ""
    echo "Status: failed"
    echo "Summary: 部分测试失败"
    exit 1
fi
