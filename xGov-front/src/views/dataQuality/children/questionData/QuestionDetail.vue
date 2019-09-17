<template>
  <el-dialog
    :visible.sync="questionDetailVisible"
    :before-close="closeDialog"
    class="question-detail"
    :lock-scroll="false"
    width="760px"
    :fullscreen="fullscreen"
    @open="openDialog"
    :close-on-click-modal="false"
  >
    <span slot="title">
      <div style="float: left;">问题数据记录</div>
      <div class="fullscreen" @click="switchFullscreen">
        <svg-icon :iconClass="!fullscreen ? '全屏' : '缩小'"></svg-icon>
      </div>
    </span>
    <el-table
      class="tb-edit"
      :data="tableData"
      style="width: 100%"
      :height="fullscreen ? '' : '320'"
      v-loading="isLoadingTable"
    >
      <template v-for="(col, index) in cols">
        <el-table-column
          :key="index"
          :show-overflow-tooltip="true"
          :label="col.label"
          :prop="col.label"
        >
          <template slot-scope="scope">
            <span v-text="scope.row[scope.column.property].value"></span><br />
            <span
              style="color: #F67F89"
              v-if="scope.row[scope.column.property].errorMsgs"
              v-text="scope.row[scope.column.property].errorMsgs"
            ></span>
          </template>
        </el-table-column>
      </template>
    </el-table>
    <el-pagination
      class="ar mt15"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
      :current-page="currentPage"
      :page-size="pageLength"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>
  </el-dialog>
</template>

<script>
import ElRow from "element-ui/packages/row/src/row";

export default {
  name: "",
  props: ["questionDetailVisible", "isQuestionDetail"],
  components: { ElRow },
  data() {
    return {
      cols: [],
      tableData: [],
      isLoadingTable: false,

      currentPage: 1,
      total: 0,
      pageLength: 10,
      fullscreen: false
    };
  },
  methods: {
    closeDialog() {
      this.cols = [];
      this.tableData = [];
      this.fullscreen = false;
      this.currentPage = 1;
      this.total = 0;
      this.pageLength = 10;
      this.$emit("questionDetailBack");
    },
    handleCurrentChange(cb) {
      this.currentPage = cb;
      this.getData();
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0) {
        if (rowIndex % 2 === 0) {
          return {
            rowspan: 2,
            colspan: 1
          };
        } else {
          return {
            rowspan: 0,
            colspan: 0
          };
        }
      }
    },
    handleSizeChange(cb) {
      this.pageLength = cb;
      this.currentPage = 1;
      this.getData();
    },
    openDialog() {
      this.getData();
    },
    getData() {
      let params = {
        id: this.isQuestionDetail.id,
        start: (this.currentPage - 1) * this.pageLength,
        length: this.pageLength
      };
      this.isLoadingTable = true;
      this.$urlApi.dataQuality.problemDetail(params).then(res => {
        this.isLoadingTable = false;
        let heads = [];
        for (let item in res.tableHead) {
          heads.push({
            label: res.tableHead[item],
            prop: res.tableHead[item] + ".value",
            errorMsgs: res.tableHead[item] + ".errorMsgs"
          });
        }
        this.cols = heads;
        this.tableData = res.data;
        this.total = res.recordsFiltered;
      });
    },
    switchFullscreen() {
      this.fullscreen = !this.fullscreen;
    }
  },
  mounted: function() {},
  watch: {
    isQuestionDetail: {
      handler(val, oldVal) {},
      deep: true
    }
  }
};
</script>

<style lang="scss">
.question-detail {
  .el-table td,
  .el-table .el-table th {
    padding: 6px 0;
  }
  .fullscreen {
    float: right;
    margin-right: 50px;
    font-size: 14px;
    cursor: pointer;
    color: #647094;
  }
}
</style>
