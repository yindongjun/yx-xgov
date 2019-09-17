<template>
  <section class="main-data-list">
    <!-- <div class="main-data-t">
      <el-row>
        <el-form
          :model="ruleForm"
          ref="ruleForm"
          inline
          class="demo-form-inline"
        >
          <el-form-item label="数据库类型">
            <el-select
              v-model="ruleForm.type"
              size="mini"
              placeholder="请选择数据库类型"
              @change="dataSelect"
            >
              <el-option value="" label="全部"></el-option>
              <el-option value="MySQL" label="MySQL"></el-option>
              <el-option value="Oracle" label="Oracle"></el-option>
              <el-option value="SqlServer" label="SqlServer"></el-option>
              <el-option value="DB2" label="DB2"></el-option>
              <el-option value="PostgreSQL" label="PostgreSQL"></el-option>
              <el-option value="Sybase" label="Sybase"></el-option>
              <el-option value="Teradata" label="Teradata"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="数据源名称">
            <el-input
              v-model="ruleForm.name"
              style="width: 300px;"
              @keyup.enter.native="searchList"
              size="mini"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="danger" size="mini" @click="searchList"
              >查 询</el-button
            >
          </el-form-item>
        </el-form>
      </el-row>
    </div> -->
    <el-row>
      <el-button class="grey-btn" @click="selectSource" size="small"
        >选择数据源</el-button
      >
    </el-row>
    <ul class="main-data-b">
      <div v-if="listInfo.length === 0" class="no-data-pic-outer valign">
        <div class="no-data-box">
          <img
            class="no-data-pic"
            src="../../../../assets/images/noDataPic.png"
          />
          <p class="no-data-text">暂无数据</p>
        </div>
      </div>
      <li :key="item.id" v-for="item in listInfo">
        <p class="check-header">{{ item.dataName }}</p>
        <div class="check-body">
          <span>{{ item.dataName }}</span
          ><br />
          数据源名称
        </div>
        <div class="check-footer">数据表个数：{{ item.tableNumber }}个</div>
        <div class="check-set">
          <span @click="editTask(item.id)"
            ><svg-icon iconClass="编辑1"></svg-icon
          ></span>
          <span @click="deleteTask(item.id)"
            ><svg-icon iconClass="删除"></svg-icon
          ></span>
        </div>
      </li>
    </ul>
    <el-pagination
      class="ar"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="12"
      layout="total, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>
    <el-dialog
      :close-on-click-modal="false"
      title="提示"
      :visible.sync="dialogVisible"
      @open="openModal"
      @close="closeModal"
      width="536px"
    >
      <el-table
        :data="sourceList"
        @selection-change="handleSelectionChange"
        height="350"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="datasourceName" label="数据源名称">
        </el-table-column>
        <el-table-column prop="databaseType" label="数据库类型">
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" type="info" size="small"
          >取 消</el-button
        >
        <el-button
          type="danger"
          size="small"
          @click="submitSource"
          :loading="buttonLoading"
          >保 存</el-button
        >
      </span>
    </el-dialog>
  </section>
</template>

