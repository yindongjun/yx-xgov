<template>
  <el-dialog
    title="选择数据源"
    :visible.sync="setRuleDesignVisible"
    :before-close="closeDialog"
    class="edit-design-info"
    :lock-scroll="false"
    width="500px"
    :close-on-click-modal="false"
  >
    <el-row class="business-basic-b">
      <el-table
        :data="tableData"
        @selection-change="handleSelectionChange"
        height="350"
        style="width: 100%;"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="datasourceName" label="数据源名称">
        </el-table-column>
      </el-table>
    </el-row>
    <span slot="footer" class="dialog-footer">
      <el-button type="info" size="small" @click="closeDialog">关 闭</el-button>
      <el-button
        type="danger"
        size="small"
        @click="saveData"
        :loading="buttonLoading"
        >保存</el-button
      >
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: "",
  props: ["setRuleDesignVisible", "modalTime"],
  components: {},
  data() {
    return {
      buttonLoading: false,
      tableData: [],
      selectList: []
    };
  },
  methods: {
    getData() {
      this.$urlApi.dataQuality.getAvailableSource().then(res => {
        this.tableData = res.data;
      });
    },
    closeDialog() {
      this.$emit("setRuleDesignBack");
      this.tableData = [];
      this.selectList = [];
    },
    handleSelectionChange(val) {
      this.selectList = val;
    },
    saveData() {
      if (this.selectList.length === 0) {
        this.$toast("error", "请至少选择一个数据源！");
      } else {
        let ids = this.selectList.map(item => item.id).join();
        this.buttonLoading = true;
        this.$urlApi.dataQuality
          .createDesignBySourceId({ sourceId: ids })
          .then(res => {
            this.buttonLoading = false;
            this.$toast("success", "新建成功");
            this.$emit("setRuleDesignBack", 1);
            this.tableData = [];
            this.selectList = [];
          })
          .catch(e => {
            this.buttonLoading = false;
          });
      }
    }
  },
  mounted: function() {},
  watch: {
    modalTime: {
      handler(val, oldVal) {
        this.getData();
      }
    }
  }
};
</script>

<style lang="scss">
.edit-design-info {
  .el-dialog__body {
    height: 350px;
    .business-basic-b {
      height: 100%;
      .el-table {
        td,
        .el-table th {
          padding: 6px 0;
        }
      }
    }
  }
}
</style>
