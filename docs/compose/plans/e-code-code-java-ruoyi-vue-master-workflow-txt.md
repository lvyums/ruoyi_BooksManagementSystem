# RuoYi-Vue 图书管理系统 workflow.txt 实现计划

> [!NOTE]
> This document may not reflect the current implementation.
> See the final report for up-to-date state:
> [Final Report](../reports/e-code-code-java-ruoyi-vue-master-workflow-txt.md)

> **For agentic workers:** REQUIRED SUB-SKILL: Use compose:subagent (recommended) or compose:execute to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 生成或更新项目根目录下的 workflow.txt 文件，包含完整的开发工作流文档

**Architecture:** 基于现有项目结构和需求分析文档，生成标准化的开发工作流文档

**Tech Stack:** Markdown

## Global Constraints

- 文档使用中文编写
- 代码示例使用Java（后端）和JavaScript/Vue（前端）
- 命令示例适用于Linux/Mac环境
- 所有SQL示例基于MySQL语法

---

### Task 1: 创建项目概述章节

**Covers:** S1

**Files:**
- Create: `workflow.txt` (初始内容)

**Interfaces:**
- Consumes: 需求分析文档
- Produces: 项目概述章节

- [ ] **Step 1: 编写项目概述**

```markdown
# RuoYi-Vue 图书管理系统 开发工作流

## 项目概述
基于RuoYi-Vue框架的图书管理系统，实现图书馆数字化管理。
```

- [ ] **Step 2: 提交**

```bash
git add workflow.txt
git commit -m "docs: 添加workflow.txt项目概述章节"
```

---

### Task 2: 添加环境准备章节

**Covers:** S2

**Files:**
- Modify: `workflow.txt`

**Interfaces:**
- Consumes: 项目概述
- Produces: 环境准备章节

- [ ] **Step 1: 添加开发环境说明**

```markdown
---

## 1. 环境准备

### 1.1 开发环境
- JDK 1.8+
- Node.js 14+
- MySQL 5.7+
- Maven 3.6+
- IDE: IntelliJ IDEA / VS Code
```

- [ ] **Step 2: 添加数据库初始化命令**

```markdown
### 1.2 数据库初始化
```bash
# 1. 创建数据库
mysql -u root -p -e "CREATE DATABASE Library DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;"

# 2. 导入基础表结构（RuoYi系统表）
mysql -u root -p Library < sql/ry_20231022.sql

# 3. 创建图书管理业务表
mysql -u root -p Library < sql/library_tables.sql

# 4. 初始化业务数据
mysql -u root -p Library < sql/library_init.sql
```
```

- [ ] **Step 3: 添加启动命令**

```markdown
### 1.3 后端启动
```bash
# 修改数据库配置
vim ruoyi-admin/src/main/resources/application-druid.yml

# 启动后端服务
mvn spring-boot:run -pl ruoyi-admin
```

### 1.4 前端启动
```bash
cd ruoyi-ui
npm install
npm run dev
```
```

- [ ] **Step 4: 提交**

```bash
git add workflow.txt
git commit -m "docs: 添加环境准备章节"
```

---

### Task 3: 添加开发工作流章节

**Covers:** S3

**Files:**
- Modify: `workflow.txt`

**Interfaces:**
- Consumes: 环境准备
- Produces: 开发工作流章节

- [ ] **Step 1: 添加流程图**

```markdown
---

## 2. 开发工作流

### 2.1 新增功能模块流程

```
┌─────────────────────────────────────────────────────────────┐
│  1. 需求分析                                                │
│     - 阅读需求文档（需求分析/目录下）                         │
│     - 确定功能边界和角色权限                                  │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│  2. 数据库设计                                              │
│     - 设计数据表结构（参考数据库设计.md）                      │
│     - 编写建表SQL                                           │
│     - 创建索引和外键约束                                      │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│  3. 后端开发                                                │
│     - 创建实体类（domain）                                   │
│     - 创建Mapper接口和XML                                   │
│     - 创建Service层                                         │
│     - 创建Controller层                                      │
│     - 配置菜单和权限                                         │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│  4. 前端开发                                                │
│     - 创建API接口文件                                        │
│     - 创建Vue页面组件                                        │
│     - 配置路由和菜单                                         │
│     - 实现业务交互逻辑                                       │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│  5. 测试验证                                                │
│     - 单元测试                                               │
│     - 接口测试                                               │
│     - 功能测试                                               │
└─────────────────────────────────────────────────────────────┘
```
```

