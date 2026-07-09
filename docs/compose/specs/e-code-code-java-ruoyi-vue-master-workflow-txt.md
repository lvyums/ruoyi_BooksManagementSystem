# RuoYi-Vue 图书管理系统 workflow.txt 规范

> [!NOTE]
> This document may not reflect the current implementation.
> See the final report for up-to-date state:
> [Final Report](../reports/e-code-code-java-ruoyi-vue-master-workflow-txt.md)

## S1. 项目概述

基于RuoYi-Vue框架的图书管理系统，实现图书馆数字化管理。系统包含读者管理、图书管理、借阅管理、用户权限管理等功能模块。

**技术栈：**
- 后端：Spring Boot 2.x + MyBatis + MySQL
- 前端：Vue.js 2.x + Element UI + Axios
- 构建工具：Maven (后端) + npm/Webpack (前端)

---

## S2. 环境要求

### S2.1 开发环境
| 工具 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 1.8+ | Java开发工具包 |
| Node.js | 14+ | 前端运行环境 |
| MySQL | 5.7+ | 数据库 |
| Maven | 3.6+ | 后端构建工具 |
| IDE | IntelliJ IDEA / VS Code | 开发工具 |

### S2.2 数据库初始化
- 创建数据库 `Library`，字符集 UTF-8
- 导入RuoYi系统基础表结构
- 创建图书管理业务表（TB_ReaderType, TB_Reader, TB_Book, TB_Borrow）
- 初始化业务数据（读者类别、系统管理员）

---

## S3. 开发工作流规范

### S3.1 新增功能模块流程
```
需求分析 → 数据库设计 → 后端开发 → 前端开发 → 测试验证
```

### S3.2 各阶段产出物
1. **需求分析**：需求文档、用例图、角色权限定义
2. **数据库设计**：ER图、建表SQL、索引设计
3. **后端开发**：实体类、Mapper、Service、Controller
4. **前端开发**：API接口、Vue组件、路由配置
5. **测试验证**：单元测试、接口测试、功能测试

---

## S4. 后端开发规范

### S4.1 包结构
```
com.ruoyi.library/
├── domain/          # 实体类
├── mapper/          # MyBatis Mapper接口
├── service/         # 业务逻辑层
│   └── impl/        # Service实现类
└── controller/      # 控制器层
```

### S4.2 实体类规范
- 遵循JavaBean规范
- 使用Lombok注解简化代码
- 字段命名使用驼峰命名法

### S4.3 Service层规范
- 接口定义在service包，实现在service.impl包
- 使用@Transactional注解管理事务
- 业务异常使用ServiceException抛出

### S4.4 Controller层规范
- 继承BaseController获取分页等通用方法
- 使用@RestController和@RequestMapping
- 使用@PreAuthorize进行权限控制
- 返回AjaxResult或TableDataInfo

---

## S5. 前端开发规范

### S5.1 目录结构
```
ruoyi-ui/src/
├── api/library/     # API接口文件
├── views/library/   # 页面组件
└── router/          # 路由配置（自动生成）
```

### S5.2 API接口规范
- 使用axios封装的request工具
- 统一响应格式处理
- 错误码统一定义

### S5.3 Vue组件规范
- 使用Element UI组件库
- 遵循单文件组件规范
- 使用v-hasPermi指令进行按钮权限控制

---

## S6. 业务逻辑规范

### S6.1 读者管理
- 办理借书证：设置默认值、插入记录
- 挂失/解除挂失：修改证件状态
- 注销借书证：检查未归还图书后注销

### S6.2 图书管理
- 新书入库：批量创建图书对象
- 图书状态流转：在馆 ↔ 借出 → 遗失/变卖/销毁

### S6.3 借阅管理
- 借书：校验读者状态、超期、限额、图书状态
- 还书：计算超期罚款、更新记录
- 续借：校验续借次数

---

## S7. 权限配置规范

### S7.1 菜单配置
- 使用sys_menu表存储菜单信息
- 菜单类型：M(目录)、C(菜单)、F(按钮)

### S7.2 按钮权限
- 权限标识格式：`模块:实体:操作`
- 示例：`library:reader:list`, `library:reader:add`

---

## S8. 测试规范

### S8.1 单元测试
- 使用JUnit + Mockito
- 测试Service层业务逻辑

### S8.2 接口测试
- 使用Postman/Apifox
- 覆盖所有API接口

### S8.3 功能测试清单
- 读者管理：增删改查、挂失/解除挂失、批量办理
- 图书管理：入库、信息维护、上下架、报废
- 借阅管理：借书、续借、还书、罚款处理

---

## S9. 部署规范

### S9.1 后端打包
```bash
mvn clean package -Dmaven.test.skip=true
```

### S9.2 前端打包
```bash
cd ruoyi-ui && npm run build:prod
```

### S9.3 部署方式
- 后端：jar包 + Spring Profiles
- 前端：Nginx静态文件服务
- API代理：Nginx反向代理到后端服务

---

## S10. 文档结构要求

workflow.txt文件应包含以下章节：
1. 项目概述
2. 环境准备（开发环境、数据库初始化、后端启动、前端启动）
3. 开发工作流（新增功能模块流程）
4. 后端开发规范（包结构、实体类、Service、Controller）
5. 前端开发规范（目录结构、API接口、Vue组件）
6. 业务逻辑实现（读者管理、借阅管理核心逻辑）
7. 权限配置（菜单配置、按钮权限）
8. 测试工作流（单元测试、接口测试、功能测试）
9. 部署工作流（打包、部署配置）
10. 维护工作流（数据备份、日志查看）
