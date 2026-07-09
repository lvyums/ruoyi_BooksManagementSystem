#!/bin/bash
# Test: Verify workflow.txt contains required project overview sections

WORKFLOW_FILE="E:/Code/code.java/RuoYi-Vue-master/workflow.txt"
ERRORS=0

echo "Testing workflow.txt content..."

# Test 1: File exists
if [ ! -f "$WORKFLOW_FILE" ]; then
    echo "FAIL: workflow.txt does not exist"
    ERRORS=$((ERRORS + 1))
else
    echo "PASS: workflow.txt exists"
fi

# Test 2: Contains project title
if grep -q "# RuoYi-Vue 图书管理系统 开发工作流" "$WORKFLOW_FILE"; then
    echo "PASS: Contains project title"
else
    echo "FAIL: Missing project title '# RuoYi-Vue 图书管理系统 开发工作流'"
    ERRORS=$((ERRORS + 1))
fi

# Test 3: Contains project overview section
if grep -q "## 项目概述" "$WORKFLOW_FILE"; then
    echo "PASS: Contains '## 项目概述' section"
else
    echo "FAIL: Missing '## 项目概述' section"
    ERRORS=$((ERRORS + 1))
fi

# Test 4: Overview has content (not empty)
OVERVIEW_CONTENT=$(sed -n '/## 项目概述/,/^##/p' "$WORKFLOW_FILE" | head -n 10)
if echo "$OVERVIEW_CONTENT" | grep -q "图书管理"; then
    echo "PASS: Overview section has content about 图书管理"
else
    echo "FAIL: Overview section appears empty or missing content"
    ERRORS=$((ERRORS + 1))
fi

# Test 5: Overview mentions RuoYi-Vue framework
if echo "$OVERVIEW_CONTENT" | grep -q "RuoYi-Vue"; then
    echo "PASS: Overview mentions RuoYi-Vue framework"
else
    echo "FAIL: Overview does not mention RuoYi-Vue framework"
    ERRORS=$((ERRORS + 1))
fi

# Test 6: Overview mentions key technologies
if echo "$OVERVIEW_CONTENT" | grep -qE "(Spring Boot|Vue|MySQL|MyBatis)"; then
    echo "PASS: Overview mentions key technologies"
else
    echo "FAIL: Overview does not mention key technologies"
    ERRORS=$((ERRORS + 1))
fi

# Summary
echo ""
if [ $ERRORS -eq 0 ]; then
    echo "All tests PASSED!"
    exit 0
else
    echo "$ERRORS test(s) FAILED"
    exit 1
fi
