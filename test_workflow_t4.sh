#!/bin/bash
# TDD Test: Verify workflow.txt contains backend development standards section
# Task T4: 添加后端开发规范章节

PASS=0
FAIL=0
WORKFLOW_FILE="workflow.txt"

echo "=== TDD Test: workflow.txt 后端开发规范章节验证 ==="
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

# Test 2: Contains '## 3. 后端开发规范' header
if grep -q "## 3\. 后端开发规范" "$WORKFLOW_FILE"; then
    echo "[PASS] Test 2: 包含 '## 3. 后端开发规范' 章节标题"
    PASS=$((PASS + 1))
else
    echo "[FAIL] Test 2: 缺少 '## 3. 后端开发规范' 章节标题"
    FAIL=$((FAIL + 1))
fi

# Test 3: Contains ruoyi-system package structure
if grep -q "ruoyi-system/" "$WORKFLOW_FILE"; then
    echo "[PASS] Test 3: 包含 ruoyi-system 包结构"
    PASS=$((PASS + 1))
else
    echo "[FAIL] Test 3: 缺少 ruoyi-system 包结构"
    FAIL=$((FAIL + 1))
fi

# Test 4: Contains Reader实体类示例
if grep -q "public class Reader" "$WORKFLOW_FILE"; then
    echo "[PASS] Test 4: 包含 Reader 实体类示例"
    PASS=$((PASS + 1))
else
    echo "[FAIL] Test 4: 缺少 Reader 实体类示例"
    FAIL=$((FAIL + 1))
fi

# Test 5: Contains IReaderService接口
if grep -q "public interface IReaderService" "$WORKFLOW_FILE"; then
    echo "[PASS] Test 5: 包含 IReaderService 接口"
    PASS=$((PASS + 1))
else
    echo "[FAIL] Test 5: 缺少 IReaderService 接口"
    FAIL=$((FAIL + 1))
fi

# Test 6: Contains ReaderController示例
if grep -q "public class ReaderController" "$WORKFLOW_FILE"; then
    echo "[PASS] Test 6: 包含 ReaderController 示例"
    PASS=$((PASS + 1))
else
    echo "[FAIL] Test 6: 缺少 ReaderController 示例"
    FAIL=$((FAIL + 1))
fi

# Summary
echo ""
echo "=== 测试结果 ==="
echo "通过: $PASS / 6"
echo "失败: $FAIL / 6"

if [ $FAIL -eq 0 ]; then
    echo ""
    echo "Status: success"
    echo "Summary: 所有测试通过，workflow.txt 包含完整的后端开发规范章节"
    exit 0
else
    echo ""
    echo "Status: failed"
    echo "Summary: 部分测试失败"
    exit 1
fi