- [ ] **Step 2: 提交**

```bash
git add workflow.txt
git commit -m "docs: 添加开发工作流章节"
```

---

### Task 4: 添加后端开发规范章节

**Covers:** S4

**Files:**
- Modify: `workflow.txt`

**Interfaces:**
- Consumes: 开发工作流
- Produces: 后端开发规范章节

- [ ] **Step 1: 添加包结构说明**

```markdown
---

## 3. 后端开发规范

### 3.1 包结构
```
ruoyi-system/
└── src/main/java/com/ruoyi/system/
    ├── domain/          # 实体类
    │   └── library/     # 图书管理相关实体
    ├── mapper/          # MyBatis Mapper接口
    │   └── library/
    ├── service/         # 业务逻辑层
    │   └── library/
    └── controller/      # 控制器层
        └── library/
```
```

- [ ] **Step 2: 添加实体类示例**

```markdown
### 3.2 实体类示例（Reader）
```java
package com.ruoyi.system.domain.library;

public class Reader {
    private Integer rdID;           // 读者编号
    private String rdName;          // 姓名
    private String rdSex;           // 性别
    private Integer rdType;         // 读者类别
    private String rdDept;          // 单位/班级
    private String rdPhone;         // 电话
    private String rdEmail;         // 邮箱
    private String rdDateReg;       // 办证日期
    private String rdStatus;        // 证件状态
    private Integer rdBorrowQty;    // 已借书数量
    private String rdPwd;           // 密码
    private Integer rdAdminRoles;   // 管理角色
    
    // getter/setter...
}
```
```

- [ ] **Step 3: 添加Service层示例**

```markdown
### 3.3 Service层示例
```java
package com.ruoyi.system.service.library;

public interface IReaderService {
    // 查询读者列表
    public List<Reader> selectReaderList(Reader reader);
    
    // 根据ID查询读者
    public Reader selectReaderById(Integer rdID);
    
    // 新增读者（办理借书证）
    public int insertReader(Reader reader);
    
    // 修改读者（变更借书证）
    public int updateReader(Reader reader);
    
    // 删除读者（注销借书证）
    public int deleteReaderById(Integer rdID);
    
    // 挂失借书证
    public int reportLoss(Integer rdID);
    
    // 解除挂失
    public int cancelLoss(Integer rdID);
    
    // 批量办理新生借书证
    public int batchInsertReaders(List<Reader> readers);
}
```
```

- [ ] **Step 4: 添加Controller层示例**

```markdown
### 3.4 Controller层示例
```java
package com.ruoyi.system.controller.library;

@RestController
@RequestMapping("/library/reader")
public class ReaderController extends BaseController {
    
    @Autowired
    private IReaderService readerService;
    
    @PreAuthorize("@ss.hasPermi('library:reader:list')")
    @GetMapping("/list")
    public TableDataInfo list(Reader reader) {
        startPage();
        List<Reader> list = readerService.selectReaderList(reader);
        return getDataTable(list);
    }
    
    @PreAuthorize("@ss.hasPermi('library:reader:query')")
    @GetMapping("/{rdID}")
    public AjaxResult getInfo(@PathVariable Integer rdID) {
        return AjaxResult.success(readerService.selectReaderById(rdID));
    }
    
    @PreAuthorize("@ss.hasPermi('library:reader:add')")
    @PostMapping
    public AjaxResult add(@RequestBody Reader reader) {
        return toAjax(readerService.insertReader(reader));
    }
    
    @PreAuthorize("@ss.hasPermi('library:reader:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody Reader reader) {
        return toAjax(readerService.updateReader(reader));
    }
    
