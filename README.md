# 📚 RuoYi-LibMS  
> 基于 **RuoYi-Vue** 前后端分离框架的图书管理系统（课程设计版）

---

## 1. 项目简介
本项目是长江大学 2025-2026-1 学期《数据库原理及应用课程设计》成果，采用 **SpringBoot + Vue3 + MySQL** 技术栈，在若依框架基础上二次开发，覆盖 **读者管理、图书管理、借还续、罚款、权限隔离** 等完整图书馆业务流程，可直接作为毕业设计/课程设计模板或小型图书馆生产系统使用。

---

## 2. 主要功能
| 模块 | 核心功能 |
| ---- | -------- |
| 🔑 系统管理 | 用户/角色/菜单管理、RBAC 权限、密码加密、登录验证码 |
| 👤 读者管理 | 办证、挂失、补办、注销、批量导入、证号自动迁移 |
| 📖 图书管理 | 新书入库、信息维护、变卖/销毁、库存状态实时同步 |
| 📤 借阅管理 | 借书、还书、续借、超期/遗失罚款、操作员痕迹 |
| 👀 个人中心 | 当前借阅、历史记录、自助续借、超期提醒 |
| 🛡️ 数据权限 | 普通读者仅看本人记录；多级管理员分范围查看 |
| 📊 异常拦截 | 全局统一返回、事务回滚、接口级权限注解 |

---

## 3. 技术架构
| 层级 | 技术选型 |
| ---- | -------- |
| 后端 | SpringBoot 2.7 + MyBatis + Spring Security + Druid |
| 前端 | Vue3 + Element-Plus + Axios + RuoYi-Vue 代码生成器 |
| 数据库 | MySQL 8.0（兼容 5.7） |
| 工具 | JDK 1.8、Maven 3.6、Node 16+、Navicat |

---

## 4. 快速启动

### 4.1 拉取代码
```bash
git clone https://github.com/lvyums/ruoyi_BooksManagementSystem.git
```

### 4.2 数据库初始化
1. 创建数据库 `ry_library`。
2. 执行 `sql/ry_library.sql` 建表并插入基础数据（菜单、角色、字典）。

### 4.3 后端配置
```bash
cd ruoyi-admin
# 修改 resources/application-druid.yml 数据源
mvn clean package -DskipTests
java -jar target/ruoyi-admin.jar
```
默认端口 8080，接口文档：http://localhost:8080/swagger-ui/

### 4.4 前端启动
```bash
cd ruoyi-ui
npm install
npm run dev
```
默认端口 80，浏览器访问 http://localhost

---

## 5. 业务规则速览
| 规则 | 说明 |
| ---- | ---- |
| 可借数量 | 按读者类别动态控制（本科 10 本、教师 20 本…） |
| 借期 | 本科 30 天、教师 60 天，可续借 1 次 |
| 超期罚款 | 0.05 元/天，遗失罚款 = 书价 × 3（优先遗失） |
| 证件有效期 | 学生按学制（4/3/3 年），教师永久 |
| 权限位运算 | `rd_admin_roles` 1-8 分别对应 4 类管理员，可叠加 |

---

## 6. 二次开发指南
1. 代码生成  
   修改 `ruoyi-generator` 数据源 → 新建表 → 一键生成前后端代码 → 复制到对应模块即可。
2. 新增角色  
   在 **系统管理-角色管理** 添加角色 → 分配菜单 → 读者信息里把 `rd_admin_roles` 置为对应位值。
3. 前端自定义  
   页面路径 `ruoyi-ui/src/views/library/...`，已封装通用 `CRUD.vue`，参照现有页面修改。

---

## 7. 演示截图
（位于 `/docs/screenshot/`）

| 登录页 | 借还书工作台 | 个人借阅中心 |
| ------ | ------------ | ------------ |
| ![login](docs/screenshot/login.png) | ![borrow](docs/screenshot/borrow.png) | ![my](docs/screenshot/my.png) |

---

## 8. 已知问题 & TODO
- [ ] 图书分类模糊搜索未做  
- [ ] 短信/邮件超期提醒待集成  
- [ ] 接口限流、防刷、密码强度升级  
- [ ] 低版本浏览器样式兼容  
欢迎提 Issue / PR 共同完善！

---

## 9. 开源协议
MIT © 2025 zhangwenxuan（课程设计原版）  
可自由学习、二次开发，引用请注明出处。

---

## 10. 联系方式
- 项目地址：https://github.com/lvyums/ruoyi_BooksManagementSystem  
- 问题反馈：请提 Issue，24h 内回复
```
