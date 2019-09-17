<template>
  <div class="quality-report">
    <div class="select-data-table">
      <label class="label-class">请选择数据表生成报告</label>
      <el-button class="grey-btn" @click="selectDataTable" size="small">
        <svg-icon iconClass="添加-q"></svg-icon>选择
      </el-button>
    </div>
    <div class="search-operate">
      <div class="operate-left">
        <el-button class="grey-btn" size="small" @click="searchReport">
          <svg-icon ref="svgRefresh" iconClass="g-更新"></svg-icon>刷新
        </el-button>
        <el-button
          :disabled="checkData.length === 0"
          class="grey-btn"
          size="small"
          @click="deleteReport(-1)"
        >
          <svg-icon iconClass="删除"></svg-icon>删除
        </el-button>
      </div>
      <div class="operate-right">
        <el-date-picker
          style="float: left;margin-right: 10px;"
          size="mini"
          v-model="searchTime"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
        ></el-date-picker>
        <el-button type="danger" size="mini" @click="searchReport"
          >查询</el-button
        >
      </div>
    </div>
    <el-table
      :data="tableData"
      style="width: 100%;"
      @selection-change="changeTableHandle"
      height="calc(100% - 135px)"
    >
      <template slot="empty">
        <div class="show-empty">
          暂无数据，请先<span style="color: #3BA3F8;cursor:pointer" @click="selectDataTable"
            > 选择数据表，输入报告名称，然后生成报告</span
          >
        </div>
      </template>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="name" label="报表名称"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template slot-scope="scope">
          <span v-if="scope.row.status == 0">
            <el-tag class="error-tag">生成中</el-tag>
          </span>
          <span v-if="scope.row.status == 1">
            <el-tag class="success-tag">完成</el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status == 1"
            class="operate-btn"
            @click="showReportForPDF(scope.row)"
            type="text"
            >预览</el-button
          >
          <el-button
            class="operate-btn"
            @click="deleteReport(scope.row.id)"
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
    ></el-pagination>
    <el-dialog
      :close-on-click-modal="false"
      width="40%"
      title="选择数据表生成报告"
      :visible.sync="selectDataTableVisible"
      @open="open"
    >
      <el-form
        :model="formData"
        ref="baseForm"
        :rules="rules"
        size="small"
        label-width="100px"
        label-position="left"
      >
        <el-form-item label="报表名称" prop="reportName">
          <el-input v-model="formData.reportName"></el-input>
        </el-form-item>
      </el-form>
      <div class="tree-data">
        <el-scrollbar style="height: 100%">
          <el-tree
            v-if="selectDataTableVisible"
            ref="tree"
            :data="treeData"
            default-expand-all
            show-checkbox
            node-key="id"
            :props="defaultProps"
          ></el-tree>
        </el-scrollbar>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="info" size="small" @click="close">取 消</el-button>
        <el-button
          type="danger"
          size="small"
          @click="submitDataTable"
          :loading="buttonLoading"
          >{{ buttonLoading ? "生成中" : "生成报告" }}</el-button
        >
      </span>
    </el-dialog>
    <preview-report
      :previewReport="previewReport"
      @previewReportCB="previewReportCB"
    ></preview-report>
  </div>
</template>

<script>
import PreviewReport from "./components/PreviewReport.vue";
import { changeUTCtoDateTime } from "@/common/common_tools.js";

