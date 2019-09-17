<template>
  <section class="task-manage-section main-outer">
    <el-row class="common-condition ar mb15"></el-row>
    <el-row class="main-inner">
      <div class="search_list">
        <el-row>
          <el-col :span="24">
            <span class="search_title">任务状态:</span>
            <el-radio-group
              v-model="applicationStatus"
              size="small"
              @change="changeStatus"
            >
              <el-radio-button label="">全部</el-radio-button>
              <el-radio-button label="1">已启用</el-radio-button>
              <el-radio-button label="2">已暂停</el-radio-button>
              <el-radio-button label="3">已完成</el-radio-button>
            </el-radio-group>
            <div style="float: right">
              <el-input
                v-model="searchName"
                @keyup.enter.native="changeStatus"
                placeholder="请输入任务名称进行查询"
                style="width: 330px"
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
        <!-- <el-row>
          <el-col :span="24">
            <span class="search_title">任务类型:</span>
            <el-radio-group
              v-model="taskType"
              size="small"
              @change="changeType"
            >
              <el-radio-button label="">全部</el-radio-button>
              <el-radio-button label="0">采集任务</el-radio-button>
              <el-radio-button label="1">质量任务</el-radio-button>
            </el-radio-group>
            <div style="float: right">
              <el-input
                v-model="searchName"
                @keyup.enter.native="changeStatus"
                placeholder="请输入任务名称进行查询"
                style="width: 330px"
                size="mini"
              >
                <i slot="prefix" class="el-input__icon el-icon-search"></i>
              </el-input>
              <el-button type="danger" size="mini" @click="changeStatus"
                >查询</el-button
              >
            </div>
          </el-col>
        </el-row> -->
      </div>
      <el-row class="mt20 task-manage-content">
        <el-table :data="tableData" style="width: 100%;" height="100%">
          <template slot="empty">
            <div class="show-empty">
              暂无数据，请先配置<span
                class="redText" @click="configQuality"
                > 质量任务 </span
              >
            </div>
          </template>
          <el-table-column
            prop="taskName"
            label="任务名称"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column label="任务类型" show-overflow-tooltip>
            <template slot-scope="scope">
              <span v-if="scope.row.taskType == 0">采集任务</span>
              <span v-else>质量任务</span>
            </template>
          </el-table-column>
          <el-table-column label="任务状态" show-overflow-tooltip>
            <template slot-scope="scope">
              <el-tag class="new-tag" v-if="scope.row.status == 1"
                >已启用</el-tag
              >
              <el-tag class="error-tag" v-if="scope.row.status == 2"
                >已暂停</el-tag
              >
              <el-tag class="success-tag" v-if="scope.row.status == 3"
                >已完成</el-tag
              >
            </template>
          </el-table-column>
          <el-table-column label="调度方式" show-overflow-tooltip>
            <template slot-scope="scope">
              <span v-if="scope.row.jobCron == 4">单次</span>
              <span v-else>周期性</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="创建时间"
            show-overflow-tooltip
            :formatter="this.$utils.dateFormat"
          >
          </el-table-column>
          <el-table-column label="操作" width="240">
            <template slot-scope="scope">
              <el-button
                class="operate-btn operate-btns"
                @click="editTask(scope.row.id)"
                type="text"
                >编辑</el-button
              >
              <el-button
                class="operate-btn operate-btns"
                @click="resumeTask(scope.row.id)"
                v-if="scope.row.status == 2"
                type="text"
                >启用</el-button
              >
              <el-button
                class="operate-btn operate-btns"
                @click="pauseTask(scope.row.id)"
                v-if="scope.row.status == 1"
                type="text"
                >暂停</el-button
              >
              <el-button
                class="operate-btn operate-btns"
                @click="excuteTask(scope.row.id)"
                type="text"
                >立即执行</el-button
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
    <edit-dispatch-task
      :EditDispatchTaskVisible="EditDispatchTaskVisible"
      :isEditRelation="isEditRelation"
      @EditDispatchTaskVisibleBack="EditDispatchTaskVisibleBack"
    ></edit-dispatch-task>
  </section>
</template>

<script>
import EditDispatchTask from "./EditDispatchTask";
export default {
  name: "task-manage",
  components: {
    EditDispatchTask
  },
  data() {
    return {
      total: 0,
      currentPage: 1,
      pageLength: 10,
      deleteSelection: [],

      applicationStatus: "",
      taskType: "",
      tableData: [],
      searchName: "",

      EditDispatchTaskVisible: false,
      isEditRelation: {
        currTime: new Date().getTime(),
        id: ""
      }
    };
  },
  methods: {
    getList(page) {
      page && (this.currentPage = page);
      let param = {
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength,
        taskType: this.taskType,
        status: this.applicationStatus.toString(),
        taskName: this.searchName
      };
      this.$urlApi.dispatchTask.getDispatchTaskList(param).then(res => {
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
    changeType(val) {
      this.getList(1);
    },
    EditDispatchTaskVisibleBack(cb) {
      this.EditDispatchTaskVisible = false;
      this.isEditRelation.id = -1;
      if (cb === 1) {
        this.getList();
      }
    },
    editTask(id) {
      let tempDate = new Date().getTime();
      this.isEditRelation.id = id;
      this.isEditRelation.currTime = tempDate;
      this.EditDispatchTaskVisible = true;
    },
    resumeTask(id) {
      this.$confirm("确认要重启调度吗", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      })
        .then(() => {
          this.$urlApi.dispatchTask.dispatchResume({ id: id }).then(res => {
            this.getList();
            this.$toast("success", "重启成功");
          });
        })
        .catch(() => {});
    },
    pauseTask(id) {
      this.$confirm("确认要暂停调度吗", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      })
        .then(() => {
          this.$urlApi.dispatchTask.dispatchPause({ id: id }).then(res => {
            this.getList();
            this.$toast("success", "暂停成功");
          });
        })
        .catch(() => {});
    },
    excuteTask(id) {
      this.$confirm("确认要立即执行调度吗", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      })
        .then(() => {
          this.$urlApi.dispatchTask
            .dispatchExcuteTask({ taskId: id })
            .then(res => {
              this.getList();
              this.$toast("success", "立即执行成功");
            });
        })
        .catch(() => {});
    },
    configDataCollect() {
      this.$router.push('/metaData/default/collcet')
    },
    configQuality() {
      this.$router.push('/quality/default/task')
    }
  },
  mounted: function() {
    this.getList();
  }
};
</script>

<style lang="scss">
.task-manage-section {
  height: 100%;
  .main-inner {
    height: calc(100% - 35px);
    .task-manage-content {
      height: calc(100% - 125px);
      .el-table__empty-block {
        align-items: baseline;
        .show-empty {
          margin-top: 20px;
        }
      }
      .show-empty {
        width: 100%;
        text-align: center;
        padding: 10px 0;
      }
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
