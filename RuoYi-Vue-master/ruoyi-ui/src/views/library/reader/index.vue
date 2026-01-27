<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="rdName">
        <el-input
          v-model="queryParams.rdName"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="性别" prop="rdSex">
        <el-select v-model="queryParams.rdSex" placeholder="请选择性别(0男 1女)" clearable>
          <el-option
            v-for="dict in dict.type.sys_user_sex"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="单位/班级" prop="rdDept">
        <el-input
          v-model="queryParams.rdDept"
          placeholder="请输入单位/班级"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电话" prop="rdPhone">
        <el-input
          v-model="queryParams.rdPhone"
          placeholder="请输入电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电子邮箱" prop="rdEmail">
        <el-input
          v-model="queryParams.rdEmail"
          placeholder="请输入电子邮箱"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="办证日期" prop="rdDateReg">
        <el-date-picker clearable
          v-model="queryParams.rdDateReg"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择办证日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="状态" prop="rdStatus">
        <el-select v-model="queryParams.rdStatus" placeholder="请选择状态(0有效 1挂失 2注销)" clearable>
          <el-option
            v-for="dict in dict.type.library_reader_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="已借书数量" prop="rdBorrowQty">
        <el-input
          v-model="queryParams.rdBorrowQty"
          placeholder="请输入已借书数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="密码" prop="rdPwd">
        <el-input
          v-model="queryParams.rdPwd"
          placeholder="请输入密码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="管理角色" prop="rdAdminRoles">
        <el-input
          v-model="queryParams.rdAdminRoles"
          placeholder="请输入管理角色"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['library:reader:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['library:reader:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['library:reader:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
        >导入</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['library:reader:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="readerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="借书证号" align="center" prop="rdID" />
      <el-table-column label="姓名" align="center" prop="rdName" />
      <el-table-column label="性别(0男 1女)" align="center" prop="rdSex">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.rdSex"/>
        </template>
      </el-table-column>
      <el-table-column label="读者类别" align="center" prop="rdType" />
      <el-table-column label="单位/班级" align="center" prop="rdDept" />
      <el-table-column label="电话" align="center" prop="rdPhone" />
      <el-table-column label="电子邮箱" align="center" prop="rdEmail" />
      <el-table-column label="办证日期" align="center" prop="rdDateReg" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.rdDateReg, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="照片" align="center" prop="rdPhoto" />
      <el-table-column label="状态(0有效 1挂失 2注销)" align="center" prop="rdStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.library_reader_status" :value="scope.row.rdStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="已借书数量" align="center" prop="rdBorrowQty" />
      <el-table-column label="密码" align="center" prop="rdPwd" />
      <el-table-column label="管理角色" align="center" prop="rdAdminRoles" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['library:reader:edit']"
          >修改</el-button>
          <!-- 只有状态正常的才有必要补办，注销的就别补办了 -->
          <el-button
            v-if="scope.row.rdStatus !== '2'"
            size="mini"
            type="text"
            icon="el-icon-document-copy"
            @click="handleReissue(scope.row)"
          >补办</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['library:reader:remove']"
          >删除</el-button>
        </template>
      </el-table-column>



      <el-table-column label="读者类别" align="center" prop="rdType">
        <template slot-scope="scope">
          <!-- 调用一个辅助方法或者是过滤器来显示名称 -->
          <el-tag v-if="scope.row.rdType">{{ formatReaderTypeName(scope.row.rdType) }}</el-tag>
          <span v-else>未分类</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改读者信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="rdName">
          <el-input v-model="form.rdName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别(0男 1女)" prop="rdSex">
          <el-select v-model="form.rdSex" placeholder="请选择性别(0男 1女)">
            <el-option
              v-for="dict in dict.type.sys_user_sex"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="单位/班级" prop="rdDept">
          <el-input v-model="form.rdDept" placeholder="请输入单位/班级" />
        </el-form-item>
        <el-form-item label="电话" prop="rdPhone">
          <el-input v-model="form.rdPhone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="电子邮箱" prop="rdEmail">
          <el-input v-model="form.rdEmail" placeholder="请输入电子邮箱" />
        </el-form-item>
        <el-form-item label="办证日期" prop="rdDateReg">
          <el-date-picker clearable
            v-model="form.rdDateReg"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择办证日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="状态(0有效 1挂失 2注销)" prop="rdStatus">
          <el-select v-model="form.rdStatus" placeholder="请选择状态(0有效 1挂失 2注销)">
            <el-option
              v-for="dict in dict.type.library_reader_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="已借书数量" prop="rdBorrowQty">
          <el-input v-model="form.rdBorrowQty" placeholder="请输入已借书数量" />
        </el-form-item>
        <el-form-item label="密码" prop="rdPwd">
          <el-input v-model="form.rdPwd" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="管理角色" prop="rdAdminRoles">
          <el-input v-model="form.rdAdminRoles" placeholder="请输入管理角色" />
        </el-form-item>
        <el-form-item label="读者类别" prop="rdType">
          <el-select v-model="form.rdType" placeholder="请选择读者类别">
            <el-option
              v-for="item in readertypeOptions"
              :key="item.rdType"
              :label="item.rdTypeName"
              :value="item.rdType"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加导入对话框代码 (从若依 user/index.vue 抄的) -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers" :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading" :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReader, getReader, delReader, addReader, updateReader } from "@/api/library/reader"
import { listReadertype } from "@/api/library/readertype";
import { getToken } from "@/utils/auth"; // 1. 务必确认引入了 getToken
import { reissueReader } from "@/api/library/reader";
export default {
  name: "Reader",
  dicts: ['library_reader_status', 'sys_user_sex'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 读者类别选项
      readertypeOptions: [],
      // 读者信息表格数据
      readerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        rdName: null,
        rdSex: null,
        rdType: null,
        rdDept: null,
        rdPhone: null,
        rdEmail: null,
        rdDateReg: null,
        rdPhoto: null,
        rdStatus: null,
        rdBorrowQty: null,
        rdPwd: null,
        rdAdminRoles: null
      },
      // === 补全 upload 变量 ===
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/library/reader/importData"
      },

      // 表单参数
      form: {},
      // 表单校验
      rules: {
        rdType: [
          { required: true, message: "读者类别不能为空", trigger: "change" }
        ],
      }
    }
  },
  created() {
    this.getList()
    this.getReaderTypeList();
  },
  methods: {
    /** 查询读者信息列表 */
    getList() {
      this.loading = true
      listReader(this.queryParams).then(response => {
        this.readerList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 查询读者类别列表 */
    getReaderTypeList() {
      listReadertype().then(response => {
        // 若依列表接口返回的数据通常在 rows 中
        this.readertypeOptions = response.rows;
      });
    },
    formatReaderTypeName(rdType) {
      if (!this.readertypeOptions || this.readertypeOptions.length === 0) return rdType;
      // 在已加载的选项中查找
      const found = this.readertypeOptions.find(item => item.rdType === rdType);
      return found ? found.rdTypeName : rdType;
    },

    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        rdID: null,
        rdName: null,
        rdSex: null,
        rdType: null,
        rdDept: null,
        rdPhone: null,
        rdEmail: null,
        rdDateReg: null,
        rdPhoto: null,
        rdStatus: null,
        rdBorrowQty: null,
        rdPwd: null,
        rdAdminRoles: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.rdID)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加读者信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const rdID = row.rdID || this.ids
      getReader(rdID).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改读者信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.rdID != null) {
            updateReader(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addReader(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },

    /** 补办按钮操作 */
    handleReissue(row) {
      const rdID = row.rdID;
      const rdName = row.rdName;

      this.$modal.confirm('确认要为读者 "' + rdName + '" (ID:' + rdID + ') 补办新借书证吗？\n\n注意：\n1. 原借书证将被注销\n2. 原账号将无法登录\n3. 未还书籍将自动转移到新证').then(function() {
        return reissueReader(rdID);
      }).then(response => {
        this.getList(); // 刷新列表
        this.$modal.alertSuccess(response.msg);
      }).catch(() => {});
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const rdIDs = row.rdID || this.ids
      this.$modal.confirm('是否确认删除读者信息编号为"' + rdIDs + '"的数据项？').then(function() {
        return delReader(rdIDs)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },

    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('library/reader/importTemplate', {}, `reader_template_${new Date().getTime()}.xlsx`)
    },
    /** 文件上传中处理 */
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    /** 文件上传成功处理 */
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    /** 提交上传文件 */
    submitFileForm() {
      this.$refs.upload.submit();
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('library/reader/export', {
        ...this.queryParams
      }, `reader_${new Date().getTime()}.xlsx`)
    }
  }

}
</script>
