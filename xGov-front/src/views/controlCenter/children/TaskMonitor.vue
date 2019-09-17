<template>
  <section class="task-monitor-section main-outer">
    <el-row class="common-condition ar mb15"></el-row>
    <el-row v-if="homeList" class="main-inner">
      <div class="search_list">
        <el-row>
          <el-col :span="24">
            <span class="search_title">运行状态:</span>
            <el-radio-group
              v-model="applicationStatus"
              size="small"
              @change="changeStatus"
            >
              <el-radio-button label="">全部</el-radio-button>
              <el-radio-button label="0">执行中</el-radio-button>
              <el-radio-button label="1">成功</el-radio-button>
              <el-radio-button label="2">失败</el-radio-button>
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

          </el-col>
        </el-row> -->
      </div>
      <el-row class="mt20 task-monitor-content">
        <el-table :data="tableData" style="width: 100%;" height="100%">
          <template slot="empty">
            <div class="show-empty">
              暂无任务执行数据
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
          <el-table-column label="最新运行状态" show-overflow-tooltip>
            <template slot-scope="scope">
              <el-tag class="success-tag" v-if="scope.row.status == 1"
                >成功</el-tag
              >
              <el-tag class="error-tag" v-if="scope.row.status == 2"
                >失败</el-tag
              >
              <el-tag class="going-tag" v-if="scope.row.status == 0"
                >执行中</el-tag
              >
            </template>
          </el-table-column>
          <el-table-column label="调度方式" show-overflow-tooltip>
            <template slot-scope="scope">
              <span v-if="scope.row.cycleType == 0">单次</span>
              <span v-else>周期性</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="startTime"
            label="最新执行时间"
            show-overflow-tooltip
            :formatter="this.$utils.dateFormat"
          >
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button class="operate-btn" @click="showDetail(scope.row.taskId)" type="text"
                >执行历史</el-button
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
    <el-row v-else class="main-inner">
      <span style="cursor: pointer" @click="backHome"><i class="el-icon-arrow-left"></i> 任务监控列表</span>
      <el-row class="mt20 task-monitor-content">
        <el-table :data="tableDataHis" style="width: 100%;" height="100%">
          <template slot="empty">
            <div class="show-empty">
              暂无历史数据
            </div>
          </template>
          <el-table-column
            prop="startTime"
            label="执行时间"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column label="执行状态">
            <template slot-scope="obj">
              <el-tag class="success-tag" v-if="obj.row.status == 1"
                >成功</el-tag
              >
              <el-tag class="error-tag" v-if="obj.row.status == 2"
                >失败</el-tag
              >
              <el-tag class="going-tag" v-if="obj.row.status == 0"
                >执行中</el-tag
              >
            </template>
          </el-table-column>
          <el-table-column
            prop="spent"
            label="执行时长(s)"
            show-overflow-tooltip
          >
            <template slot-scope="obj">
              <span v-if="obj.row.status == 1">{{obj.row.spent}}</span>
              <span v-else>/</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button class="operate-btn" @click="showLog(scope.row.id)" type="text"
                >日志</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          class="ar mt15"
          @size-change="handleSizeChange1"
          @current-change="handleCurrentChange1"
          :current-page="currentPage1"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="10"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total1"
        >
        </el-pagination>
      </el-row>
    </el-row>
    <history-info
      :historyInfoVisible='historyInfoVisible'
      :taskID='taskID'
      :logsId='logsId'
      @historyInfoBack='historyInfoBack'
    ></history-info>
  </section>
</template>

<script>
import HistoryInfo from "./taskMonitor/historyInfo.vue";
import { changeUTCtoDateTime } from "@/common/common_tools.js";

export default {
  name: "task-monitor",
  components: {HistoryInfo},
  data() {
    return {
      total: 0,
      currentPage: 1,
      pageLength: 10,
      total1: 0,
      currentPage1: 1,
      pageLength1: 10,
      deleteSelection: [],

      applicationStatus: "",
      taskType: "",
      tableData: [],
      searchName: "",
      taskID: '',
      historyInfoVisible: false,
      homeList: true,
      tableDataHis:[],
      logsId: ''
    };
  },
  methods: {
    getList(page) {
      page && (this.currentPage = page);
      let param = {
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength,
        taskType: this.taskType,
        status: this.applicationStatus,
        taskName: this.searchName
      };
      this.$urlApi.dispatchTask.monitorList(param).then(res => {
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
    handleSizeChange1(cb) {
      this.pageLength1 = cb;
      this.getHistoryList(this.taskID);
    },
    handleCurrentChange1(cb) {
      this.currentPage1 = cb;
      this.getHistoryList(this.taskID);
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
    getHistoryList(id) {
      return new Promise((resolve, reject) => {
      let param = {
        start: (this.currentPage1 - 1) * this.pageLength1,
        length: this.pageLength1,
        taskId: id
      };
      this.$urlApi.dispatchTask.getMonitorHistories(param).then(res => {
        this.tableDataHis = res.data.map(c => {
          c.startTime = changeUTCtoDateTime("yyyy-MM-dd HH:mm:ss", c.startTime)
          c.endTime = changeUTCtoDateTime("yyyy-MM-dd HH:mm:ss", c.endTime)
          return c
        });
        this.total1 = res.recordsFiltered;
        resolve();
      });
      })
    },
    showDetail(id) {
      this.taskID = id;
      this.getHistoryList(id).then(() => {
        this.homeList = false
      })
    },
    backHome () {
      this.homeList = true;
      this.tableDataHis = [];
      this.total1 = 0;
      this.currentPage1 = 1;
      this.pageLength1 = 10;
    },
    showLog(id) {
      this.logsId = id
      this.historyInfoVisible = true;
    },
    historyInfoBack() {
      this.historyInfoVisible = false;
    }
  },
  mounted: function() {
    this.getList();
  }
};
</script>

<style lang="scss">
.task-monitor-section {
  height: 100%;
  .main-inner {
    height: calc(100% - 35px);
    .task-monitor-content {
      height: calc(100% - 125px);
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
