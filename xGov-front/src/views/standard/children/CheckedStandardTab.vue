<template>
  <section class="checked-standard-tab-section">
    <div class="search_list">
      <el-row>
        <el-col :span="24">
          <span class="search_title">分类:</span>
          <el-radio-group
            v-model="searchType"
            size="small"
            @change="changeStatus"
          >
            <el-radio-button label="">全部</el-radio-button>
            <el-radio-button label="0">标准维护</el-radio-button>
            <el-radio-button label="1">标准分类</el-radio-button>
            <el-radio-button label="2">代码</el-radio-button>
          </el-radio-group>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <span class="search_title">审核结果:</span>
          <el-radio-group
            v-model="checkResult"
            size="small"
            @change="checkResultHandle"
          >
            <el-radio-button label="">全部</el-radio-button>
            <el-radio-button label="1">通过</el-radio-button>
            <el-radio-button label="2">退回</el-radio-button>
          </el-radio-group>
          <div style="float: right">
            <el-input
              v-model="searchName"
              @keyup.enter.native="changeStatus"
              placeholder="请输入任务名称进行查询"
              style="width: 300px"
              size="mini"
            >
              <i slot="prefix" class="el-input__icon el-icon-search"></i>
            </el-input>
            <el-button type="danger" size="mini" @click="changeStatus"
              >查询</el-button
            >
          </div>
        </el-col>
      </el-row>
    </div>
    <el-row class="mt20 check-standard-content">
      <el-table
        :data="tableData"
        style="width: 100%;"
        height="calc(100% - 120px)"
      >
        <template slot="empty">
          <div class="show-empty">
            暂无数据，请先在<span style="color: #3BA3F8; cursor:pointer" @click="backToCheck"> 待审核标准 </span
            >进行审核
          </div>
        </template>
        <el-table-column prop="taskName" label="任务名称" show-overflow-tooltip>
          <template slot-scope="scope">
            <div
              type="text"
              style="white-space: nowrap;overflow: hidden;text-overflow: ellipsis;"
              class="dataElementName-class"
              @click="showDataSource(scope.row)"
            >
              {{ scope.row.taskName }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="所属类型" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.type == 0">标准维护</span>
            <span v-if="scope.row.type == 1">标准分类</span>
            <span v-if="scope.row.type == 2">代码</span>
          </template>
        </el-table-column>
        <el-table-column prop="submitter" label="审核人" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          prop="auditTime"
          label="审核时间"
          :formatter="this.$utils.dateFormat"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column label="审核结果" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-tag class="success-tag" v-if="scope.row.auditResult == 1"
              >通过</el-tag
            >
            <el-tag class="error-tag" v-if="scope.row.auditResult == 2"
              >退回</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-button
              class="operate-btn"
              @click="showDetail(scope.row.id)"
              type="text"
              >审核记录</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="ar mt15"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="10"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-row>
    <el-dialog
      :close-on-click-modal="false"
      title="审核记录"
      @open="openCheckList"
      @close="closeCheckList"
      :visible.sync="dialogTableVisible"
    >
      <el-table
        :data="checkDataList"
        height="300px"
        style="width: 100%;"
      >
        <el-table-column prop="auditor" label="审核人" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          prop="auditTime"
          label="审核时间"
          :formatter="this.$utils.dateFormat"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column label="审核结果" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-tag class="success-tag" v-if="scope.row.auditResult == 1"
              >通过</el-tag
            >
            <el-tag class="error-tag" v-if="scope.row.auditResult == 2"
              >退回</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column
          prop="auditDesc"
          label="审核描述"
          show-overflow-tooltip
        >
        </el-table-column>
      </el-table>
      <el-pagination
        class="ar mt15"
        @size-change="handleSizeChangeResult"
        @current-change="handleCurrentChangeResult"
        :current-page="currentPageResult"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="10"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalResult"
      >
      </el-pagination>
    </el-dialog>
    <add-update-meta
      :AddUpdateVisible="AddUpdateVisible"
      :isEditMeta="isEditMeta"
      @AddUpdateVisibleBack="AddUpdateVisibleBack"
    ></add-update-meta>
    <add-or-update-data-set-dialog
      :isEditMeta="isEditSet"
      :addOrUpdateDataSetDialogVisible="addOrUpdateDataSetDialogVisible"
      @addOrUpdateDataSetDialogVisibleEvent="updateDialogStatus"
    ></add-or-update-data-set-dialog>
    <add-edit-code
      :currNodeId="currNodeId"
      :AddEditCodeListVisible="AddEditCodeListVisible"
      :isEditCodeList="isEditCodeList"
      @editCodeListBack="editCodeListBack"
    ></add-edit-code>
  </section>
</template>

<script>
import AddUpdateMeta from "./dataMeta/AddUpdateMeta.vue";
import AddOrUpdateDataSetDialog from "./dataCollect/AddOrUpdateDataSetDialog";
import AddEditCode from "./business/AddEditCode.vue";
export default {
  name: "checked-standard-tab",
  components: {
    AddUpdateMeta,
    AddOrUpdateDataSetDialog,
    AddEditCode
  },
  props: ["checkedStandardVisible"],
  data() {
    return {
      total: 0,
      currentPage: 1,
      pageLength: 10,
      deleteSelection: [],

      dialogTableVisible: false,

      searchType: "",
      searchName: "",
      checkResult: "",
      tableData: [],

      checkDataList: [],
      totalResult: 0,
      currentPageResult: 1,
      pageLengthResult: 10,
      CurrId: "",
      AddUpdateVisible: false,
      addOrUpdateDataSetDialogVisible: false,
      AddEditCodeListVisible: false,
      isEditSet: {
        id: "",
        currTime: new Date().getTime(),
        isHistory: false
      },
      isEditMeta: {
        id: "",
        currTime: new Date().getTime(),
        isHistory: false
      },
      isEditCodeList: {
        currTime: new Date().getTime(),
        id: "",
        isHistory: false
      },
      currNodeId: ""
    };
  },
  methods: {
    getList(page) {
      page && (this.currentPage = page);
      let param = {
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength,
        type: this.searchType,
        auditresult: this.checkResult,
        taskName: this.searchName,
        status: "1"
      };
      this.$urlApi.dataStandard.standardAuditList(param).then(res => {
        this.tableData = res.data;
        this.total = res.recordsFiltered;
      });
    },
    handleSizeChange(cb) {
      this.pageLength = cb;
      this.getList(1);
    },
    handleCurrentChange(cb) {
      this.currentPage = cb;
      this.getList();
    },
    changeFun(val) {
      this.deleteSelection = val;
    },
    changeStatus(val) {
      this.getList(1);
    },
    checkResultHandle(val) {
      this.getList(1);
    },
    updataAudit(row) {
      if (row.status == 1 || row.status == 2) {
        this.$toast("warning", "只有待审核的才能被审核");
        return;
      }
      this.$confirm("确认要审核吗", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      })
        .then(() => {
          this.$urlApi.dataStandard
            .updataAuditStatus({ ids: row.id })
            .then(res => {
              this.getList();
              this.$toast("success", "审核成功");
            });
        })
        .catch(() => {});
    },
    getResultList() {
      let param = {
        start: (this.currentPageResult - 1) * 10,
        length: this.pageLengthResult,
        id: this.CurrId
      };
      this.$urlApi.dataStandard.listAuditDetail(param).then(res => {
        this.checkDataList = res.data;
        this.totalResult = res.recordsFiltered;
      });
    },
    showDetail(id) {
      this.dialogTableVisible = true;
      this.CurrId = id;
    },
    openCheckList() {
      this.getResultList(this.CurrId);
    },
    closeCheckList() {
      this.dialogTableVisible = false;
      this.checkDataList = [];
    },
    handleSizeChangeResult(cb) {
      this.pageLengthResult = cb;
      this.getList(1);
    },
    handleCurrentChangeResult() {
      this.currentPageResult = cb;
      this.getList();
    },
    AddUpdateVisibleBack(cb) {
      this.AddUpdateVisible = false;
      this.isEditMeta.id = "";
      this.isEditMeta.isHistory = false;
      this.getList();
    },
    updateDialogStatus() {
      this.addOrUpdateDataSetDialogVisible = false;
      this.isEditMeta.id = "";
      this.isEditMeta.isHistory = false;
      this.getList();
    },
    editCodeListBack() {
      this.AddEditCodeListVisible = false;
      this.isEditCodeList.id = "";
      this.isEditCodeList.isHistory = false;
      this.getList();
    },
    showDataSource(item) {
      let tempDate = new Date().getTime();
      if (item.type == 0) {
        this.AddUpdateVisible = true;
        this.isEditMeta.currTime = tempDate;
        this.isEditMeta.isHistory = true;
        this.isEditMeta.id = item.flowId;
      } else if (item.type == 1) {
        this.addOrUpdateDataSetDialogVisible = true;
        this.isEditSet.currTime = tempDate;
        this.isEditSet.isHistory = true;
        this.isEditSet.id = item.flowId;
      } else if (item.type == 2) {
        this.AddEditCodeListVisible = true;
        this.isEditCodeList.id = item.flowId;
        this.isEditCodeList.isHistory = true;
        this.isEditCodeList.currTime = tempDate;
      }
    },
    backToCheck() {
      this.$emit('backToCheck')
    }
  },
  mounted: function() {
    this.getList();
  },
  watch: {
    checkedStandardVisible: function(val) {
      this.getList();
    }
  }
};
</script>

<style lang="scss">
.checked-standard-tab-section {
  height: 100%;
  .dataElementName-class {
    cursor: pointer;
    color: #606266;
  }
  .dataElementName-class:hover {
    color: #3ba3f8;
  }
  .search_list {
    margin-top: 10px;
    .search_title {
      display: inline-block;
      width: 60px;
      text-align: right;
      margin-right: 10px;
    }
    .el-radio-button__inner {
      border: 0;
    }
    .el-radio-button__orig-radio:checked + .el-radio-button__inner {
      color: #3ba3f8;
      border: 0;
      background: transparent;
      -webkit-box-shadow: none;
      -moz-box-shadow: none;
      -o-box-shadow: none;
      box-shadow: none;
    }
  }
  .check-standard-content {
    height: 100%;
    .el-table__empty-block {
      align-items: baseline;
      .show-empty {
        margin-top: 20px;
      }
    }
  }
}
</style>