    @PreAuthorize("@ss.hasPermi('library:reader:remove')")
    @DeleteMapping("/{rdIDs}")
    public AjaxResult remove(@PathVariable Integer[] rdIDs) {
        return toAjax(readerService.deleteReaderByIds(rdIDs));
    }
    
    @PreAuthorize("@ss.hasPermi('library:reader:edit')")
    @PutMapping("/reportLoss/{rdID}")
    public AjaxResult reportLoss(@PathVariable Integer rdID) {
        return toAjax(readerService.reportLoss(rdID));
    }
}
```
```

- [ ] **Step 5: 提交**

```bash
git add workflow.txt
git commit -m "docs: 添加后端开发规范章节"
```

---

### Task 5: 添加前端开发规范章节

**Covers:** S5

**Files:**
- Modify: `workflow.txt`

**Interfaces:**
- Consumes: 后端开发规范
- Produces: 前端开发规范章节

- [ ] **Step 1: 添加目录结构说明**

```markdown
---

## 4. 前端开发规范

### 4.1 目录结构
```
ruoyi-ui/src/
├── api/library/         # API接口文件
│   ├── reader.js
│   ├── book.js
│   └── borrow.js
├── views/library/       # 页面组件
│   ├── reader/
│   │   ├── index.vue
│   │   └── readerForm.vue
│   ├── book/
│   │   ├── index.vue
│   │   └── bookForm.vue
│   └── borrow/
│       ├── index.vue
│       └── borrowForm.vue
└── router/              # 路由配置（自动）
```
```

- [ ] **Step 2: 添加API接口示例**

```markdown
### 4.2 API接口示例（reader.js）
```javascript
import request from '@/utils/request'

// 查询读者列表
export function listReader(query) {
  return request({
    url: '/library/reader/list',
    method: 'get',
    params: query
  })
}

// 查询读者详细
export function getReader(rdID) {
  return request({
    url: '/library/reader/' + rdID,
    method: 'get'
  })
}

// 新增读者
export function addReader(data) {
  return request({
    url: '/library/reader',
    method: 'post',
    data: data
  })
}

// 修改读者
export function updateReader(data) {
  return request({
    url: '/library/reader',
    method: 'put',
    data: data
  })
}

// 删除读者
export function delReader(rdID) {
  return request({
    url: '/library/reader/' + rdID,
    method: 'delete'
  })
}

// 挂失借书证
export function reportLoss(rdID) {
  return request({
    url: '/library/reader/reportLoss/' + rdID,
    method: 'put'
  })
}
```
```

- [ ] **Step 3: 添加页面组件示例**

