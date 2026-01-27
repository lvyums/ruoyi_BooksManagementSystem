<template>
  <div class="app-container">
    <el-card>
      <el-tabs v-model="activeTab">

        <!-- ================= 借书流程 ================= -->
        <el-tab-pane label="📚 借书办理" name="lend">
          <el-row :gutter="20">
            <!-- 左侧：读者信息区 -->
            <el-col :span="10">
              <el-card shadow="never" class="box-card">
                <div slot="header">
                  <span>1. 验证读者</span>
                </div>
                <el-form :inline="true" size="small"@submit.native.prevent>
                  <el-form-item label="借书证号">
                    <el-input
                      v-model="lendForm.rdID"
                      placeholder="请输入或扫描借书证号"
                      @keyup.enter.native="queryReader"
                      clearable
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="queryReader">查询</el-button>
                  </el-form-item>
                </el-form>

                <div v-if="readerInfo" class="info-box">
                  <el-descriptions title="读者信息" :column="1" border>
                    <el-descriptions-item label="姓名">{{ readerInfo.rdName }}</el-descriptions-item>
                    <el-descriptions-item label="类别">
                      <!-- 使用 formatTypeName 方法将 ID 转换为名称 -->
                      <el-tag size="small">{{ formatTypeName(readerInfo.rdType) }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="单位">{{ readerInfo.rdDept }}</el-descriptions-item>
                    <el-descriptions-item label="状态">
                      <el-tag :type="readerInfo.rdStatus === '0' ? 'success' : 'danger'">
                        {{ statusFormat(readerInfo.rdStatus) }}
                      </el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="已借数量">
                      <span style="color: red; font-weight: bold;">{{ readerInfo.rdBorrowQty }}</span> 本
                    </el-descriptions-item>
                  </el-descriptions>
                </div>
              </el-card>
            </el-col>

            <!-- 右侧：图书信息区 -->
            <el-col :span="14">
              <el-card shadow="never" class="box-card">
                <div slot="header">
                  <span>2. 录入图书并借阅</span>
                </div>
                <el-form :inline="true" size="small" :disabled="!readerInfo || readerInfo.rdStatus !== '0'"@submit.native.prevent>
                  <el-form-item label="图书ID">
                    <el-input
                      v-model="lendForm.bkID"
                      placeholder="请输入图书ID"
                      ref="bookInput"
                      @keyup.enter.native="queryBook"
                      clearable
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="info" plain icon="el-icon-search" @click="queryBook">查询</el-button>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" icon="el-icon-check" @click="handleLend">确认借阅</el-button>
                  </el-form-item>
                </el-form>

                <div v-if="bookInfo" class="info-box">
                  <el-descriptions title="图书信息" :column="2" border>
                    <el-descriptions-item label="书名">{{ bookInfo.bkName }}</el-descriptions-item>
                    <el-descriptions-item label="作者">{{ bookInfo.bkAuthor }}</el-descriptions-item>
                    <el-descriptions-item label="出版社">{{ bookInfo.bkPress }}</el-descriptions-item>
                    <el-descriptions-item label="当前状态">
                      <el-tag :type="bookInfo.bkStatus === '0' ? 'success' : 'warning'">
                        {{ bookStatusFormat(bookInfo.bkStatus) }}
                      </el-tag>
                    </el-descriptions-item>
                  </el-descriptions>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>

        <!-- ================= 还书流程 ================= -->
        <el-tab-pane label="🔄 还书办理" name="return">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form :inline="true" size="small">
                <el-form-item label="借阅记录ID">
                  <el-input
                    v-model="returnForm.borrowID"
                    placeholder="请输入借阅ID"
                    @keyup.enter.native="queryBorrow"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="queryBorrow">查询记录</el-button>
                </el-form-item>
              </el-form>

              <div v-if="borrowInfo" class="info-box">
                <el-alert
                  v-if="borrowInfo.lsHasReturn === '1'"
                  title="该图书已归还"
                  type="success"
                  show-icon :closable="false">
                </el-alert>
                <el-descriptions title="借阅详情" :column="1" border style="margin-top: 10px">
                  <el-descriptions-item label="借阅流水号">{{ borrowInfo.borrowID }}</el-descriptions-item>
                  <el-descriptions-item label="读者ID">{{ borrowInfo.rdID }}</el-descriptions-item>
                  <el-descriptions-item label="图书ID">{{ borrowInfo.bkID }}</el-descriptions-item>
                  <el-descriptions-item label="应还日期">{{ parseTime(borrowInfo.ldDateRetPlan) }}</el-descriptions-item>
                  <el-descriptions-item label="是否超期">
                    <!-- 简单的前端计算展示，实际以点击还书后的后端计算为准 -->
                    <span v-if="isOverdue(borrowInfo.ldDateRetPlan)" style="color:red">已超期</span>
                    <span v-else style="color:green">正常</span>
                  </el-descriptions-item>
                </el-descriptions>

                <div style="margin-top: 20px; text-align: center;">
                  <el-checkbox v-model="returnForm.isLost" border>图书已遗失</el-checkbox>
                  <br/><br/>
                  <el-button type="success" icon="el-icon-refresh-left" @click="handleReturn" :disabled="borrowInfo.lsHasReturn === '1'">确认归还</el-button>
                  <!-- 续借按钮：如果已还，或者被禁用(isRenewDisabled)，则不可点 -->
                  <el-button
                    type="warning"
                    icon="el-icon-time"
                    @click="handleRenew"
                    :disabled="borrowInfo.lsHasReturn === '1' || isRenewDisabled">
                    办理续借
                  </el-button>

                  <!-- 增加一行红字提示，更直观 -->
                  <div v-if="isRenewDisabled" style="color: red; margin-top: 10px; font-size: 12px;">
                    <i class="el-icon-warning"></i> 存在超期未还书籍，续借功能已锁定
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>

      </el-tabs>
    </el-card>
  </div>
</template>

<script>
// 引入API
import { getReader } from "@/api/library/reader";
import { getBook } from "@/api/library/book";
import { getBorrow, lendBook, returnBook, renewBook } from "@/api/library/borrow";
import { listReadertype } from "@/api/library/readertype";
import { listBorrow } from "@/api/library/borrow"; // 确保引入了 list
export default {
  name: "WorkBench",
  data() {
    return {

      // 新增：是否禁用续借（默认 false）
      isRenewDisabled: false,
      activeTab: "lend",
      typeOptions: [],
      // 借书相关数据
      lendForm: {
        rdID: undefined,
        bkID: undefined
      },
      readerInfo: null,
      bookInfo: null,

      // 还书相关数据
      returnForm: {
        borrowID: undefined,
        isLost: false
      },
      borrowInfo: null
    };
  },

  created() {
    this.getReaderTypeList();
  },

  methods: {
    // ---------------- 借书逻辑 ----------------
    queryReader() {
      if (!this.lendForm.rdID) {
        this.$modal.msgError("请输入借书证号");
        return;
      }
      // 调用查询读者接口
      getReader(this.lendForm.rdID).then(response => {
        this.readerInfo = response.data;
        this.$modal.msgSuccess("读者信息读取成功");
        // ================== 新增：超期检查逻辑 ==================
        // 1. 定义查询参数：查该读者、未还的书
        const params = {
          rdID: this.readerInfo.rdID,
          lsHasReturn: '0' // 只查未还的
        };

        // 2. 调用借阅查询接口
        listBorrow(params).then(res => {
          const borrowList = res.rows || [];
          const now = new Date();

          // 3. 遍历检查是否有超期的
          let hasOverdue = false;
          let overdueCount = 0;

          borrowList.forEach(item => {
            // 后端日期字符串转时间对象
            const planDate = new Date(item.ldDateRetPlan);
            if (planDate < now) {
              hasOverdue = true;
              overdueCount++;
            }
          });

          // 4. 如果有超期，进行封锁
          if (hasOverdue) {
            this.$modal.msgError(`警告：该读者有 ${overdueCount} 本书超期未还！已锁定借阅权限。`);

            // 强制修改本地状态，让右侧借书框变灰 (利用你之前的 :disabled 逻辑)
            // 我们可以自定义一个状态叫 'locked'
            this.readerInfo.rdStatus = 'locked';
          } else {
            // 没超期，正常聚焦借书框
            this.$modal.msgSuccess("读者状态正常");
            this.$nextTick(() => {
              this.$refs.bookInput.focus();
            });
          }
        });
      }).catch(() => {
        this.readerInfo = null;
      });
    },
    queryBook() {
      if (!this.lendForm.bkID) {
        this.$modal.msgWarning("请输入图书ID");
        return;
      }
      // 调用 getBook (按主键查询)
      getBook(this.lendForm.bkID).then(response => {
        // 如果后端返回 data 为 null，说明查无此书
        if (!response.data) {
          this.bookInfo = null;
          this.$modal.msgError("未找到该ID对应的图书");
          return;
        }
        this.bookInfo = response.data;
        // this.$modal.msgSuccess("图书信息加载成功");
      }).catch(() => {
        this.bookInfo = null;
      });
    },
    handleLend() {
      if (!this.readerInfo) {
        this.$modal.msgError("请先验证读者");
        return;
      }
      // 强制要求先查询出图书信息，防止输入错误的ID直接借阅
      if (!this.bookInfo || !this.lendForm.bkID) {
        this.$modal.msgError("请先点击查询按钮，确认图书信息无误后再借阅");
        return;
      }

      // 校验状态：只有“在馆 (0)”的书才能借
      if (this.bookInfo.bkStatus !== '0') {
        this.$modal.msgError("当前图书状态不可借阅（可能已借出或丢失）");
        return;
      }

      lendBook(this.readerInfo.rdID, this.lendForm.bkID).then(response => {
        this.$modal.msgSuccess("借阅成功！");
        this.queryReader(); // 刷新读者借书数量

        // 重置图书表单
        this.lendForm.bkID = undefined;
        this.bookInfo = null;

        // 聚焦回输入框
        this.$nextTick(() => {
          this.$refs.bookInput.focus();
        });
      });
    },

    // ---------------- 还书逻辑 ----------------
    queryBorrow() {
      if (!this.returnForm.borrowID) return;
      // 1. 先重置状态
      this.isRenewDisabled = false;

      // 2. 查询当前这条借阅记录
      getBorrow(this.returnForm.borrowID).then(response => {
        this.borrowInfo = response.data;

        if (!this.borrowInfo) {
          this.$modal.msgError("未找到借阅记录");
          return;
        }

        // 3. 【核心逻辑】拿着读者ID，去查他名下所有未还的书
        this.checkReaderOverdueStatus(this.borrowInfo.rdID);

      }).catch(() => {
        this.borrowInfo = null;
      });
    },

// 【新增方法】检查读者是否有超期书
    checkReaderOverdueStatus(rdID) {
      const params = {
        rdID: rdID,
        lsHasReturn: '0' // 只查未还的
      };

      listBorrow(params).then(res => {
        const list = res.rows || [];
        const now = new Date();
        let overdueCount = 0;
        let isCurrentBookOverdue = false; // 当前这本书是否超期

        list.forEach(item => {
          // 比较日期
          if (new Date(item.ldDateRetPlan) < now) {
            overdueCount++;
            // 顺便看看是不是当前查的这本书
            if (item.borrowID == this.returnForm.borrowID) {
              isCurrentBookOverdue = true;
            }
          }
        });

        // 4. 判断逻辑
        if (overdueCount > 0) {
          this.isRenewDisabled = true; // 禁用续借按钮

          // 组织提示语
          let msg = "";
          if (isCurrentBookOverdue) {
            msg = "当前图书已超期，无法续借，请尽快归还！";
          } else {
            msg = `该读者名下有 ${overdueCount} 本书超期未还，已被限制续借权限！请先归还所有超期书籍。`;
          }

          // 5. 弹出警告 (前端告知)
          this.$modal.msgWarning(msg);
        }
      });
    },
    handleReturn() {
      this.$modal.confirm('确认归还该图书吗？' + (this.returnForm.isLost ? ' (已标记为遗失)' : '')).then(() => {
        returnBook(this.returnForm.borrowID, this.returnForm.isLost).then(res => {
          this.$modal.msgSuccess("归还成功");
          this.queryBorrow(); // 刷新显示
        });
      });
    },
    handleRenew() {
      this.$modal.confirm('确认续借该图书吗？').then(() => {
        renewBook(this.returnForm.borrowID).then(res => {
          this.$modal.msgSuccess("续借成功");
          this.queryBorrow(); // 刷新显示
        });
      });
    },

    // ---------------- 辅助方法 ----------------
    statusFormat(status) {
      const map = {'0': '有效', '1': '挂失', '2': '注销'};
      return map[status] || status;
    },
    bookStatusFormat(status) {
      const map = {'0': '在馆', '1': '借出', '2': '遗失', '3': '变卖', '4': '销毁'};
      return map[status] || status;
    },
    isOverdue(planDate) {
      if(!planDate) return false;
      return new Date() > new Date(planDate);
    },
    // 4. 新增获取类别列表的方法 (添加这个方法)
    getReaderTypeList() {
      listReadertype().then(response => {
        this.typeOptions = response.rows;
      });
    },

    // 5. 新增格式化名称的方法
    formatTypeName(rdType) {
      if (!rdType) return '';
      // 在 typeOptions 数组里找 id 匹配的那一项
      const found = this.typeOptions.find(item => item.rdType == rdType);
      return found ? found.rdTypeName : rdType; // 找到了返回名称，找不到返回ID
    }
  }
};
</script>

<style scoped>
.info-box {
  margin-top: 20px;
}
</style>
