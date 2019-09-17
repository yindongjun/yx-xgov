<template>
  <el-dialog
    :close-on-click-modal="false"
    title="提示"
    :visible.sync="infoVisible"
    :before-close="closeDialog"
    class="analyze-detail-info"
    width="760px"
  >
    <el-row class="business-basic-t">
      <el-col :span="12" class="over" :title="metaName">
        元数据名称：{{ metaName }}
      </el-col>
      <el-col :span="12" class="over" :title="metaCode">
        元数据代码：{{ metaCode }}
      </el-col>
      <el-col :span="12" class="over" :title="metaType">
        元数据类型：{{ metaType }}
      </el-col>
    </el-row>
    <el-row class="business-basic-b">
      <el-table :data="tableData" height="100%" style="width: 100%">
        <el-table-column prop="name" label="属性名称"> </el-table-column>
        <el-table-column prop="code" label="属性代码"> </el-table-column>
        <el-table-column
          prop="attrValue"
          :show-overflow-tooltip="true"
          label="属性值"
        >
        </el-table-column>
      </el-table>
    </el-row>
    <span slot="footer" class="dialog-footer">
      <el-button type="info" size="small" @click="closeDialog">关 闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: "",
  props: ["infoVisible", "isEditRelation"],
  components: {},
  data() {
    return {
      metaName: "",
      metaCode: "",
      metaType: "",
      tableData: []
    };
  },
  methods: {
    getData() {
      this.$urlApi.metaData
        .basicInfoMsg({ id: this.isEditRelation.id })
        .then(res => {
          this.tableData = res.data.attrs;
          this.metaName = res.data.name;
          this.metaCode = res.data.code;
          this.metaType = res.data.metaModelName;
        });
    },
    closeDialog() {
      this.$emit("detailHandleback");
      this.tableData = [];
      this.metaName = "";
      this.metaCode = "";
      this.metaType = "";
    }
  },
  mounted: function() {},
  watch: {
    isEditRelation: {
      handler(val, oldVal) {
        this.getData();
      },
      deep: true
    }
  }
};
</script>

<style lang="scss">
.analyze-detail-info {
  .el-dialog__body {
    height: 350px;
    .business-basic-t {
      height: 90px;
      padding: 15px 10px;
      line-height: 30px;
    }
    .business-basic-b {
      height: -moz-calc(100% - 100px);
      height: -webkit-calc(100% - 100px);
      height: calc(100% - 100px);
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
