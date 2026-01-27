<template>
  <div class="app-container">
    <!-- 1. 顶部概览卡片 (提升成熟度) -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #409EFF"><i class="el-icon-reading"></i></div>
          <div class="stat-info">
            <div class="stat-title">当前借阅</div>
            <div class="stat-value">{{ borrowList.length }} <span class="unit">本</span></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #E6A23C"><i class="el-icon-timer"></i></div>
          <div class="stat-info">
            <div class="stat-title">即将到期(5天内)</div>
            <div class="stat-value text-warning">{{ soonDueCount }} <span class="unit">本</span></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #F56C6C"><i class="el-icon-warning-outline"></i></div>
          <div class="stat-info">
            <div class="stat-title">已超期</div>
            <div class="stat-value text-danger">{{ overdueCount }} <span class="unit">本</span></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card" style="background: #fdf6ec; border-color: #faecd8;">
          <div style="font-size: 14px; color: #e6a23c; line-height: 1.8;">
            <i class="el-icon-info"></i> <b>续借规则提示：</b><br/>
            1. 超期图书不可续借<br/>
            2. 已达最大续借次数不可续借<br/>
            3. 请在到期前及时处理
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 2. 核心功能区 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span><i class="el-icon-notebook-1"></i> 未归还图书列表</span>
        <div style="float: right;">
          <el-button
            type="primary"
            icon="el-icon-refresh"
            :disabled="multipleSelection.length === 0"
            @click="handleBatchRenew"
          >批量续借</el-button>
          <el-button icon="el-icon-refresh-right" circle @click="getList"></el-button>
        </div>
      </div>

      <!-- 3. 数据表格 -->
      <el-table
        v-loading="loading"
        :data="borrowList"
        border
        stripe
        @selection-change="handleSelectionChange"
        :row-class-name="tableRowClassName"
      >
        <el-table-column type="selection" width="55" align="center" :selectable="canRenew" />

        <el-table-column label="借书日期" align="center" prop="ldDateOut" width="120">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.ldDateOut, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>

        <el-table-column label="图书名称" min-width="180" align="center">
          <template slot-scope="scope">
            <!-- 直接取 row.bookName，如果没有就显示 ID -->
            {{ scope.row.bookName ? scope.row.bookName : scope.row.bkID }}
          </template>
        </el-table-column>

        <el-table-column label="应还日期" align="center" prop="ldDateRetPlan" width="120">
          <template slot-scope="scope">
            {{ parseTime(scope.row.ldDateRetPlan, '{y}-{m}-{d}') }}
          </template>
        </el-table-column>

        <el-table-column label="状态提示" align="center" width="140">
          <template slot-scope="scope">
            <el-tag v-if="isOverdue(scope.row.ldDateRetPlan)" type="danger" effect="dark">
              <i class="el-icon-error"></i> 已超期
            </el-tag>
            <el-tag v-else-if="isSoonDue(scope.row.ldDateRetPlan)" type="warning" effect="dark">
              <i class="el-icon-time"></i> 即将到期
            </el-tag>
            <el-tag v-else type="success" effect="plain">
              <i class="el-icon-circle-check"></i> 借阅中
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="续借次数" align="center" prop="ldContinueTimes" width="100">
          <template slot-scope="scope">
            <el-tag size="medium">{{ scope.row.ldContinueTimes }} 次</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" width="120" fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-time"
              :disabled="!canRenew(scope.row)"
              @click="handleRenew(scope.row)"
            >续借</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { listBorrow, renewBook } from "@/api/library/borrow";
// 如果后端没有返回书名，需要引入 getBook 接口手动查询，建议后端 BorrowMapper.xml 关联查询 bkName
// import { getBook } from "@/api/library/book";

export default {
  name: "PersonalBorrow",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 借阅表格数据
      borrowList: [],
      // 选中数组
      multipleSelection: []
    };
  },
  computed: {
    // 计算即将到期的数量
    soonDueCount() {
      return this.borrowList.filter(item => this.isSoonDue(item.ldDateRetPlan) && !this.isOverdue(item.ldDateRetPlan)).length;
    },
    // 计算超期的数量
    overdueCount() {
      return this.borrowList.filter(item => this.isOverdue(item.ldDateRetPlan)).length;
    }
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询借阅列表 */
    getList() {
      this.loading = true;
      // 构造查询参数：只查未还的
      const queryParams = {
        lsHasReturn: '0'
      };

      listBorrow(queryParams).then(response => {
        this.borrowList = response.rows;
        this.loading = false;

        // 如果后端没返回书名，这里可以遍历 IDs 去查书名，但为了性能建议后端关联查询
      });
    },

    /** 状态计算逻辑：是否超期 */
    isOverdue(planDate) {
      if (!planDate) return false;
      return new Date() > new Date(planDate); // 当前时间 > 应还时间
    },

    /** 状态计算逻辑：是否即将到期 (5天内) */
    isSoonDue(planDate) {
      if (!planDate) return false;
      const now = new Date();
      const plan = new Date(planDate);
      const diffTime = plan - now;
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      return diffDays >= 0 && diffDays <= 5;
    },

    /** 判断某一行是否允许续借 (用于 Checkbox 禁用) */
    canRenew(row) {
      // 规则：超期不能续借
      if (this.isOverdue(row.ldDateRetPlan)) return false;
      // 规则：这里可以加次数判断，例如 > 2 次禁用，需结合后端读者类别判断，前端暂不做硬性限制
      return true;
    },

    /** 表格行样式 */
    tableRowClassName({row, rowIndex}) {
      if (this.isOverdue(row.ldDateRetPlan)) {
        return 'error-row'; // 变红
      } else if (this.isSoonDue(row.ldDateRetPlan)) {
        return 'warning-row'; // 变橙
      }
      return '';
    },

    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.multipleSelection = selection;
    },

    /** 单本续借 */
    handleRenew(row) {
      this.$confirm('确认要续借《' + (row.params?.bookName || '该图书') + '》吗？', "续借确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return renewBook(row.borrowID);
      }).then(() => {
        this.$message.success("续借成功！应还日期已延长。");
        this.getList(); // 刷新列表
      }).catch(err => {
        // 后端抛出异常（如次数超限），这里会自动显示错误提示
      });
    },

    /** 批量续借 */
    handleBatchRenew() {
      const ids = this.multipleSelection.map(item => item.borrowID);
      const names = this.multipleSelection.map(item => item.params?.bookName || item.bkID).join(", ");

      this.$confirm('确认要批量续借以下图书吗？\n' + names, "批量续借", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(async () => {
        // 循环调用续借接口 (简单实现)
        let successCount = 0;
        for (let id of ids) {
          try {
            await renewBook(id);
            successCount++;
          } catch (e) {
            // 某一本失败不影响其他
          }
        }
        this.$message.success("批量操作完成，成功续借 " + successCount + " 本");
        this.getList();
      });
    }
  }
};
</script>

<style scoped>
.mb-20 {
  margin-bottom: 20px;
}
.stat-card {
  display: flex;
  align-items: center;
  padding: 10px;
  cursor: pointer;
}
.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-right: 15px;
}
.stat-info .stat-title {
  color: #909399;
  font-size: 14px;
  margin-bottom: 5px;
}
.stat-info .stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}
.stat-info .unit {
  font-size: 12px;
  color: #909399;
  font-weight: normal;
}
.text-warning { color: #E6A23C !important; }
.text-danger { color: #F56C6C !important; }

/* 表格超期样式 */
::v-deep .el-table .error-row {
  background: #fef0f0;
}
::v-deep .el-table .warning-row {
  background: #fdf6ec;
}
</style>
