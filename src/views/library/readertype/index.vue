<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="类别名称" prop="rdTypeName">
        <el-input
          v-model="queryParams.rdTypeName"
          placeholder="请输入类别名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="可借书数量" prop="CanLendQty">
        <el-input
          v-model="queryParams.CanLendQty"
          placeholder="请输入可借书数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="可借书天数" prop="CanLendDay">
        <el-input
          v-model="queryParams.CanLendDay"
          placeholder="请输入可借书天数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="可续借次数" prop="CanContinueTimes">
        <el-input
          v-model="queryParams.CanContinueTimes"
          placeholder="请输入可续借次数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="罚款率(元/天)" prop="PunishRate">
        <el-input
          v-model="queryParams.PunishRate"
          placeholder="请输入罚款率(元/天)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="证书有效期(年)" prop="DateValid">
        <el-input
          v-model="queryParams.DateValid"
          placeholder="请输入证书有效期(年)"
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
          v-hasPermi="['library:readertype:add']"
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
          v-hasPermi="['library:readertype:edit']"
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
          v-hasPermi="['library:readertype:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['library:readertype:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="readertypeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="读者类别代码" align="center" prop="rdType" />
      <el-table-column label="类别名称" align="center" prop="rdTypeName" />
      <el-table-column label="可借书数量" align="center" prop="CanLendQty" />
      <el-table-column label="可借书天数" align="center" prop="CanLendDay" />
      <el-table-column label="可续借次数" align="center" prop="CanContinueTimes" />
      <el-table-column label="罚款率(元/天)" align="center" prop="PunishRate" />
      <el-table-column label="证书有效期(年)" align="center" prop="DateValid" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['library:readertype:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['library:readertype:remove']"
          >删除</el-button>
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

    <!-- 添加或修改读者类别对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="类别名称" prop="rdTypeName">
          <el-input v-model="form.rdTypeName" placeholder="请输入类别名称" />
        </el-form-item>
        <el-form-item label="可借书数量" prop="CanLendQty">
          <el-input v-model="form.CanLendQty" placeholder="请输入可借书数量" />
        </el-form-item>
        <el-form-item label="可借书天数" prop="CanLendDay">
          <el-input v-model="form.CanLendDay" placeholder="请输入可借书天数" />
        </el-form-item>
        <el-form-item label="可续借次数" prop="CanContinueTimes">
          <el-input v-model="form.CanContinueTimes" placeholder="请输入可续借次数" />
        </el-form-item>
        <el-form-item label="罚款率(元/天)" prop="PunishRate">
          <el-input v-model="form.PunishRate" placeholder="请输入罚款率(元/天)" />
        </el-form-item>
        <el-form-item label="证书有效期(年)" prop="DateValid">
          <el-input v-model="form.DateValid" placeholder="请输入证书有效期(年)" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReadertype, getReadertype, delReadertype, addReadertype, updateReadertype } from "@/api/library/readertype"

export default {
  name: "Readertype",
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
      // 读者类别表格数据
      readertypeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        rdTypeName: null,
        CanLendQty: null,
        CanLendDay: null,
        CanContinueTimes: null,
        PunishRate: null,
        DateValid: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        rdTypeName: [
          { required: true, message: "类别名称不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询读者类别列表 */
    getList() {
      this.loading = true
      listReadertype(this.queryParams).then(response => {
        this.readertypeList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        rdType: null,
        rdTypeName: null,
        CanLendQty: null,
        CanLendDay: null,
        CanContinueTimes: null,
        PunishRate: null,
        DateValid: null
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
      this.ids = selection.map(item => item.rdType)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加读者类别"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const rdType = row.rdType || this.ids
      getReadertype(rdType).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改读者类别"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.rdType != null) {
            updateReadertype(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addReadertype(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const rdTypes = row.rdType || this.ids
      this.$modal.confirm('是否确认删除读者类别编号为"' + rdTypes + '"的数据项？').then(function() {
        return delReadertype(rdTypes)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('library/readertype/export', {
        ...this.queryParams
      }, `readertype_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