<script>
export default {
  name: "",
  components: {},
  data() {
    return {
      buttonLoading: false,
      dialogVisible: false,
      selectList: [],
      sourceList: [
        { name: "tesk-ku", type: "ETL" },
        { name: "tesk-ku", type: "ETL" },
        { name: "tesk-ku", type: "ETL" },
        { name: "tesk-ku", type: "ETL" }
      ],
      ruleForm: {
        type: "",
        name: ""
      },
      total: 0,
      currentPage: 1,
      pageLength: 12,
      listInfo: []
    };
  },
  methods: {
    getList() {
      let param = {
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength,
        maindataName: this.ruleForm.name,
        type: this.ruleForm.type
      };
      this.$urlApi.propertyManage.queryMainData(param).then(res => {
        this.listInfo = res.data;
        this.total = res.recordsFiltered;
      });
    },
    dataSelect() {
      this.getList();
    },
    handleSizeChange(cb) {
      this.this.pageLength = cb;
      this.currentPage = 1;
      this.getList();
    },
    handleCurrentChange(cb) {
      this.currentPage = cb;
      this.getList();
    },
    searchList() {
      this.getList();
    },
    selectSource() {
      this.dialogVisible = true;
    },
    openModal() {
      this.$urlApi.propertyManage.getDatasource().then(res => {
        this.sourceList = res.data;
      });
    },
    closeModal() {
      this.sourceList = [];
      this.selectList = [];
    },
    handleSelectionChange(val) {
      this.selectList = val;
    },
    submitSource() {
      this.buttonLoading = true;
      this.$urlApi.propertyManage
        .addMainData({ dataSourceIds: this.selectList.map(c =>c.id).join(',') })
        .then(res => {
          this.buttonLoading = false;
          this.sourceList = res.data;
          this.dialogVisible = false;
          this.$toast("success", "新建成功");
          this.getList();
        })
        .catch(e => {
          this.buttonLoading = false;
        });
    },
    editTask(id) {
      this.$router.push({
        path: "/property/default/maindata/detail",
        query: { id: id }
      });
    },
    deleteTask(id) {
      this.$confirm("确认要删除吗", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      })
        .then(() => {
          this.$urlApi.propertyManage.deleteDataBase({ id: id }).then(res => {
            this.$toast("success", "删除成功");
            this.getList();
          });
        })
        .catch(() => {});
    }
  },
  mounted: function() {
    this.getList();
  }
};
</script>

<style lang="scss">
.main-data-list {
  height: 100%;
  .el-dialog {
    .el-dialog__body {
      padding: 15px;
      padding-top: 0;
      .el-table {
        td,
        .el-table th {
          padding: 6px 0;
        }
      }
    }
  }
  .main-data-b {
    margin-top: 30px;
    height: -moz-calc(100% - 175px);
    height: -webkit-calc(100% - 175px);
    height: calc(100% - 175px);
    overflow: auto;
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;
    li {
      margin-right: 60px;
      overflow: hidden;
      margin-bottom: 30px;
      border: 1px solid #e1e5ec;
      text-align: center;
      width: 187px;
      box-shadow: 0px 1px 4px 0px rgba(215, 215, 215, 0.5);
      border-radius: 2px;
      position: relative;
      transition: linear 2s; /* Safari and Chrome */
      height: 142px;
      .check-header {
        line-height: 32px;
        background: #acb5c3;
        color: #ffffff;
        margin: 0;
      }
      .check-body {
        background: rgba(249, 249, 250, 1);
        padding-top: 19px;
        box-sizing: border-box;
        padding-bottom: 16px;
        span {
          line-height: 28px;
          font-size: 16px;
          font-weight: bold;
        }
      }
      .check-footer {
        line-height: 32px;
        border-top: 1px solid #e1e5ec;
        height: 32px;
      }
      .check-set {
        position: absolute;
        width: 100%;
        height: 32px;
        background: rgba(51, 55, 67, 0.7);
        bottom: -32px;
        left: 0;
        cursor: pointer;
        padding-left: 48px;
        box-sizing: border-box;
        transition: 0.5s;
        span {
          width: 45px;
          float: left;
          height: 100%;
          font-size: 16px;
          line-height: 32px;
          text-align: center;
          color: #ffffff;
          &:hover {
            background: rgba(255, 255, 255, 0.27);
          }
        }
      }
      &:hover {
        .check-set {
          bottom: 0;
        }
      }
    }
    .no-data-pic-outer {
      height: 100%;
      text-align: center;
      position: relative;
      width: 100%;
      .no-data-box {
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        .no-data-text {
          color: #9da1a9;
          padding: 20px 0;
        }
      }
    }
  }
}
</style>
