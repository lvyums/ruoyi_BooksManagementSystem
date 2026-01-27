# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Frontend (ruoyi-ui)
- Install dependencies: `cd ruoyi-ui && npm install`
- Development server: `npm run dev`
- Production build: `npm run build:prod`
- Staging build: `npm run build:stage`
- Preview build: `npm run preview`

### Backend (Spring Boot)
- Run application: `mvn spring-boot:run` (from root directory)
- Package application: `mvn clean package`
- Run tests: `mvn test`

## Architecture Overview

RuoYi-Vue is a Spring Boot + Vue.js based management system with the following structure:

1. **Frontend** (`ruoyi-ui`):
   - Vue 2.x with Element UI component library
   - Modular structure with views for different system features (user management, department, roles, etc.)
   - API calls to backend using Axios
   - Routing managed by Vue Router
   - State management using Vuex

2. **Backend**:
   - Spring Boot application providing RESTful APIs
   - Security handled by Spring Security with JWT authentication
   - Redis used for caching and session management
   - MyBatis for database operations
   - Modular package structure following domain-driven design

3. **Key Features**:
   - Role-based access control with dynamic menu loading
   - Code generation for CRUD operations
   - System monitoring and logging
   - Multi-tenant support

4. **File Structure**:
   - `ruoyi-ui/`: Vue frontend application
   - `ruoyi-admin/`: Spring Boot main application module
   - `ruoyi-system/`: System business logic module
   - `ruoyi-common/`: Shared utilities and components