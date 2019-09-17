<template>
  <section class="check-standard-tab-section">
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
          <el-button
            size="mini"
            class="grey-btn"
            @click="sendAudit(-1)"
            :disabled="this.deleteSelection.length <= 0"
            >批量审核</el-button
          >
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
        @selection-change="changeFun"
        height="calc(100% - 120px)"
      >
        <template slot="empty">
          <div class="show-empty">
            暂无待审核数据标准
          </div>
        </template>
        <el-table-column type="selection" width="55"> </el-table-column>
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
        <el-table-column prop="submitter" label="送审人" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          prop="submitTime"
          label="送审时间"
          :formatter="this.$utils.dateFormat"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column label="状态" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-tag class="new-tag" v-if="scope.row.status == 0">待审核</el-tag>
            <el-tag class="success-tag" v-if="scope.row.status == 1"
              >通过</el-tag
            >
            <el-tag class="error-tag" v-if="scope.row.status == 2">退回</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-button
              class="operate-btn"
              @click="sendAudit(scope.row)"
              type="text"
              >审核</el-button
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
      width="500px"
      title="审核"
      :visible.sync="checkAuditVisible"
      :before-close="closeDialog"
      @open='openAudit'
      append-to-body
    >
      <el-form
        :model="checkAuditForm"
        :rules="rules"
        ref="checkAuditForm"
        size="small"
        label-width="80px"
        label-position="right"
      >
        <el-form-item label="审核结论" prop="status">
          <el-radio-group v-model="checkAuditForm.status" @change="changeRadio">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">退回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="typeof checkAuditForm.ids === 'number' && !!changeReason.length" label="变更原因">
          <label style="font-size:12px;">{{changeReason}}</label>
        </el-form-item>
        <el-form-item label="描述" prop="comment">
          <el-input
            type="textarea"
            :rows="3"
            placeholder="请输入内容"
            v-model="checkAuditForm.comment"
          >
          </el-input>
        </el-form-item>
      </el-form>
      <div v-if="typeof checkAuditForm.ids === 'string'" style="width: 100%;text-align: center;color: #f78989;font-size: 13px;padding: 10px 0;"><i class="el-icon-warning"></i> 批量审批不支持查看变更原因，请在单个审核中查看！</div>
      <span slot="footer" class="dialog-footer">
        <el-button type="info" size="small" @click="closeDialog"
          >取 消</el-button
        >
        <el-button
          type="danger"
          size="small"
          @click="submitAudit('checkAuditForm')"
          :loading="buttonLoading"
          >保 存</el-button
        >
      </span>
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
  name: "check-standard-tab",
  props: ["checkStandardVisible"],
  components: {
    AddUpdateMeta,
    AddOrUpdateDataSetDialog,
    AddEditCode
  },
  data() {
    return {
      buttonLoading: false,
      total: 0,
      currentPage: 1,
      pageLength: 10,
      deleteSelection: [],
      currId: "",

      searchType: "",
      searchName: "",
      tableData: [],

      AddUpdateVisible: false,
      addOrUpdateDataSetDialogVisible: false,
      AddEditCodeListVisible: false,
      checkAuditVisible: false,
      checkAuditForm: {
        ids: "",
        status: "",
        comment: ""
      },
      rules: {
        status: [
          { required: true, message: "请填写审核结论", trigger: "blur" }
        ],
        comment: [
          { required: true, message: "请填写描述", trigger: "change1" },
          { min: 1, max: 50, message: "长度在 1 到 50 个字符", trigger: "blur" }
        ]
      },
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
      currNodeId: "",
      scopeRow: [],
      changeReason: ''
    };
  },
  methods: {
    getList(page, type) {
      page && (this.currentPage = page);
      let param = {
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength,
        type: this.searchType,
        taskName: this.searchName,
        status: "0"
      };
      this.$urlApi.dataStandard.standardAuditList(param).then(res => {
        if (type === "delete") {
          if (
            res.recordsFiltered % this.pageLength === 0 &&
            this.currentPage > 1
          ) {
            this.getList(res.recordsFiltered / this.pageLength);
          }
        }
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
    sendAudit(val) {
      if (val === -1) {
        this.checkAuditForm.ids = this.deleteSelection
          .map(item => item.id)
          .join();
      } else {
        this.checkAuditForm.ids = val.id;
        this.type = val.type
      }
      this.checkAuditVisible = true;
    },
    updataAudit(row) {
      this.currId = row.id;
      this.scopeRow = [];
      this.scopeRow.push({ id: row.id });
      this.checkAuditForm.ids = row.id;
      this.checkAuditVisible = true;
    },
    changeRadio(val) {
      if (val == 1) {
        this.checkAuditForm.comment = "通过";
      } else {
        this.checkAuditForm.comment = "";
      }
    },
    submitAudit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          this.$urlApi.dataStandard
            .updataAuditStatus(this.checkAuditForm)
            .then(res => {
              this.buttonLoading = false;
              this.$toast("success", "操作成功");
              this.$refs["checkAuditForm"].resetFields();
              this.checkAuditVisible = false;
              this.getList(0, "delete");
            })
            .catch(e => {
              this.buttonLoading = false;
            });
        } else {
          return false;
        }
      });
    },
    closeDialog() {
      this.$refs["checkAuditForm"].resetFields();
      this.checkAuditVisible = false;
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
    openAudit() {
      if (typeof this.checkAuditForm.ids === 'number') {
        const params = {
          id: this.checkAuditForm.ids,
          type: this.type
        }
        this.$urlApi.dataStandard
          .changeDetail(params)
          .then(res => {
            if (res.code === '200') {
              this.changeReason = res.data.changeInfo ? res.data.changeInfo : ''
            }
          })
      }
    }
  },
  mounted: function() {
    this.getList();
  },
  watch: {
    checkStandardVisible: function(val) {
      this.getList();
    }
  }
};
</script>

<style lang="scss">
.check-standard-tab-section {
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
