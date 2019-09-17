<template>
  <section class="quality-task-section main-outer">
    <el-row class="common-condition ar mb15"></el-row>
    <el-row class="main-inner">
      <div class="search_list">
        <el-row class="mt10">
          <el-button size="mini" class="grey-btn" @click="addRule"
            >新建</el-button
          >
          <el-button
            size="mini"
            class="grey-btn"
            @click="() => deleteList()"
            :disabled="this.deleteSelection.length <= 0"
            >删除</el-button
          >
          <div style="float: right">
            <el-input
              v-model="taskName"
              @keyup.enter.native="getList(1)"
              placeholder="请输入任务名进行查询"
              style="width: 300px"
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
      <el-row class="mt20 quality-task-content">
        <el-table
          :data="tableData"
          @selection-change="changeFun"
          applicationStatus
          style="width: 100%;"
          height="100%"
        >
          <template slot="empty">
            <div class="show-empty">
              暂无数据，请先<span style="color: #3BA3F8;cursor:pointer" @click="addRule"> 新建质量任务</span>
            </div>
          </template>
          <el-table-column type="selection" width="55"> </el-table-column>
          <el-table-column prop="taskName" label="任务名" show-overflow-tooltip>
            <template slot-scope="scope">
              <div
                type="text"
                style="white-space: nowrap;overflow: hidden;text-overflow: ellipsis;"
                class="dataElementName-class"
                @click="showTaskInfo(scope.row)"
              >
                {{ scope.row.taskName }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="datasourceName" label="所属数据源">
            <template slot-scope="scope">
              <el-link style="font-size: 12px" @click="showDatasource(scope.row)" :underline="false">{{scope.row.datasourceName}}</el-link>
            </template>
          </el-table-column>
          <el-table-column label="调度规则">
            <template slot-scope="scope">
              <span v-if="scope.row.cycleType === '0'">单次调度</span>
              <span v-if="scope.row.cycleType === '1'">周期性调度</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="创建时间"
            :formatter="this.$utils.dateFormat"
          >
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button
                class="operate-btn"
                @click="editDetail(scope.row)"
                type="text"
                >编辑</el-button
              >
              <el-button
                class="operate-btn"
                @click="deleteList(scope.row)"
                type="text"
                >删除</el-button
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
    <set-quality-task
      :qualityTaskVisible="qualityTaskVisible"
      :isqualityTask="isqualityTask"
      @qualityTaskBack="qualityTaskBack"
    ></set-quality-task>
    <quality-task-info
      :taskInfoVisible="taskInfoVisible"
      :taskID="taskID"
      @taskInfoBack="taskInfoBack"></quality-task-info>
    <el-dialog
      title="数据源详情"
      :visible.sync="dialog.visible"
      :before-close="closeDialog"
      :lock-scroll="false"
      width="50rem"
      @open="openDialog"
      :close-on-click-modal="false">
      <ViewDataSource ref="viewDataSource"></ViewDataSource>
    </el-dialog>
  </section>
</template>

<script>
import SetQualityTask from "./qualityTask/SetQualityTask.vue";
import qualityTaskInfo from "./qualityTask/QualityTaskInfo.vue"
import ViewDataSource from './components/ViewDataSource'
export default {
  name: "task-monitor",
  components: { SetQualityTask, qualityTaskInfo, ViewDataSource },
  data() {
    return {
      total: 0,
      currentPage: 1,
      pageLength: 10,
      deleteSelection: [],
      taskName: "",
      tableData: [],

      qualityTaskVisible: false,
      isqualityTask: {
        id: "",
        currTime: new Date().getTime(),
        row: {}
      },
      taskInfoVisible: false,
      taskID:'',
      dialog: {
        visible: false,
        data: ''
      }
    };
  },
  methods: {
    getList(page, type) {
      page && (this.currentPage = page);
      let param = {
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength,
        taskName: this.taskName
      };
      this.$urlApi.dataQuality.qualitytasksList(param).then(res => {
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
    addRule() {
      this.qualityTaskVisible = true;
      this.isqualityTask.currTime = new Date().getTime();
    },
    editDetail(row) {
      this.qualityTaskVisible = true;
      this.isqualityTask.currTime = new Date().getTime();
      this.isqualityTask.id = row.id;
      this.isqualityTask.row = row;
    },
    deleteList(item) {
      let ids;
      if (!item) {
        ids = this.deleteSelection.map(item => item.id).join();
      } else {
        ids = item.id;
      }
      this.$confirm("确认要删除吗", "系统消息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        cancelButtonClass: "cancelButton",
        confirmButtonClass: "confirmButton",
        closeOnClickModal: false
      })
        .then(() => {
          this.$urlApi.dataQuality
            .deleteQualityTask({ tableDesignId: ids })
            .then(res => {
              this.getList(0, "delete");
              this.$toast("success", "删除成功");
            });
        })
        .catch(() => {});
    },
    qualityTaskBack() {
      this.qualityTaskVisible = false;
      this.isqualityTask.id = "";
      this.getList();
    },
    showTaskInfo(row) {
      this.taskID = row.id;
      this.taskInfoVisible = true;
    },
    taskInfoBack() {
      this.taskInfoVisible = false;
    },
    showDatasource (data) {
      this.dialog.visible = true
      this.dialog.data = data
    },
    openDialog () {
      this.$nextTick(() => {
        this.$refs.viewDataSource.init(this.dialog.data)
      })
    },
    closeDialog () {
      this.dialog.visible = false
      this.dialog.data = ''
    }
  },
  mounted: function() {
    this.getList();
  }
};
</script>

<style lang="scss">
.quality-task-section {
  height: 100%;
  .main-inner {
    height: calc(100% - 35px);
    .quality-task-content {
      height: calc(100% - 95px);
      .dataElementName-class {
        cursor: pointer;
        color: #606266;
      }
      .dataElementName-class:hover {
        color: #3ba3f8;
      }
      .el-table__empty-block {
        align-items: baseline;
        .show-empty {
          margin-top: 20px;
        }
      }
    }
  }
  .search_list {
    margin-top: 10px;
    .search_title {
      display: inline-block;
      width: 70px;
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