export default {
  name: "quality-report",
  components: { PreviewReport },
  data() {
    const validateName = (rule, value, callback) => {
      if (value) {
        if (value.length >= 50) {
          callback(new Error("报表名称长度不超过50个字符"));
        } else {
          callback();
        }
      } else {
        callback(new Error("请填写报表名称"));
      }
    };
    return {
      formData: {
        reportName: ""
      },
      rules: {
        reportName: [
          {
            required: true,
            validator: validateName,
            trigger: ["change", "blur"]
          }
        ]
      },
      searchTime: [],
      checkData: [],
      tableData: [],
      total: 0,
      currentPage: 1,
      pageLength: 10,
      selectDataTableVisible: false,
      buttonLoading: false,
      treeData: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      previewReport: {
        visible: false,
        row: ""
      }
    };
  },
  methods: {
    getReport(page, type) {
      page && (this.currentPage = page);
      let param = {
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength,
        ...(this.searchTime
          ? this.searchTime[0]
            ? { startDate: +this.searchTime[0] }
            : {}
          : {}),
        ...(this.searchTime
          ? this.searchTime[1]
            ? { endDate: +this.searchTime[1] }
            : {}
          : {})
      };
      this.$urlApi.dataQuality.listReports(param).then(res => {
        if (type === "delete") {
          if (
            res.recordsFiltered % this.pageLength === 0 &&
            this.currentPage > 1
          ) {
            this.getReport(res.recordsFiltered / this.pageLength);
          }
        }
        const tableData = res.data;
        tableData.forEach(c => {
          c.createTime = changeUTCtoDateTime(
            "yyyy-MM-dd HH:mm:ss",
            c.createTime
          );
        });
        this.tableData = tableData;
        this.total = res.recordsFiltered;
      });
    },
    searchReport() {
      const transform = this.$refs.svgRefresh.$el.style.transform
        ? this.$refs.svgRefresh.$el.style.transform.match(/\d+/)[0]
        : 0;
      this.$refs.svgRefresh.$el.style.transition = "transform 1s";
      let a;
      if (transform) {
        a = parseInt(transform) + 360;
      } else {
        a = 360;
      }
      this.$refs.svgRefresh.$el.style.transform = `rotate(${a}deg)`;
      this.getReport(1);
    },
    selectDataTable() {
      this.selectDataTableVisible = true;
    },
    downloadReport(id) {},
    deleteReport(id) {
      let reportIds;
      if (id === -1) {
        reportIds = this.checkData.map(c => c.id).join(",");
      } else {
        reportIds = id;
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
          this.$urlApi.dataQuality.deleteReport({ reportIds }).then(res => {
            this.getReport(0, "delete");
            this.$toast("success", "删除成功");
          });
        })
        .catch(e => {
          throw new Error(e);
          this.$toast("error", "删除失败");
        });
    },
    changeTableHandle(val) {
      this.checkData = val;
    },
    showReportForPDF(row) {
      this.previewReport.row = row;
      this.previewReport.visible = true;
    },
    handleSizeChange(size) {
      this.pageLength = size;
      this.getReport();
    },
    handleCurrentChange(page) {
      this.currentPage = page;
      this.getReport();
    },
    open() {
      this.$refs["baseForm"] && this.$refs["baseForm"].resetFields();
      this.$urlApi.dataQuality.listTables({}).then(res => {
        if (res.code == 200) {
          const getTreeData = arr => {
            const a = arr => {
              arr.forEach(c => {
                if (c.children) {
                  a(c.children);
                } else {
                  if (c.tableName) {
                    c.name = c.tableName;
                  }
                }
              });
            };
            a(arr);
            return arr;
          };
          this.treeData = getTreeData(res.data);
        }
      });
    },
    submitDataTable() {
      this.$refs["baseForm"].validate(valid => {
        if (valid) {
          const selectTable = this.$refs.tree.getCheckedNodes();
          if (selectTable.length === 0) {
            return this.$toast("error", "请先选择数据表");
          }
          const tableInfoIds = selectTable
            .filter(c => !!c.tableName)
            .map(k => k.id)
            .join(",");
          this.buttonLoading = true;
          this.$urlApi.dataQuality
            .createReport({ tableInfoIds, name: this.formData.reportName })
            .then(res => {
              if (res.code == 200) {
                this.$toast("success", "报告生成成功！");
                this.getReport(1);
                this.buttonLoading = false;
                this.selectDataTableVisible = false;
                this.checkData = [];
              }
            })
            .catch(e => {
              this.buttonLoading = false;
            });
        }
      });
    },
    close() {
      this.selectDataTableVisible = false;
      this.checkData = [];
    },
    previewReportCB() {
      this.previewReport.visible = false;
      this.previewReport.row = "";
    }
  },
  mounted() {
    this.getReport();
  }
};
</script>

<style lang="scss">
.quality-report {
  height: calc(100% - 60px);
  padding: 20px 30px;
  margin: 45px 20px;
  background-color: #fff;
  box-sizing: border-box;
  margin-bottom: 0;
  .label-class {
    margin-right: 10px;
    font-size: 13px;
  }
  .el-table__empty-block {
    align-items: baseline;
    .show-empty {
      margin-top: 20px;
    }
  }
  .search-operate {
    margin-top: 30px;
    .operate-left {
      float: left;
    }
    .operate-right {
      float: right;
    }
  }
}
.tree-data {
  height: 20rem;
  .el-scrollbar__wrap {
    overflow-x: hidden;
  }
}
</style>
