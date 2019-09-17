<template>
  <section class="warn-setting-section main-outer">
    <el-row class="common-condition ar mb15"></el-row>
    <el-row class="main-inner">
      <div class="search_list">
        <el-row>
          <el-col :span="24">
            <el-button size="mini" class="grey-btn" @click="addWarnRule(-1)"
              >新建</el-button
            >
            <el-button
              size="mini"
              class="grey-btn"
              @click="deleteList"
              :disabled="this.deleteSelection.length <= 0"
              >删除</el-button
            >
          </el-col>
          <div style="float: right">
            <el-input
              v-model="alarmName"
              @keyup.enter.native="getList(1)"
              placeholder="请输入告警任务名进行查询"
              style="width: 330px"
              size="mini"
            >
              <i slot="prefix" class="el-input__icon el-icon-search"></i>
            </el-input>
            <el-button type="danger" size="mini" @click="getList(1)"
              >查询</el-button
            >
          </div>
        </el-row>
      </div>
      <el-row class="mt20 warn-setting-content">
        <el-table
          :data="tableData"
          style="width: 100%;"
          @selection-change="changeFun"
          height="100%"
        >
          <template slot="empty">
            <div class="show-empty">
              暂无数据，请先<span class="redText" @click="addWarnRule(-1)"> 新建告警规则</span>
            </div>
          </template>
          <el-table-column type="selection" width="55"> </el-table-column>
          <el-table-column
            prop="alarmName"
            label="告警任务名称"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column label="告警原因" show-overflow-tooltip>
            <template slot-scope="scope">
              <el-tag class="success-tag" v-if="scope.row.alarmReasion == 1"
                >成功</el-tag
              >
              <el-tag class="error-tag" v-if="scope.row.alarmReasion == 0"
                >失败</el-tag
              >
              <el-tag class="going-tag" v-if="scope.row.alarmReasion == 2"
                >成功/失败</el-tag
              >
            </template>
          </el-table-column>
          <el-table-column label="告警方式" show-overflow-tooltip>
            <template slot-scope="scope">
              <span v-if="scope.row.alarmMethod == 1">邮件</span>
              <span v-if="scope.row.alarmMethod == 2">短信</span>
              <span v-if="scope.row.alarmMethod == 3">邮件/短信</span>
            </template>
          </el-table-column>
          <!-- <el-table-column
            prop="receivePeople"
            label="接收人"
            show-overflow-tooltip
          >
          </el-table-column> -->
          <el-table-column
            prop="warnSwitch"
            label="告警开关"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                @change="changeSwitch(scope.row.id, scope.row.status)"
              >
              </el-switch>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="240">
            <template slot-scope="scope">
              <el-button
                class="operate-btn"
                @click="addWarnRule(scope.row.id)"
                type="text"
                >编辑</el-button
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
    </el-row>
    <add-or-update-warn-rule-dialog
      :isEditRelation="isEditRelation"
      :addOrUpdateWarnRuleDialogVisible="addOrUpdateWarnRuleDialogVisible"
      @addOrUpdateWarnRuleDialogVisibleEvent="updateDialogStatus"
    ></add-or-update-warn-rule-dialog>
  </section>
</template>

<script>
import AddOrUpdateWarnRuleDialog from "./warnSetting/AddOrUpdateWarnRuleDialog";

export default {
  name: "warn-setting",
  components: {
    AddOrUpdateWarnRuleDialog
  },
  data() {
    return {
      addOrUpdateWarnRuleDialogVisible: false,
      isEditRelation: {
        currTime: new Date().getTime(),
        id: ""
      },
      alarmName: "",
      total: 0,
      currentPage: 1,
      pageLength: 10,
      deleteSelection: [],
      tableData: []
    };
  },
  methods: {
    getList(page, type) {
      page && (this.currentPage = page);
      let param = {
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength,
        alarmName: this.alarmName
      };
      this.$urlApi.dispatchTask.queryAlarmList(param).then(res => {
        res.data.forEach(item => {
          if (item.status === 1) {
            item.status = true;
          } else {
            item.status = false;
          }
        });
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
    updateDialogStatus: function(cb) {
      this.addOrUpdateWarnRuleDialogVisible = false;
      this.isEditRelation.id = -1;
      if (cb === 1) {
        this.getList();
      }
    },
    addWarnRule(num) {
      let tempDate = new Date().getTime();
      this.addOrUpdateWarnRuleDialogVisible = true;
      this.isEditRelation.id = num;
      this.isEditRelation.currTime = tempDate;
    },
    deleteList() {
      let ids = this.deleteSelection.map(item => item.id).join();
      this.$confirm("确认要删除吗", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      })
        .then(() => {
          this.$urlApi.dispatchTask.deleteAlarm({ id: ids }).then(res => {
            this.getList(0, "delete");
            this.$toast("success", "删除成功");
          });
        })
        .catch(() => {});
    },
    changeSwitch(id, status) {
      this.$urlApi.dispatchTask
        .checkIsOpen({ alarmId: id, isOpen: status })
        .then(res => {
          if (!res.status) {
            this.$confirm("该报警规则已被引用，确定要关闭吗?", "系统消息", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning",
              cancelButtonClass: "cancelButton",
              confirmButtonClass: "confirmButton",
              closeOnClickModal: false
            })
              .then(() => {
                this.$urlApi.dispatchTask
                  .openAlarm({ alarmId: id, isOpen: status })
                  .then(res => {
                    this.getList();
                  });
              })
              .catch(() => {
                this.getList();
              });
          } else {
            this.$urlApi.dispatchTask
              .openAlarm({ alarmId: id, isOpen: status })
              .then(res => {
                this.getList();
              });
          }
        });
    }
  },
  mounted: function() {
    this.getList();
  }
};
</script>

<style lang="scss">
.warn-setting-section {
  height: 100%;
  .main-inner {
    height: calc(100% - 35px);
    .warn-setting-content {
      height: calc(100% - 125px);
    }
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
}
</style>
