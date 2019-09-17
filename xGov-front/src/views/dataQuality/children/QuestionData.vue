<template>
  <section class="quality-rule main-outer">
    <el-row class="common-condition ar mb15"></el-row>
    <el-row class="main-inner">
      <div class="search_list">
        <el-row>
          <span class="search_title">处理状态：</span>
          <el-radio-group
            v-model="applicationStatus"
            size="small"
            @change="changeStatus"
          >
            <el-radio-button label="">全部</el-radio-button>
            <el-radio-button label="0">待处理</el-radio-button>
            <el-radio-button label="1">已处理</el-radio-button>
          </el-radio-group>
        </el-row>
        <el-row :gutter="20" class="mt15">
            <span class="search_title">任务名：</span>
            <el-input
              v-model="taskName"
              style="width: 300px"
              @keyup.enter.native="changeStatus"
              placeholder="请输入任务名进行查询"
              size="small"
              class="mt10"
            ></el-input>
            <span class="search_title">表名：</span>
            <el-input
              v-model="searchName"
              style="width: 300px"
              @keyup.enter.native="changeStatus"
              placeholder="请输入表名进行查询"
              size="small"
              class="mt10"
            ></el-input>
            <el-button
              type="danger"
              size="mini"
              @click="searchData"
              style="margin-top:5px;margin-left: 10px;"
              >查询</el-button
            >
        </el-row>
        <el-row :gutter="20" class="mt15">
          <div style="margin-left: 20px">
            <el-button
            type="danger"
            size="mini"
            @click="batchOperate"
            :disabled="this.deleteSelection.length <= 0"
            >处理</el-button>
          </div>
        </el-row>
      </div>
      <div class="qdtable quality-rule-content">
        <el-table
          :data="tableData"
          style="width: 100%;"
          @selection-change="changeFun"
          height="100%"
        >
          <el-table-column
            type="selection"
            width="55"
            :selectable="checkDisabled"
          >
          </el-table-column>
          <el-table-column prop="tableName" label="表名"> </el-table-column>
          <el-table-column prop="dataSourceName" label="所属数据源">
            <template slot-scope="scope">
              <el-link style="font-size: 12px" @click="showDatasource(scope.row)" :underline="false">{{scope.row.dataSourceName}}</el-link>
            </template>
          </el-table-column>
          <el-table-column prop="errorDataNum" label="问题数据">
          </el-table-column>
          <el-table-column prop="taskName" label="任务名称"> </el-table-column>
          <el-table-column
            prop="createTime"
            label="创建时间"
            :formatter="this.$utils.dateFormat"
          >
          </el-table-column>
          <el-table-column label="状态">
            <template slot-scope="scope">
              <div>
                <el-tag v-if="scope.row.status == 0" class="new-tag"
                  >待处理</el-tag
                >
                <el-tag v-else class="success-tag">已处理</el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180">
            <template slot-scope="scope">
              <el-button
                type="text"
                class="operate-btn"
                @click="questionDetail(scope.row.id)"
                >问题详情</el-button
              >
              <el-button
                type="text"
                class="operate-btn"
                v-if="scope.row.status == 0"
                @click="dealOperate(scope.row)"
                >处理</el-button
              >
              <el-button
                type="text"
                class="operate-btn"
                @click="qualityDesignExport(scope.row)"
                >导出</el-button
              >
              <el-button
                type="text"
                class="operate-btn"
                v-if="scope.row.status == 1"
                @click="operateLog(scope.row)"
                >操作记录</el-button
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
          :page-size="pageLength"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        >
        </el-pagination>
      </div>
    </el-row>
    <deal-operate
      :dealOperateVisible="dealOperateVisible"
      :isdealOperate="isdealOperate"
      @dealOperateBack="dealOperateBack"
    ></deal-operate>
    <operate-log
      :operateLogVisible="operateLogVisible"
      :isOperateLog="isOperateLog"
      @operateLogBack="operateLogBack"
    ></operate-log>
    <question-detail
      :questionDetailVisible="questionDetailVisible"
      :isQuestionDetail="isQuestionDetail"
      @questionDetailBack="questionDetailBack"
    ></question-detail>
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
import DealOperate from "./questionData/DealOpterate.vue";
import OperateLog from "./questionData/OperateLog.vue";
import QuestionDetail from "./questionData/QuestionDetail.vue";
import ViewDataSource from './components/ViewDataSource'
export default {
  name: "",
  components: { DealOperate, OperateLog, QuestionDetail, ViewDataSource },
  data() {
    return {
      applicationStatus: "",
      sourceId: "",
      options: [{ id: "", datasourceName: "全部" }],
      problemResPeple: "",
      formInline: {
        user: "",
        region: ""
      },
      tableData: [],
      dealOperateVisible: false,
      isdealOperate: {
        currTime: new Date().getTime(),
        ids: "",
        dealComment: ""
      },
      operateLogVisible: false,
      isOperateLog: {
        currTime: new Date().getTime(),
        id: ""
      },
      questionDetailVisible: false,
      isQuestionDetail: {
        currTime: new Date().getTime(),
        id: ""
      },
      total: 0,
      currentPage: 1,
      pageLength: 10,
      deleteSelection: [],
      searchName: "",
      taskName: "",
      qualityDesignTableExport: this.$urlApi.dataStandard.qualityDesignTableExport(),
      enableStatus: "全选",
      dispatchStatus: "全选",
      dialog: {
        visible: false,
        data: ''
      }
    };
  },
  methods: {
    getList(page) {
      page && (this.currentPage = page);
      let param = {
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength,
        sourceId: this.sourceId,
        status: this.applicationStatus,
        ownerId: "",
        tableName: this.searchName,
        taskName: this.taskName
      };
      this.$urlApi.dataQuality.queryProblemData(param).then(res => {
        this.tableData = res.data;
        this.total = res.recordsFiltered;
      });
    },
    searchData() {
      this.getList(1);
    },
    changeSelect() {
      this.getList(1);
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
    changeStatus() {
      this.getList(1);
    },
    getAllDataBase() {
      this.$urlApi.dataQuality.getAllDBList().then(res => {
        this.options = this.options.concat(res.data || []);
      });
    },
    dealOperate(item) {
      let currTime = new Date().getTime();
      this.isdealOperate.currTime = currTime;
      this.isdealOperate.dealComment = item.dealComment;
      this.isdealOperate.dealPeople = item.dealPeople;
      this.isdealOperate.ids = item.id;
      this.dealOperateVisible = true;
    },
    batchOperate(item) {
      let currTime = new Date().getTime();
      this.isdealOperate.currTime = currTime;
      this.isdealOperate.dealComment = item.dealComment;
      this.isdealOperate.dealPeople = item.dealPeople;
      this.isdealOperate.ids = this.deleteSelection.map(item => item.id).join();
      this.dealOperateVisible = true;
    },
    checkDisabled(row) {
      if (row.status === 1) {
        return false;
      } else {
        return true;
      }
    },
    dealOperateBack(cb) {
      this.dealOperateVisible = false;
      this.getList();
    },
    operateLog(cb) {
      let currTime = new Date().getTime();
      this.operateLogVisible = true;
      this.isOperateLog.currTime = currTime;
      this.isOperateLog.dealPeople = cb.dealPeople;
      this.isOperateLog.dealComment = cb.dealComment;
      this.isOperateLog.dealTime = cb.dealTime;
    },
    operateLogBack(cb) {
      this.operateLogVisible = false;
    },
    questionDetail(id) {
      let currTime = new Date().getTime();
      this.questionDetailVisible = true;
      this.isQuestionDetail.currTime = currTime;
      this.isQuestionDetail.id = id;
    },
    questionDetailBack(cb) {
      this.questionDetailVisible = false;
    },
    qualityDesignExport(row) {
      const url = `${this.qualityDesignTableExport}?resultCountId=${row.id}`;
      window.open(url);
    },
    enableStatusChange(val) {},
    dispatchStatusChange(val) {},
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
    this.getAllDataBase();
    this.getList();
  }
};
</script>

<style lang="scss" >
.quality-rule {
  height: 100%;
  .main-inner {
    height: calc(100% - 35px);
    .quality-rule-content {
      height: calc(100% - 190px);
      .el-table__empty-block {
        align-items: baseline;
        .el-table__empty-text {
          margin-top: 20px;
        }
      }
    }
  }
  .search_list {
    margin-top: 10px;
    .searchStatus {
      margin-bottom: 20px;
      font-size: 13px;
    }
    .search_title {
      display: inline-block;
      width: 70px;
      text-align: right;
      margin-right: 10px;
    }
    .el-radio__label {
      font-size: 13px;
    }
    .el-radio-button__inner {
      border: 0;
    }
    .el-radio__inner:hover {
      border-color: #3ba3f8;
    }
    .el-radio__input.is-checked .el-radio__inner {
      border-color: #3ba3f8;
      background-color: #3ba3f8;
    }
    .el-radio__input.is-checked + .el-radio__label {
      color: #3ba3f8;
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
  .qualityDesignTableExport {
    color: #3ba3f8;
    text-decoration: none;
    margin-left: 5px;
  }
}
</style>
