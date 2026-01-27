<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="图书条码/书号" prop="bkCode">
        <el-input
          v-model="queryParams.bkCode"
          placeholder="请输入图书条码/书号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="书名" prop="bkName">
        <el-input
          v-model="queryParams.bkName"
          placeholder="请输入书名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="作者" prop="bkAuthor">
        <el-input
          v-model="queryParams.bkAuthor"
          placeholder="请输入作者"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="出版社" prop="bkPress">
        <el-input
          v-model="queryParams.bkPress"
          placeholder="请输入出版社"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="出版日期" prop="bkDatePress">
        <el-date-picker clearable
          v-model="queryParams.bkDatePress"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择出版日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="ISBN" prop="bkISBN">
        <el-input
          v-model="queryParams.bkISBN"
          placeholder="请输入ISBN"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分类号" prop="bkCatalog">
        <el-input
          v-model="queryParams.bkCatalog"
          placeholder="请输入分类号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="语言" prop="bkLanguage">
        <el-input
          v-model="queryParams.bkLanguage"
          placeholder="请输入语言(0中文 1英文 2日文)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="页数" prop="bkPages">
        <el-input
          v-model="queryParams.bkPages"
          placeholder="请输入页数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="价格" prop="bkPrice">
        <el-input
          v-model="queryParams.bkPrice"
          placeholder="请输入价格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="入馆日期" prop="bkDateIn">
        <el-date-picker clearable
          v-model="queryParams.bkDateIn"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择入馆日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="状态" prop="bkStatus">
        <el-select v-model="queryParams.bkStatus" placeholder="请选择状态(0在馆 1借出 2遗失 3变卖 4销毁)" clearable>
          <el-option
            v-for="dict in dict.type.library_book_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['library:book:add']"
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
          v-hasPermi="['library:book:edit']"
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
          v-hasPermi="['library:book:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['library:book:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="bookList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="图书序号" align="center" prop="bkID" />
      <el-table-column label="图书条码/书号" align="center" prop="bkCode" />
      <el-table-column label="书名" align="center" prop="bkName" />
      <el-table-column label="作者" align="center" prop="bkAuthor" />
      <el-table-column label="出版社" align="center" prop="bkPress" />
      <el-table-column label="出版日期" align="center" prop="bkDatePress" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.bkDatePress, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="ISBN" align="center" prop="bkISBN" />
      <el-table-column label="分类号" align="center" prop="bkCatalog" />
      <el-table-column label="语言(0中文 1英文 2日文)" align="center" prop="bkLanguage" />
      <el-table-column label="页数" align="center" prop="bkPages" />
      <el-table-column label="价格" align="center" prop="bkPrice" />
      <el-table-column label="入馆日期" align="center" prop="bkDateIn" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.bkDateIn, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="内容简介" align="center" prop="bkBrief" />
      <el-table-column label="封面" align="center" prop="bkCover" />
      <el-table-column label="状态(0在馆 1借出 2遗失 3变卖 4销毁)" align="center" prop="bkStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.library_book_status" :value="scope.row.bkStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['library:book:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['library:book:remove']"
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

    <!-- 添加或修改图书信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="图书条码/书号" prop="bkCode">
          <el-input v-model="form.bkCode" placeholder="请输入图书条码/书号" />
        </el-form-item>
        <el-form-item label="书名" prop="bkName">
          <el-input v-model="form.bkName" placeholder="请输入书名" />
        </el-form-item>
        <el-form-item label="作者" prop="bkAuthor">
          <el-input v-model="form.bkAuthor" placeholder="请输入作者" />
        </el-form-item>
        <el-form-item label="出版社" prop="bkPress">
          <el-input v-model="form.bkPress" placeholder="请输入出版社" />
        </el-form-item>
        <el-form-item label="出版日期" prop="bkDatePress">
          <el-date-picker clearable
            v-model="form.bkDatePress"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择出版日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="ISBN" prop="bkISBN">
          <el-input v-model="form.bkISBN" placeholder="请输入ISBN" />
        </el-form-item>
        <el-form-item label="分类号" prop="bkCatalog">
          <el-input v-model="form.bkCatalog" placeholder="请输入分类号" />
        </el-form-item>
        <el-form-item label="语言(0中文 1英文 2日文)" prop="bkLanguage">
          <el-input v-model="form.bkLanguage" placeholder="请输入语言(0中文 1英文 2日文)" />
        </el-form-item>
        <el-form-item label="页数" prop="bkPages">
          <el-input v-model="form.bkPages" placeholder="请输入页数" />
        </el-form-item>
        <el-form-item label="价格" prop="bkPrice">
          <el-input v-model="form.bkPrice" placeholder="请输入价格" />
        </el-form-item>
        <el-form-item label="入馆日期" prop="bkDateIn">
          <el-date-picker clearable
            v-model="form.bkDateIn"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择入馆日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="内容简介" prop="bkBrief">
          <el-input v-model="form.bkBrief" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="状态(0在馆 1借出 2遗失 3变卖 4销毁)" prop="bkStatus">
          <el-select v-model="form.bkStatus" placeholder="请选择状态(0在馆 1借出 2遗失 3变卖 4销毁)">
            <el-option
              v-for="dict in dict.type.library_book_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
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
import { listBook, getBook, delBook, addBook, updateBook } from "@/api/library/book"

export default {
  name: "Book",
  dicts: ['library_book_status'],
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
      // 图书信息表格数据
      bookList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bkCode: null,
        bkName: null,
        bkAuthor: null,
        bkPress: null,
        bkDatePress: null,
        bkISBN: null,
        bkCatalog: null,
        bkLanguage: null,
        bkPages: null,
        bkPrice: null,
        bkDateIn: null,
        bkBrief: null,
        bkCover: null,
        bkStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询图书信息列表 */
    getList() {
      this.loading = true
      listBook(this.queryParams).then(response => {
        this.bookList = response.rows
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
        bkID: null,
        bkCode: null,
        bkName: null,
        bkAuthor: null,
        bkPress: null,
        bkDatePress: null,
        bkISBN: null,
        bkCatalog: null,
        bkLanguage: null,
        bkPages: null,
        bkPrice: null,
        bkDateIn: null,
        bkBrief: null,
        bkCover: null,
        bkStatus: null
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
      this.ids = selection.map(item => item.bkID)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加图书信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const bkID = row.bkID || this.ids
      getBook(bkID).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改图书信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.bkID != null) {
            updateBook(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addBook(this.form).then(response => {
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
      const bkIDs = row.bkID || this.ids
      this.$modal.confirm('是否确认删除图书信息编号为"' + bkIDs + '"的数据项？').then(function() {
        return delBook(bkIDs)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('library/book/export', {
        ...this.queryParams
      }, `book_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