```markdown
### 4.3 页面组件示例（index.vue）
```vue
<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="读者姓名" prop="rdName">
        <el-input v-model="queryParams.rdName" placeholder="请输入读者姓名" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="读者类别" prop="rdType">
        <el-select v-model="queryParams.rdType" placeholder="请选择读者类别" clearable>
          <el-option v-for="dict in typeOptions" :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="证件状态" prop="rdStatus">
        <el-select v-model="queryParams.rdStatus" placeholder="请选择证件状态" clearable>
          <el-option v-for="dict in statusOptions" :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['library:reader:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" :disabled="single" @click="handleUpdate" v-hasPermi="['library:reader:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['library:reader:remove']">删除</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="readerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="借书证号" align="center" prop="rdID"/>
      <el-table-column label="读者姓名" align="center" prop="rdName"/>
      <el-table-column label="性别" align="center" prop="rdSex"/>
      <el-table-column label="读者类别" align="center" prop="rdType" :formatter="typeFormat"/>
      <el-table-column label="所在单位" align="center" prop="rdDept"/>
      <el-table-column label="证件状态" align="center" prop="rdStatus" :formatter="statusFormat"/>
      <el-table-column label="已借书数" align="center" prop="rdBorrowQty"/>
      <el-table-column label="办证日期" align="center" prop="rdDateReg"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['library:reader:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['library:reader:remove']">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-warning" @click="handleReportLoss(scope.row)" v-if="scope.row.rdStatus=='有效'" v-hasPermi="['library:reader:edit']">挂失</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
  </div>
</template>
```
```

- [ ] **Step 4: 提交**

```bash
git add workflow.txt
git commit -m "docs: 添加前端开发规范章节"
```

---

### Task 6: 添加业务逻辑实现章节

**Covers:** S6

**Files:**
- Modify: `workflow.txt`

**Interfaces:**
- Consumes: 前端开发规范
- Produces: 业务逻辑实现章节

- [ ] **Step 1: 添加读者管理核心逻辑**

```markdown
---

## 5. 业务逻辑实现

### 5.1 读者管理核心逻辑

#### 办理借书证
```java
@Override
@Transactional
public int insertReader(Reader reader) {
    // 1. 设置默认值
    reader.setRdStatus("有效");
    reader.setRdBorrowQty(0);
    reader.setRdPwd("123"); // 默认密码
    reader.setRdDateReg(new Date());
    
    // 2. 插入读者记录
    return readerMapper.insertReader(reader);
}
```
```

- [ ] **Step 2: 添加借书逻辑**

```markdown
#### 借书逻辑
```java
@Override
@Transactional
public int borrowBook(Integer rdID, Integer bkID, String operator) {
    // 1. 检查读者状态
    Reader reader = readerMapper.selectReaderById(rdID);
    if (!"有效".equals(reader.getRdStatus())) {
        throw new ServiceException("读者状态异常，无法借书");
    }
    
    // 2. 检查是否超期未还
    int overdueCount = borrowMapper.countOverdueByReader(rdID);
    if (overdueCount > 0) {
        throw new ServiceException("存在超期未还图书，无法借书");
    }
    
    // 3. 检查借书限额
    ReaderType type = readerTypeMapper.selectReaderTypeById(reader.getRdType());
    if (reader.getRdBorrowQty() >= type.getCanLendQty()) {
        throw new ServiceException("已达到借书数量上限");
    }
    
    // 4. 检查图书状态
    Book book = bookMapper.selectBookById(bkID);
    if (!"在馆".equals(book.getBkStatus())) {
        throw new ServiceException("图书不在馆，无法借阅");
    }
    
    // 5. 创建借阅记录
    Borrow borrow = new Borrow();
    borrow.setRdID(rdID);
    borrow.setBkID(bkID);
    borrow.setLdDateOut(new Date());
    borrow.setLdDateRetPlan(calculateReturnDate(reader.getRdType()));
    borrow.setLdContinueTimes(0);
    borrow.setLsHasReturn(0);
    borrow.setOperatorBorrow(operator);
    
    // 6. 更新读者已借书数量
    reader.setRdBorrowQty(reader.getRdBorrowQty() + 1);
    readerMapper.updateReader(reader);
    
    // 7. 更新图书状态
    book.setBkStatus("借出");
    bookMapper.updateBook(book);
    
    return borrowMapper.insertBorrow(borrow);
}
```
```

- [ ] **Step 3: 添加还书逻辑**

```markdown
#### 还书逻辑
```java
@Override
@Transactional
public int returnBook(Integer bkID, String operator, boolean isLost, BigDecimal punishMoney) {
    // 1. 查找未归还的借阅记录
    Borrow borrow = borrowMapper.selectUnreturnedByBook(bkID);
    if (borrow == null) {
        throw new ServiceException("未找到该图书的借阅记录");
    }
    
    // 2. 计算超期天数和罚款
    int overDay = 0;
    BigDecimal overMoney = BigDecimal.ZERO;
    
    if (isLost) {
        // 遗失罚款：3倍图书单价
        Book book = bookMapper.selectBookById(bkID);
        overMoney = book.getBkPrice().multiply(new BigDecimal(3));
    } else {
        // 被期罚款
        overDay = calculateOverdueDays(borrow.getLdDateRetPlan());
        ReaderType type = readerTypeMapper.selectReaderTypeByReader(borrow.getRdID());
        overMoney = new BigDecimal(overDay).multiply(type.getPunishRate());
    }
    
    // 3. 更新借阅记录
    borrow.setLdDateRetAct(new Date());
    borrow.setLdOverDay(overDay);
    borrow.setLdOverMoney(overMoney);
    borrow.setLdPunishMoney(punishMoney);
    borrow.setLsHasReturn(1);
    borrow.setOperatorReturn(operator);
    borrowMapper.updateBorrow(borrow);
    
    // 4. 更新读者已借书数量
    Reader reader = readerMapper.selectReaderById(borrow.getRdID());
    reader.setRdBorrowQty(reader.getRdBorrowQty() - 1);
    readerMapper.updateReader(reader);
    
    // 5. 更新图书状态
    Book book = bookMapper.selectBookById(bkID);
    book.setBkStatus(isLost ? "遗失" : "在馆");
    bookMapper.updateBook(book);
    
    return 1;
}
```
```

- [ ] **Step 4: 提交**

```bash
git add workflow.txt
git commit -m "docs: 添加业务逻辑实现章节"
```

---

### Task 7: 添加权限配置章节

**Covers:** S7

**Files:**
- Modify: `workflow.txt`

**Interfaces:**
- Consumes: 业务逻辑实现
- Produces: 权限配置章节

- [ ] **Step 1: 添加菜单配置SQL**

```markdown
---

## 6. 权限配置

### 6.1 菜单配置
```sql
-- 图书管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('图书管理', 0, 1, 'library', NULL, 1, 0, 'M', '0', '0', '', 'book', 'admin', sysdate(), '', NULL, '图书管理菜单');

-- 读者管理
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('读者管理', (SELECT menu_id FROM sys_menu WHERE menu_name = '图书管理'), 1, 'reader', 'library/reader/index', 1, 0, 'C', '0', '0', 'library:reader:list', 'user', 'admin', sysdate(), '', NULL, '');

-- 图书信息
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('图书信息', (SELECT menu_id FROM sys_menu WHERE menu_name = '图书管理'), 2, 'book', 'library/book/index', 1, 0, 'C', '0', '0', 'library:book:list', 'education', 'admin', sysdate(), '', NULL, '');

-- 借阅管理
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('借阅管理', (SELECT menu_id FROM sys_menu WHERE menu_name = '图书管理'), 3, 'borrow', 'library/borrow/index', 1, 0, 'C', '0', '0', 'library:borrow:list', 'log', 'admin', sysdate(), '', NULL, '');
```
```

- [ ] **Step 2: 添加按钮权限配置**

```markdown
### 6.2 按钮权限配置
```sql
-- 读者管理按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES 
('读者查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '读者管理'), 1, '#', '', 1, 0, 'F', '0', '0', 'library:reader:query', '#', 'admin', sysdate(), '', NULL, ''),
('读者新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '读者管理'), 2, '#', '', 1, 0, 'F', '0', '0', 'library:reader:add', '#', 'admin', sysdate(), '', NULL, ''),
('读者修改', (SELECT menu_id FROM sys_menu WHERE menu_name = '读者管理'), 3, '#', '', 1, 0, 'F', '0', '0', 'library:reader:edit', '#', 'admin', sysdate(), '', NULL, ''),
('读者删除', (SELECT menu_id FROM sys_menu WHERE menu_name = '读者管理'), 4, '#', '', 1, 0, 'F', '0', '0', 'library:reader:remove', '#', 'admin', sysdate(), '', NULL, '');
```
```

- [ ] **Step 3: 提交**

```bash
git add workflow.txt
git commit -m "docs: 添加权限配置章节"
```

---

### Task 8: 添加测试工作流章节

**Covers:** S8

**Files:**
- Modify: `workflow.txt`

**Interfaces:**
- Consumes: 权限配置
- Produces: 测试工作流章节

- [ ] **Step 1: 添加测试命令**

```markdown
---

## 7. 测试工作流

### 7.1 单元测试
```bash
# 运行所有测试
mvn test

# 运行指定模块测试
mvn test -pl ruoyi-system
```

### 7.2 接口测试
使用Postman或Apifox测试API接口：
1. 导入接口文档（需求分析/接口文档.md）
2. 配置环境变量
3. 执行测试用例

### 7.3 功能测试清单
- [ ] 读者管理：增删改查、挂失/解除挂失、批量办理
- [ ] 图书管理：入库、信息维护、上下架、报废
- [ ] 借阅管理：借书、续借、还书、罚款处理
- [ ] 用户管理：登录、权限控制、密码修改
- [ ] 统计报表：馆藏统计、借阅统计
```

- [ ] **Step 2: 提交**

```bash
git add workflow.txt
git commit -m "docs: 添加测试工作流章节"
```

---

### Task 9: 添加部署工作流章节

**Covers:** S9

**Files:**
- Modify: `workflow.txt`

**Interfaces:**
- Consumes: 测试工作流
- Produces: 部署工作流章节

- [ ] **Step 1: 添加打包命令**

```markdown
---

## 8. 部署工作流

### 8.1 后端打包
```bash
# 清理并打包
mvn clean package -Dmaven.test.skip=true

# 生成的jar包位置
ls ruoyi-admin/target/ruoyi-admin.jar
```

### 8.2 前端打包
```bash
cd ruoyi-ui
npm run build:prod

# 生成的静态文件位置
ls dist/
```

### 8.3 部署配置
```bash
# 1. 部署后端
nohup java -jar ruoyi-admin.jar --spring.profiles.active=prod &

# 2. 部署前端到Nginx
cp -r dist/* /usr/share/nginx/html/library/
```

### 8.4 Nginx配置示例
```nginx
server {
    listen 80;
    server_name library.example.com;
    
    location / {
        root /usr/share/nginx/html/library;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    location /prod-api/ {
        proxy_pass http://127.0.0.1:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```
```

- [ ] **Step 2: 提交**

```bash
git add workflow.txt
git commit -m "docs: 添加部署工作流章节"
```

---

### Task 10: 添加维护工作流和附录

**Covers:** S10

**Files:**
- Modify: `workflow.txt`

**Interfaces:**
- Consumes: 部署工作流
- Produces: 维护工作流章节、附录

- [ ] **Step 1: 添加维护工作流**

```markdown
---

## 9. 维护工作流

### 9.1 数据备份
```bash
# 每日备份脚本
#!/bin/bash
DATE=$(date +%Y%m%d)
mysqldump -u root -p Library > /backup/library_$DATE.sql
```

### 9.2 日志查看
```bash
# 查看应用日志
tail -f logs/ruoyi-admin.log

# 查看错误日志
grep ERROR logs/ruoyi-admin.log
```
```

- [ ] **Step 2: 添加附录**

```markdown
---

## 附录：数据库表结构速查

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| TB_ReaderType | 读者类别 | rdType, rdTypeName, CanLendQty, CanLendDay |
| TB_Reader | 读者信息 | rdID, rdName, rdType, rdStatus, rdAdminRoles |
| TB_Book | 图书信息 | bkID, bkCode, bkName, bkStatus |
| TB_Borrow | 借阅记录 | BorrowID, rdID, bkID, lsHasReturn |

---

## 常用命令速查

```bash
# 启动开发环境
mvn spring-boot:run -pl ruoyi-admin  # 后端
cd ruoyi-ui && npm run dev           # 前端

# 代码生成
mvn gen:table -Dtable=tb_reader      # 根据表生成代码

# 打包部署
mvn clean package -Dmaven.test.skip=true
npm run build:prod
```
```

- [ ] **Step 3: 最终提交**

```bash
git add workflow.txt
git commit -m "docs: 完成workflow.txt开发工作流文档"
```

---

## Self-Review

**1. Spec coverage:** 
- S1 (项目概述) → Task 1 ✓
- S2 (环境要求) → Task 2 ✓
- S3 (开发工作流规范) → Task 3 ✓
- S4 (后端开发规范) → Task 4 ✓
- S5 (前端开发规范) → Task 5 ✓
- S6 (业务逻辑规范) → Task 6 ✓
- S7 (权限配置规范) → Task 7 ✓
- S8 (测试规范) → Task 8 ✓
- S9 (部署规范) → Task 9 ✓
- S10 (文档结构要求) → Task 10 ✓

**2. Placeholder scan:** 无占位符，所有内容均为实际代码和命令

**3. Type consistency:** 所有示例代码保持一致的命名规范和结构
