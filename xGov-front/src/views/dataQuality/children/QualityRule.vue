<template>
  <section class="quality-rule-section main-outer">
    <el-row class="common-condition ar mb15"></el-row>
    <el-row class="main-inner">
      <div class="search_list">
        <el-row>
          <span class="search_title">应用状态:</span>
          <el-radio-group
            v-model="applicationStatus"
            size="small"
            @change="changeStatus"
          >
            <el-radio-button label="">全部</el-radio-button>
            <el-radio-button label="0">启用</el-radio-button>
            <el-radio-button label="1">禁用</el-radio-button>
          </el-radio-group>
        </el-row>
        <el-row>
          <span class="search_title">作用数据源:</span>
          <el-select
            v-model="dataSourceVal"
            size="small"
            placeholder="请选择数据源"
            @change="changeSelect"
          >
            <el-option
              v-for="item in Dataoptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-row>
        <el-row class="mt10">
          <el-button size="mini" class="grey-btn" @click="addRule"
            >新建</el-button
          >
          <el-button
            size="mini"
            class="grey-btn"
            @click="deleteList"
            :disabled="this.deleteSelection.length <= 0"
            >删除</el-button
          >
          <div style="float: right">
            <el-input
              v-model="searchName"
              @keyup.enter.native="changeStatus"
              placeholder="请输入规则名称进行查询"
              style="width: 330px"
              size="mini"
            >
              <i slot="prefix" class="el-input__icon el-icon-search"></i>
            </el-input>
            <el-button type="danger" size="mini" @click="changeStatus"
              >查询</el-button
            >
          </div>
        </el-row>
      </div>
      <el-row class="mt20">
        <el-table
          :data="tableData"
          @selection-change="changeFun"
          style="width: 100%;"
        >
          <el-table-column type="selection" width="55"> </el-table-column>
          <el-table-column
            prop="ruleName"
            label="规则名称"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <el-button type="text" @click="showDataSource(scope.row)">{{
                scope.row.ruleName
              }}</el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="datasourceName"
            label="作用数据源"
            show-overflow-tooltip
          >
          </el-table-column>
          <el-table-column prop="columnNum" label="字段个数"> </el-table-column>
          <el-table-column prop="checkFieldNum" label="校验字段数">
          </el-table-column>
          <el-table-column prop="ruleNum" label="规则总数"> </el-table-column>
          <el-table-column label="状态">
            <template slot-scope="scope">
              <el-tag class="success-tag" v-if="scope.row.status == 'OPEN'"
                >已启用</el-tag
              >
              <el-tag class="error-tag" v-if="scope.row.status == 'CLOSE'"
                >已禁用</el-tag
              >
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-tooltip
                class="item"
                effect="dark"
                content="编辑"
                placement="top"
              >
                <i class="table-icon-menu" @click="showDetail(scope.row.id)"
                  ><svg-icon iconClass="编辑1"></svg-icon
                ></i>
              </el-tooltip>
              <el-tooltip
                class="item"
                effect="dark"
                content="质量规则重新生成"
                placement="top"
              >
                <i
                  class="table-icon-menu"
                  @click="reGenQualityRule(scope.row.id)"
                  ><svg-icon iconClass="g-更新"></svg-icon
                ></i>
              </el-tooltip>
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
    <design-details
      :designDetailsVisible="designDetailsVisible"
      :isEditDetail="isEditDetail"
      @designDetailBack="designDetailBack"
    ></design-details>
    <edit-design-rule
      :editDesignRuleVisible="editDesignRuleVisible"
      :isDesignRule="isDesignRule"
      @editDesignRuleBack="editDesignRuleBack"
    ></edit-design-rule>
  </section>
</template>

<script>
import DesignDetails from "./qualityRule/DesignDetails.vue";
import EditDesignRule from "./qualityRule/EditDesignRule.vue";
export default {
  name: "task-monitor",
  components: {
    DesignDetails,
    EditDesignRule
  },
  data() {
    return {
      total: 0,
      currentPage: 1,
      pageLength: 10,
      deleteSelection: [],

      dataSourceVal: "",

      applicationStatus: "",
      tableData: [],
      Dataoptions: [{ name: "全部", id: "" }],

      editDesignRuleVisible: false,

      designDetailsVisible: false,
      isEditDetail: {
        id: "",
        currTime: new Date().getTime()
      },
      isDesignRule: {
        id: "",
        currTime: new Date().getTime(),
        isHistory: false
      },
      searchName: ""
    };
  },
  methods: {
    getList(page, type) {
      page && (this.currentPage = page);
      let param = {
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength,
        status: this.applicationStatus,
        ruleName: this.searchName,
        sourceId: this.dataSourceVal
      };
      this.$urlApi.dataQuality.getRuleList(param).then(res => {
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
    getDataSource() {
      this.$urlApi.dataSource.getAllDataSource({ pid: -1 }).then(res => {
        this.Dataoptions = this.Dataoptions.concat(res.data);
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
    changeSelect(val) {
      this.getList(1);
    },
    changeFun(val) {
      this.deleteSelection = val;
    },
    changeStatus(val) {
      this.getList(1);
    },
    showDetail(id) {
      let tempDate = new Date().getTime();
      this.editDesignRuleVisible = true;
      this.isDesignRule.currTime = tempDate;
      this.isDesignRule.id = id;
    },
    reGenQualityRule(id) {
      this.$urlApi.dataQuality.updateQualityRule({ ruleId: id }).then(res => {
        this.$toast("success", "质量规则生成成功");
      });
      this.getList();
    },
    addRule() {
      let tempDate = new Date().getTime();
      this.designDetailsVisible = true;
      this.isEditDetail.currTime = tempDate;
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
          this.$urlApi.dataQuality.deletesAction({ ids: ids }).then(res => {
            this.getList(0, "delete");
            this.$toast("success", "删除成功");
          });
        })
        .catch(() => {});
    },
    designDetailBack(cb) {
      this.designDetailsVisible = false;
      if (cb === 1) {
        this.getList();
      }
    },
    editDesignRuleBack(cb) {
      this.editDesignRuleVisible = false;
      if (cb === 1) {
        this.getList();
      }
    },
    showDataSource(item) {
      let tempDate = new Date().getTime();
      this.editDesignRuleVisible = true;
      this.isDesignRule.currTime = tempDate;
      this.isDesignRule.isHistory = true;
      this.isDesignRule.id = item.id;
    }
  },
  mounted: function() {
    this.getDataSource();
    this.getList();
  }
};
</script>

<style lang="scss">
.quality-rule-section {
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
