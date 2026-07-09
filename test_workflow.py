#!/usr/bin/env python3
# Test script to verify workflow.txt contains required permission configuration

import re

def test_workflow():
    with open('workflow.txt', 'r', encoding='utf-8') as f:
        content = f.read()
    
    print("Testing workflow.txt for permission configuration...")
    errors = []
    
    # Test 1: Check for section heading
    if re.search(r"## 6\. 权限配置", content):
        print("PASS: Section '## 6. 权限配置' found")
    else:
        errors.append("Section '## 6. 权限配置' not found")
        print("FAIL: Section '## 6. 权限配置' not found")
    
    # Test 2: Check for 图书管理菜单 insert SQL (parent_id 0)
    if re.search(r"INSERT INTO sys_menu.*VALUES \('图书管理', 0,", content, re.DOTALL):
        print("PASS: 图书管理菜单 insert SQL found")
    else:
        errors.append("图书管理菜单 insert SQL not found")
        print("FAIL: 图书管理菜单 insert SQL not found")
    
    # Test 3: Check for 读者管理 submenu insert SQL
    if re.search(r"INSERT INTO sys_menu.*VALUES \('读者管理',.*library:reader:list", content, re.DOTALL):
        print("PASS: 读者管理 submenu insert SQL found")
    else:
        errors.append("读者管理 submenu insert SQL not found")
        print("FAIL: 读者管理 submenu insert SQL not found")
    
    # Test 4: Check for 图书信息 submenu insert SQL
    if re.search(r"INSERT INTO sys_menu.*VALUES \('图书信息',.*library:book:list", content, re.DOTALL):
        print("PASS: 图书信息 submenu insert SQL found")
    else:
        errors.append("图书信息 submenu insert SQL not found")
        print("FAIL: 图书信息 submenu insert SQL not found")
    
    # Test 5: Check for 借阅管理 submenu insert SQL
    if re.search(r"INSERT INTO sys_menu.*VALUES \('借阅管理',.*library:borrow:list", content, re.DOTALL):
        print("PASS: 借阅管理 submenu insert SQL found")
    else:
        errors.append("借阅管理 submenu insert SQL not found")
        print("FAIL: 借阅管理 submenu insert SQL not found")
    
    # Test 6: Check for button permissions for 读者管理
    if (re.search(r"'读者查询'.*library:reader:query", content) and
        re.search(r"'读者新增'.*library:reader:add", content) and
        re.search(r"'读者修改'.*library:reader:edit", content) and
        re.search(r"'读者删除'.*library:reader:remove", content)):
        print("PASS: Button permissions for 读者管理 found")
    else:
        errors.append("Button permissions for 读者管理 not found")
        print("FAIL: Button permissions for 读者管理 not found")
    
    # Test 7: Check for button permissions for 图书信息
    if (re.search(r"'图书查询'.*library:book:query", content) or
        re.search(r"'图书新增'.*library:book:add", content) or
        re.search(r"'图书修改'.*library:book:edit", content) or
        re.search(r"'图书删除'.*library:book:remove", content)):
        print("PASS: Button permissions for 图书信息 found")
    else:
        errors.append("Button permissions for 图书信息 not found")
        print("FAIL: Button permissions for 图书信息 not found")
    
    # Test 8: Check for button permissions for 借阅管理
    if (re.search(r"'借阅查询'.*library:borrow:query", content) or
        re.search(r"'借阅新增'.*library:borrow:add", content) or
        re.search(r"'借阅修改'.*library:borrow:edit", content) or
        re.search(r"'借阅删除'.*library:borrow:remove", content)):
        print("PASS: Button permissions for 借阅管理 found")
    else:
        errors.append("Button permissions for 借阅管理 not found")
        print("FAIL: Button permissions for 借阅管理 not found")
    
    if errors:
        print(f"\n{len(errors)} test(s) failed")
        return False
    else:
        print("\nAll tests passed!")
        return True

if __name__ == "__main__":
    import sys
    sys.exit(0 if test_workflow() else 1)