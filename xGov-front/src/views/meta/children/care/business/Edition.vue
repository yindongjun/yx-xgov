<template>
  <section class="business-edition">
    <el-row class="business-edition-t"> </el-row>
    <el-row class="business-edition-b">
      <el-table :data="tableData3" height="100%" style="width: 100%">
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="versionName" label="版本号"> </el-table-column>
        <el-table-column
          prop="createTime"
          :formatter="this.$utils.dateFormat"
          label="版本产生时间"
        >
        </el-table-column>
        <el-table-column prop="versionDescription" label="描述">
        </el-table-column>
      </el-table>
    </el-row>
  </section>
</template>

<script>
export default {
  name: "",
  components: {},
  props: ["EditionVisible", "currNodeId"],
  data() {
    return {
      tableData3: []
    };
  },
  methods: {
    getData() {
      this.$urlApi.metaData
        .getMetaVersion({ id: this.currNodeId })
        .then(res => {
          this.tableData3 = res.data;
        });
    }
  },
  mounted: function() {},
  watch: {
    EditionVisible: function(val) {
      this.getData();
    }
  }
};
</script>

<style lang="scss">
.business-edition {
  height: 100%;
  .business-edition-b {
    height: 100%;
    .el-table {
      td,
      .el-table th {
        padding: 6px 0;
      }
    }
  }
}
</style>
