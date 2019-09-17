<template>
  <section class="business-basic">
    <el-row class="business-basic-t">
      <el-col :span="12"> 元数据名称：{{ metaName }} </el-col>
      <el-col :span="12"> 元数据代码：{{ metaCode }} </el-col>
      <el-col :span="12"> 元数据类型：{{ metaType }} </el-col>
    </el-row>
    <el-row class="business-basic-b">
      <el-table :data="tableData3" height="100%" style="width: 100%">
        <el-table-column prop="name" label="属性名称"> </el-table-column>
        <el-table-column prop="code" label="属性代码"> </el-table-column>
        <el-table-column prop="attrValue" label="属性值"> </el-table-column>
      </el-table>
    </el-row>
  </section>
</template>

<script>
import ElRow from "element-ui/packages/row/src/row";
import ElCol from "element-ui/packages/col/src/col";

export default {
  name: "",
  components: {
    ElCol,
    ElRow
  },
  props: ["basicVisible", "currNodeId"],
  data() {
    return {
      metaName: "",
      metaCode: "",
      metaType: "",
      tableData3: []
    };
  },
  methods: {
    getData() {
      this.$urlApi.metaData.basicInfoMsg({ id: this.currNodeId }).then(res => {
        this.tableData3 = res.data.attrs;
        this.metaName = res.data.name;
        this.metaCode = res.data.code;
        this.metaType = res.data.metaModelName;
      });
    }
  },
  mounted: function() {
    this.getData();
  },
  watch: {
    basicVisible: function(val) {
      this.getData();
    }
  }
};
</script>

<style lang="scss">
.business-basic {
  height: 100%;
  .business-basic-t {
    height: 120px;
    padding: 15px 10px;
    line-height: 45px;
  }
  .business-basic-b {
    height: -moz-calc(100% - 135px);
    height: -webkit-calc(100% - 135px);
    height: calc(100% - 135px);
    .el-table {
      td,
      .el-table th {
        padding: 6px 0;
      }
    }
  }
}
</style>
